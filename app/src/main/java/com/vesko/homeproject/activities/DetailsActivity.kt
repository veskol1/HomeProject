package com.vesko.homeproject.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.vesko.homeproject.R
import com.vesko.homeproject.models.numbers.ImageNumber
import com.vesko.homeproject.models.numbers.Numbers
import com.vesko.homeproject.models.themes.ThemeType
import com.vesko.homeproject.models.themes.Themes
import com.vesko.homeproject.utils.ScreenshotUtil
import com.vesko.homeproject.utils.StoreImageUtil
import java.io.File


class DetailsActivity : AppCompatActivity() {
    private lateinit var backgroundImage: ImageView
    private lateinit var numberAgeImageView: ImageView
    private lateinit var babyNameTextView: TextView
    private lateinit var babyAgeTitleTextView: TextView
    private lateinit var placeholderImage: ImageView
    private lateinit var cameraImageButton: ImageButton
    private lateinit var shareButtonLayout: ConstraintLayout
    private lateinit var shareButton: ImageButton
    private lateinit var backButton: ImageButton
    private lateinit var themesList: ArrayList<ThemeType>
    private lateinit var imageNumbersList: ArrayList<ImageNumber>
    private var uriImage = "" //will hold uri(string path) to the selected image from phones gallery

    private val IMAGE_URL = "https://as2.ftcdn.net/jpg/02/16/85/19/500_F_216851969_42JnrCBh9acjRk3hkFRzfKLqoA3CpDmk.jpg"
    private val BABE_AGE_YEARS = "com.vesko.homeproject.mainactivty.activities.babyAgeYears"
    private val BABE_AGE_MONTHS = "com.vesko.homeproject.mainactivty.activities.babyAgeMonths"
    private val BABE_NAME = "com.vesko.homeproject.mainactivty.activities.babysName"
    private val BABE_IMAGE = "com.vesko.homeproject.mainactivtyactivities.babysImage"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        initialize()
        updateUI()
        updateCameraButtonPosition()
        buttonsFunctionality()
    }

    private fun buttonsFunctionality() {
        cameraImageButton.setOnClickListener {
            Glide.with(this)
                .load(IMAGE_URL)
                .centerCrop()
                .into(placeholderImage)
        }

        backButton.setOnClickListener {
            finish()
        }

        shareButton.setOnClickListener {
            takeAndStoreScreenshot()
            shareScreenshot()
        }
    }


    private fun initialize() {
        backgroundImage = findViewById(R.id.background_view)
        numberAgeImageView = findViewById(R.id.number_image_view)
        babyNameTextView = findViewById(R.id.baby_name_text_view)
        babyAgeTitleTextView = findViewById(R.id.months_years_text_view)
        cameraImageButton = findViewById(R.id.camera_button)
        shareButton = findViewById(R.id.share_button)
        shareButtonLayout = findViewById(R.id.share_button_layout)
        placeholderImage = findViewById(R.id.placeholder_image)
        backButton = findViewById(R.id.back_button)

        themesList = Themes().themesList
        imageNumbersList = Numbers().numbersList
    }


    private fun updateUI() {
        val ageInYears = intent.getIntExtra(BABE_AGE_YEARS, 0)
        val ageInMonths = intent.getIntExtra(BABE_AGE_MONTHS, 0)
        val babyName = intent.getStringExtra(BABE_NAME).toString()
        val uriImage = intent.getStringExtra(BABE_IMAGE).toString()

        //Updates baby's name and actual age
        babyNameTextView.text = getString(R.string.baby_name, babyName)
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

        //changes theme background/camera/placeholder images
        themesList.shuffle()
        val theme = themesList[0]
        backgroundImage.setImageResource(theme.themeBackgroundImagePath)
        cameraImageButton.setImageResource(theme.cameraImagePath)
        placeholderImage.setImageResource(theme.placeHolderImagePath)

        if(uriImage.isNotEmpty())
            placeholderImage.setImageURI(uriImage.toUri())

    }

    //Take and store screenshot
    private fun takeAndStoreScreenshot() {
        val bitmap = ScreenshotUtil.instance.takeScreenshotForView(backgroundImage.rootView, arrayListOf(backButton,cameraImageButton,shareButtonLayout))
        StoreImageUtil.instance.storeImage(bitmap, this.filesDir)
    }

    //Share screenshot using Intent
    private fun shareScreenshot() {
        val imageFile = File(this.filesDir, "icon.png")
        // generate URI, I defined authority as the application ID in the Manifest, the last param is file I want to open
        val uri = FileProvider.getUriForFile(this, "com.vesko.homeproject.fileprovider", imageFile)

        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            type = "image/png"
        }
        startActivity(shareIntent)
    }


    private fun updateCameraButtonPosition() {
        var horizontalBias = 0.74f
        val density = resources.displayMetrics.density
        if (density <= 2.7)
            horizontalBias = 0.725f
        else if (density >= 3.5)
            horizontalBias = 0.70f

        val cl = findViewById<View>(R.id.activity_details_constraint) as ConstraintLayout
        val cs = ConstraintSet()
        cs.clone(cl)
        cs.setHorizontalBias(R.id.camera_button, horizontalBias)  //update actualHorizontalBias
        cs.applyTo(cl)
    }



}