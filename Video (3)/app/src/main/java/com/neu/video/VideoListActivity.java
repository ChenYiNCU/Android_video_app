package com.neu.video;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.neu.video.pojo.ResultDTO;
import com.neu.video.pojo.Video;
import com.neu.video.utils.GsonObjectCallback;
import com.neu.video.utils.OkHttp3Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class VideoListActivity extends AppCompatActivity {

    private ListView lv_video;
    private List<Map<String,Object>> videoList=new ArrayList<Map<String,Object>> ();
    private String[] s=new String[]{"vimg","vname","vid","vtime","vdirector","vactor"};
    private int[] i=new int[]{R.id.iv_vImg,R.id.iv_vName,R.id.iv_vId,R.id.iv_vTime,R.id.iv_vDirector,R.id.iv_vActor};
    private SimpleAdapter videoAdapter;
    private Video video=new Video();
    private String jsonString;//用于保存前台向后台发送的数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();
        initComponent();
        init();
    }


    private void initComponent(){
        lv_video=findViewById(R.id.lv_video);
    }

    private void init(){
        String url = "http:192.168.8.62:8080/videoplayer/video/findAll";
        OkHttp3Utils.getInstance().doGet(url, new GsonObjectCallback<ResultDTO>() {
            @Override
            public void onUi(ResultDTO resultDTO) {
                videoList.clear();
                videoList.addAll(resultDTO.getDatas());
                videoAdapter=new SimpleAdapter(VideoListActivity.this,
                        videoList,
                        R.layout.lv_manage_video,
                        s,
                        i);

                videoAdapter.notifyDataSetChanged();
                videoAdapter.notifyDataSetInvalidated();

                //适配器使用网络图片
                videoAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                    @Override
                    public boolean setViewValue(View view, Object data, String textRepresentation) {
                        if (view instanceof ImageView && data instanceof String){
                            ImageView iv=(ImageView) view;
                            try{
                                Glide.with(VideoListActivity.this).load(data).into(iv);
                            }catch (Exception e){e.printStackTrace();}
                            return true;
                        }else
                            return false;
                    }
                });

                lv_video.setAdapter(videoAdapter);


                //ListView点击事件
                lv_video.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //根据需要将Map中数据抽取存入User对象中然后使用Intent将User对象传递到UserDesc页面
                        Intent intent = new Intent(VideoListActivity.this,VideoDescActivity.class);
                        intent.putExtra("vid",Double.valueOf(videoList.get(position).get("vid").toString()).intValue());
                        startActivity(intent);
                    }
                });


                //ListView长按事件
                lv_video.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                        //触发弹出对话框功能
                        //定义对话框
                        AlertDialog.Builder builder=new AlertDialog.Builder(VideoListActivity.this);
                        builder.setMessage("确认删除？");
                        builder.setTitle("提示");
                        //确定按钮以及事件
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //获取要删除的id
//                                int id=videoList.get(position).get("vid");
                                int id=Double.valueOf(videoList.get(position).get("vid").toString()).intValue();
                                //将当前适配器指定位置的数据先移出
                                if (videoList.remove(position)!=null){
                                    //调用删除的业务逻辑
                                    System.out.println("删除的用户编号是："+id);
                                    String url = "http:192.168.8.62:8080/videoplayer/video/delete";
                                    video.setVid(id);
                                    //将User对象转换为GSON
                                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();//设置日期格式
                                    jsonString = gson.toJson(video);
                                    //该方法需要发送json数据到后台
                                    OkHttp3Utils.getInstance().doPostJson(url, jsonString, new GsonObjectCallback<ResultDTO>() {
                                        @Override
                                        public void onUi(ResultDTO resultDTO) {
                                            //考虑将数据放入到组件中
                                            Toast.makeText(VideoListActivity.this,"删除成功！",Toast.LENGTH_SHORT).show();
                                        }
                                        @Override
                                        public void onFailed(Call call, IOException e) {
                                            System.out.println(e);
                                        }
                                    });

                                }else {
                                    Toast.makeText(VideoListActivity.this,"删除失败！",Toast.LENGTH_SHORT).show();
                                }
                                videoAdapter.notifyDataSetChanged();
                                videoAdapter.notifyDataSetInvalidated();
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        builder.create().show();
                        return true;
                    }
                });
            }
            @Override
            public void onFailed(Call call, IOException e) {
                System.out.println(e);
            }
        });
    }

    public void toMuser(View view){
        Intent intent=new Intent(VideoListActivity.this,UserListActivity.class);
        startActivity(intent);
    }

    public void toMvideo(View view){
        Intent intent=new Intent(VideoListActivity.this,VideoListActivity.class);
        startActivity(intent);
    }


    public void toUpload(View view){
        Intent intent=new Intent(VideoListActivity.this,UploadVideoActivity.class);
        startActivity(intent);
    }

    public void toLogin(View view){
        Intent intent=new Intent(VideoListActivity.this,LoginActivity.class);
        startActivity(intent);
    }
}
