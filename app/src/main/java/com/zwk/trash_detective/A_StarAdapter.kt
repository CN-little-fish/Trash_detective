package com.zwk.trash_detective

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zwk.trash_detective.Bean.D_BaiTrashData

class A_StarAdapter(val trashlist : List<D_BaiTrashData>) : RecyclerView.Adapter<A_StarAdapter.ViewHolder>() {

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val trashname : TextView = view.findViewById(R.id.String_name)
        val firstname : TextView = view.findViewById(R.id.First_char)
        val description : TextView = view.findViewById(R.id.String_description)
        val root : TextView = view.findViewById(R.id.String_score)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.new_item,parent,false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener{
            val position = viewHolder.adapterPosition
            val trashdata = trashlist[position]
            val bundle = Bundle()
            bundle.putSerializable("onlineData", trashdata)
            var intent = Intent()//获取全局跳转，adatper是一个普通的类
            intent.putExtras(bundle)
            intent.setClass(it.context, M_DescriptionActivity::class.java)
            it.context.startActivity(intent)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val trash = trashlist[position]
        if (trash.keyword != "") holder.trashname.text = trash.keyword else holder.trashname.text = "暂无数据"
        if (trash.keyword != "") holder.firstname.text = (trash.keyword)[0].toString() else holder.firstname.text = "无"
        if (trash.description != "") holder.description.text = trash.description else holder.description.text = "暂无数据"
        if (trash.root != "") holder.root.text = trash.root else holder.root.text = "暂无数据"
    }

    override fun getItemCount(): Int = (trashlist.size)

}