package com.zwk.trash_detective

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_user_info.*

class S_UserInfoActivity : AppCompatActivity() {
    lateinit var username : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        val sp_pref = getSharedPreferences("user_mes", MODE_PRIVATE)
        username = sp_pref.getString("user","")
        user_name.text = username
        control()
    }

    private fun control(){
        star.setOnClickListener {
            val intent = Intent(this, V_StarViewActivity::class.java)
            startActivity(intent)
        }
        change_password.setOnClickListener {
            val intents = Intent(this, S_UserCheckActivity::class.java)
            startActivity(intents)
        }
        back_toolBar.setOnClickListener {
            finish()
        }
        help_setting.setOnClickListener {

        }
    }

}