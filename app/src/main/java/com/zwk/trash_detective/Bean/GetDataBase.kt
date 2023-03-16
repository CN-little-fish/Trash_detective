package com.zwk.trash_detective.Bean

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import kotlinx.android.synthetic.main.activity_user_check.*

class GetDataBase {

    fun getAllData(context: Context): ArrayList<D_UserData>? {
        val list = ArrayList<D_UserData>()
        //如果数据不存在，则创建数据，然后获取可读可写的数据库对象，如果存在，直接打开；
        val sql = B_TrashDatabase(context, "trash.db", null, 3)
        val db = sql.writableDatabase
        val cursor: Cursor = db.query("user", null, null, null, null, null, "id DESC")
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val email = cursor.getString(cursor.getColumnIndex("email"))
            val phonenum = cursor.getString(cursor.getColumnIndex("phonenum"))
            val password = cursor.getString(cursor.getColumnIndex("password"))
            list.add(D_UserData(id, name, password, email, phonenum))
        }
        cursor.close()
        db.close()
        return list
    }

    fun getStarData(context: Context, user: String): ArrayList<D_BaiTrashData> {
        var trashlist = ArrayList<D_BaiTrashData>()
        val st = B_TrashDatabase(context, "trash.db", null, 3)
        val db = st.writableDatabase
        val cursor: Cursor = db.query("star", null, null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndex("uuid")).equals(user)){
                val description: String = cursor.getString(cursor.getColumnIndex("description"))
                val name: String = cursor.getString(cursor.getColumnIndex("name"))
                val root: String = cursor.getString(cursor.getColumnIndex("root"))
                var temp = D_BaiTrashData(name, 0.toString(), root, description,null)
                trashlist.add(temp)
            }
        }
        cursor.close()
        db.close()
        return trashlist
    }

    fun ColTrash(context: Context,Trash: D_BaiTrashData): D_SqlTrashData? {
        var helper = B_TrashDatabase(context, "trash.db", null, 3)
        //如果数据不存在，则创建数据，然后获取可读可写的数据库对象，如果存在，直接打开；
        val db = helper.writableDatabase
        var flag = 0
        var num = 0
        var  data: D_SqlTrashData? = null
        //查询数据所有数据
        val cursor = db.query("trashdata", null, null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            val sortId = cursor.getInt(cursor.getColumnIndex("sortId"))
            val star = cursor.getInt(cursor.getColumnIndex("star"))
            val url = cursor.getString(cursor.getColumnIndex("url"))
            val description = cursor.getString(cursor.getColumnIndex("description"))
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val root = cursor.getString(cursor.getColumnIndex("root"))
            num++
            if (Trash.getKeyword() == name) {
                data = D_SqlTrashData(id, sortId, name, root, description, url, star)
                flag = 1
                val mdb = helper.writableDatabase
                mdb.execSQL("update trashdata set description = ? where name = ?", arrayOf<Any>(Trash.getDescription(), name))
                break
            }
        }
        if (flag == 0) {
            insertApi(num + 1, 5, Trash.keyword, Trash.root, Trash.description, Trash.image_url, 0, context)
            data = D_SqlTrashData(num + 1, 5, Trash.keyword, Trash.root, Trash.description, Trash.image_url, 0)
        }
        cursor.close()
        db.close()
        return data
    }

    fun addUserTable(context: Context, name: String, password: String, email: String, phonenum: String) {
        val sql = B_TrashDatabase(context, "trash.db", null, 3)
        val db = sql.writableDatabase
        var list = getAllData(context)
        //把插入的数据全部封装到ContentValues对象中
        val values = ContentValues()
        values.put("id", (list?.size ?: 0) + 1)
        values.put("name", name)
        values.put("password", password)
        values.put("email", email)
        values.put("phonenum", phonenum)
        db.insert("user", null, values)
        db.close()
    }

    fun insertApi(id: Int, sortId: Int, name: String?, root: String?, description: String?, url: String?, star: Int, context: Context) {
        var helper = B_TrashDatabase(context, "trash.db", null, 3)
        //如果数据库不存在，则创建数据，然后获取可读可写的数据库对象，如果存在，直接打开；
        val db = helper.writableDatabase
        //把插入的数据全部封装到ContentValues对象中
        val values = ContentValues()
        values.put("id", id)
        values.put("sortId", sortId)
        values.put("name", name)
        values.put("root", root)
        values.put("description", description)
        values.put("url", url)
        values.put("star", star)
        db.insert("trashdata", null, values)
        db.close()
    }

    fun searchData(context: Context): ArrayList<String> {
        val str = ArrayList<String>()
        //扫描数据库，将信息放入booklist
        var helper = B_TrashDatabase(context, "trash.db", null, 3) //打开数据库
        var db = helper.readableDatabase //获取数据库
        var cursor = db.query("record", null, null, null, null, null, null) //查询Book表中的所有数据
        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndex("name"))
            str.add(name)
        }
        cursor.close()
        db.close()
        return str
    }

    fun deleteDate(context: Context) {
        val helper = B_TrashDatabase(context, "trash.db", null, 3) //打开数据库
        val db = helper.readableDatabase
        db.execSQL("delete from record")
        db.close()
    }

    fun getSearchLikeData(context: Context,name : String): ArrayList<D_BaiTrashData> {
        val str = ArrayList<D_BaiTrashData>()
        val helper = B_TrashDatabase(context, "trash.db", null, 3) //打开数据库
        val db = helper.readableDatabase
        var cursor = db.query("trashdata", null, "name like '%$name%'", null, null, null, null)
        while (cursor.moveToNext())
        {
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val root = cursor.getString(cursor.getColumnIndex("root"))
            val description = cursor.getString(cursor.getColumnIndex("description"))
            val thdata = D_BaiTrashData(name, "0", root, description, null)
            str.add(thdata)
        }
        cursor.close()
        db.close()
        return str
    }

    fun getPassword(context: Context,user : String): String {
        val helper = B_TrashDatabase(context, "trash.db", null, 3)
        val db = helper.writableDatabase
        var temp = ""
        // 查询Book表中所有的数据
        val cursor = db.query("user", null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            if(cursor.getString(cursor.getColumnIndex("name")).equals(user) ){
                temp = cursor.getString(cursor.getColumnIndex("password"))
                break
            }
        }
        cursor.close()
        db.close()
        return temp
    }

    fun changePassword(context: Context,password : String,user: String) {
        val helper = B_TrashDatabase(context, "trash.db", null, 3)
        val db = helper.writableDatabase
        db.execSQL("update user set password = ? where name = ?", arrayOf<Any>(password, user))
    }

    fun deleteStar(context: Context,name: String,user: String){
        val helper = B_TrashDatabase(context, "trash.db", null, 3)
        val db = helper.writableDatabase
        db.execSQL("delete from star where name = ? and uuid = ?", arrayOf<String>(name, user))
        db.close()
    }

    fun insertStar(context: Context, user:String, name: String, root: String, description: String){
        val helper = B_TrashDatabase(context, "trash.db", null, 3)
        val db = helper.writableDatabase
        db.execSQL("insert into star (uuid,name,root,description) values(?,?,?,?)", arrayOf<Any>(user, name, root, description))
        db.close()
    }

    fun addStar(context: Context,star : Int, name: String){
        val helper = B_TrashDatabase(context, "trash.db", null, 3)
        val db = helper.writableDatabase
        db.execSQL("update trashdata set star = ? where name = ?", arrayOf(star, name))
        db.close()
    }

    fun setStar(context: Context,user: String,name: String): Boolean {
        var flag = false
        val helper = B_TrashDatabase(context, "trash.db", null, 3)
        val db = helper.writableDatabase
        val cursors: Cursor = db.query("star", null, null, null, null, null, null, null)
        while (cursors.moveToNext()) {
            if (cursors.getString(cursors.getColumnIndex("name")) == name && cursors.getString(cursors.getColumnIndex("uuid")) == user) {
                flag = true
                break
            }
        }
        cursors.close()
        db.close()
        return flag
    }
}