package com.example.gofit

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gofit.api.MemberApi
import com.example.gofit.databinding.ActivityAktivitasGymBinding
import com.example.gofit.databinding.ActivityBookingGymBinding
import com.example.gofit.models.BookingGym
import com.shashank.sony.fancytoastlib.FancyToast
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class AktivitasGymActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAktivitasGymBinding
    private var queue: RequestQueue? = null
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAktivitasGymBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()

        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)

        val id = sharedPreferences.getString("id", null)
        queue = Volley.newRequestQueue(this)

        binding.srBooking.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            if(id != null){
                allDataBooking(id)
            }
        })

        if(id != null){
            allDataBooking(id)
        }


    }

    private fun allDataBooking(id: String) {
        binding.srBooking.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(
                Method.GET, MemberApi.GET_ALL_URL+ id,
                Response.Listener { response ->
                    var jo = JSONObject(response.toString())
                    var BookingGym = arrayListOf<BookingGym>()
                    var id: Int = jo.getJSONArray("data").length()

                    for (i in 0 until id) {
                        var data = BookingGym(
                            jo.getJSONArray("data").getJSONObject(i).getString("KODE_BOOKING_GYM"),
                            jo.getJSONArray("data").getJSONObject(i).getString("ID_MEMBER"),
                            jo.getJSONArray("data").getJSONObject(i)
                                .getString("TANGGAL_BOOKING_GYM"),
                            jo.getJSONArray("data").getJSONObject(i)
                                .getString("TANGGAL_YANG_DIBOOKING_GYM"),
                            jo.getJSONArray("data").getJSONObject(i).getString("SLOT_WAKTU"),
                            jo.getJSONArray("data").getJSONObject(i)
                                .getString("STATUS_PRESENSI_GYM"),
                            jo.getJSONArray("data").getJSONObject(i)
                                .getString("WAKTU_PRESENSI"),
                        )
                        BookingGym.add(data)
                    }

                    var data_array: Array<BookingGym> = BookingGym.toTypedArray()

                    val layoutManager = LinearLayoutManager(this)
                    val adapter: BookingGymAdapter = BookingGymAdapter(BookingGym, this)
                    val rvPermission: RecyclerView = findViewById(R.id.rv_booking)

                    rvPermission.layoutManager = layoutManager
                    rvPermission.setHasFixedSize(true)
                    rvPermission.adapter = adapter

                    binding.srBooking.isRefreshing = false

                    if (!data_array.isEmpty()) {
                        FancyToast.makeText(this@AktivitasGymActivity, "Berhasil Mendapatkan Data!", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()
                    } else {
                        FancyToast.makeText(this@AktivitasGymActivity, "Data Tidak Ditemukan", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()
                    }

                },
                Response.ErrorListener { error ->
                    binding.srBooking.isRefreshing = true
                    try {
                        val responseBody =
                            String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        FancyToast.makeText(
                            this,
                            errors.getString("message"),
                            FancyToast.LENGTH_SHORT, FancyToast.INFO, false
                        ).show()
                        binding.srBooking.isRefreshing = false
                    } catch (e: Exception) {
                        FancyToast.makeText(this, e.message, FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
                        binding.srBooking.isRefreshing = false
                    }

                }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Authorization"] = "Bearer " + sharedPreferences.getString("token", null);
                return headers
            }
        }
        queue!!.add(stringRequest)


    }
}