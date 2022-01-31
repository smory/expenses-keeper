package sk.smorada.expenseskeerer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import sk.smorada.expenseskeerer.databinding.ActivityMainBinding
import sk.smorada.expenseskeerer.helper.DbDataHelper
import sk.smorada.expenseskeerer.ui.camera.CameraActivity
import sk.smorada.expenseskeerer.ui.details.DetailsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val TAG = this::class.simpleName!!

    private val photoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { data ->
                val intent = Intent(this, DetailsActivity::class.java).apply {
                    putExtra("photoPath", data.getStringExtra("photoPath")?: "")
                }
                documentCreationLauncher.launch(intent)
            }
        }
    }

    private val documentCreationLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == Activity.RESULT_OK) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.fab.setOnClickListener { view ->
            if(++counter % 2 == 1) {
                photoLauncher.launch(Intent(this, CameraActivity::class.java))
            } else {
                DbDataHelper.getLastDocumentMaybe()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        val intent = Intent(this, DetailsActivity::class.java).apply { putExtra("doc", it) }
                        documentCreationLauncher.launch(intent)
                    }
            }
        }
    }

    var counter = 0
}