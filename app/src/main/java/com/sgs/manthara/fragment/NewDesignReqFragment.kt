package com.sgs.manthara.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.sgs.manthara.R
import com.sgs.manthara.databinding.FragmentNewDesginReqBinding
import com.sgs.manthara.databinding.FragmentSelectImageBinding
import com.sgs.manthara.databinding.LogoutDialogBinding
import com.sgs.manthara.jewelRetrofit.JewelFactory
import com.sgs.manthara.jewelRetrofit.JewelRepo
import com.sgs.manthara.jewelRetrofit.JewelVM
import com.sgs.manthara.jewelRetrofit.MainPreference
import com.sgs.manthara.jewelRetrofit.Resources
import com.sgs.manthara.location.FusedLocationService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.Calendar

val Bitmap.path: String
    get() {
        return toString()
    }

const val FILE_NAME_DIESEL = "photo.jpg"
const val REQUEST_CODE_DIESEL = 42
const val THUMBNAIL_SIZE = 1280
const val REQUEST_CODE_DIESEL_GALLERY = 52

var dieselFile: MutableList<File>? = null

class NewDesignReqFragment : Fragment() {
    private lateinit var binding: FragmentNewDesginReqBinding
    private lateinit var mainPreference: MainPreference
    private var lt = ""
    private var ln = ""
    private var nameList: MutableList<String> = mutableListOf()
    private var id: MutableList<String> = mutableListOf()
    private var scheme = ""
    private val jewelSoftVM: JewelVM by lazy {
        val repos = JewelRepo()
        val factory = JewelFactory(repos)
        ViewModelProvider(this, factory)[JewelVM::class.java]
    }
    private lateinit var alertDialog: AlertDialog
    private lateinit var selectImageBinding: FragmentSelectImageBinding
    private val cameraRequest = 1888
    private var photoFileDiesel: File? = null
    private var filesDiesel: MutableList<Bitmap> = mutableListOf()
    private var filesString: MutableList<String> = mutableListOf()
    private var filesDiesel2: MutableList<Uri> = mutableListOf()
    private lateinit var logoutDialogBinding: LogoutDialogBinding
    private var photosIdeal: Bitmap? = null
    private var dieselPhoto: String = ""
    private var photoMaterial: MutableList<String> = mutableListOf()
    private var dks: Bitmap? = null
    private var count = 0
    private var multiList: MutableList<MultipartBody.Part> = mutableListOf()
    private var device: String = ""
    private var catId = ""
    private var subId = ""
    private var proId = ""
    private var last = ""
    private lateinit var selectedCaptionDiesel: EditText
    private lateinit var selectedOrderDiesel: EditText
    private var jsArray = JSONArray()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewDesginReqBinding.inflate(inflater, container, false)
        alertDialog = AlertDialog.Builder(requireContext()).create()
        mainPreference = MainPreference(requireContext())
        goldType()
        FusedLocationService.latitudeFlow.observe(requireActivity()) {
            lt = it.latitude.toString()
            ln = it.longitude.toString()
            Log.i("TAG", "onCreateL:$lt")
            Log.i("TAG", "onCreateLo:$ln")

        }

        device =
            Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)

        binding.tvUpload.setOnClickListener {
            addImage()
        }

        binding.btnSubmit.setOnClickListener {
            imgSend()
        }

        binding.ibView.setOnClickListener {
            findNavController().navigate(R.id.viewPage)
        }


        return binding.root
    }

    private fun goldType() {
        val deviceId =
            Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.jewellType(
                "18",
                mainPreference.getCid().first(),
                deviceId,
                ln,
                lt,
                mainPreference.getUserId().first(),
                "1"

            )
        }
        goldRespones()
    }

    private fun goldRespones() {
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.jewellTypeFlow.collect {
                when (it) {
                    is Resources.Loading -> {

                    }

                    is Resources.Error -> {

                    }

                    is Resources.Success -> {
                        try {
                            nameList.clear()
                            id.clear()
                            for (i in it.data!!) {
                                nameList.add(i.sub_category)
                                id.add(i.id)
                            }
                            val arrayAdapter = ArrayAdapter(
                                requireContext(), R.layout.complete_text_view, nameList
                            )
                            binding.atvJewelType.setAdapter(arrayAdapter)
                            binding.atvJewelType.threshold = 1
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }
                }
            }
        }
        binding.atvJewelType.onItemClickListener = AdapterView.OnItemClickListener { _, _, _, _ ->
            for (i in 0 until nameList.size) {
                try {
                    if (binding.atvJewelType.text.toString() == nameList[i]) {
                        scheme = id[i]
                        Log.i("TAG", "forsalessss:$id")
                    }
                } catch (e: IndexOutOfBoundsException) {
                    e.printStackTrace()
                }
            }

        }
    }

    private fun addImage() {
        lifecycleScope.launchWhenStarted {

            val layout = layoutInflater.inflate(R.layout.fragment_select_image, null)
            selectImageBinding = FragmentSelectImageBinding.bind(layout)
            alertDialog.setView(layout)
            alertDialog.show()
            selectImageBinding.camera.setOnClickListener {
                if (ContextCompat.checkSelfPermission(
                        requireContext(), Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_DENIED
                ) ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(Manifest.permission.CAMERA), cameraRequest
                )

                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                photoFileDiesel = getPhotoFile(FILE_NAME_DIESEL)

                // This DOESN'T work for API >= 24 (starting 2016)
                // takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFile)

                val fileProvider = FileProvider.getUriForFile(
                    requireContext(), "com.sgs.manthara", photoFileDiesel!!
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
                if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {

                    try {
                        startActivityForResult(takePictureIntent, REQUEST_CODE_DIESEL)
                    } catch (e: SecurityException) {
                        e.printStackTrace()
                    }


                } else {
                    startActivityForResult(takePictureIntent, REQUEST_CODE_DIESEL)

                    Toast.makeText(requireContext(), "Unable to open camera", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            selectImageBinding.gallery.setOnClickListener {
                val intent = Intent()
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(
                    Intent.createChooser(intent, "Select Image(s)"), REQUEST_CODE_DIESEL_GALLERY
                )//one can be replaced with any action code

            }
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_DIESEL && resultCode == Activity.RESULT_OK) {

            try {
                val takenImageMaterial: Bitmap =
                    BitmapFactory.decodeFile(photoFileDiesel!!.absolutePath)
                dieselFile?.add(photoFileDiesel!!.absoluteFile)

                val byteArrayOutputStream = ByteArrayOutputStream()
                takenImageMaterial.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)


                val image1: Uri? = getImageUri(requireContext(), takenImageMaterial)
                filesDiesel2.add(image1!!)

                val images2 = getThumbnail(image1)
                filesDiesel.add(images2!!)
                photosIdeal = images2


                photosIdeal!!.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
                val imgByte: ByteArray = byteArrayOutputStream.toByteArray()
                photoMaterial.add(Base64.encodeToString(imgByte, Base64.DEFAULT))


            } catch (e: NullPointerException) {
                e.printStackTrace()
            }

            photoMaterial.add(dieselPhoto)
        }

        if (requestCode == REQUEST_CODE_DIESEL_GALLERY && resultCode == Activity.RESULT_OK) {

            if (resultCode == Activity.RESULT_OK) {

//                single Images
                try {
                    if (data != null) {
                        val uri = data.data
                        val bitmap = getThumbnail(uri!!)
                        filesDiesel.add(bitmap!!)

                    }

                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }
            }
        }

        addMultipleImage()
        alertDialog.dismiss()

    }

    @SuppressLint("InflateParams")
    private fun addMultipleImage() {

        binding.parentLinearLayoutDiesel.removeViews(
            0, binding.parentLinearLayoutDiesel.childCount - 1
        )
        for ((_, i) in (filesDiesel.withIndex())) {
            val inflater =
                requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val rowView: View = inflater.inflate(R.layout.bill, null)
            binding.parentLinearLayoutDiesel.addView(
                rowView, binding.parentLinearLayoutDiesel.childCount - 1
            )

            binding.parentLinearLayoutDiesel.isFocusable
            val selectedImageDiesel = rowView.findViewById<ImageView>(R.id.bill_image)
            selectedImageDiesel!!.setImageBitmap(i)


            selectedImageDiesel.id = binding.parentLinearLayoutDiesel.childCount
            selectedImageDiesel.setOnClickListener {
                val imageIndex = it.id
                val dialogImage = filesDiesel[imageIndex - 2]
                val alertDialog =
                    androidx.appcompat.app.AlertDialog.Builder(requireContext()).create()
                val layout = layoutInflater.inflate(R.layout.logout_dialog, null)
                logoutDialogBinding = LogoutDialogBinding.bind(layout)
                logoutDialogBinding.image.setImageBitmap(dialogImage)
                alertDialog.setView(layout)
                logoutDialogBinding.image.setOnClickListener {
                    alertDialog.dismiss()
                }

                alertDialog.show()
            }

//            Remove Specific Picture
            val close = rowView.findViewById<ImageView>(R.id.close_bill)
            close.id = binding.parentLinearLayoutDiesel.childCount
            try {
                close.setOnClickListener {
                    val inde = it.id
                    binding.parentLinearLayoutDiesel.removeView(rowView)
                    try {
                        filesDiesel.removeAt(inde - 2)
                        addMultipleImage()
                        addphotos()
                    } catch (e: IndexOutOfBoundsException) {
                        e.printStackTrace()
                    }

                }

            } catch (e: IndexOutOfBoundsException) {
                e.printStackTrace()
            }

        }

        addphotos()

    }

    private fun addphotos() {
        multiList.clear()
        count = 0

        for (j in 0 until filesDiesel.size) {

            try {
                count++

                filesDiesel[j]

                multiList.add(buildImageBodyPart("imageapi$j", filesDiesel[j]))

            } catch (e: IndexOutOfBoundsException) {
                e.printStackTrace()
            }

        }

        Log.i("TAG", "addMultipleImagemultiList:$count")
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver, inImage, "IMG_" + Calendar.getInstance().time, null
        )


        return Uri.parse(path)
    }


    private fun getPhotoFile(fileName: String): File {
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        val storageDirectory = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

    private fun getThumbnail(uri: Uri?): Bitmap? {

        try {
            var input: InputStream? =
                uri?.let { requireActivity().contentResolver.openInputStream(it) }
            val onlyBoundsOptions = BitmapFactory.Options()
            onlyBoundsOptions.inJustDecodeBounds = true
            onlyBoundsOptions.inDither = true //optional
            onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888 //optional
            BitmapFactory.decodeStream(input, null, onlyBoundsOptions)
            input!!.close()
            if (onlyBoundsOptions.outWidth == -1 || onlyBoundsOptions.outHeight == -1) return null
            val originalSize =
                if (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) onlyBoundsOptions.outHeight else onlyBoundsOptions.outWidth
            val ratio = if (originalSize > THUMBNAIL_SIZE) originalSize / THUMBNAIL_SIZE else 100
            val bitmapOptions = BitmapFactory.Options()
            bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio).toInt()
            bitmapOptions.inDither = true //optional
            bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888 //optional
            input = uri?.let { requireActivity().contentResolver.openInputStream(it) }
            val bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions)
            input!!.close()
            dks = bitmap
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return dks
    } // This is Uri into Bitmap.

    private fun getPowerOfTwoForSampleRatio(ratio: Number): Int {

        val k = Integer.highestOneBit(kotlin.math.floor(ratio.toDouble()).toInt())
        return if (k == 0) 100 else k
    }

    private fun buildImageBodyPart(fileName: String, bitmap: Bitmap): MultipartBody.Part {
        val leftImageFile = convertBitmapToFile(fileName.plus(".jpg"), bitmap)
        val reqFile = leftImageFile.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(fileName, leftImageFile.name, reqFile)
    } // This Method is Bitmap into file

    private fun convertBitmapToFile(
        fileName: String,
        bitmap: Bitmap,
    ): File {
        //create a file to write bitmap data
        val file = File(requireContext().cacheDir, fileName)
        file.createNewFile()

        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos)
        val bitMapData = bos.toByteArray()

        //write the bytes in file
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            fos?.write(bitMapData)
            fos?.flush()
            fos?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return file
    } // This also Bitmap into file


    private fun imgSend() {

        if (filesDiesel.size > 4) {
            Toast.makeText(requireContext(), "Only 4 Images Can Apply", Toast.LENGTH_SHORT).show()
        } else {
            lifecycleScope.launchWhenStarted {
                val type: RequestBody = "19".toRequestBody("text/plain".toMediaTypeOrNull())
                val cid: RequestBody =
                    mainPreference.getCid().first().toRequestBody("text/plain".toMediaTypeOrNull())
                val ln: RequestBody = ln.toRequestBody("text/plain".toMediaTypeOrNull())
                val lt: RequestBody = lt.toRequestBody("text/plain".toMediaTypeOrNull())
                val mydevice: RequestBody = device.toRequestBody("text/plain".toMediaTypeOrNull())
                val uid: RequestBody = mainPreference.getUserId().first()
                    .toRequestBody("text/plain".toMediaTypeOrNull())
                val count: RequestBody =
                    count.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val scheme: RequestBody =
                    scheme.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val gram: RequestBody =
                    binding.etGram.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val description: RequestBody = binding.etDescription.text.toString()
                    .toRequestBody("text/plain".toMediaTypeOrNull())


                jewelSoftVM.addImagesProperty(
                    type,
                    cid,
                    ln,
                    lt,
                    mydevice,
                    uid,
                    count,
                    scheme,
                    gram,
                    description,
                    multiList
                )
            }
            sendImage()
        }
    }

    private fun sendImage() {
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.addImageFlow.collect {
                when (it) {
                    is Resources.Loading -> {
                        binding.progress.visibility = View.VISIBLE
                        binding.btnSubmit.visibility = View.GONE
                        Log.i("TAG", "sendLoadingImage:${it.message}")
                    }

                    is Resources.Error -> {
                        binding.progress.visibility = View.VISIBLE
                        binding.btnSubmit.visibility = View.GONE
                        delay(1000)
                        binding.progress.visibility = View.GONE
                        binding.btnSubmit.visibility = View.VISIBLE
                        Log.i("TAG", "sendErrorImage:${it.message}")
                    }

                    is Resources.Success -> {
                        binding.progress.visibility = View.GONE
                        binding.btnSubmit.visibility = View.VISIBLE
                        Log.i("TAG", "sendSuccessImage:${it.data}")
                        var msg = ""
                        for (i in it.data!!) {
                            msg = i.message
                        }

                        if (msg == "Order SuccessFully") {
                            Toast.makeText(
                                requireContext(),
                                "Image Upload Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            findNavController().navigate(R.id.tickFragment)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Not save",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }
}

//val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
//    val int = Intent(requireActivity(), HomeActivity::class.java)
//    startActivity(int)
//    requireActivity().finish()
//}