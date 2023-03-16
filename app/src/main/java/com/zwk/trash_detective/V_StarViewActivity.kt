package com.zwk.trash_detective

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.zwk.trash_detective.Bean.D_BaiTrashData
import com.zwk.trash_detective.Bean.GetDataBase
import kotlinx.android.synthetic.main.activity_star_view.*

class V_StarViewActivity : AppCompatActivity() {
    lateinit var sp_pref : SharedPreferences
    lateinit var user : String
    private lateinit var trashlist : ArrayList<D_BaiTrashData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_star_view)
        sp_pref = getSharedPreferences("user_mes", MODE_PRIVATE)
        user = sp_pref.getString("user", "")
        trashlist = GetDataBase().getStarData(this,user)
        initAdapter()
        backpage.setOnClickListener {
            finish()
        }
    }

    private fun initAdapter(){
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        userstar.layoutManager = layoutManager
        val adapter = A_StarAdapter(trashlist)
        userstar.adapter = adapter
    }
}