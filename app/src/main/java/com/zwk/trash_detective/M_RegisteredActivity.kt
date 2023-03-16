package com.zwk.trash_detective

import android.content.ContentValues
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.zwk.trash_detective.Bean.B_TrashDatabase
import com.zwk.trash_detective.Bean.GetDataBase
import kotlinx.android.synthetic.main.activity_registered.*

class M_RegisteredActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR //禁止横屏
        title = "用户注册" //顶部标题改成用户注册
        setContentView(R.layout.activity_registered)
        clickControl()
    }


    private fun clickControl() {

        uv_back.setOnClickListener{
            val intent = Intent(this@M_RegisteredActivity, M_LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        btn_cgps.setOnClickListener {
            //获取用户输入的用户名、密码、验证码
            val username: String = et_rgsName.getText().toString().trim { it <= ' ' }
            val password1: String = c_psw.getText().toString().trim { it <= ' ' }
            val password2: String = s_psws.getText().toString().trim { it <= ' ' }
            val email: String = row_paw.getText().toString().trim { it <= ' ' }
            val phonenum: String = et_rgsPhoneNum.getText().toString().trim { it <= ' ' }
            //注册验证
            if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password1) && !TextUtils.isEmpty(password2)&& !TextUtils.isEmpty(phonenum)) {
                //判断两次密码是否一致
                if (password1 == password2) {
                    Toast.makeText(this, "验证通过，注册成功", Toast.LENGTH_SHORT).show()
                    //将用户名和密码加入到数据库中
                    GetDataBase().addUserTable(this,username, password2, email, phonenum)
                    val intent = Intent(this@M_RegisteredActivity, M_LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "两次密码不一致,注册失败", Toast.LENGTH_SHORT).show()
                    s_psws.setText("")
                }
            } else {
                Toast.makeText(this, "注册信息不完善,注册失败", Toast.LENGTH_SHORT).show()
            }
        }
    }
}