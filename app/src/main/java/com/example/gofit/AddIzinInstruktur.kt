package com.example.gofit

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gofit.api.InstrukturApi
import com.example.gofit.databinding.ActivityAddIzinInstrukturBinding
import com.example.gofit.models.Izin
import com.google.gson.Gson
import com.shashank.sony.fancytoastlib.FancyToast
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class AddIzinInstruktur : AppCompatActivity() {


    private var etIdIzinInstruktur: EditText? = null
    private var etNamaInstruktur: EditText? = null
    private var edTglIzin: EditText? = null
    private var etKeteranganIzin: EditText? = null
    private var etTanggalKonfirmasiIzin: EditText? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityAddIzinInstrukturBinding

    companion object{
        const val LAUNCH_ADD_ACTIVITY = 123
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddIzinInstrukturBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

//        setContentView(R.layout.activity_add_izin_instruktur)
        queue = Volley.newRequestQueue(this)

        supportActionBar?.hide()
        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)

        val id = sharedPreferences.getInt("id", -1)
        queue = Volley.newRequestQueue(this)
//
//        val fabAdd = findViewById<Button>(R.id.button_create)

        setDropdownSchedule(id)

        binding.btnSave.setOnClickListener{
            storePermission(id)
        }

//        binding.btnCancel.setOnClickListener{
//            val moveDaftar = Intent(this@AddIzinInstruktur, ::class.java)
//            startActivity(moveDaftar)
//        }

    }

    fun setDropdownSchedule(id: Int) {
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, InstrukturApi.GETDATAJADWAL + id, Response.Listener { response ->
                var jo = JSONObject(response.toString())
                var schedule = arrayListOf<String>()
                var id : Int = jo.getJSONArray("data").length()

                for(i in 0 until id) {
//                    var data = DropDownSchedule(
//                        jo.getJSONArray("data").getJSONObject(i).getString("TANGGAL_JADWAL_HARIAN"),
//                    )
                    schedule.add(jo.getJSONArray("data").getJSONObject(i).getString("TANGGAL_HARIAN"))
                }
//                var data_array: Array<DropDownSchedule> = schedule.toTypedArray()

                val adapter: ArrayAdapter<String> = ArrayAdapter(this,R.layout.item_list_dropdown,R.id.dataList, schedule)
                binding.etTanggalIzinInstruktur.setAdapter(adapter)

                if (!schedule.isEmpty()) {
//                    )
                    FancyToast.makeText(this,"Berhasil melakukan izin instruktur",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show()
                }else {
                }

            }, Response.ErrorListener { error ->
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
                    Toast.makeText(
                        this@AddIzinInstruktur, e.message,
                        Toast.LENGTH_LONG
                    ).show();
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


    private fun storePermission(id: Int){
        val permisson = Izin(
            id,
            binding.inputLayoutNamaInstruktur.getEditText()?.getText().toString(),
            binding.etTanggalIzinInstruktur.text.toString(),
            null,
            binding.inputLayoutStatus.getEditText()?.getText().toString(),
            null,
            null,
        )

        val stringRequest: StringRequest =
            object : StringRequest(Method.POST, InstrukturApi.ADDIZIN, Response.Listener { response ->
                val gson = Gson()
                var permission = gson.fromJson(response, Izin::class.java)
                var resJO = JSONObject(response.toString())
//                val userobj = resJO.getJSONObject("data")

                if(permission != null) {
//                    MotionToast.darkColorToast(this,"Notification Success!",
//                        "Succesfully Create Permission!",
//                        MotionToastStyle.SUCCESS,
//                        MotionToast.GRAVITY_BOTTOM,
//                        MotionToast.LONG_DURATION,
//                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                    val intent = Intent(this@AddIzinInstruktur, IzinInstrukturActivity::class.java)
//                    sharedPreferences.edit()
//                        .putInt("id",userobj.getInt("ID_PEGAWAI"))
//                        .putString("role","MO")
//                        .putString("token",token)
//                        .apply()
                    startActivity(intent)
                }
                else {
//                    MotionToast.darkColorToast(this,"Notification Failed!",
//                        "Failed Create Permission",
//                        MotionToastStyle.ERROR,
//                        MotionToast.GRAVITY_BOTTOM,
//                        MotionToast.LONG_DURATION,
//                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                }
                return@Listener
            }, Response.ErrorListener { error ->
                try {
                    val responseBody =
                        String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    FancyToast.makeText(
                        this,
                        errors.getString("message"),
                        FancyToast.LENGTH_SHORT, FancyToast.INFO, false
                    ).show()
                }catch (e: java.lang.Exception) {
                    Toast.makeText(this@AddIzinInstruktur, e.message,
                        Toast.LENGTH_LONG).show();
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
                    val requestBody = gson.toJson(permisson)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8;"
                }
            }
        queue!!.add(stringRequest)
    }

}