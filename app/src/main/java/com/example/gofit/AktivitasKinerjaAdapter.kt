package com.example.gofit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.gofit.models.BookingGym
import com.example.gofit.models.BookingKelas
import com.example.gofit.models.HistoryAktivitasInstruktur
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AktivitasKinerjaAdapter(private var historys: List<HistoryAktivitasInstruktur>,context: Context):
    RecyclerView.Adapter<AktivitasKinerjaAdapter.ViewHolder>() {
    private val context: Context


    init {
        this.context = context
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.activity_aktivitas_kinerja_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = historys[position]

        holder.tvNamaInstruktur.text = "Nama Instruktur : ${data.NAMA_INSTRUKTUR}"
        holder.tvNamaKelas.text = "Nama Kelas: ${data.NAMA_KELAS}"
        holder.tvJamKelas.text = "Jam Kelas: ${data.WAKTU_JADWAL_UMUM}"
        holder.tvTanggalJadwalUmum.text = "Tanggal Jadwal: ${data.TANGGAL_JADWAL_UMUM}"
        holder.tvTarifKelas.text = "Tarif: ${data.TARIF_KELAS}"
        holder.tvHari.text = "Hari Jadwal Umum: ${data.HARI_JADWAL_UMUM}"



//        holder.iconDel.setOnClickListener {
//            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
//            materialAlertDialogBuilder.setTitle("Konfirmasi")
//                .setMessage("Apakah anda yakin ingin membatalkan booking kelas ini?")
//                .setNegativeButton("Batal", null)
//                .setPositiveButton("Iya"){ _, _ ->
////                    if (context is BookingKelasActivity){
////                        context.cancelBooking(data.KODE_BOOKING_KELAS)
////                    }
//                }
//                .show()
//        }
    }

    override fun getItemCount(): Int {
        return historys.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var tvNamaInstruktur: TextView
        var tvNamaKelas: TextView
        var tvTarifKelas: TextView
        var tvTanggalJadwalUmum: TextView
        var tvJamKelas: TextView
        var tvHari: TextView
        //        var iconDel: ImageButton
        var cvBook: CardView

        init {
            tvNamaInstruktur = view.findViewById(R.id.tv_namainstruktur)
            tvNamaKelas = view.findViewById(R.id.tv_namakelas)
            tvJamKelas = view.findViewById(R.id.tv_jamkelas)
            tvTanggalJadwalUmum = view.findViewById(R.id.tv_tanggaljadwalumum)
            tvTarifKelas = view.findViewById(R.id.tv_tarifkelas)
            tvHari = view.findViewById(R.id.tv_hari)

//            iconDel = view.findViewById(R.id.icon_delete)
            cvBook = view.findViewById(R.id.cv_book)
        }

    }

}