package com.neu.video;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.neu.video.pojo.ResultDTO;
import com.neu.video.pojo.VType;
import com.neu.video.pojo.Video;
import com.neu.video.utils.GsonObjectCallback;
import com.neu.video.utils.OkHttp3Utils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ClassActivity extends AppCompatActivity {
    private GridView gv_video;
    private EditText et_search;
    Video video=new Video();
    private List<Map<String,Object>> videoList;
    private SimpleAdapter videoAdapter;
    private String jsonString;//用于保存前台向后台发送的数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();
        initComponent();
        init();
    }

    private void initComponent(){
        gv_video=findViewById(R.id.gv_video);
        et_search=findViewById(R.id.et_search);

        Intent intent=getIntent();
        if(intent.getStringExtra("search")!=""){
            String search=intent.getStringExtra("search");
            if (search!=""&&search!=null){
                et_search.setText(search);
                search(findViewById(R.id.gv_video));
            }
        }
    }

    private void init() {
        String url = "http:192.168.8.62:8080/videoplayer/video/findAll";
        OkHttp3Utils.getInstance().doGet(url, new GsonObjectCallback<ResultDTO>() {
            @Override
            public void onUi(ResultDTO resultDTO) {
                //考虑将数据放入到组件中
                System.out.println("状态码:" + resultDTO.getCode());
                System.out.println("信息:" + resultDTO.getMsg());
                System.out.println("单个对象数据:" + resultDTO.getData());
                System.out.println("多个对象数据:" + resultDTO.getDatas());
                videoList = resultDTO.getDatas();

                videoAdapter=new SimpleAdapter(ClassActivity.this,
                        videoList,
                        R.layout.lv_video_style,
                        new String[]{"vid","vimg","vname"},
                        new int[]{R.id.tv_playNum,R.id.iv_videoImg,R.id.tv_videoName});

                //适配器使用网络图片
                videoAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                    @Override
                    public boolean setViewValue(View view, Object data, String textRepresentation) {
                        if (view instanceof ImageView && data instanceof String) {
                            ImageView iv = (ImageView) view;
                            try {
                                Glide.with(ClassActivity.this).load(data).into(iv);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return true;
                        } else
                            return false;
                    }
                });
                gv_video.setAdapter(videoAdapter);
            }

            @Override
            public void onFailed(Call call, IOException e) {
                System.out.println(e);
            }
        });

        //ListView点击事件
        gv_video.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //根据需要将Map中数据抽取存入User对象中然后使用Intent将User对象传递到UserDesc页面
                System.out.println(Double.valueOf(videoList.get(position).get("vid").toString()));
                Intent intent = new Intent(ClassActivity.this,PlayActivity.class);
                intent.putExtra("vid",Double.valueOf(videoList.get(position).get("vid").toString()).intValue());
                startActivity(intent);
            }
        });
    }

    public void toIndex(View view){
        Intent intent=new Intent(ClassActivity.this,IndexActivity.class);
        startActivity(intent);
    }

    public void toClass(View view){
        Intent intent=new Intent(ClassActivity.this,ClassActivity.class);
        startActivity(intent);
    }

    public void toPersonal(View view){
        Intent intent=new Intent(ClassActivity.this,PersonalActivity.class);
        startActivity(intent);
    }

    public void search(View view){
        String search=et_search.getText().toString();
//        //1.创建OkHttpClient对象
//        OkHttpClient okHttpClient = new OkHttpClient();
//        //2.创建Request对象，设置一个url地址,设置请求方式。
//        Request request = new Request.Builder().url("http://192.168.8.62:8080/videoplayer/video/search?src="+search).method("get",null).build();
//        //3.创建一个call对象,参数就是Request请求对象
//        Call call = okHttpClient.newCall(request);
//        //4.请求加入调度，重写回调方法
//        call.enqueue(new Callback() {
//            //请求失败执行的方法
//            @Override
//            public void onFailure(Call call, IOException e) {
//                System.out.println("get请求失败");
//            }
//            //请求成功执行的方法
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String str=response.body().string();      //得到返回的json字符串
//                Gson gson = new Gson();
//                //2. 声明Type对象
//                Type type = new TypeToken<List<Map<String,Object>>>(){}.getType();
//                //3. 解析Json String为List
//                videoList = gson.fromJson(str, type);
//                videoAdapter = new SimpleAdapter(ClassActivity.this,
//                        videoList,
//                        R.layout.lv_video_style,
//                        new String[]{"vimg", "vname"},
//                        new int[]{R.id.iv_videoImg, R.id.tv_videoName});
//
//                //适配器使用网络图片
//                videoAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
//                    @Override
//                    public boolean setViewValue(View view, Object data, String textRepresentation) {
//                        if (view instanceof ImageView && data instanceof String) {
//                            ImageView iv = (ImageView) view;
//                            try {
//                                Glide.with(ClassActivity.this).load(data).into(iv);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            return true;
//                        } else
//                            return false;
//                    }
//                });
//                gv_video.setAdapter(videoAdapter);
//                System.out.println("get请求成功");
//            }
//        });

        String url="http://192.168.8.62:8080/videoplayer/video/search";
        video.setVname(search);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();//设置日期格式
        jsonString = gson.toJson(video);
        //该方法需要发送json数据到后台
        OkHttp3Utils.getInstance().doPostJson(url, jsonString, new GsonObjectCallback<ResultDTO>() {
            @Override
            public void onUi(ResultDTO resultDTO) {
                //考虑将数据放入到组件中
                videoList = resultDTO.getDatas();

                videoAdapter=new SimpleAdapter(ClassActivity.this,
                        videoList,
                        R.layout.lv_video_style,
                        new String[]{"vid","vimg","vname"},
                        new int[]{R.id.tv_playNum,R.id.iv_videoImg,R.id.tv_videoName});

                //适配器使用网络图片
                videoAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                    @Override
                    public boolean setViewValue(View view, Object data, String textRepresentation) {
                        if (view instanceof ImageView && data instanceof String) {
                            ImageView iv = (ImageView) view;
                            try {
                                Glide.with(ClassActivity.this).load(data).into(iv);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return true;
                        } else
                            return false;
                    }
                });
                gv_video.setAdapter(videoAdapter);
            }
            @Override
            public void onFailed(Call call, IOException e) {
                System.out.println(e);
            }
        });

    }

    public void type1(View view){
        findByTid(1);
    }
    public void type2(View view){
        findByTid(2);
    }
    public void type3(View view){
        findByTid(3);
    }
    public void type4(View view){
        findByTid(4);
    }
    public void type5(View view){
        findByTid(5);
    }
    public void type6(View view){
        findByTid(6);
    }


    public void findByTid(int id){
        String url = "http://192.168.8.62:8080/videoplayer/video/findByType";
        VType vType=new VType();
        vType.setTid(id);
        video.setVtype(vType);
        //将User对象转换为GSON
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();//设置日期格式
        jsonString = gson.toJson(video);
        //该方法需要发送json数据到后台
        OkHttp3Utils.getInstance().doPostJson(url, jsonString, new GsonObjectCallback<ResultDTO>() {
            @Override
            public void onUi(ResultDTO resultDTO) {
                //考虑将数据放入到组件中
                System.out.println("状态码:" + resultDTO.getCode());
                System.out.println("信息:" + resultDTO.getMsg());
                System.out.println("单个对象数据:" + resultDTO.getData());
                System.out.println("多个对象数据:" + resultDTO.getDatas());
                videoList = resultDTO.getDatas();

                videoAdapter=new SimpleAdapter(ClassActivity.this,
                        videoList,
                        R.layout.lv_video_style,
                        new String[]{"vid","vimg","vname"},
                        new int[]{R.id.tv_playNum,R.id.iv_videoImg,R.id.tv_videoName});

                //适配器使用网络图片
                videoAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                    @Override
                    public boolean setViewValue(View view, Object data, String textRepresentation) {
                        if (view instanceof ImageView && data instanceof String) {
                            ImageView iv = (ImageView) view;
                            try {
                                Glide.with(ClassActivity.this).load(data).into(iv);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return true;
                        } else
                            return false;
                    }
                });
                gv_video.setAdapter(videoAdapter);
            }
            @Override
            public void onFailed(Call call, IOException e) {
                System.out.println(e);
            }
        });
    }

}
