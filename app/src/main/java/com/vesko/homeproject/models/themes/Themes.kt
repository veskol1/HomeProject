package com.vesko.homeproject.models.themes

import com.vesko.homeproject.R

data class Themes(val themesList: ArrayList<ThemeType>){

    constructor() : this(
        themesList = arrayListOf(
            ThemeType(
                1,
                "yellow",
                R.drawable.android_elephant_popup_wide,
                R.drawable.camera_icon_yellow,
                R.drawable.default_place_holder_yellow
            ),
            ThemeType(
                2,
                "blue",
                R.drawable.android_pelican_popup_wide,
                R.drawable.camera_icon_blue,
                R.drawable.default_place_holder_blue
            ),
            ThemeType(
                3,
                "green",
                R.drawable.android_fox_popup_wide,
                R.drawable.camera_icon_green,
                R.drawable.default_place_holder_green
            ),
        )
    )


}
