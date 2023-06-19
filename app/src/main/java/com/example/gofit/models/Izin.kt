package com.example.gofit.models

class Izin(
    var ID_INSTRUKTUR: Int, var INSTRUKTUR_PENGGANTI: String,
    var TANGGAL_IZIN: String, var TANGGAL_PENGAJUAN: String?, var KETERANGAN_IZIN: String,
    var STATUS_KONFIRMASI: String?, var TANGGAL_KONFIRMASI: String?
) {
}