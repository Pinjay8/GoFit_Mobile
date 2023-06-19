package com.example.gofit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gofit.api.LoginApi
import com.example.gofit.databinding.ActivityGantiPasswordBinding
import com.example.gofit.databinding.ActivityMainBinding
import com.example.gofit.models.Auth
import com.google.gson.Gson
import com.shashank.sony.fancytoastlib.FancyToast
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class GantiPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGantiPasswordBinding
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGantiPasswordBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        queue = Volley.newRequestQueue(this)
//        setContentView(R.layout.activity_ganti_password)
        supportActionBar?.hide()

        binding.btnReset.setOnClickListener(View.OnClickListener  {
            FancyToast.makeText(this,"Kembali ke tampilan login",
                FancyToast.LENGTH_LONG,
                FancyToast.INFO,false).show()
            val moveDaftar = Intent(this@GantiPasswordActivity, MainActivity::class.java)
            startActivity(moveDaftar)
        })

        binding.btnDaftar.setOnClickListener(View.OnClickListener {
            changePassword()
        })
    }


    private fun changePassword(){

        val auth = Auth(
            binding.inputLayoutEmail.getEditText()?.getText().toString(),
            binding.inputLayoutPassword.getEditText()?.getText().toString())

        val stringRequest: StringRequest =
            object : StringRequest(Request.Method.POST, LoginApi.RESET_PASSWORD_URL, Response.Listener { response ->
                val gson = Gson()
//                var user_pegawai = gson.fromJson(response, Pegawai::class.java)
//                var user_member = gson.fromJson(response, Member::class.java)

                var user= gson.fromJson(response, Auth::class.java)

                var resJO = JSONObject(response.toString())
                val userobj = resJO.getJSONObject("user")

                if(user!=null) {
                    FancyToast.makeText(this,"Berhasil Ganti Passsword",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show()
                    val intent = Intent(this@GantiPasswordActivity, MainActivity::class.java)
                    startActivity(intent)
                }
                else {
                    FancyToast.makeText(this,"Tidak Berhasil Ganti Password",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show()
                }
                return@Listener
            }, Response.ErrorListener { error ->
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    FancyToast.makeText(this, errors.getString("message"), FancyToast.LENGTH_LONG, FancyToast.INFO, false).show()
                }catch (e: Exception) {
                    FancyToast.makeText(this, e.message, FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show()
                }
            }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(auth)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8;"
                }
            }
        queue!!.add(stringRequest)
    }
}