package com.example.gofit.api

class LoginApi {

    companion object {
//  val BASE_URL = "http://192.168.0.116/WebP3L/public/api/"
    val BASE_URL = "https://quashafit.my.id/api/"

        val LOGIN_URL = BASE_URL + "loginUser"
        val RESET_PASSWORD_URL = BASE_URL + "gantiPassword"
        val LOGOUT_URL = BASE_URL + "logout"
        val tampilanJadwal = BASE_URL + "dataJadwal"
    }
}