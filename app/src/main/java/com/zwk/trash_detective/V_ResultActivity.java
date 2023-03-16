package com.zwk.trash_detective;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zwk.trash_detective.Bean.D_BaiTrashData;
import com.zwk.trash_detective.Bean.GetDataBase;

import java.util.ArrayList;
import java.util.List;

public class V_ResultActivity extends AppCompatActivity {
    private RecyclerView list;
    List<D_BaiTrashData> str = new ArrayList<>();
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Bundle bundle = this.getIntent().getExtras();
        name = (String) bundle.getSerializable("name");
        init(name);
    }

//    private void init2(String name1) {
//        //扫描数据库，将收藏信息读出
//        B_RecordSqlOpenHelper mdb = new B_RecordSqlOpenHelper(this, "trash.db", null, 3);//打开数据库
//        SQLiteDatabase sd = mdb.getReadableDatabase();//获取数据库
//        Cursor cursor=sd.query("trashdata",null,"name like '%" + name1 + "%'",null,null,null,null);//查询Book表中的所有数据
//        while(cursor.moveToNext()){
//            String name=cursor.getString(cursor.getColumnIndex("name"));
//            str2.add(name);
//        }
//        list2.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1,str2));
//        cursor.close();
//    }

    private void init(String name) {
        list = findViewById(R.id.Rlist);
        //list2 = findViewById(R.id.list22);
        str = new GetDataBase().getSearchLikeData(this, name);

        list.setLayoutManager(new LinearLayoutManager(this));
        A_ResultAdapter adapter = new A_ResultAdapter(str);
        list.setAdapter(adapter);
    }

}