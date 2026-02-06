package com.example.p4_madrid_adrianerika.models

import com.example.p4_madrid_adrianerika.R

enum class SubType(val parentType: Type, val subTitle: Int) {
    // RESTAURANTS
    R_PIZZERIA(Type.RESTAURANT,R.string.PIZZERIAS),
    R_HAMBURGER(Type.RESTAURANT,R.string.HAMBURGUERS),
    R_BUFFET(Type.RESTAURANT,R.string.BUFFETS),

    // CINEMAS
    C_MANY_S(Type.CINEMA,R.string.MANY_SEATS),
    C_FEW_S(Type.CINEMA,R.string.FEW_SEATS),

    // PARKS
    P_BIG(Type.PARK,R.string.BIG_PARKS),
    P_LIT(Type.PARK,R.string.LITTLE_PARKS)
}