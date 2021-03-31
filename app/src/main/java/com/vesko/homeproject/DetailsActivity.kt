package com.vesko.homeproject

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.bumptech.glide.Glide

class DetailsActivity : AppCompatActivity() {
    data class Theme(val id: Int, val color: String, val themeBackgroundImagePath: Int, val cameraImagePath: Int, val placeHolderImagePath: Int)
    data class ImageNumber(val name: String, val value: Float, val numberPath: Int)

    private lateinit var backgroundImage: ImageView
    private lateinit var numberAgeImageView: ImageView
    private lateinit var babyNameTextView: TextView
    private lateinit var babyAgeTitleTextView: TextView
    private lateinit var placeholderImage: ImageView
    private lateinit var cameraImageButton: ImageButton
    private lateinit var shareButton: ImageButton
    private lateinit var backButton: ImageButton
    private lateinit var themesList: ArrayList<Theme>
    private lateinit var imageNumbersList: ArrayList<ImageNumber>
    private val IMAGE_URL = "https://as2.ftcdn.net/jpg/02/50/10/03/1000_F_250100321_1hYz6jdwgiKmXz3mC4q0BCnR83jSTSbL.jpg"
    private val BABE_AGE_YEARS = "com.vesko.homeproject.mainactivty.babyAgeYears"
    private val BABE_AGE_MONTHS = "com.vesko.homeproject.mainactivty.babyAgeMonths"
    private val BABE_NAME = "com.vesko.homeproject.mainactivty.babysName"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        initialize()
        updateUI()
        updateCameraButtonPosition()
        updatePlaceholderImagePosition1()

        shareButton.setOnClickListener {

        }

        cameraImageButton.setOnClickListener {
            Glide.with(this)
                .load(IMAGE_URL)
                .centerCrop()
                .into(placeholderImage)
        }

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun initialize() {
        backgroundImage = findViewById(R.id.background_view)
        numberAgeImageView = findViewById(R.id.number_image_view)
        babyNameTextView = findViewById(R.id.baby_name_text_view)
        babyAgeTitleTextView = findViewById(R.id.months_years_text_view)
        cameraImageButton = findViewById(R.id.camera_button)
        shareButton = findViewById(R.id.share_button)
        placeholderImage = findViewById(R.id.placeholder_image)
        backButton = findViewById(R.id.back_button)

        themesList = arrayListOf(
            Theme(1 , "yellow", R.drawable.android_elephant_popup_wide, R.drawable.camera_icon_yellow , R.drawable.default_place_holder_yellow),
            Theme(2 , "blue", R.drawable.android_pelican_popup_wide, R.drawable.camera_icon_blue , R.drawable.default_place_holder_blue),
            Theme(3 , "green", R.drawable.android_fox_popup_wide, R.drawable.camera_icon_green , R.drawable.default_place_holder_green),
        )
        imageNumbersList = arrayListOf(
            ImageNumber("zero", 0f , R.drawable.zero),
            ImageNumber("one", 1f , R.drawable.one),
            ImageNumber("two", 2f , R.drawable.two),
            ImageNumber("three", 3f , R.drawable.three),
            ImageNumber("four", 4f , R.drawable.four),
            ImageNumber("five", 5f , R.drawable.five),
            ImageNumber("six", 6f , R.drawable.six),
            ImageNumber("seven", 7f , R.drawable.seven),
            ImageNumber("eight", 8f , R.drawable.eight),
            ImageNumber("nine", 9f , R.drawable.nine),
            ImageNumber("ten", 10f , R.drawable.ten),
            ImageNumber("eleven", 11f , R.drawable.eleven),
            ImageNumber("twelve", 12f , R.drawable.twelve),
            ImageNumber("one_half", 1.5f , R.drawable.one_half),
        )

    }

    private fun updateUI() {
        val ageInYears = intent.getIntExtra(BABE_AGE_YEARS, -1)
        val ageInMonths = intent.getIntExtra(BABE_AGE_MONTHS, -1)
        val babyName = intent.getStringExtra(BABE_NAME).toString()

        babyNameTextView.text = getString(R.string.baby_name,babyName)

        if (ageInYears == 0) {
            numberAgeImageView.setImageResource(imageNumbersList[ageInMonths].numberPath)
            babyAgeTitleTextView.text = getString(R.string.baby_age, "MONTHS")
        }
        else {
            if(ageInMonths == 6)
                numberAgeImageView.setImageResource(imageNumbersList[13].numberPath)
            else
                numberAgeImageView.setImageResource(imageNumbersList[ageInYears].numberPath)
            babyAgeTitleTextView.text = getString(R.string.baby_age, "YEARS")
        }

        themesList.shuffle()
        val theme = themesList[0]
        backgroundImage.setImageResource(theme.themeBackgroundImagePath)
        cameraImageButton.setImageResource(theme.cameraImagePath)
        placeholderImage.setImageResource(theme.placeHolderImagePath)
    }

    private fun updatePlaceholderImagePosition1(){
        var verticalBias = 0.81f

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels.toFloat()
        val height = displayMetrics.heightPixels.toFloat()

        //val baseRatio = 0.5625
        val calcRatio = (width/height)

        if(calcRatio < 0.54)
            verticalBias = 0.79f
        else if (calcRatio > 0.58)
            verticalBias = 0.8f

        val cl = findViewById<View>(R.id.activity_constraint) as ConstraintLayout
        val cs = ConstraintSet()
        cs.clone(cl)
        cs.setVerticalBias(R.id.placeholder_image, verticalBias)  //update actualHorizontalBias
        cs.applyTo(cl)


    }

    private fun updateCameraButtonPosition() {
        var horizontalBias = 0.74f
        val density = resources.displayMetrics.density
        if (density <= 2.7)
            horizontalBias = 0.725f
        else if (density >= 3.5)
            horizontalBias = 0.70f

        val cl = findViewById<View>(R.id.activity_constraint) as ConstraintLayout
        val cs = ConstraintSet()
        cs.clone(cl)
        cs.setHorizontalBias(R.id.camera_button, horizontalBias)  //update actualHorizontalBias
        cs.applyTo(cl)
    }



}