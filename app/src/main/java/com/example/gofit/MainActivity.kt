package com.example.gofit

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.example.gofit.databinding.ActivityMainBinding
import com.example.gofit.models.Auth
import com.google.gson.Gson
import com.shashank.sony.fancytoastlib.FancyToast
import org.json.JSONObject
import java.nio.charset.StandardCharsets



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var queue: RequestQueue? = null
    var etEmail : String = ""
    var etPassword : String = ""
    var etNama : String = ""
    var mBundle : Bundle? = null
    var tempEmail : String = "admin"
    var tempPass : String = "admin"

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        queue = Volley.newRequestQueue(this)

        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
//        setContentView(R.layout.activity_main)
        supportActionBar?.hide()


        binding.btnGantiPassword.setOnClickListener(View.OnClickListener  {
            FancyToast.makeText(this,"Anda berada dihalaman ganti password",FancyToast.LENGTH_LONG,FancyToast.INFO,false).show()
            val moveDaftar = Intent(this@MainActivity, GantiPasswordActivity::class.java)
            startActivity(moveDaftar)
        })

        binding.btnInformasiUmum.setOnClickListener(View.OnClickListener {
            FancyToast.makeText(this,"Anda berada dihalaman informasi umum",FancyToast.LENGTH_LONG,FancyToast.INFO,false).show()
            val moveDaftar = Intent(this@MainActivity, UmumActivity::class.java)
            startActivity(moveDaftar)
        })


        binding.btnMasuk.setOnClickListener(View.OnClickListener {

            etEmail = binding.inputLayoutEmailLogin.editText?.text.toString()
            etPassword = binding.inputLayoutPasswordLogin.editText?.text.toString()

            binding.inputLayoutEmailLogin.setError(null)
            binding.inputLayoutPasswordLogin.setError(null)



            val auth = Auth(etEmail,etPassword)

            val stringRequest : StringRequest = object:
                StringRequest(Request.Method.POST, LoginApi.LOGIN_URL, Response.Listener { response ->

                    val gson = Gson()
                    var jsonObj = JSONObject(response.toString())
                    var userObjectData = jsonObj.getJSONObject("user")


                    if(userObjectData.has("ID_MEMBER")){
                        val token = jsonObj.getString("access_token")
                        sharedPreferences.edit()
                            .putString("id", userObjectData.getString("ID_MEMBER"))
                            .putString("role", "member")
                            .putString("status", "login")
                            .putString("token", token)
                            .apply()
                        val move = Intent(this@MainActivity, HomeActivity::class.java)
                        FancyToast.makeText(this,"Selamat datang "  + sharedPreferences.getString("role",null) + " pada aplikasi GoFit",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show()
                        startActivity(move)
                    }else if(userObjectData.has("ID_PEGAWAI")){
                        val token = jsonObj.getString("access_token")
                        sharedPreferences.edit()
                            .putInt("id", userObjectData.getInt("ID_PEGAWAI"))
                            .putString("role", "Manajer Operasional")
                            .putString("status", "login")
                            .putString("token", token)
                            .apply()
                        FancyToast.makeText(this,"Selamat datang "  + sharedPreferences.getString("role",null) +" pada aplikasi GoFit",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show()
                        val move = Intent(this@MainActivity, HomeActivity::class.java)
                        startActivity(move)
                    }else if(userObjectData.has("ID_INSTRUKTUR")){
                        val token = jsonObj.getString("access_token")
                        sharedPreferences.edit()
                            .putInt("id", userObjectData.getInt("ID_INSTRUKTUR"))
                            .putString("role", "instruktur")
                            .putString("status", "login")
                            .putString("token", token)
                            .apply()
                        FancyToast.makeText(this,"Selamat datang " + sharedPreferences.getString("role",null) + " pada aplikasi GoFit",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show()
                        val move = Intent(this@MainActivity, HomeActivity::class.java)
                        startActivity(move)
                    }
//                    FancyToast.makeText(this, "", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()

                }, Response.ErrorListener { error ->
                    try {
                        val responseBody =
                            String(error.networkResponse.data, StandardCharsets.UTF_8)
                        if (error.networkResponse.statusCode == 400) {
                            binding.inputLayoutEmailLogin.setError("Email salah!")
                            binding.inputLayoutPasswordLogin.setError("Password salah!")
                        }else if (error.networkResponse.statusCode == 400) {
                            val jsonObject = JSONObject(responseBody)
                            val jsonObject1 = jsonObject.getJSONObject("message")
                            for (i in jsonObject1.keys()) {
                                if (i == "Email") {
                                    binding.inputLayoutEmailLogin.error = jsonObject1.getJSONArray(i).getString(0)
                                }
                                if (i == "password") {
                                    binding.inputLayoutPasswordLogin.error = jsonObject1.getJSONArray(i).getString(0)
                                }
                            }
                        }else {
                            val errors = JSONObject(responseBody)
                            FancyToast.makeText(this, errors.getString("message"), FancyToast.LENGTH_LONG, FancyToast.INFO, false).show()
                        }

                    } catch (e: Exception){
                        FancyToast.makeText(this, e.message, FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show()
                    }
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = "Bearer " + sharedPreferences.getString("token",null);
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

                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["Email"] = binding.emailInputLogin.text.toString()
                    params["password"] = binding.passwordInputLogin.text.toString()
                    return params
               }
            }
            queue!!.add(stringRequest)
        })

    }
}