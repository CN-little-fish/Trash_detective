package com.zwk.trash_detective;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.zwk.trash_detective.Bean.B_TrashDatabase;
import com.zwk.trash_detective.Bean.D_searchData;
import com.zwk.trash_detective.Bean.GetDataBase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class M_SearchActivity extends AppCompatActivity {
    ArrayList str = new ArrayList<String>();
    ListView list;
    SearchView searchView;
    Button clear;
    B_TrashDatabase helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        str = new GetDataBase().searchData(this);
        init();
        clickControl();
    }

    private void init() {
        helper = new B_TrashDatabase(this, "trash.db", null, 3); //打开数据库
        list = findViewById(R.id.list22);
        list.setTextFilterEnabled(true);
        list.setAdapter(new ArrayAdapter(M_SearchActivity.this, android.R.layout.simple_list_item_1,str));
        clear = findViewById(R.id.clearbutton);

        searchView = findViewById(R.id.searchview);
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("查找");
    }

    private void clickControl(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SQLiteDatabase db = helper.getWritableDatabase();
                Cursor cursor = db.query("record", null, null, null, null, null, null);
                ContentValues values = new ContentValues();
                values.put("name", query);
                values.put("id", cursor.getCount()+1);
                db.insert("record", null, values);
                db.close();

                Bundle bundle = new Bundle();
                bundle.putSerializable("name", query);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(M_SearchActivity.this, V_ResultActivity.class);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(TextUtils.isEmpty(newText)){
                    list.clearTextFilter();
                }else {
                    list.setFilterText(newText);
                }
                return false;
            }
        });

        clear.setOnClickListener(view -> {
            deleteDate();
            load();
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(M_SearchActivity.this, (Integer) str.get(i), Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putSerializable("name", (Serializable) str.get(i));
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(M_SearchActivity.this, V_ResultActivity.class);
                startActivity(intent);
            }
        });
    }


    private void deleteDate(){
        new GetDataBase().deleteDate(this);
        Toast.makeText(M_SearchActivity.this,"清除成功", Toast.LENGTH_SHORT).show();
    }

    private void load(){
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

}


