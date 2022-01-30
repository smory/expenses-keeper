package sk.smorada.expenseskeerer.ui.camera

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import io.fotoapparat.Fotoapparat
import sk.smorada.expenseskeerer.R
import sk.smorada.expenseskeerer.databinding.ActivityCameraBinding
import java.io.File

class CameraActivity : AppCompatActivity() {

    private val TAG = "Camera"
    private lateinit var binding: ActivityCameraBinding
    private lateinit var fotoapparat: Fotoapparat
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                configureFotoapparat()
                fotoapparat.start()
            } else {
                onPermissionNotGranted()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()
        handlePermission()
    }

    private fun handlePermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            configureFotoapparat()
        } else {
            showPrePermissionRequestDialog()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                cancelActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun configureFotoapparat() {
        fotoapparat = Fotoapparat(
            this,
            binding.vCamera,
            cameraErrorCallback = { e -> Log.e(TAG, "", e) })

        binding.vTakePhoto.setOnClickListener {
            val file = File(filesDir, "${System.currentTimeMillis()}.jpg")
            Log.v(TAG, file.absolutePath)
            fotoapparat.takePicture().saveToFile(file)
                .whenAvailable { result ->
                if (result == null) {
                    onFailedToCreatePhoto()
                } else {
                    finishWithResult(file)
                }
            }
        }
    }

    private fun showPrePermissionRequestDialog() {
        val dialog = AlertDialog.Builder(this).setTitle(R.string.dialog_permission_title)
            .setMessage(R.string.dialog_permission_message)
            .setPositiveButton(R.string.dialog_yes) { p0, p1 -> requestPermissionLauncher.launch(Manifest.permission.CAMERA) }
            .setNegativeButton(R.string.dialog_no) { dialog, which -> onPermissionNotGranted() }
            .setIcon(R.drawable.ic_no_camera)
            .create()
        dialog.show()
    }

    private fun onPermissionNotGranted() {
        Toast.makeText(
            this,
            R.string.error_permission_not_grated,
            Toast.LENGTH_LONG
        ).show()
        cancelActivity()
    }

    private fun cancelActivity() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    private fun finishWithResult(image: File) {
        setResult(Activity.RESULT_OK, Intent().putExtra("path", image.absolutePath))
        finish()
    }

    private fun onFailedToCreatePhoto() {
        Toast.makeText(
            this,
            R.string.error_save_photo,
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onStart() {
        super.onStart()
        if (::fotoapparat.isInitialized) {
            fotoapparat.start()
        }
    }

    override fun onStop() {
        super.onStop()
        if (::fotoapparat.isInitialized) {
            fotoapparat.stop()
        }
    }
}
