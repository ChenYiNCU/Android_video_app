package com.neu.video;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bumptech.glide.Glide;
import com.neu.video.pojo.ResultDTO;
import com.neu.video.utils.GsonObjectCallback;
import com.neu.video.utils.OkHttp3Utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class DiscussListActivity extends AppCompatActivity {
    private ListView lv_discuss;
    private SimpleAdapter userAdapter;
    private List<Map<String,Object>> userList;
    int vid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discuss_list);

        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        Intent intent=getIntent();
        vid=intent.getIntExtra("vid",1);
        init();
//        userList=getDate();
        getwords(vid);    //要改

    }



    public void init(){
        lv_discuss=findViewById(R.id.lv_discuss);

    }


    public void getwords(int vid) {
        String url = "http://192.168.8.62:8080/videoplayer/video/getAllWords?vid="+vid;
        OkHttp3Utils.getInstance().doGet(url, new GsonObjectCallback<ResultDTO>() {
            @Override
            public void onUi(ResultDTO resultDTO) {
                //考虑将数据放入到组件中
                System.out.println("状态码:" + resultDTO.getCode());
                System.out.println("信息:" + resultDTO.getMsg());
                System.out.println("单个对象数据:" + resultDTO.getData());
                System.out.println("多个对象数据:" + resultDTO.getDatas());
                userList = resultDTO.getDatas();
                System.out.println(userList);

                userAdapter=new SimpleAdapter(DiscussListActivity.this,userList,R.layout.words_style,new String[]{"uimg","uname","words"},
                        new int[]{R.id.iv_userImage,R.id.tv_name,R.id.tv_words});

                //适配器使用网络图片
                userAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                    @Override
                    public boolean setViewValue(View view, Object data, String textRepresentation) {
                        if (view instanceof ImageView && data instanceof  String){
                            ImageView iv =(ImageView) view;
                            Glide.with(DiscussListActivity.this)
                                    .load(data)
                                    .into(iv);
                            return true;
                        }
                        else{
                            return false;
                        }

                    }
                });
                lv_discuss.setAdapter(userAdapter);

            }
            @Override
            public void onFailed(Call call, IOException e) {
                System.out.println("失败"+e);
            }

        });
    }




















    public void toMuser(View view){
        Intent intent=new Intent(DiscussListActivity.this,UserListActivity.class);
        startActivity(intent);
    }

    public void toMvideo(View view){
        Intent intent=new Intent(DiscussListActivity.this,VideoListActivity.class);
        startActivity(intent);
    }

    public void toMtype(View view){
        Intent intent=new Intent(DiscussListActivity.this,DiscussListActivity.class);
        startActivity(intent);
    }
}
