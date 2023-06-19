package com.example.gofit.api

class MemberApi {

    companion object {
//        val BASE_URL = "http://192.168.0.116/WebP3L/public/api/"
        val BASE_URL = "https://quashafit.my.id/api/"

        val GET_ALL_URL = BASE_URL + "indexGym/"

        val BATALGYM = BASE_URL + "batalGym/"
        val BATALKELAS= BASE_URL + "batalKelas/"
        val STOREDATA = BASE_URL + "tambahBooking"
        val STOREDATAKELAS = BASE_URL + "tambahBookingKelas"
        val GETDATAMEMBER = BASE_URL + "dataMember/"

        val GETDATABOOKINGKELAS = BASE_URL + "dataBookingKelas/"

//        val GET_BY_ID_URL = BASE_URL + "member/"
////       val ADD_URL = BASE_URL + "member"
////       val UPDATE_URL = BASE_URL + "member/"
////       val DELETE_URL = BASE_URL + "member/"
//        val LOGIN_URL = BASE_URL + "member/login_process"
    }
}