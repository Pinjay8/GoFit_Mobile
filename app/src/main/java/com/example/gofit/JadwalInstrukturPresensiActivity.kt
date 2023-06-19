package com.example.gofit

import android.content.Context
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
import com.example.gofit.api.PresensiBookingKelasApi
import com.example.gofit.databinding.ActivityJadwalInstrukturPresensiBinding
import com.example.gofit.models.JadwalInstruktur
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class JadwalInstrukturPresensiActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityJadwalInstrukturPresensiBinding
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJadwalInstrukturPresensiBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()
        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)

        val id = sharedPreferences.getInt("id",0)
        queue = Volley.newRequestQueue(this)

        binding.srJadwalinstruktur.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener{allData(id)})
        allData(id)
    }

    private fun allData(id: Int) {
        binding.srJadwalinstruktur.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Request.Method.GET, PresensiBookingKelasApi.GET_ALL_URL + id, Response.Listener { response ->
                var jo = JSONObject(response.toString())
                var schedule = arrayListOf<JadwalInstruktur>()
                var id : Int = jo.getJSONArray("data").length()

                for(i in 0 until id) {
                    var data = JadwalInstruktur(
                        jo.getJSONArray("data").getJSONObject(i).getString("TANGGAL_HARIAN"),
                        jo.getJSONArray("data").getJSONObject(i).getString("NAMA_INSTRUKTUR"),
                        jo.getJSONArray("data").getJSONObject(i).getString("NAMA_KELAS"),
                        jo.getJSONArray("data").getJSONObject(i).getString("STATUS_JADWAL_HARIAN"),
                        jo.getJSONArray("data").getJSONObject(i).getString("HARI_JADWAL_UMUM"),
                        jo.getJSONArray("data").getJSONObject(i).getInt("ID_INSTRUKTUR"),
                        jo.getJSONArray("data").getJSONObject(i).getDouble("TARIF_KELAS"),

                    )
                    schedule.add(data)
                }
                var data_array: Array<JadwalInstruktur> = schedule.toTypedArray()

                val layoutManager = LinearLayoutManager(this)
                val adapter : JadwalInstrukturPresensiAdapter = JadwalInstrukturPresensiAdapter(schedule,this)
                val rvPermission : RecyclerView = findViewById(R.id.rv_jadwalinstruktur)

                rvPermission.layoutManager = layoutManager
                rvPermission.setHasFixedSize(true)
                rvPermission.adapter = adapter

                binding.srJadwalinstruktur.isRefreshing = false

                if (!data_array.isEmpty()) {
//                    Toast.makeText(this@ScheduleInstructorActivity, "Data Berhasil Diambil!", Toast.LENGTH_SHORT).show()


                }else {

                }

            }, Response.ErrorListener { error ->
                binding.srJadwalinstruktur.isRefreshing = true
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(this@JadwalInstrukturPresensiActivity, errors.getString("message"), Toast.LENGTH_SHORT).show()

                } catch (e: Exception){
                    Toast.makeText(this@JadwalInstrukturPresensiActivity, e.message, Toast.LENGTH_SHORT).show()

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
}