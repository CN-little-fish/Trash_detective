package com.zwk.trash_detective

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.zwk.trash_detective.Bean.D_UserData
import com.zwk.trash_detective.Bean.GetDataBase
import kotlinx.android.synthetic.main.activity_login.*

class M_LoginActivity : AppCompatActivity() {

    private lateinit var  sp : SharedPreferences
    private lateinit var editor : SharedPreferences.Editor
    val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN) //隐藏状态栏
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR //禁止横屏
        verifyStoragePermissions(this)
        setContentView(R.layout.activity_login)
        setInitData()
    }

    private fun verifyStoragePermissions(activity : Activity) : Boolean {
        if (Build.VERSION.SDK_INT < 23) {
            return true
        }
        val permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
        return if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            false
        } else {
            true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "授权失败,请去设置打开权限", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setInitData(){
        sp = getSharedPreferences("user_mes", Context.MODE_PRIVATE)
        editor = sp.edit()
        if (sp.getBoolean("flag", false)) {
            val user_read = sp.getString("user", "")
            val psw_read = sp.getString("psw", "")
            et_User.setText(user_read)
            et_Psw.setText(psw_read)
        }
        clickControl()
    }

    private fun clickControl() {

        tv_Register.setOnClickListener {
            val intent = Intent(this, M_RegisteredActivity::class.java) //跳转到注册界面
            startActivity(intent)
            finish()
        }

        btn_Login.setOnClickListener {
            X_LoadingDialog.show(this)
            val name = et_User.text.toString().trim { it <= ' ' }
            val password = et_Psw.text.toString().trim { it <= ' ' }
            lateinit var userName: String
            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
                //val data: ArrayList<UserData>? = getAllData()
                val data = GetDataBase().getAllData(this)
                var ifUser = false
                var ifSaved = false
                if (data != null) {
                    for (i in 0 until data.size) {
                        val user: D_UserData = data[i]
                        if (name == user.name && password == user.password ||
                            name == user.email && password == user.password ||
                            name == user.phonenum && password == user.password
                        ) {
                            userName = user.name
                            ifUser = true
                            ifSaved = if (cb_rmbPsw.isChecked) {
                                editor.putBoolean("flag", true)
                                editor.putString("user", user.name)
                                editor.putString("psw", user.password)
                                editor.apply()
                                Toast.makeText(this, "成功记住密码", Toast.LENGTH_SHORT).show()
                                true
                            } else {
                                editor.putString("user", user.name)
                                editor.putString("psw", "")
                                editor.apply()
                                false
                            }
                            break
                        } else {
                            ifUser = false
                        }
                    }
                }

                if (ifUser) {
                    if (ifSaved) {
                        cb_rmbPsw.isChecked = true
                    }
                    Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show()
                    var target: Runnable
                    //用线程启动
                    val thread: Thread = object : Thread() {
                        override fun run() {
                            try {
                                //LoadingDialog.dismiss()
                                sleep(2000) //2秒 模拟登录时间
                                val username: String = userName
                                val intent = Intent(this@M_LoginActivity, M_MainActivity::class.java)
                                //设置自己跳转到成功的界面
                                intent.putExtra("user_name",username);
                                startActivity(intent)
                                finish()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                    thread.start() //打开线程
                } else {
                    val thread: Thread = object : Thread() {
                        override fun run() {
                            try {
                                sleep(2000) //2秒 模拟登录时间
                                X_LoadingDialog.dismiss()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                    thread.start() //打开线程
                    Toast.makeText(this@M_LoginActivity, "用户名或密码不正确，请重新输入", Toast.LENGTH_SHORT).show()
                }
            } else {
                val thread: Thread = object : Thread() {
                    override fun run() {
                        try {
                            sleep(2000) //2秒 模拟登录时间
                            X_LoadingDialog.dismiss()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
                thread.start() //打开线程
                Toast.makeText(this, "请输入你的用户名或密码", Toast.LENGTH_SHORT).show()
            }
        }
    }
}