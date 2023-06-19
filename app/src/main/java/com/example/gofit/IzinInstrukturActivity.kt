package com.example.gofit

import IzinAdapter
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gofit.api.InstrukturApi
import com.example.gofit.models.Izin
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.activity_izin_instruktur.*
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class IzinInstrukturActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityIzinInstrukturBinding
    private var queue: RequestQueue? = null
    private lateinit var sharedPreferences: SharedPreferences
    private var layoutLoading: LinearLayout? = null
    private var izinAdapter: IzinAdapter? = null
    private var srIzin: SwipeRefreshLayout? = null

    companion object{
        const val LAUNCH_ADD_ACTIVITY = 123
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = ActivityIzinInstrukturBinding.inflate(layoutInflater)
//        setContentView(binding!!.root)
        setContentView(R.layout.activity_izin_instruktur)

        queue = Volley.newRequestQueue(this)
        srIzin = findViewById(R.id.sr_Izin)
        layoutLoading = findViewById(R.id.layout_loading)

        supportActionBar?.hide()

        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)

        val id = sharedPreferences.getInt("id", -1)
        queue = Volley.newRequestQueue(this)

        srIzin?.setOnRefreshListener (SwipeRefreshLayout.OnRefreshListener {
            allDataIzin(id)
        })

        val fabAdd = findViewById<Button>(R.id.button_create)
        fabAdd.setOnClickListener{
            val i = Intent(this@IzinInstrukturActivity, AddIzinInstruktur::class.java)
            startActivityForResult(i, LAUNCH_ADD_ACTIVITY)
        }

//        val rvIzin =  findViewById<RecyclerView>(R.id.rv_izin)
//        izinAdapter = IzinAdapter(ArrayList(), this)
//        rvIzin.layoutManager = LinearLayoutManager(this)
//        rvIzin.adapter = izinAdapter

        allDataIzin(id)
    }

    private fun allDataIzin(id: Int) {
        srIzin!!.isRefreshing = true

        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, InstrukturApi.GET_ALL_URL + id, Response.Listener{ response ->
                //   val gson = Gson()
                //   var mahasiswa : Array<Mahasiswa> = gson.fromJson(response, Array<Mahasiswa>::class.java)

//                var jo = JSONObject(response.toString())
//                var donaturArray = arrayListOf<Donatur>()
                var jo = JSONObject(response.toString())
                var instrukturArray = arrayListOf<Izin>()
                var id : Int = jo.getJSONArray("data").length()

                for(i in 0 until id) {
                    var data = Izin(
//                        jo.getJSONArray("data").getJSONObject(i).getInt("ID_IZIN"),
                        jo.getJSONArray("data").getJSONObject(i).getInt("ID_INSTRUKTUR"),
                        jo.getJSONArray("data").getJSONObject(i).getString("INSTRUKTUR_PENGGANTI"),
                        jo.getJSONArray("data").getJSONObject(i).getString("TANGGAL_IZIN"),
                        jo.getJSONArray("data").getJSONObject(i).getString("TANGGAL_PENGAJUAN"),
                        jo.getJSONArray("data").getJSONObject(i).getString("KETERANGAN_IZIN"),
                        jo.getJSONArray("data").getJSONObject(i).getString("STATUS_KONFIRMASI"),
                        jo.getJSONArray("data").getJSONObject(i).getString("TANGGAL_KONFIRMASI"),
                    )
                   instrukturArray.add(data)
                }

                var dataArray: Array<Izin> = instrukturArray.toTypedArray()


                val layoutManager = LinearLayoutManager(this)
                val adapter : IzinAdapter = IzinAdapter(instrukturArray,this)
                val rvPermission : RecyclerView = findViewById(R.id.rv_izin)

                rvPermission.layoutManager = layoutManager
                rvPermission.setHasFixedSize(true)
                rvPermission.adapter = adapter


//                izinAdapter!!.setIzinInstrukturList(dataArray)
//                izinAdapter!!.filter.filter(!!.query)

                srIzin!!.isRefreshing = false

                if(!dataArray.isEmpty())
                //                 Toast.makeText(this@AddDonaturActivity, "Data berhasil diambil", Toast.LENGTH_SHORT).show()
                    FancyToast.makeText(this@IzinInstrukturActivity, "Berhasil Mendapatkan Data!", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()

                else
                    FancyToast.makeText(this@IzinInstrukturActivity, "Data Tidak Ditemukan", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()

            }, Response.ErrorListener{ error ->
                srIzin!!.isRefreshing = true

                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    FancyToast.makeText(
                        this,
                        errors.getString("message"),
                        FancyToast.LENGTH_SHORT, FancyToast.INFO, false
                    ).show()
                }catch(e: Exception){
                    FancyToast.makeText(this, e.message, FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String>{
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer " + sharedPreferences.getString("token",null);
                headers["Accept"] = "application/json"
                return headers
            }
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8;"
            }
        }
        queue!!.add(stringRequest)
    }

//    override  fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
//        super.onActivityResult(requestCode, resultCode, data)
//        if(requestCode == LAUNCH_ADD_ACTIVITY && resultCode == RESULT_OK) allDataIzin()
//    }

    private fun setLoading(isLoading: Boolean){
        if(isLoading){
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            layoutLoading!!.visibility = View.VISIBLE
        }else{
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutLoading!!.visibility = View.GONE
        }
    }


}
