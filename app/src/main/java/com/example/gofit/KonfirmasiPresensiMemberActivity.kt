package com.example.gofit

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gofit.api.BookingKelasApi
import com.example.gofit.databinding.ActivityKonfirmasiPresensiMemberBinding
import com.example.gofit.models.HistoriBookingKelasMember
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class KonfirmasiPresensiMemberActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityKonfirmasiPresensiMemberBinding
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKonfirmasiPresensiMemberBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()
        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)

        val id = sharedPreferences.getInt("id",0)
        val tanggal = sharedPreferences.getString("tanggal_harian",null)
        queue = Volley.newRequestQueue(this)

        binding.srKonfirmasiPresensiMember.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener{
            if (tanggal != null) {
                allData(tanggal)
            }
        })
        if (tanggal != null) {
            allData(tanggal)
        }
    }
    private fun allData(id: String) {
        binding.srKonfirmasiPresensiMember.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, BookingKelasApi.HISTORIPRESENSIKELAS + id, Response.Listener { response ->
                var jo = JSONObject(response.toString())
                var history = arrayListOf<HistoriBookingKelasMember>()
                var id : Int = jo.getJSONArray("data").length()

                for(i in 0 until id) {
                    var data = HistoriBookingKelasMember(
                        jo.getJSONArray("data").getJSONObject(i).getString("KODE_BOOKING_KELAS"),
                        jo.getJSONArray("data").getJSONObject(i).getString("NAMA_KELAS"),
                        jo.getJSONArray("data").getJSONObject(i).getString("ID_MEMBER"),
                        jo.getJSONArray("data").getJSONObject(i).getString("NAMA_MEMBER"),
                        jo.getJSONArray("data").getJSONObject(i).getString("WAKTU_PRESENSI"),
                        jo.getJSONArray("data").getJSONObject(i).getString("STATUS_PRESENSI_KELAS")
                    )
                    history.add(data)
                }
                var data_array: Array<HistoriBookingKelasMember> = history.toTypedArray()

                val layoutManager = LinearLayoutManager(this)
                val adapter : KonfirmasiPresensiMemberAdapter = KonfirmasiPresensiMemberAdapter(history,this)
                val rvPermission : RecyclerView = findViewById(R.id.rv_KonfirmasiMember)

                rvPermission.layoutManager = layoutManager
                rvPermission.setHasFixedSize(true)
                rvPermission.adapter = adapter

                binding.srKonfirmasiPresensiMember.isRefreshing = false

                if (!data_array.isEmpty()) {
//                    Toast.makeText(this@JanjiTemuActivity, "Data Berhasil Diambil!", Toast.LENGTH_SHORT).show()
                }else {

                }

            }, Response.ErrorListener { error ->
                binding.srKonfirmasiPresensiMember.isRefreshing = true
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
//                    Toast.makeText(this@JanjiTemuActivity, errors.getString("message"), Toast.LENGTH_SHORT).show()

                    binding.srKonfirmasiPresensiMember.isRefreshing = false
                } catch (e: Exception){
//                    Toast.makeText(this@JanjiTemuActivity, e.message, Toast.LENGTH_SHORT).show()

                    binding.srKonfirmasiPresensiMember.isRefreshing = false
                }

            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Authorization"] = "Bearer " + sharedPreferences.getString("token",null);
                return headers
            }
        }
        queue!!.add(stringRequest)
    }



    fun update(kode_booking: String, status: String){
        val booking = HistoriBookingKelasMember(
            kode_booking,
            null,
            null,
            null,
            null,
            status,
        )

        val stringRequest: StringRequest =
            object : StringRequest(Request.Method.POST, BookingKelasApi.KONFIMASIPRESENSI, Response.Listener { response ->
                val gson = Gson()
                var booking_data = gson.fromJson(response, HistoriBookingKelasMember::class.java)

                var resJO = JSONObject(response.toString())
                val userobj = resJO.getJSONObject("data")

                if(booking_data!= null) {

                    val intent = Intent(this@KonfirmasiPresensiMemberActivity, JadwalInstrukturPresensiActivity::class.java)
                    sharedPreferences.edit()
                        .putString("tanggal_harian",null)
                        .apply()
                    startActivity(intent)
                }
                else {

                }
                return@Listener
            }, Response.ErrorListener { error ->
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(this@KonfirmasiPresensiMemberActivity, errors.getString("message"), Toast.LENGTH_SHORT).show()

                }catch (e: java.lang.Exception) {
                    Toast.makeText(this@KonfirmasiPresensiMemberActivity, e.message,
                        Toast.LENGTH_LONG).show();
                }
            }) {
                @kotlin.jvm.Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(booking)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                @kotlin.jvm.Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    headers["Authorization"] = "Bearer " + sharedPreferences.getString("token",null);
                    return headers
                }

                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8;"
                }
            }
        queue!!.add(stringRequest)
    }
}