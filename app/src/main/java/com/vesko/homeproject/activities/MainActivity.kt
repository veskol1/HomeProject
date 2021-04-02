package com.vesko.homeproject.activities

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View.OnFocusChangeListener
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.google.android.material.datepicker.MaterialDatePicker.Builder.datePicker
import com.google.android.material.textfield.TextInputEditText
import com.vesko.homeproject.R
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var babyNameEditText: TextInputEditText
    private lateinit var selectedDateEditText: TextInputEditText
    private lateinit var nextButton: Button
    private lateinit var changePicButton: Button
    private lateinit var babyImageView: ImageView
    private var nameFilled = false
    private var dateFilled = false
    private var babyAgeInMonths = 0
    private var babyAgeInYears = 0
    private var selectedBabeBirthdayDay = 0
    private var selectedBabeBirthdayMonth = 0
    private var selectedBabeBirthdayYear = 0
    private var uriImage = "" //will hold uri(string path) to the selected image from phones gallery
    private val IMAGE_URL = "https://as2.ftcdn.net/jpg/02/16/85/19/500_F_216851969_42JnrCBh9acjRk3hkFRzfKLqoA3CpDmk.jpg"
    private val BABE_AGE_YEARS = "com.vesko.homeproject.mainactivty.activities.babyAgeYears"
    private val BABE_AGE_MONTHS = "com.vesko.homeproject.mainactivty.activities.babyAgeMonths"
    private val BABE_NAME = "com.vesko.homeproject.mainactivty.activities.babysName"
    private val BABE_IMAGE = "com.vesko.homeproject.mainactivtyactivities.babysImage"
    private val IMAGE_PICK_CODE = 1005
    private val PERMISSION_CODE_READ = 1001
    private val PERMISSION_CODE_WRITE = 1002



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialize()
        updateUI()
        checkIfDetailedFilled()

        focusFunctionalityOnNameEditText()
        focusAndClickFunctionalityOnDatePicker()

        changePicButton.setOnClickListener {
            checkPermissionForImage()
            pickImageFromGallery()
        }

        nextButton.setOnClickListener {
            saveData()
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra(BABE_AGE_YEARS, babyAgeInYears)
            intent.putExtra(BABE_AGE_MONTHS, babyAgeInMonths)
            intent.putExtra(BABE_NAME, babyNameEditText.text.toString())
            intent.putExtra(BABE_IMAGE, uriImage)
            startActivity(intent)
        }
    }

    /*using shared preferences to store data and restore data after app restarts*/
    private fun saveData() {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(getString(R.string.save_baby_name), babyNameEditText.text.toString())
            putString(getString(R.string.save_baby_birthday), selectedDateEditText.text.toString())
            putInt(getString(R.string.save_baby_birthday_day), selectedBabeBirthdayDay)
            putInt(getString(R.string.save_baby_birthday_month), selectedBabeBirthdayMonth)
            putInt(getString(R.string.save_baby_birthday_year), selectedBabeBirthdayYear)
            apply()
        }
    }

    private fun updateUI() {
        if(uriImage.isNotEmpty()) {
            checkPermissionForImage()
            babyImageView.setImageURI(uriImage.toUri())
        }
        else{
            Glide.with(this)
                .load(IMAGE_URL)
                .into(babyImageView)
        }
    }

    private fun initialize() {
        babyNameEditText = findViewById(R.id.baby_name_edit_text)
        selectedDateEditText = findViewById(R.id.date_select)
        nextButton = findViewById(R.id.show_birthday_screen_button)
        changePicButton = findViewById(R.id.change_picture_button)
        babyImageView = findViewById(R.id.baby_image_view)

        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)

        if(sharedPref!= null){
            val babeName = sharedPref.getString(getString(R.string.save_baby_name), "")
            babyNameEditText.setText(babeName)
            val birthDayString = sharedPref.getString(getString(R.string.save_baby_birthday), "")
            selectedDateEditText.setText(birthDayString)
            selectedBabeBirthdayDay = sharedPref.getInt(
                getString(R.string.save_baby_birthday_day),
                0
            )
            selectedBabeBirthdayMonth = sharedPref.getInt(
                getString(R.string.save_baby_birthday_month),
                0
            )
            selectedBabeBirthdayYear = sharedPref.getInt(
                getString(R.string.save_baby_birthday_year),
                0
            )
            calculateBabyAge()
            uriImage = sharedPref.getString(getString(R.string.save_loaded_image_uri), "").toString()
        }
    }


    private fun checkPermissionForImage() {
        if ((checkSelfPermission(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            && (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)) {
            requestPermissions(arrayOf(READ_EXTERNAL_STORAGE), PERMISSION_CODE_READ) // GIVE AN INTEGER VALUE FOR PERMISSION_CODE_READ LIKE 1001
            requestPermissions(arrayOf(WRITE_EXTERNAL_STORAGE), PERMISSION_CODE_WRITE) // GIVE AN INTEGER VALUE FOR PERMISSION_CODE_WRITE LIKE 1002
        }
    }

    private fun pickImageFromGallery() {
        val photoPickerIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, IMAGE_PICK_CODE) // GIVE AN INTEGER VALUE FOR IMAGE_PICK_CODE LIKE 1000
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            babyImageView.setImageURI(data?.data)
            uriImage = data?.data.toString()

            //save after restart
            val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
            with(sharedPref.edit()) {
                putString(getString(R.string.save_loaded_image_uri), uriImage)
                apply()
            }
        }
    }


    private fun focusFunctionalityOnNameEditText() {
        babyNameEditText.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus){
                if (babyNameEditText.text?.isEmpty() == true) {
                    nameFilled = false
                    nextButton.isEnabled = false
                }
                else
                    nameFilled = true
            }else{
                nextButton.isEnabled = false
            }
        }
    }


    //this function is responsible on updating the Date textView after picking the date we want
    private fun focusAndClickFunctionalityOnDatePicker() {

        val builder = datePicker()
        builder.setTitleText("Select birthday date")
        val materialDatePicker = builder.build()
        materialDatePicker.isCancelable = false

        //Opens dialog for picking Date
        this.selectedDateEditText.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                materialDatePicker.show(supportFragmentManager, "TIME_PICKER")
                dateFilled = true
                this.selectedDateEditText.clearFocus()
                checkIfDetailedFilled()
            }
        }

        //Opens dialog for picking Date when dialog closed without picking date (solves bug)
        this.selectedDateEditText.setOnClickListener {
            materialDatePicker.show(supportFragmentManager, "TIME_PICKER")
            dateFilled = false
        }

        //updates the textView with the selected Date
        materialDatePicker.addOnPositiveButtonClickListener {
            this.selectedDateEditText.setText(materialDatePicker.headerText)
            val selectedBirthday = Calendar.getInstance(TimeZone.getDefault())
            selectedBirthday.time = Date(it)
            calculateBabyAge(selectedBirthday)
        }


        //updates the nextButton status when canceled selection
        materialDatePicker.addOnNegativeButtonClickListener {
            if (this.selectedDateEditText.text?.isEmpty() == true) {
                dateFilled = false
                nextButton.isEnabled = false
            }
        }
    }


    private fun calculateBabyAge(birthdayDate: Calendar? = null) {
        val date = DatePicker(this)

        if (birthdayDate != null) {
            babyAgeInYears = 0
            babyAgeInMonths = 0

            selectedBabeBirthdayDay = birthdayDate.get(Calendar.DAY_OF_MONTH)
            selectedBabeBirthdayMonth = birthdayDate.get(Calendar.MONTH) + 1
            selectedBabeBirthdayYear = birthdayDate.get(Calendar.YEAR)
        }
        val todayDay = date.dayOfMonth
        val todayMonth = date.month + 1
        val todayYear = date.year


        when {
            (todayYear > selectedBabeBirthdayYear) -> {
                if ( (todayMonth > selectedBabeBirthdayMonth) || ((todayMonth == selectedBabeBirthdayMonth) && (todayDay >= selectedBabeBirthdayDay)))
                    babyAgeInYears = todayYear - selectedBabeBirthdayYear
                else if ( (todayMonth < selectedBabeBirthdayMonth) || ((todayMonth == selectedBabeBirthdayMonth) && (todayDay < selectedBabeBirthdayDay)))
                    babyAgeInYears = todayYear - selectedBabeBirthdayYear - 1
                babyAgeInMonths = if ((babyAgeInYears == 0) && (todayDay < selectedBabeBirthdayDay)) //get months of baby if he is under 1 years age
                    12 - selectedBabeBirthdayMonth + todayMonth - 1
                else if(todayDay < selectedBabeBirthdayDay)
                    12 - selectedBabeBirthdayMonth + todayMonth - 1
                else
                    12 - selectedBabeBirthdayMonth + todayMonth
            }
            (todayMonth > selectedBabeBirthdayMonth) -> {
                babyAgeInMonths = if(todayDay >= selectedBabeBirthdayDay){
                    todayMonth - selectedBabeBirthdayMonth
                } else{
                    todayMonth - selectedBabeBirthdayMonth - 1
                }
            }
            else -> {
                babyAgeInYears = 0
                babyAgeInMonths = 0
            }
        }
    }
    
    private fun checkIfDetailedFilled() {
        if ((dateFilled && nameFilled) || ((babyNameEditText.text?.isNotEmpty() == true) && (selectedDateEditText.text?.isNotEmpty() == true)))
            nextButton.isEnabled = true
    }


}