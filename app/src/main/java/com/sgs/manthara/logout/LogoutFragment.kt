package com.sgs.manthara.logout

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sgs.manthara.ImagePreferrence
import com.sgs.manthara.R
import com.sgs.manthara.activity.DashBoardActivity
import com.sgs.manthara.activity.LoginWithOtpActivity
import com.sgs.manthara.databinding.LogoutFragmentBinding
import com.sgs.manthara.jewelRetrofit.MainPreference
import java.io.ByteArrayOutputStream
import java.io.File

class LogoutFragment : BottomSheetDialogFragment() {

    private lateinit var binding: LogoutFragmentBinding
    private lateinit var logoutVM: LogoutVM
    private lateinit var mainPreference: MainPreference
    private lateinit var imagePreferrence: ImagePreferrence

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = LogoutFragmentBinding.inflate(inflater, container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainPreference = MainPreference(requireContext())
        imagePreferrence = ImagePreferrence.getInstance(requireContext())!!
        val repo = Logoutrepositary(mainPreference)
        val factory = LogoutFactory(repo)
        logoutVM = ViewModelProvider(this,factory)[LogoutVM::class.java]

        binding.logout.setOnClickListener {

            logoutVM.clearValues()
            Log.i("TAG","Logout:${logoutVM.clearValues()}")

            val bitmap = drawableToBitmap(requireContext(), R.drawable.app_logo)
            val baos = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val b: ByteArray = baos.toByteArray()
            val encodedImage: String = Base64.encodeToString(b, Base64.DEFAULT)
            imagePreferrence.setString("image_data", encodedImage)

            dialog?.dismiss()
            requireActivity().startActivity(Intent(requireContext(), LoginWithOtpActivity::class.java))
            requireActivity().finish()
        }
        binding.cancel.setOnClickListener {

            dialog?.dismiss()
        }
    }

    private fun deleteCache() {
        try {
            val dir: File = requireContext().cacheDir
            deleteDir(dir)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun deleteDir(dir: File?): Boolean {
        return if (dir != null && dir.isDirectory) {
            val children: Array<String> = dir.list() as Array<String>
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
            dir.delete()
        } else if (dir != null && dir.isFile) {
            dir.delete()
        } else {
            false
        }
    }

    private fun getStatusFromPosition(position: Boolean): Int {

        return if (position) {
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED
        } else {
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED
        }
    }

    private fun drawableToBitmap(context: Context, drawableId: Int): Bitmap? {
        val drawable = context.getDrawable(drawableId)
        return if (drawable != null) {
            val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        } else {
            null
        }
    }

}