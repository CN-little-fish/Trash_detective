//此代码要用

package com.zwk.trash_detective;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.zwk.trash_detective.Bean.D_BaiTrashData;
import com.zwk.trash_detective.Bean.GetDataBase;
import com.zwk.trash_detective.Bean.D_SqlTrashData;
import com.zwk.trash_detective.Bean.B_TrashDatabase;

public class M_DescriptionActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView name_view;
    private TextView type_view;
    private TextView description_view;
    private TextView classification_view ;
    private ImageView classification_img;
    private Button back_btn;
    private ImageButton refresh_btn;
    private ImageView add_things;
    private TextView star_view;

    private Handler handler ;
    private D_BaiTrashData Trash;
    String[] classification_names={"可回收垃圾","有害垃圾","厨余垃圾","其他垃圾","未分类垃圾"};
    D_SqlTrashData data;
    int[] classification_imgs = {R.drawable.recy,R.drawable.poison,R.drawable.cook,R.drawable.other, R.drawable.unknown};
    String user;
    boolean setstar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        handler =new Handler();

        Intent intent=getIntent();
        // 实例化一个Bundle
        Bundle bundle=intent.getExtras();
        //获取里面的Persion里面的数据
        Trash = (D_BaiTrashData) bundle.getSerializable("onlineData");
        X_LoadingDialog.show(this);
        SharedPreferences sp_pref = getSharedPreferences("user_mes", Context.MODE_PRIVATE);
        user = sp_pref.getString("user","");

        selectApi();
        this.InitView();
        this.startAThread();

    }

    private void InitView(){
        back_btn = findViewById(R.id.back);
        refresh_btn = findViewById(R.id.refresh);
        name_view = findViewById(R.id.name);
        type_view = findViewById(R.id.types);
        star_view = findViewById(R.id.star);
        description_view = findViewById(R.id.description);
        classification_view = findViewById(R.id.classification);
        classification_img = findViewById(R.id.trash);
        add_things = findViewById(R.id.favorty);

        refresh_btn.setOnClickListener(this);
        back_btn.setOnClickListener(this);
        add_things.setOnClickListener(this);
        star_view.setOnClickListener(this);

        if (setstar){
            add_things.setImageResource(R.drawable.add);
        }else{
            add_things.setImageResource(R.drawable.null_add);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.favorty: {
                if (setstar){ //当有收藏点击取消搜藏
                    add_things.setImageResource(R.drawable.null_add);
                    new GetDataBase().deleteStar(this, data.getName(), user);
                    setstar = false;
                }else{
                    add_things.setImageResource(R.drawable.add);
                    new GetDataBase().insertStar(this, user, data.getName(),data.getRoot(),data.getDescription());
                    setstar = true;
                }

                break;
            }
            case R.id.star: {
                new GetDataBase().addStar(this, data.getStar() + 1, data.getName());
                data.setStar(data.getStar() + 1);
                star_view.setText("点赞：" + data.getStar());
                break;
            }
            case R.id.back: {
                finish();
                break;
            }
            case R.id.refresh: {
                X_LoadingDialog.show(this);
                this.startAThread();
                break;
            }
        }
    }

    private void  startAThread(){new DescriptionThread().start();}

    private class updateUI implements Runnable {
        private final D_BaiTrashData trash;
        updateUI(D_BaiTrashData trash){this.trash = trash;}
        @Override
        public void run() {
            try {
                name_view.setText(data.getName());
                name_view.setTextSize(30);
                star_view.setText("点赞：" + data.getStar());
                classification_img.setImageResource(classification_imgs[data.getSortId()-1]);
                description_view.setText(data.getDescription());
                description_view.setTextSize(20);
                classification_view.setText(data.getRoot());
                classification_view.setTextSize(25);
                type_view.setText(classification_names[data.getSortId()-1]);
                type_view.setTextSize(20);

            }catch (Exception e){
                showToast("出现错误，请重试");
                e.printStackTrace();
            }
            X_LoadingDialog.dismiss();
        }
    }

    private class DescriptionThread extends Thread{
        @Override
        public void run() {
            handler.post(new updateUI(Trash));
        }
    }

    private void selectApi() {
        data = new GetDataBase().ColTrash(this,Trash);
        setstar = new GetDataBase().setStar(this, user,data.getName());
    }

    private void showToast(final String text) {
        runOnUiThread(() -> Toast.makeText(M_DescriptionActivity.this, text, Toast.LENGTH_SHORT).show());
    }
}



//    private void deleteApi() {
//        //如果数据不存在，则创建数据，然后获取可读可写的数据库对象，如果存在，直接打开；
//        SQLiteDatabase db = helper.getWritableDatabase();
//
//        //删除name=阿龙 id=10的记录
//        db.delete("user","name =? and id = ?",new String[]{"阿龙","10"});
//
//        //writableDatabase.execSQL("delete from user where name = ?", new Object[]{"藏三"});
//        db.close();
//    }
//
//    private void updateApi(String name) {
//        //如果数据不存在，则创建数据，然后获取可读可写的数据库对象，如果存在，直接打开；
//        SQLiteDatabase db = helper.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("name", name);
//        //修改id=1的记录中的name = 张三丰
//        db.update("trashdata", values, "id = ?", new String[]{"1"});
//
//        //writableDatabase.execSQL("update user set id = ? where name = ?", new Object[]{6,"王五"});
//        db.close();
//    }
//
//    public void insertApi(int id, int sortId, String name, String root, String description, String url, int star) {
//        //如果数据库不存在，则创建数据，然后获取可读可写的数据库对象，如果存在，直接打开；
//        SQLiteDatabase db = helper.getWritableDatabase();
//        //把插入的数据全部封装到ContentValues对象中
//        ContentValues values = new ContentValues();
//        values.put("id",id);
//        values.put("sortId",sortId);
//        values.put("name",name);
//        values.put("root",root);
//        values.put("description",description);
//        values.put("url",url);
//        values.put("star",star);
//        db.insert("trashdata", null, values);
//        //writableDatabase.execSQL("insert into user(id,name,phone) values(?,?,?)", new Object[]{4, "赵六","17888"});
//        db.close();
//    }
