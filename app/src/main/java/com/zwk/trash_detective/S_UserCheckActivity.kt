package com.zwk.trash_detective

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.zwk.trash_detective.Bean.B_TrashDatabase
import com.zwk.trash_detective.Bean.GetDataBase
import kotlinx.android.synthetic.main.activity_user_check.*

class S_UserCheckActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_check)
        onControlClick()
    }

    fun onControlClick(){
        btn_cg.setOnClickListener {
            val trow_paw = row_paw.text.toString().trim { it <= ' ' }
            val tc_psw = c_psw.text.toString().trim { it <= ' ' }
            val ts_psws = s_psws.text.toString().trim { it <= ' ' }
            val user = getSharedPreferences("user_mes", MODE_PRIVATE).getString("user","")
            var row_password = GetDataBase().getPassword(this, user)

            if(trow_paw.isNotEmpty() and tc_psw.isNotEmpty() and ts_psws.isNotEmpty()) {
                if(trow_paw == row_password){
                    if(tc_psw == ts_psws){
                        user?.let { it1 -> GetDataBase().changePassword(this, tc_psw, it1) }
                        Toast.makeText(this, "修改密码成功！",Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
                        Toast.makeText(this, "俩次密码输入不一致",Toast.LENGTH_SHORT).show()
                        s_psws.text.clear()
                    }
                }else{
                    Toast.makeText(this, "原密码输入失败，请重新输入",Toast.LENGTH_SHORT).show()
                    row_paw.text.clear()
                }
            }else{
                Toast.makeText(this, "有没有输入的内容！",Toast.LENGTH_SHORT).show()
            }
        }
        uv_back.setOnClickListener {
            finish()
        }
    }


}