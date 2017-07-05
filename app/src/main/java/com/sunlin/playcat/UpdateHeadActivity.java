package com.sunlin.playcat;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;
import com.sunlin.playcat.common.ImageHelp;
import com.sunlin.playcat.common.LogC;
import com.sunlin.playcat.common.ShowMessage;
import com.sunlin.playcat.view.BottomPopView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateHeadActivity extends MyActivtiy implements View.OnClickListener {
    private String TAG="UpdateHeadActivity";
    private BottomPopView bottomPopView;

    private ImageView img;
    private EditText img_content;
    private Button nati;
    private Button pai;
    private Button submit;
    LinearLayout photo_full;
    private static String paiImgUrl;
    private Uri headUri;


    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            String msg = "";
            switch (menuItem.getItemId()) {
                case R.id.action_edit:
                    msg += "Click edit";
                    break;
                case R.id.action_share:
                    msg += "Click share";
                    break;
                case R.id.action_settings:
                    msg += "Click setting";
                    break;
            }

            if(!msg.equals("")) {
                Toast.makeText(UpdateHeadActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化导航栏
        ToolbarBuild("修改头像",true,true);
        ToolbarBackListense();
        ToolbarSetListense(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenuItem(v);
            }
        });

        initView();
        //

    }
    private void initView() {
        img = (ImageView) findViewById(R.id.img);
        nati = (Button) findViewById(R.id.natives);
        pai = (Button) findViewById(R.id.pai);
        submit = (Button) findViewById(R.id.submit);
        img_content=(EditText)findViewById(R.id.img_content);
        photo_full=(LinearLayout)findViewById(R.id.photo_full);

        nati.setOnClickListener(this);
        pai.setOnClickListener(this);
        submit.setOnClickListener(this);

        View.OnClickListener keyboard_hide = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) UpdateHeadActivity.this
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }

        };
        photo_full.setClickable(true);
        photo_full.setOnClickListener(keyboard_hide);
    }
    public void showMenuItem(View parent)
    {
        bottomPopView = new BottomPopView(this, parent) {
            @Override
            public void onTopButtonClick() {
                //拍照

            }
            @Override
            public void onBottomButtonClick() {
                //选择本地图片
            }
        };
        bottomPopView.setTopText("拍照");
        bottomPopView.setBottomText("选择图片");
        // 显示底部菜单
        bottomPopView.show();
    }
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_update_head;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.natives:
                Crop.pickImage(this);
                break;
            case R.id.pai:
                paiImgUrl=ImageHelp.PaiImg("temp_head.jpg",this);
                break;
            case R.id.submit:
                if (headUri == null) {
                    ShowMessage.taskShow(UpdateHeadActivity.this,"创建头像失败");
                }else{
                    submitUploadFile();
                }
                break;
        }
    }
    /**
     * 拍照上传

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch(requestCode) {
                case 1:

                    Bundle extras = data.getExtras();
                    Bitmap b = (Bitmap) extras.get("data");
                    img.setImageBitmap(b);
                    String name = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
                    String fileNmae = Environment.getExternalStorageDirectory().toString()+ File.separator+"playcat/temp/image/"+name+".jpg";
                    srcPath = fileNmae;
                    System.out.println(srcPath+"----------保存路径1");
                    File myCaptureFile =new File(fileNmae);
                    try {
                        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                            if(!myCaptureFile.getParentFile().exists()){
                                myCaptureFile.getParentFile().mkdirs();
                            }
                            BufferedOutputStream bos;
                            bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
                            b.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                            bos.flush();
                            bos.close();
                        }else{

                            ShowMessage.taskShow(UpdateHeadActivity.this, "保存失败，SD卡无效");
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case Crop.REQUEST_PICK:
                    try {
                        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
                        Crop.of(data.getData(), destination).asSquare().start(this);

                        Log.d(TAG, "onActivityResult:相册 " + data.getData().toString());

                        ContentResolver resolver = getContentResolver();
                        InputStream inputStream = resolver.openInputStream(data.getData());
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        img.setImageBitmap(bitmap);
                        //这是获取的图片保存在sdcard中的位置
                        System.out.println(srcPath + "----------保存路径2");

                    }catch (Exception e){
                        LogC.write(e,TAG);
                        ShowMessage.taskShow(UpdateHeadActivity.this,"SD卡读取失败，请重试");
                    }
                    break;
                case Crop.REQUEST_CROP:
                    try {
                        img.setImageURI(Crop.getOutput(data));
                    }catch (Exception e){
                        LogC.write(e,TAG);
                        ShowMessage.taskShow(UpdateHeadActivity.this,"图片压缩失败，请重试");
                    }
                case Crop.RESULT_ERROR:
                    ShowMessage.taskShow(UpdateHeadActivity.this,Crop.getError(data).getMessage());
                default:
                    break;
            };
        }n =1;
    }*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        }else  if(requestCode==ImageHelp.PAI_BACK){
            beginCrop(Uri.fromFile(new File(paiImgUrl)));
        }
        else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
    }
    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(UpdateHeadActivity.this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            headUri=Crop.getOutput(result);
            img.setImageDrawable(null);
            img.setImageURI(headUri);

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void submitUploadFile(){
        try {
            Bitmap photoBmp = ImageHelp.getBitmapFormUri(UpdateHeadActivity.this, headUri);
            if (photoBmp == null) {
                return;
            }
            Log.i(TAG, "请求的fileName=" + headUri.toString());

        } catch (IOException e) {
            e.printStackTrace();
            LogC.write(e,TAG);
            ShowMessage.taskShow(UpdateHeadActivity.this,"修改头像失败，请重试");
        }
    }

}
