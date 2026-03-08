package com.example.p4_madrid_adrianerika.ui

// All app strings in English and Spanish
// Add new strings here when needed
object AppStrings {

    // --- LOGIN ---
    val loginTitle = mapOf("EN" to "Discover Madrid", "ES" to "Descubre Madrid")
    val loginButton = mapOf("EN" to "LOGIN", "ES" to "ENTRAR")
    val loginUsername = mapOf("EN" to "Username", "ES" to "Usuario")
    val loginPassword = mapOf("EN" to "Password", "ES" to "Contraseña")
    val loginRemember = mapOf("EN" to "Remember me", "ES" to "Recuérdame")
    val loginNoAccount = mapOf("EN" to "Don't have an account? ", "ES" to "¿No tienes cuenta? ")
    val loginRegister = mapOf("EN" to "Register", "ES" to "Regístrate")
    val loginError = mapOf("EN" to "Fill in all fields", "ES" to "Rellena todos los campos")

    // --- REGISTER ---
    val registerTitle = mapOf("EN" to "Create your account", "ES" to "Crea tu cuenta")
    val registerButton = mapOf("EN" to "REGISTER", "ES" to "REGISTRARSE")
    val registerHaveAccount = mapOf("EN" to "Already have an account? ", "ES" to "¿Ya tienes cuenta? ")
    val registerLogin = mapOf("EN" to "Login", "ES" to "Iniciar sesión")
    val registerError = mapOf("EN" to "Fill in all fields", "ES" to "Rellena todos los campos")

    // --- LIST ---
    val filter = mapOf("EN" to "FILTER", "ES" to "FILTRAR")
    val favorites = mapOf("EN" to "Favorites", "ES" to "Favoritos")

    // --- INFO ---
    val favorite = mapOf("EN" to "Favorite", "ES" to "Favorito")
    val distanceCalculating = mapOf("EN" to "Calculating distance...", "ES" to "Calculando distancia...")
    val distanceNotAvailable = mapOf("EN" to "Distance not available", "ES" to "Distancia no disponible")
    val distanceDenied = mapOf("EN" to "Permission denied", "ES" to "Permiso denegado")
    val mapsButton = mapOf("EN" to "MAPS", "ES" to "MAPAS")
    val shareButton = mapOf("EN" to "SHARE", "ES" to "COMPARTIR")
    val shareText = mapOf("EN" to "Check out this place: ", "ES" to "Mira este sitio: ")

    // --- HEADER ---
    val darkMode = mapOf("EN" to "Dark Mode", "ES" to "Modo oscuro")
    val lightMode = mapOf("EN" to "Light Mode", "ES" to "Modo claro")
    val logout = mapOf("EN" to "Log out", "ES" to "Cerrar sesión")
    val languageButton = mapOf("EN" to "Español", "ES" to "English")
    val redTheme = mapOf("EN" to "Red Theme", "ES" to "Tema Rojo")
    val blueTheme = mapOf("EN" to "Blue Theme", "ES" to "Tema Azul")
    val pinkTheme = mapOf("EN" to "Pink Theme", "ES" to "Tema Rosa")
    val aboutUs = mapOf("EN" to "About us", "ES" to "Sobre nosotros")

    // --- HOME ---
    val restaurants = mapOf("EN" to "RESTAURANTS", "ES" to "RESTAURANTES")
    val cinemas = mapOf("EN" to "CINEMAS", "ES" to "CINES")
    val parks = mapOf("EN" to "PARKS", "ES" to "PARQUES")

    // --- INFO ---
    val distanceFrom = mapOf("EN" to "%.1f km from you", "ES" to "%.1f km de ti")
}

// Extension function to get string by current language
fun Map<String, String>.tr(lang: String): String = this[lang] ?: this["EN"] ?: ""