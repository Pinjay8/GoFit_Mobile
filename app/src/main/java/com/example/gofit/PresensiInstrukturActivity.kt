package com.example.gofit

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gofit.api.PresensiInstrukturApi
import com.example.gofit.databinding.ActivityPresensiInstrukturBinding
import com.example.gofit.models.InstructorAttendance
import com.example.gofit.models.PresensiInstruktur
import com.google.gson.Gson
import com.shashank.sony.fancytoastlib.FancyToast
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class PresensiInstrukturActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityPresensiInstrukturBinding
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPresensiInstrukturBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()
        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)

        val id = sharedPreferences.getInt("id",0)
        queue = Volley.newRequestQueue(this)

        binding.srPresensi.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener{allData()})
        allData()
    }

    private fun allData() {
        binding.srPresensi.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, PresensiInstrukturApi.GET_ALL_URL, Response.Listener { response ->
                var jo = JSONObject(response.toString())
                var schedule = arrayListOf<PresensiInstruktur>()
                var id : Int = jo.getJSONArray("data").length()

                for(i in 0 until id) {
                    var data = PresensiInstruktur(
                        jo.getJSONArray("data").getJSONObject(i).getString("TANGGAL_HARIAN"),
                        jo.getJSONArray("data").getJSONObject(i).getString("NAMA_INSTRUKTUR"),
                        jo.getJSONArray("data").getJSONObject(i).getString("NAMA_KELAS"),
                        jo.getJSONArray("data").getJSONObject(i).getString("STATUS_JADWAL_HARIAN"),
                        jo.getJSONArray("data").getJSONObject(i).getString("HARI_JADWAL_UMUM"),
                        jo.getJSONArray("data").getJSONObject(i).getInt("ID_INSTRUKTUR"),
                        jo.getJSONArray("data").getJSONObject(i).getDouble("TARIF_KELAS"),
                        jo.getJSONArray("data").getJSONObject(i).getString("JAM_MULAI"),
                        jo.getJSONArray("data").getJSONObject(i).getString("JAM_SELESAI"),
                    )
                    schedule.add(data)
                }
                var data_array: Array<PresensiInstruktur> = schedule.toTypedArray()

                val layoutManager = LinearLayoutManager(this)
                val adapter : PresensiAdapter = PresensiAdapter(schedule,this)
                val rvPermission : RecyclerView = findViewById(R.id.rv_presensi)

                rvPermission.layoutManager = layoutManager
                rvPermission.setHasFixedSize(true)
                rvPermission.adapter = adapter

                binding.srPresensi.isRefreshing = false

                if (!data_array.isEmpty()) {
//                    Toast.makeText(this@ScheduleInstructorActivity, "Data Berhasil Diambil!", Toast.LENGTH_SHORT).show()
//                    FancyToast.makeText(this@PresensiInstrukturActivity, "Berhasil Mendapatkan Data!", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()
                }else {
                    FancyToast.makeText(this@PresensiInstrukturActivity, "Tidak Berhasil Mendapatkan Data!", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()
                }

            }, Response.ErrorListener { error ->
                binding.srPresensi.isRefreshing = true
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    FancyToast.makeText(this, errors.getString("message"), FancyToast.LENGTH_LONG, FancyToast.INFO, false).show()

                } catch (e: Exception){
                    FancyToast.makeText(this, e.message, FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
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

    fun store(id_instruktur: Int, tanggal: String){
        val presence = InstructorAttendance(
            id_instruktur,
            tanggal
        )

        val stringRequest: StringRequest =
            object : StringRequest(Request.Method.POST, PresensiInstrukturApi.STOREDATA, Response.Listener { response ->
                val gson = Gson()
                var presence_data = gson.fromJson(response, InstructorAttendance::class.java)

                var resJO = JSONObject(response.toString())
                val userobj = resJO.getJSONObject("data")

                if(presence_data!= null) {
                    FancyToast.makeText(this@PresensiInstrukturActivity, "Berhasil Mengupdate Jam Kelas!", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
                    val intent = Intent(this@PresensiInstrukturActivity, PresensiInstrukturActivity::class.java)
                    startActivity(intent)
                }
                else {
                    FancyToast.makeText(this@PresensiInstrukturActivity, "Tidak Berhasil Mendapatkan Data!", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()
                }
                return@Listener
            }, Response.ErrorListener { error ->
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    FancyToast.makeText(
                        this,
                        errors.getString("message"),
                        FancyToast.LENGTH_SHORT, FancyToast.INFO, false
                    ).show()
                }catch (e: java.lang.Exception) {
                    FancyToast.makeText(this, e.message, FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
                }
            }) {
                @kotlin.jvm.Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    headers["Authorization"] = "Bearer " + sharedPreferences.getString("token",null);
                    return headers
                }

                @kotlin.jvm.Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(presence)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8;"
                }
            }
        queue!!.add(stringRequest)
    }
}