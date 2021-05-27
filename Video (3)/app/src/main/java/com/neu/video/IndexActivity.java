package com.neu.video;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.neu.video.pojo.MyApp;
import com.neu.video.pojo.ResultDTO;
import com.neu.video.pojo.VUser;
import com.neu.video.utils.GsonObjectCallback;
import com.neu.video.utils.OkHttp3Utils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class IndexActivity extends AppCompatActivity implements OnBannerListener {
    private VUser vUser;
    private Banner mBanner;
    private EditText et_search;
//    private ListView lv_video;
    private List<Map<String,Object>> videoList;
    private SimpleAdapter videoAdapter;
    private GridView gv_video;

    private ImageButton b_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        initView();
        initComponent();
        init();
    }

    private void initComponent(){
//        lv_video=findViewById(R.id.lv_video);
        gv_video=findViewById(R.id.gv_video);
        et_search=findViewById(R.id.et_search);
        b_login=findViewById(R.id.b_login);
    }

    private void init(){
        //显示或隐藏登陆按钮
        MyApp myApp= (MyApp) getApplication();
        vUser=myApp.getvUser();
        if(vUser == null){  //当前没有用户登陆
            b_login.setVisibility(View.VISIBLE);
        }else{
            b_login.setVisibility(View.GONE);
        }

        String url = "http:192.168.8.62:8080/videoplayer/video/findAll";
        OkHttp3Utils.getInstance().doGet(url, new GsonObjectCallback<ResultDTO>() {
            @Override
            public void onUi(ResultDTO resultDTO) {
                //考虑将数据放入到组件中
                System.out.println("状态码:"+resultDTO.getCode());
                System.out.println("信息:"+resultDTO.getMsg());
                System.out.println("单个对象数据:"+resultDTO.getData());
                System.out.println("多个对象数据:"+resultDTO.getDatas());
                videoList =resultDTO.getDatas();
                    videoAdapter=new SimpleAdapter(IndexActivity.this,
                            videoList,
                            R.layout.lv_video_style,
                            new String[]{"vid","vimg","vname"},
                            new int[]{R.id.tv_playNum,R.id.iv_videoImg,R.id.tv_videoName});

                    //适配器使用网络图片
                    videoAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                        @Override
                        public boolean setViewValue(View view, Object data, String textRepresentation) {
                            if (view instanceof ImageView && data instanceof String){
                                ImageView iv=(ImageView) view;
                                try{
                                    Glide.with(IndexActivity.this).load(data).into(iv);
                                }catch (Exception e){e.printStackTrace();}
                                return true;
                            }else
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
                Intent intent = new Intent(IndexActivity.this,PlayActivity.class);
                intent.putExtra("vid",Double.valueOf(videoList.get(position).get("vid").toString()).intValue());
                startActivity(intent);
            }
        });

    }

    public void toIndex(View view){
        Intent intent=new Intent(IndexActivity.this,IndexActivity.class);
        startActivity(intent);
    }

    public void toClass(View view){
        Intent intent=new Intent(IndexActivity.this,ClassActivity.class);
        startActivity(intent);
    }

    public void toPersonal(View view){
        Intent intent=new Intent(IndexActivity.this,PersonalActivity.class);
        startActivity(intent);
    }

    public void toLogin(View view){
        Intent intent=new Intent(IndexActivity.this,LoginActivity.class);
        startActivity(intent);
    }

    public void toPlay(View view){
        Intent intent=new Intent(IndexActivity.this,PlayActivity.class);
        startActivity(intent);
    }

    public void search(View view){
        String search=et_search.getText().toString();
        Intent intent = new Intent(IndexActivity.this,ClassActivity.class);
        intent.putExtra("search",search);
        startActivity(intent);
    }





    public void initView() {
        mBanner = findViewById(R.id.mBanner);
        //图片资源
        int[] imageResourceID = new int[]{R.drawable.first, R.drawable.second, R.drawable.third, R.drawable.forth};
        List<Integer> imgeList = new ArrayList<>();
        //轮播标题
        String[] mtitle = new String[]{"米花之味", "夏日日剧全攻略", "了不起的匠人", "国产电影实力派投票"};
        List<String> titleList = new ArrayList<>();

        for (int i = 0; i < imageResourceID.length; i++) {
            imgeList.add(imageResourceID[i]);//把图片资源循环放入list里面
            titleList.add(mtitle[i]);//把标题循环设置进列表里面
            //设置图片加载器，通过Glide加载图片
            mBanner.setImageLoader(new ImageLoader() {
                @Override
                public void displayImage(Context context, Object path, ImageView imageView) {
                    Glide.with(IndexActivity.this).load(path).into(imageView);
                }
            });
            //设置轮播的动画效果,里面有很多种特效,可以到GitHub上查看文档。
            mBanner.setBannerAnimation(Transformer.Default);
            mBanner.setImages(imgeList);//设置图片资源
            mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);//设置banner显示样式（带标题的样式）
            mBanner.setBannerTitles(titleList); //设置标题集合（当banner样式有显示title时）
            //设置指示器位置（即图片下面的那个小圆点）
            mBanner.setIndicatorGravity(BannerConfig.CENTER);
            mBanner.setDelayTime(3000);//设置轮播时间3秒切换下一图
            mBanner.setOnBannerListener(this);//设置监听
            mBanner.start();//开始进行banner渲染
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBanner.startAutoPlay();//开始轮播
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBanner.stopAutoPlay();//结束轮播
    }

    //对轮播图设置点击监听事件
    @Override
    public void OnBannerClick(int position) {
        Toast.makeText(this, "你点击了第" + (position + 1) + "张轮播图", Toast.LENGTH_SHORT).show();
    }
}
