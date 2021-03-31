package com.vesko.homeproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.OnFocusChangeListener
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialDatePicker.Builder.datePicker
import com.google.android.material.textfield.TextInputEditText
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var babyNameEditText: TextInputEditText
    private lateinit var selectedDateEditText: TextInputEditText
    private lateinit var nextButton: Button
    private lateinit var babyImageView: ImageView
    private var nameFilled = false
    private var dateFilled = false
    private var babyAgeInMonths = 0
    private var babyAgeInYears = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        babyNameEditText = findViewById(R.id.baby_name_edit_text)
        selectedDateEditText = findViewById(R.id.date_select)
        nextButton = findViewById(R.id.show_birthday_screen_button)
        babyImageView = findViewById(R.id.baby_image_view)

        focusFunctionalityNameEditText()
        focusAndClickFunctionalityDatePicker()

        Glide.with(this).load("https://as2.ftcdn.net/jpg/02/16/85/19/500_F_216851969_42JnrCBh9acjRk3hkFRzfKLqoA3CpDmk.jpg").into(
            babyImageView
        )

        nextButton.setOnClickListener {
           // val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("com.vesko.homeproject.mainactivty.babyAgeYears",babyAgeInYears)
            intent.putExtra("com.vesko.homeproject.mainactivty.babyAgeMonths",babyAgeInMonths)
            intent.putExtra("com.vesko.homeproject.mainactivty.babysName", babyNameEditText.text.toString())
            startActivity(intent)
        }
    }


    private fun focusFunctionalityNameEditText() {
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
    private fun focusAndClickFunctionalityDatePicker() {

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


            Log.d("here","here1=")
            val selectedBirthday = Calendar.getInstance(TimeZone.getDefault())
            selectedBirthday.time = Date(it)
            calculateBabyAge(selectedBirthday)
            Log.d("here","here3=")
        }


        //updates the nextButton status when canceled selection
        materialDatePicker.addOnNegativeButtonClickListener {
            if (this.selectedDateEditText.text?.isEmpty() == true) {
                dateFilled = false
                nextButton.isEnabled = false
            }
        }
    }

    private fun calculateBabyAge(birthdayDate: Calendar) {
        val date = DatePicker(this)
        Log.d("here","here2=")
        val selectedDay = birthdayDate.get(Calendar.DAY_OF_MONTH)
        val selectedMonth = birthdayDate.get(Calendar.MONTH) + 1
        val selectedYear = birthdayDate.get(Calendar.YEAR)

        val todayDay = date.dayOfMonth
        val todayMonth = date.month + 1
        val todayYear = date.year

        if ((todayYear - selectedYear) > 0)
            babyAgeInYears = todayYear - selectedYear
        else if (todayMonth > selectedMonth){
            if(todayDay >= selectedDay){
                babyAgeInMonths = todayMonth - selectedMonth
            }
            else{
                babyAgeInMonths = todayMonth - selectedMonth - 1
            }
        }
        else {
            babyAgeInYears = 0
            babyAgeInMonths = 0
        }

        Log.d("here","babyAgeInMonths="+babyAgeInMonths)
        Log.d("here","babyAgeInYears="+babyAgeInYears)

    }


    private fun checkIfDetailedFilled() {
        if (dateFilled && nameFilled)
            nextButton.isEnabled = true
    }


}