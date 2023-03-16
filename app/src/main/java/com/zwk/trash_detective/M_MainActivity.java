//此代码要用
package com.zwk.trash_detective;

import com.baidu.aip.imageclassify.AipImageClassify;
import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.zwk.trash_detective.Bean.D_BaiTrashData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class M_MainActivity extends AppCompatActivity {

    File imagefile;
    Uri imageuri;
    private int control_flag = 0;
    private String APP_ID = "27207070";
    private String API_KEY = "18cHrYr9qm19xE6DDyTKWegh";
    private String SECRET_KEY = "krFPCNQLGX16PB4UMIiXcsd9UWbGXu5G";
    private AipImageClassify aipImageClassify =  new AipImageClassify(APP_ID, API_KEY, SECRET_KEY);
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.CAMERA};
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);

        imagefile = new File(Environment.getExternalStorageDirectory(), "cache.jpg");
        imageuri = FileProvider.getUriForFile(this, this.getPackageName() + ".provider", imagefile);
        //imageuri = Uri.fromFile(imagefile);
        this.context = this;
        InitCamera();
    }

    public boolean verifyStoragePermissions(Activity activity) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, 200);
            return false;
        } else {
            return true;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK){
            Toast.makeText(this, "失败", Toast.LENGTH_SHORT).show();
            return;
        }
        switch(requestCode){
            case 1:
                X_LoadingDialog.show(this);
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageuri));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] datas = baos.toByteArray();
                    showToast("正在保存图像");
                    new M_MainActivity.NetLoadingThread(datas).start();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    class NetLoadingThread extends Thread{
        byte[] capturedImage;
        NetLoadingThread(byte[] capturedImage){
            this.capturedImage = capturedImage;
        }
        @Override
        public void run() {
            Intent intent = null;
            showToast("正在发送请求");
            aipImageClassify.setConnectionTimeoutInMillis(2000);
            aipImageClassify.setSocketTimeoutInMillis(6000);
            HashMap options = new HashMap<String, String>();
            options.put("baike_num", "6");
            try {
                JSONObject res = aipImageClassify.advancedGeneral(capturedImage, options);
                JSONArray jsonarray  = new JSONArray(res.get("result").toString());
                Bundle bundle = new Bundle();
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject temp = jsonarray.getJSONObject(i);
                    D_BaiTrashData tr = new D_BaiTrashData(temp.get("keyword").toString(),temp.get("score").toString(), temp.get("root").toString(),temp.get("baike_info").toString());
                    bundle.putSerializable("onlineData" + i,tr);
                }
                if (bundle != null){
                    intent = new Intent(M_MainActivity.this, V_TrashShowActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtras(bundle);
                    intent.putExtra("control_flag",control_flag);
                }
                X_LoadingDialog.dismiss();
                if (intent != null) {
                    try{
                        context.startActivity(intent);
                        finish();
                    }catch (ActivityNotFoundException e){
                        e.printStackTrace();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                showToast("连接服务异常");
                X_LoadingDialog.dismiss();
            }
        }
    }

    private void InitCamera(){
        Button start_btn=findViewById(R.id.start);
        start_btn.setOnClickListener(view -> {
                if (checkCameraHardware()) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //return-data:是将结果保存在data中返回，在onActivityResult中，直接调用intent.getdata()就可以获取值了，这里设为fase，就是不让它保存在data中，
                    intent.putExtra("return-data", false);
                    //MediaStore.EXTRA_OUTPUT：由于我们不让它保存在Intent的data域中，但我们总要有地方来保存我们的图片啊，这个参数就是转移保存地址的，对应Value中保存的URI就是指定的保存地址。
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
                    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                    intent.putExtra("noFaceDetection", true);
                    control_flag = 1;
                    startActivityForResult(intent, 1);
                }
        });

        ImageView image = findViewById(R.id.imageView);
        image.setOnClickListener(view -> {
            ForwardActivity(S_SettingActivity.class);
        });

        ImageView user = findViewById(R.id.enter_user);
        user.setOnClickListener(view -> {
            ForwardActivity(S_UserInfoActivity.class);
        });

        ImageView image2 = findViewById(R.id.imageView2);
        image2.setOnClickListener(view -> {
            ForwardActivity(M_SearchActivity.class);
        });

    }

    private boolean checkCameraHardware() {
        if (M_MainActivity.this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            return true;
        } else {
            showToast("您的设备中没有相机");
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showToast("授权成功");
            } else {
                showToast("授权失败,请去设置打开权限");
            }
        }
    }

    private void ForwardActivity(Class c){
        Intent intent = new Intent(M_MainActivity.this, c);
        startActivity(intent);
    }

    private void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(M_MainActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
