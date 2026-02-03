package com.example.p4_madrid_adrianerika.models

enum class SubType(val parentType: Type) {
    // RESTAURANTS
    R_PIZZERIA(Type.RESTAURANT),
    R_HAMBURGER(Type.RESTAURANT),
    R_BUFFET(Type.RESTAURANT),

    // CINEMAS

    C_MANY_S(Type.CINEMA),
    C_FEW_S(Type.CINEMA),

    // PARKS

    P_BIG(Type.PARK),
    P_LIT(Type.PARK)
}