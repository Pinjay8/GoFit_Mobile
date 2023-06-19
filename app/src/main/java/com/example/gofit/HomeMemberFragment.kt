package com.example.gofit

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gofit.api.LoginApi
import com.example.gofit.databinding.FragmentHomeMemberBinding
import com.example.gofit.models.Auth
import com.google.gson.Gson
import com.shashank.sony.fancytoastlib.FancyToast
import org.json.JSONObject
import java.nio.charset.StandardCharsets



class HomeMemberFragment : Fragment() {

    private lateinit var namaView: TextView
    private var _binding: FragmentHomeMemberBinding? = null

    private val binding get() = _binding!!

    private var queue: RequestQueue? = null
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeMemberBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = (activity as HomeActivity).getSharedPreferences()
//        namaView = view.findViewById(R.id.namaView)
        queue = Volley.newRequestQueue(activity)


//        namaView.text = sharedPreferences!!.getString("NAMA_MEMBER", "")

        binding.cardGym.setOnClickListener{
            val intent = Intent(activity, BookingGymActivity::class.java)
            startActivity(intent)
        }

        binding.card.setOnClickListener{
            val intent = Intent(activity, BookingKelasActivity::class.java)
            startActivity(intent)
        }

        binding.btnAktivitasGym.setOnClickListener{
            val move = Intent(activity, AktivitasGymActivity::class.java)
            startActivity(move)
        }

        binding.btnAktivitasKelas.setOnClickListener{
            val move = Intent(activity, AktivitasKelasActivity::class.java)
            startActivity(move)
        }
    }

}