package com.neu.video;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.neu.video.pojo.MyApp;
import com.neu.video.pojo.ResultDTO;
import com.neu.video.pojo.VUser;
import com.neu.video.utils.GsonObjectCallback;
import com.neu.video.utils.OkHttp3Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class MyStarActivity extends AppCompatActivity {
    private ListView lv_video;
    private TextView nostar;
    private SimpleAdapter videoAdapter;
    private List<Map<String,Object>> videoList=new ArrayList<Map<String,Object>>();
    private String[] s=new String[]{"vimg","vname","vid","vtime","vdirector","vactor"};
    private int[] i=new int[]{R.id.iv_vImg,R.id.iv_vName,R.id.iv_vId,R.id.iv_vTime,R.id.iv_vDirector,R.id.iv_vActor};
    private int pid;
    private VUser vUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_star);

        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        initComponent();
        init();


    }

    private void initComponent(){
        lv_video=findViewById(R.id.lv_video);
        nostar=findViewById(R.id.nostar);
    }

    public void init(){
        //得到用户的id
        MyApp myApp= (MyApp) getApplication();
        vUser=myApp.getvUser();
        if (vUser==null){

        }else{
            pid=myApp.getvUser().getId();
            System.out.println("向后台拿我的所有收藏");
//        pid=1;
            getMyStar(pid);

        }

    }
    public void getMyStar(int pid){
        //得到所有的收藏
        String url = "http:192.168.8.62:8080/videoplayer/video/getMyStar?pid="+pid;
        OkHttp3Utils.getInstance().doGet(url, new GsonObjectCallback<ResultDTO>() {
            @Override
            public void onUi(ResultDTO resultDTO) {
                videoList.clear();
                videoList.addAll(resultDTO.getDatas());      //得到返回的集合
                System.out.println(videoList.size());
                if (videoList.size()==0){
                    nostar.setVisibility(View.VISIBLE);
                }else{
                    nostar.setVisibility(View.GONE);
                    videoAdapter = new SimpleAdapter(MyStarActivity.this,
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
                            if (view instanceof ImageView && data instanceof String) {
                                ImageView iv = (ImageView) view;
                                try {
                                    Glide.with(MyStarActivity.this).load(data).into(iv);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return true;
                            } else
                                return false;
                        }
                    });

                    lv_video.setAdapter(videoAdapter);
                    //ListView点击事件
                    lv_video.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //根据需要将Map中数据抽取存入User对象中然后使用Intent将User对象传递到UserDesc页面
                            Intent intent = new Intent(MyStarActivity.this,PlayActivity.class);
                            intent.putExtra("vid",Double.valueOf(videoList.get(position).get("vid").toString()).intValue());
                            startActivity(intent);
                        }
                    });

                }
            }

            @Override
            public void onFailed(Call call, IOException e) {

            }
        });
    }

}
