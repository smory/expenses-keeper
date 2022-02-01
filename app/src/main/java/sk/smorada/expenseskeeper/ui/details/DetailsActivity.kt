package sk.smorada.expenseskeeper.ui.details

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.android.material.datepicker.MaterialDatePicker
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import sk.smorada.expenseskeeper.Consts
import sk.smorada.expenseskeeper.R
import sk.smorada.expenseskeeper.databinding.ActivityDetailsBinding
import sk.smorada.expenseskeeper.extentions.Extensions.containsText
import sk.smorada.expenseskeeper.extentions.Extensions.isConvertibleToFloat
import sk.smorada.expenseskeeper.extentions.Extensions.setImageFromPath
import sk.smorada.expenseskeeper.extentions.Extensions.textAsFloat
import sk.smorada.expenseskeeper.extentions.Extensions.toFormattedDateString
import sk.smorada.expenseskeeper.model.Document

class DetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailsBinding
    private val tag = this::class.simpleName
    private val pickerTag = "datePicker"

    private var document: Document? = null
    private val model: DetailsViewModel by viewModels()

    private var chosenInvoiceDate = -1L
    private var chosenDueDate = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setupContentView()
        initModel()
        setupToolbar()
        setupOnClickListeners()
    }

    private fun setupContentView() {
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        getDocumentFromIntent()?.let {  // to remove jumping animation when activity launches
            binding.gInvoiceControls.visibility = if(it.isInvoice()) View.VISIBLE else View.GONE
        }
        setContentView(binding.root)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (document?.id != 0) {
            menuInflater.inflate(R.menu.menu_details, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete -> {
                model.delete().subscribe { onDocumentDeleted() }
                true
            }
            R.id.edit -> {
                enableEditing(true)
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onDocumentDeleted() {
        Toast.makeText(this, R.string.deleted, Toast.LENGTH_LONG).show()
        finish()
    }

    private fun initModel() {
        model.iniModel(getDocumentFromIntent(), getPhotoPathFormIntent())
        model.documentLiveData.observe(this, this::setValues)
    }

    private fun getDocumentFromIntent(): Document? = intent.getParcelableExtra<Document>(Consts.DOCUMENT_PARAM)
    private fun getPhotoPathFormIntent(): String = document?.photoPath ?: intent.getStringExtra(Consts.PHOTO_PATH_PARAM) ?: ""

    private fun setValues(document: Document) {
        this.document = document
        binding.swInvoice.isChecked = document.isInvoice()
        binding.vPhoto.setImageFromPath(document.photoPath)
        binding.gInvoiceControls.visibility = if (document.isInvoice()) View.VISIBLE else View.GONE
        binding.etInvoiceNo.setText(document.invoiceNumber)
        binding.etCreationDate.setText(document.documentDate.toFormattedDateString())
        chosenInvoiceDate = document.documentDate
        binding.etDueDate.setText(document.dueDate.toFormattedDateString())
        chosenDueDate = document.dueDate
        binding.etCurrency.setText(document.currency)
        binding.etTotal.setText(document.totalPrice.toString())
        binding.etPartner.setText(document.partner)
        binding.etNote.setText(document.note)
        binding.swPaid.isChecked = document.paid
        enableEditing(document.id == 0)
        invalidateOptionsMenu()
    }

    private fun enableEditing(enabled: Boolean) {
        binding.gButtons.visibility = if (enabled) View.VISIBLE else View.GONE
        val views = listOf<View>(
            binding.tilCreationDate,
            binding.tilDueDate,
            binding.tilInvoiceNo,
            binding.tilTotal,
            binding.tilCurrency,
            binding.tilPartner,
            binding.tilNote,
            binding.swPaid,
            binding.swInvoice,
        )
        views.forEach {
            it.isEnabled = enabled
        }
    }


    private fun setupOnClickListeners() {
        binding.swInvoice.setOnCheckedChangeListener { _, isChecked ->
            binding.gInvoiceControls.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
        binding.etCreationDate.setOnClickListener {
            pickDate(chosenInvoiceDate) {
                chosenInvoiceDate = it
                binding.etCreationDate.setText(it.toFormattedDateString())
                chosenDueDate = it + 15 * 24 * 60 * 60 * 1000
                binding.etDueDate.setText(chosenDueDate.toFormattedDateString())
            }
        }
        binding.etDueDate.setOnClickListener {
            pickDate(chosenDueDate) {
                chosenDueDate = it
                binding.etDueDate.setText(it.toFormattedDateString())
            }
        }
        binding.bSave.setOnClickListener { onSaveClicked() }
        binding.bCancel.setOnClickListener { onCancelClicked() }
    }

    private fun onSaveClicked() {
        val valid = validateData()
        if (valid) {
            val single = model.save(
                if(binding.swInvoice.isChecked) binding.etInvoiceNo.text.toString() else null,
                binding.etCurrency.text.toString(),
                binding.etTotal.textAsFloat(),
                chosenInvoiceDate,
                chosenDueDate,
                binding.etPartner.text.toString(),
                binding.etNote.text.toString(),
                binding.swPaid.isChecked
            )

            single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it) {
                        Log.v(tag, "Data written to database.")
                        setResult(Activity.RESULT_OK)
                    } else {
                        Log.v(tag, "No need to write data to database.")
                        setResult(Activity.RESULT_CANCELED)
                    }
                    finish()
                }, {
                    Log.e(tag, "Failed to save data", it)
                })
        }
    }

    private fun onCancelClicked() {
        if (document?.id != 0) {  // turn off edit mode
            enableEditing(false)
        } else {  // cancel entering of new entry
            model.cancel()
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    private fun validateData(): Boolean {
        var valid = true
        val isInvoice = binding.swInvoice.isChecked
        if (isInvoice && !binding.etInvoiceNo.containsText()) {
            valid = false
            binding.tilInvoiceNo.error = getString(R.string.required)
        }

        if (!binding.etTotal.isConvertibleToFloat() || binding.etTotal.textAsFloat() <= 0.00F) {
            valid = false
            binding.tilTotal.error = getString(R.string.invalid)
        }

        if (!binding.etCurrency.containsText()) {
            valid = false
            binding.tilCurrency.error = getString(R.string.required)
        }

        if (!binding.etPartner.containsText()) {
            valid = false
            binding.tilPartner.error = getString(R.string.required)
        }

        return valid
    }

    private fun pickDate(initialValue: Long, onResult: (result: Long) -> Unit) {
        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.dialog_select_date)
            .setSelection(initialValue)
            .build()
        picker.addOnPositiveButtonClickListener {
            onResult.invoke(it)
        }
        picker.show(supportFragmentManager, pickerTag)
    }
}