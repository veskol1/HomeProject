package com.vesko.homeproject.models.numbers

import com.vesko.homeproject.R

class Numbers(val numbersList : ArrayList<ImageNumber>){

    constructor() : this(
        numbersList = arrayListOf(
            ImageNumber("zero", 0f, R.drawable.zero),
            ImageNumber("one", 1f, R.drawable.one),
            ImageNumber("two", 2f, R.drawable.two),
            ImageNumber("three", 3f, R.drawable.three),
            ImageNumber("four", 4f, R.drawable.four),
            ImageNumber("five", 5f, R.drawable.five),
            ImageNumber("six", 6f, R.drawable.six),
            ImageNumber("seven", 7f, R.drawable.seven),
            ImageNumber("eight", 8f, R.drawable.eight),
            ImageNumber("nine", 9f, R.drawable.nine),
            ImageNumber("ten", 10f, R.drawable.ten),
            ImageNumber("eleven", 11f, R.drawable.eleven),
            ImageNumber("twelve", 12f, R.drawable.twelve),
            ImageNumber("one_half", 1.5f, R.drawable.one_half),
        )
    )


}

