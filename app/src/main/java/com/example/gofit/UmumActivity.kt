package com.example.gofit

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gofit.api.LoginApi
import com.example.gofit.databinding.ActivityUmumBinding
import com.example.gofit.models.JadwalHarian
import com.shashank.sony.fancytoastlib.FancyToast
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class UmumActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUmumBinding
    private var queue: RequestQueue? = null
    private lateinit var sharedPreferences: SharedPreferences

    private var srClass: SwipeRefreshLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUmumBinding.inflate(layoutInflater)
        setContentView(binding!!.root)


        queue = Volley.newRequestQueue(this)

        supportActionBar?.hide()

        allData()
    }

    private fun allData() {
        binding.srClass.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, LoginApi.tampilanJadwal, Response.Listener { response ->
                var jo = JSONObject(response.toString())
                var schedule = arrayListOf<JadwalHarian>()
                var id : Int = jo.getJSONArray("data").length()

                for(i in 0 until id) {
                    var data = JadwalHarian(
                        jo.getJSONArray("data").getJSONObject(i).getString("TANGGAL_HARIAN"),
                        jo.getJSONArray("data").getJSONObject(i).getString("NAMA_INSTRUKTUR"),
                        jo.getJSONArray("data").getJSONObject(i).getString("NAMA_KELAS"),
                        jo.getJSONArray("data").getJSONObject(i).getString("STATUS_JADWAL_HARIAN"),
                        jo.getJSONArray("data").getJSONObject(i).getString("HARI_JADWAL_UMUM"),
                        jo.getJSONArray("data").getJSONObject(i).getInt("ID_KELAS"),
                        jo.getJSONArray("data").getJSONObject(i).getDouble("TARIF_KELAS"),
                    )
                    schedule.add(data)
                }
                var data_array: Array<JadwalHarian> = schedule.toTypedArray()

                val layoutManager = LinearLayoutManager(this)
                val adapter : JadwalAdapter = JadwalAdapter(schedule,this)
                val rvPermission : RecyclerView = findViewById(R.id.rv_jadwal)

                rvPermission.layoutManager = layoutManager
                rvPermission.setHasFixedSize(true)
                rvPermission.adapter = adapter

                binding.srClass.isRefreshing = false

                if (!data_array.isEmpty()) {
                    FancyToast.makeText(this@UmumActivity, "Berhasil Mendapatkan Data!", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()
                }else {
//                    MotionToast.darkToast(
//                        context as Activity, "Notification Display!",
//                        "Data not found",
//                        MotionToastStyle.SUCCESS,
//                        MotionToast.GRAVITY_BOTTOM,
//                        MotionToast.LONG_DURATION,
//                        ResourcesCompat.getFont(
//                            context as Activity,
//                            www.sanju.motiontoast.R.font.helvetica_regular
//                        )
//                    )
                }

            }, Response.ErrorListener { error ->
                binding.srClass.isRefreshing = true
                try {

                    val responseBody =
                        String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    FancyToast.makeText(
                        this,
                        errors.getString("message"),
                        FancyToast.LENGTH_SHORT, FancyToast.INFO, false
                    ).show()
                } catch (e: Exception){
                     FancyToast.makeText(
                            this,
                            e.message,
                            FancyToast.LENGTH_SHORT,
                            FancyToast.ERROR,
                            false
                        ).show()
                }
            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
//                headers["Authorization"] = "Bearer " + sharedPreferences.getString("token",null);
                return headers
            }
        }
        queue!!.add(stringRequest)
    }

}