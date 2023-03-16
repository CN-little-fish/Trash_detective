package com.zwk.trash_detective

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.zwk.trash_detective.Bean.D_BaiTrashData
import kotlinx.android.synthetic.main.activity_main2.*
import java.io.File

class V_TrashShowActivity : AppCompatActivity() {

    private val trashlist = ArrayList<D_BaiTrashData>()
    lateinit var openphoto : File


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        getSendData()
        initadapter()

        button.setOnClickListener {
            val intent = Intent(this@V_TrashShowActivity, M_MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getSendData(){
        val intent = intent
        // 实例化一个Bundle
        val flag : Int = intent.getIntExtra("control_flag", 0)
        val bundle = intent.extras
        //获取里面的Persion里面的数据
        for (i in 0 until 5){
            trashlist.add(bundle.getSerializable("onlineData$i") as D_BaiTrashData)
        }
        if (flag == 1){
            openphoto = File(Environment.getExternalStorageDirectory(), "cache.jpg")
        }
        else if (flag == 2){
            openphoto = File(Environment.getExternalStorageDirectory(), "cache1.jpg")
        }
        val options: BitmapFactory.Options = BitmapFactory.Options()
        options.inJustDecodeBounds = true//只测量待加载图片，不实际加载，从而节省开销
        var bitmap = BitmapFactory.decodeFile(openphoto.path)
        imageViews.setImageBitmap(bitmap)
    }

    private fun initadapter(){
        val layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager
        val adapter = A_TrashAdapter(trashlist)
        recyclerView.adapter = adapter
    }

}

