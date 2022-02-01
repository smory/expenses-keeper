package sk.smorada.expenseskeeper.ui.list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.paging.insertSeparators
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import sk.smorada.expenseskeeper.Consts
import sk.smorada.expenseskeeper.databinding.ActivityListBinding
import sk.smorada.expenseskeeper.model.Document
import sk.smorada.expenseskeeper.ui.camera.CameraActivity
import sk.smorada.expenseskeeper.ui.details.DetailsActivity
import sk.smorada.expenseskeeper.ui.list.recycler.ListAdapter
import sk.smorada.expenseskeeper.ui.list.recycler.model.Separator

class ListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding
    private val tag = this::class.simpleName!!

    private val viewModel: ListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        setupRecyclerWithDataSource()
        setupAddNewDocument()
    }

    private fun setupAddNewDocument() {
        binding.fab.setOnClickListener { _ ->
            startDocumentCreation()
        }
    }

    private fun setupRecyclerWithDataSource() {
        val adapter = ListAdapter()
        binding.recycler.adapter = adapter
        lifecycleScope.launch {
            viewModel.pagingDataFlow.collectLatest { pagingData ->
                val transformedPagingData =
                    pagingData.insertSeparators { before: Document?, after: Document? ->
                        when {
                            before == null -> null
                            after == null -> null
                            else -> Separator()
                        }
                    }
                adapter.submitData(transformedPagingData)
            }
        }

        adapter.addOnPagesUpdatedListener {
            binding.tvEmptyMessage.visibility =
                if (adapter.itemCount == 0) View.VISIBLE else View.GONE
        }
    }

    private fun startDocumentCreation() {
        photoLauncher.launch(Intent(this, CameraActivity::class.java))
    }

    private val photoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { data ->
                    val intent = Intent(this, DetailsActivity::class.java).apply {
                        putExtra(Consts.PHOTO_PATH_PARAM, data.getStringExtra(Consts.PHOTO_PATH_PARAM) ?: "")
                    }
                    documentCreationLauncher.launch(intent)
                }
            }
        }

    private val documentCreationLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                // no need to handle
            }
        }

}