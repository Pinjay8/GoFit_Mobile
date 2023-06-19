package com.example.gofit


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.gofit.models.JadwalInstruktur

class JadwalInstrukturPresensiAdapter (private var instructors: List<JadwalInstruktur>, context: Context): RecyclerView.Adapter<JadwalInstrukturPresensiAdapter.ViewHolder>() {
    private val context: Context

    init {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JadwalInstrukturPresensiAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.activity_jadwal_instruktur_presensi_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: JadwalInstrukturPresensiAdapter.ViewHolder, position: Int) {
        val data = instructors[position]
        val preferences = context.getSharedPreferences("login", Context.MODE_PRIVATE)
        holder.tvKelas.text = data.NAMA_KELAS
        holder.tvInstruktur.text = data.NAMA_INSTRUKTUR
        holder.tvTanggal.text = data.TANGGAL_HARIAN
        holder.tvHari.text = data.HARI_KELAS
        holder.tvKeterangan.text = data.STATUS_JADWAL_HARIAN

        holder.cvSchedule.setOnClickListener {
            if (context is JadwalInstrukturPresensiActivity){
                val intent = Intent(context,KonfirmasiPresensiMemberActivity::class.java)
                preferences.edit()
                    .putString("tanggal_harian",data.TANGGAL_HARIAN)
                    .apply()
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return instructors.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var tvKelas: TextView
        var tvInstruktur: TextView
        var tvKeterangan: TextView
        var tvHari: TextView
        var tvTanggal: TextView
        var cvSchedule: CardView

        init {
            tvKelas = view.findViewById(R.id.tv_nama_kelas_jadwalinsturktur)
            tvInstruktur = view.findViewById(R.id.tv_nama_instruktur_jadwalinstruktur)
            tvKeterangan = view.findViewById(R.id.tv_keterangan_jadwal_jadwalinstruktur)
            tvHari = view.findViewById(R.id.tv_hari_jadwal_jadwalinstruktur)
            tvTanggal = view.findViewById(R.id.tv_tanggal_jadwal_harian_jadwalinstruktur)
            cvSchedule = view.findViewById(R.id.cv_jadwalInstruktur)
        }

    }
}