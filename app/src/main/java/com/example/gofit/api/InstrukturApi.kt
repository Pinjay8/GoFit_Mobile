package com.example.gofit.api

class InstrukturApi {

    companion object {
//        val BASE_URL = "http://192.168.0.116/WebP3L/public/api/"
        val BASE_URL = "https://quashafit.my.id/api/"

//        val GET_ALL_URL = BASE_URL + "Instruktur"
        //        val GET_BY_ID_URL = BASE_URL + "profileUser/"

        val GET_ALL_URL = BASE_URL + "izinInstruktur/"
        val GETDATAJADWAL = BASE_URL + "dataJadwalHarian/"
        val ADDIZIN = BASE_URL + "tambahIzin"
        val GETDATAINSTRUKTUR = BASE_URL + "dataInstruktur/"
        val GETINSTRUKTUR = BASE_URL + "dataKinerja/"
    }
}