package com.example.p4_madrid_adrianerika.models

import com.example.p4_madrid_adrianerika.R

enum class Type(val img: Int, val titleResId: Int) {
    RESTAURANT(R.drawable.r,R.string.RESTAURANTS),
    CINEMA(R.drawable.c,R.string.CINEMAS),
    PARK(R.drawable.p,R.string.PARKS)
}