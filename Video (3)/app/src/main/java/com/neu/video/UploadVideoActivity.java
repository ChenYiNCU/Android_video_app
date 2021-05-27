package com.neu.video;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.neu.video.pojo.ResultDTO;
import com.neu.video.pojo.VType;
import com.neu.video.pojo.Video;
import com.neu.video.utils.FileUtils;
import com.neu.video.utils.GsonObjectCallback;
import com.neu.video.utils.OkHttp3Utils;
import com.neu.video.utils.PermissionUtil;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;

public class UploadVideoActivity extends AppCompatActivity {

    private String videoName="";
    private String imgName="";
    private String vurl="";
    private String vimg="";
    private Video video=new Video();
    private EditText et_vname,et_vdesc,et_vtime,et_vdirector,et_vactor,et_vtype;
    private String jsonString;//用于保存前台向后台发送的数据
    private String responseData;//用于保存后台向前台返回的响应数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);
        PermissionUtil.verifyStoragePermissions(UploadVideoActivity.this);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();
        initComponent();

    }


    private void initComponent(){
        et_vname=findViewById(R.id.et_vname);
        et_vdesc=findViewById(R.id.et_vdesc);
        et_vtime=findViewById(R.id.et_vtime);
        et_vdirector=findViewById(R.id.et_vdirector);
        et_vactor=findViewById(R.id.et_vactor);
        et_vtype=findViewById(R.id.et_vtype);
    }

    private void init(){
    }


    public void chooseVideo(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "video/*");
        startActivityForResult(intent, 1);
    }

    public void chooseImg(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, 2);
    }

    //Intent页面数据回显
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
            // 得到视频的全路径
            Uri uri = data.getData();
            videoName = FileUtils.getRealFilePath(UploadVideoActivity.this,uri);
            }
        }
        if (requestCode == 2) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                imgName = FileUtils.getRealFilePath(UploadVideoActivity.this,uri);
            }
        }
    }

    public void uploadVideo(View view) {
            File videoFile = new File(videoName);
            String url = "http://192.168.8.62:8080/videoplayer/video/uploadVideo";
            String videoFileName = videoFile.getName();
            //调用okhttp3上传图片方法
            OkHttp3Utils.uploadPic(url, videoFile, videoFileName, new GsonObjectCallback<ResultDTO>() {
                @Override
                public void onUi(ResultDTO resultDTO) {
                    String url = "http://192.168.8.62:8080/videoplayer/video/uploadVideo";
                    vurl = resultDTO.getMsg();
                    File imgFile = new File(imgName);
                    String imgFileName = imgFile.getName();
                    OkHttp3Utils.uploadPic(url, imgFile, imgFileName, new GsonObjectCallback<ResultDTO>() {
                        @Override
                        public void onUi(ResultDTO resultDTO) {
                            vimg = resultDTO.getMsg();
                            String vname = et_vname.getText().toString();
                            String vdesc = et_vdesc.getText().toString();
                            String vtime = et_vtime.getText().toString();
                            String vdirector = et_vdirector.getText().toString();
                            String vactor = et_vactor.getText().toString();
                            int vtype = Integer.parseInt(et_vtype.getText().toString());
                            String url = "http://192.168.8.62:8080/videoplayer/video/add";
                            video.setVname(vname);
                            video.setVdesc(vdesc);
                            video.setVtime(vtime);
                            video.setVdirector(vdirector);
                            video.setVactor(vactor);
                            VType vType = new VType();
                            vType.setTid(vtype);
                            video.setVtype(vType);
                            video.setVurl(vurl);
                            video.setVimg(vimg);
                            System.out.println(video.toString());//打印当前User 对象中数据
                            //将User对象转换为GSON
                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();//设置日期格式
                            jsonString = gson.toJson(video);
                            //该方法需要发送json数据到后台
                            OkHttp3Utils.getInstance().doPostJson(url, jsonString, new GsonObjectCallback<ResultDTO>() {
                                @Override
                                public void onUi(ResultDTO resultDTO) {
                                    Toast.makeText(UploadVideoActivity.this,"添加成功！",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(UploadVideoActivity.this,VideoListActivity.class);
                                    startActivity(intent);
                                }

                                @Override
                                public void onFailed(Call call, IOException e) {
                                    System.out.println(e);
                                }
                            });
                        }

                        @Override
                        public void onFailed(Call call, IOException e) {
                            System.out.println("失败！");
                        }
                    });
                }

                @Override
                public void onFailed(Call call, IOException e) {
                    System.out.println("失败！");
                }
            });
        }
}
