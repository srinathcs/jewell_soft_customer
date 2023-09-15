package com.sgs.manthara.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sgs.manthara.ImagePreferrence
import android.Manifest
import androidx.lifecycle.lifecycleScope
import com.sgs.manthara.R
import com.sgs.manthara.activity.DashBoardActivity
import com.sgs.manthara.databinding.FragmentSettingsBinding
import com.sgs.manthara.jewelRetrofit.JewelFactory
import com.sgs.manthara.jewelRetrofit.JewelRepo
import com.sgs.manthara.jewelRetrofit.JewelVM
import com.sgs.manthara.jewelRetrofit.MainPreference
import kotlinx.coroutines.flow.first
import java.io.ByteArrayOutputStream

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var mainPreference: MainPreference
    private var camera: Intent? = null
    private lateinit var imagePreferrence: ImagePreferrence

    private val jewelSoftVM: JewelVM by lazy {
        val repos = JewelRepo()
        val factory = JewelFactory(repos)
        ViewModelProvider(this, factory)[JewelVM::class.java]
    }
    private var lt = ""
    private var ln = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        mainPreference = MainPreference(requireContext())

        lifecycleScope.launchWhenStarted {
            val name = mainPreference.getMobileNo().first()
            binding.tvPhNo.setText(name)
        }

        lifecycleScope.launchWhenStarted {
            val name = mainPreference.getUserName().first()
            binding.tvName.setText(name)
        }

        binding.llNine.setOnClickListener {
            findNavController().navigate(R.id.logout)
        }

        binding.ibView.setOnClickListener {
            val intent = Intent(requireActivity(), DashBoardActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            val intent = Intent(requireActivity(), DashBoardActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        binding.llEight.setOnClickListener {
            findNavController().navigate(R.id.themeFragment)
        }

        binding.llFour.setOnClickListener {
            findNavController().navigate(R.id.aboutUsFragment)
        }

        binding.llFive.setOnClickListener {
            findNavController().navigate(R.id.feedbackFragment)
        }

        binding.llSix.setOnClickListener {
            findNavController().navigate(R.id.helpFragment)
        }

        imagePreferrence = ImagePreferrence.getInstance(requireContext())!!

        val previouslyEncodedImage = imagePreferrence.getString("image_data")

        if (!previouslyEncodedImage.equals("", ignoreCase = true)) {
            val b = Base64.decode(previouslyEncodedImage, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(b, 0, b.size)
            binding.ivImg.setImageBitmap(bitmap)
        }

        addImage()

        return binding.root
    }

    private fun addImage() {
        binding.profileBtn.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.CAMERA
                    ), 123
                )
            } else {
                image()
            }
        }
    }

    private fun image() {
        val options = arrayOf<CharSequence>("Camera", "Gallery")
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(true)
        builder.setTitle("Select the image")
        builder.setItems(options) { dialog, item ->

            if (options[item] == "Camera") {
                builder.show()
                camera = Intent()
                camera!!.action = MediaStore.ACTION_IMAGE_CAPTURE
                startActivityForResult(camera!!, 118)

                dialog.dismiss()
            } else if (options[item] == "Gallery") {
                val galleryIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, 119)

                dialog.dismiss()
            }

        }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 118) {
                val bitmap = data?.extras?.get("data") as Bitmap
                binding.ivImg.setImageBitmap(bitmap)
                val baos = ByteArrayOutputStream()
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val b: ByteArray = baos.toByteArray()
                val encodedImage: String = Base64.encodeToString(b, Base64.DEFAULT)
                imagePreferrence.setString("image_data", encodedImage)
                showToast("Profile Updated")
            } else if (requestCode == 119) {
                val uri = data?.data
                val bitmap = getBitmapFromUri(uri!!)
                binding.ivImg.setImageBitmap(bitmap)
                val baos = ByteArrayOutputStream()
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val b: ByteArray = baos.toByteArray()
                val encodedImage: String = Base64.encodeToString(b, Base64.DEFAULT)
                imagePreferrence.setString("image_data", encodedImage)
                showToast("Profile Updated")
            }
        }
    }

    private fun getBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}