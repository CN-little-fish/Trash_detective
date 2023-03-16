package com.zwk.trash_detective

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_setting.*

class S_SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        back_toolBar.setOnClickListener {
            finish()
        }

        ll_setting_app_info.setOnClickListener {
            val intent = Intent(this, S_AppInfoActivity::class.java)
            startActivity(intent)
        }

    }

}