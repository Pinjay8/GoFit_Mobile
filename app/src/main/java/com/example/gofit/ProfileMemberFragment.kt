package com.example.gofit

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gofit.api.LoginApi
import com.example.gofit.api.MemberApi
import com.example.gofit.databinding.FragmentHomeMemberBinding
import com.example.gofit.databinding.FragmentProfileMemberBinding
import com.example.gofit.models.Auth
import com.example.gofit.models.Member
import com.google.gson.Gson
import com.shashank.sony.fancytoastlib.FancyToast
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


class ProfileMemberFragment : Fragment() {

    private var _binding: FragmentProfileMemberBinding? = null

    private val binding get() = _binding!!

    private var queue: RequestQueue? = null
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileMemberBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        sharedPreferences = activity?.getSharedPreferences("login", Context.MODE_PRIVATE)

        sharedPreferences = (activity as HomeActivity).getSharedPreferences()

        val id = sharedPreferences.getString("id", null)

        queue = Volley.newRequestQueue(activity)

        getProfileById(id)


        binding.btnLogout.setOnClickListener{
            logout()
        }
    }

    private fun getProfileById(id: String?){

        val stringRequest: StringRequest = object :
            StringRequest(
                Method.GET, MemberApi.GETDATAMEMBER + id,
                { response ->

                    var jo = JSONObject(response.toString())
                    var member = arrayListOf<Member>()
                    var id : Int = jo.getJSONArray("data").length()

                    for(i in 0 until id) {
                        var data = Member(
                            jo.getJSONArray("data").getJSONObject(i).getString("ID_MEMBER"),
                            jo.getJSONArray("data").getJSONObject(i).getString("NAMA_MEMBER"),
                            jo.getJSONArray("data").getJSONObject(i).getString("EMAIL_MEMBER"),
                            jo.getJSONArray("data").getJSONObject(i).getString("MASA_AKTIVASI"),
                            jo.getJSONArray("data").getJSONObject(i).getDouble("SISA_DEPOSIT_UANG"),
                            jo.getJSONArray("data").getJSONObject(i).getString("SISA_DEPO")
                        )
                        member.add(data)
                    }
                    binding.nama.setText(jo.getJSONArray("data").getJSONObject(0).getString("NAMA_MEMBER"))
                    binding.Email.setText(jo.getJSONArray("data").getJSONObject(0).getString("EMAIL_MEMBER"))
                    if(jo.getJSONArray("data").getJSONObject(0).getString("MASA_AKTIVASI") == "null"){
                        binding.masaBerlaku.setText("Belum Aktivasi")
                    }else{
                        binding.masaBerlaku.setText(jo.getJSONArray("data").getJSONObject(0).getString("MASA_AKTIVASI"))
                    }

                    binding.sisaDepositUang.setText("Rp."+jo.getJSONArray("data").getJSONObject(0).getDouble("SISA_DEPOSIT_UANG").toString())
                    binding.sisaDepositKelas.setText(""+jo.getJSONArray("data").getJSONObject(0).getString("SISA_DEPO").toString())


                    FancyToast.makeText(requireActivity(),"Data Profile Berhasil ditampilkan", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
                },
                Response.ErrorListener{ error ->
                    try{
                        val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        FancyToast.makeText(
                            requireActivity(),
                            errors.getString("message"),
                            FancyToast.LENGTH_SHORT, FancyToast.INFO, false
                        ).show()
                    } catch (e: Exception){
                        FancyToast.makeText(requireActivity(), e.message, FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
                    }
                }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = java.util.HashMap<String, String>()
                headers["Authorization"] = "Bearer " + sharedPreferences.getString("token",null);
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)

    }

    private fun logout(){
        val auth = Auth(
            "",
            "")

        val stringRequest: StringRequest =
            object : StringRequest(Request.Method.POST, LoginApi.LOGOUT_URL, Response.Listener { response ->
                val gson = Gson()
                var user_logout = gson.fromJson(response, Auth::class.java)


                if(user_logout != null) {

                    val intent = Intent(activity, MainActivity::class.java)
                    FancyToast.makeText(context as Activity,"Berhasil Logout",
                        FancyToast.LENGTH_LONG,
                        FancyToast.SUCCESS,false).show()
                    sharedPreferences.edit()
                        .putInt("id",-1)
                        .putString("id", null)
                        .putString("role",null)
                        .putString("token",null)
                        .apply()
                    startActivity(intent)
                }
                else {
                    FancyToast.makeText(context as Activity,"Gagal Logout",
                        FancyToast.LENGTH_LONG,
                        FancyToast.SUCCESS,false).show()
                }
                return@Listener
            }, Response.ErrorListener { error ->
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    FancyToast.makeText(context as Activity, errors.getString("message"), FancyToast.LENGTH_LONG, FancyToast.INFO, false).show()
                }catch (e: Exception) {
                    FancyToast.makeText(context as Activity, e.message, FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show()
                }
            }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    headers["Authorization"] = "Bearer " + sharedPreferences.getString("token",null);
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