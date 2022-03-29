package com.pklproject.checkincheckout.ui.dashboard.absen.camera

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.controls.Audio
import com.otaliastudios.cameraview.controls.PictureFormat
import com.pklproject.checkincheckout.R
import com.pklproject.checkincheckout.databinding.FragmentCameraViewBinding
import com.pklproject.checkincheckout.viewmodel.ServiceViewModel

class CameraView : Fragment(R.layout.fragment_camera_view) {

    private val binding: FragmentCameraViewBinding by viewBinding()
    private val viewModel: ServiceViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.camera.setLifecycleOwner(this)
        binding.camera.pictureFormat = PictureFormat.JPEG
        binding.camera.audio = Audio.OFF
        binding.camera.addCameraListener(object:CameraListener() {
            override fun onPictureTaken(result: PictureResult) {
                super.onPictureTaken(result)
                viewModel.setResultPicture(result)
                findNavController().navigateUp()
            }
        })

        binding.captureButton.setOnClickListener {
            binding.camera.takePicture()
        }
    }
}