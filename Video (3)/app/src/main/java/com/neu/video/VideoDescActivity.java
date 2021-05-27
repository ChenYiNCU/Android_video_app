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

public class VideoDescActivity extends AppCompatActivity {
    private String videoName="",imgName="",vurl="",vimg="",jsonString;//用于保存前台向后台发送的数据
    private int id;
    private Video video=new Video();
    private EditText et_vname,et_vdesc,et_vtime,et_vdirector,et_vactor;
    private boolean img_flag=false,url_flag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_desc);
        PermissionUtil.verifyStoragePermissions(VideoDescActivity.this);
        Intent intent=getIntent();
        id=(int)intent.getSerializableExtra("vid");
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();
        initComponent();
        init();
    }

    private void initComponent(){
        et_vname=findViewById(R.id.et_vname);
        et_vdesc=findViewById(R.id.et_vdesc);
        et_vtime=findViewById(R.id.et_vtime);
        et_vdirector=findViewById(R.id.et_vdirector);
        et_vactor=findViewById(R.id.et_vactor);
    }

    private void init(){

        String url = "http:192.168.8.62:8080/videoplayer/video/findById";
        video.setVid(id);
        //将User对象转换为GSON
        Gson gson = new Gson();
        jsonString = gson.toJson(video);
        //该方法需要发送json数据到后台
        OkHttp3Utils.getInstance().doPostJson(url, jsonString, new GsonObjectCallback<ResultDTO>() {
            @Override
            public void onUi(ResultDTO resultDTO) {
                Gson gson = new Gson();
                String jsonString = gson.toJson(resultDTO.getData());
                video = gson.fromJson(jsonString,Video.class);
                et_vname.setText(video.getVname()+"");
                et_vdesc.setText(video.getVdesc()+"");
                et_vtime.setText(video.getVtime()+"");
                et_vdirector.setText(video.getVdirector()+"");
                et_vactor.setText(video.getVactor()+"");
                vurl=video.getVurl()+"";
                vimg=video.getVimg()+"";
            }
            @Override
            public void onFailed(Call call, IOException e) {
                System.out.println(e);
            }
        });
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
                url_flag=true;
                Uri uri = data.getData();
                videoName = FileUtils.getRealFilePath(VideoDescActivity.this,uri);
            }
        }
        if (requestCode == 2) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                img_flag=true;
                Uri uri = data.getData();
                imgName = FileUtils.getRealFilePath(VideoDescActivity.this,uri);
            }
        }
    }

    public void uploadVideo(View view) {
        video.setVname(et_vname.getText().toString());
        video.setVdesc(et_vdesc.getText().toString());
        video.setVtime(et_vtime.getText().toString());
        video.setVdirector(et_vdirector.getText().toString());
        video.setVactor(et_vactor.getText().toString());
        video.setVurl(vurl);
        video.setVimg(vimg);
        if(img_flag&&url_flag){
            up1();
        }else if(!img_flag&&url_flag){
            up2();
        }else if(img_flag&&!url_flag){
            up3();
        }else{
            up4();
        }
    }

        public void up1(){File videoFile = new File(videoName);
            String url = "http:192.168.8.62:8080/videoplayer/video/uploadVideo";
            String videoFileName = videoFile.getName();
            //调用okhttp3上传图片方法
            OkHttp3Utils.uploadPic(url, videoFile, videoFileName, new GsonObjectCallback<ResultDTO>() {
                @Override
                public void onUi(ResultDTO resultDTO) {
                    String url = "http:192.168.8.62:8080/videoplayer/video/uploadVideo";
                    vurl = resultDTO.getMsg();
                    File imgFile = new File(imgName);
                    String imgFileName = imgFile.getName();
                    OkHttp3Utils.uploadPic(url, imgFile, imgFileName, new GsonObjectCallback<ResultDTO>() {
                        @Override
                        public void onUi(ResultDTO resultDTO) {
                            vimg = resultDTO.getMsg();
                            String url = "http:192.168.8.62:8080/videoplayer/video/update";
                            System.out.println(video.toString());//打印当前User 对象中数据
                            Gson gson = new Gson();
                            jsonString = gson.toJson(video);
                            //该方法需要发送json数据到后台
                            OkHttp3Utils.getInstance().doPostJson(url, jsonString, new GsonObjectCallback<ResultDTO>() {
                                @Override
                                public void onUi(ResultDTO resultDTO) {
                                    Toast.makeText(VideoDescActivity.this,"修改成功！",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(VideoDescActivity.this,VideoListActivity.class);
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

        public void up2(){File videoFile = new File(videoName);
            String url = "http:192.168.8.62:8080/videoplayer/video/uploadVideo";
            String videoFileName = videoFile.getName();
            //调用okhttp3上传图片方法
            OkHttp3Utils.uploadPic(url, videoFile, videoFileName, new GsonObjectCallback<ResultDTO>() {
                @Override
                public void onUi(ResultDTO resultDTO) {
                    vurl = resultDTO.getMsg();
                    String url = "http:192.168.8.62:8080/videoplayer/video/update";
                            System.out.println(video.toString());//打印当前User 对象中数据
                            //将User对象转换为GSON
                            Gson gson = new Gson();
                            jsonString = gson.toJson(video);
                            //该方法需要发送json数据到后台
                            OkHttp3Utils.getInstance().doPostJson(url, jsonString, new GsonObjectCallback<ResultDTO>() {
                                @Override
                                public void onUi(ResultDTO resultDTO) {
                                    Toast.makeText(VideoDescActivity.this,"修改成功！",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(VideoDescActivity.this,VideoListActivity.class);
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
        public void up3(){
                    String url = "http:192.168.8.62:8080/videoplayer/video/uploadVideo";
                    File imgFile = new File(imgName);
                    String imgFileName = imgFile.getName();
                    OkHttp3Utils.uploadPic(url, imgFile, imgFileName, new GsonObjectCallback<ResultDTO>() {
                        @Override
                        public void onUi(ResultDTO resultDTO) {
                            vimg = resultDTO.getMsg();
                            String url = "http:192.168.8.62:8080/videoplayer/video/update";
                            System.out.println(video.toString());//打印当前User 对象中数据
                            //将User对象转换为GSON
                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();//设置日期格式
                            jsonString = gson.toJson(video);
                            //该方法需要发送json数据到后台
                            OkHttp3Utils.getInstance().doPostJson(url, jsonString, new GsonObjectCallback<ResultDTO>() {
                                @Override
                                public void onUi(ResultDTO resultDTO) {
                                    Toast.makeText(VideoDescActivity.this,"修改成功！",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(VideoDescActivity.this,VideoListActivity.class);
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

            public void up4(){
                String url = "http:192.168.8.62:8080/videoplayer/video/update";
                System.out.println(video.toString());//打印当前User 对象中数据
                //将User对象转换为GSON
                Gson gson = new Gson();
                jsonString = gson.toJson(video);
                //该方法需要发送json数据到后台
                OkHttp3Utils.getInstance().doPostJson(url, jsonString, new GsonObjectCallback<ResultDTO>() {
                @Override
                public void onUi(ResultDTO resultDTO) {
                    Toast.makeText(VideoDescActivity.this,"修改成功！",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(VideoDescActivity.this,VideoListActivity.class);
                    startActivity(intent);
                }
                @Override
                public void onFailed(Call call, IOException e) {
                    System.out.println(e);
                    }
             });
            }
}
