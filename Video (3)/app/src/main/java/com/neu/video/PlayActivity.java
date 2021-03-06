package com.neu.video;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.neu.video.pojo.Discuss;
import com.neu.video.pojo.MyApp;
import com.neu.video.pojo.ResultDTO;
import com.neu.video.pojo.VUser;
import com.neu.video.pojo.Video;
import com.neu.video.utils.GsonObjectCallback;
import com.neu.video.utils.OkHttp3Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PlayActivity extends AppCompatActivity {
    private VUser vUser;
    private LinearLayout text1,text2;
    private TextView btn1,btn2;
    private ImageView star_img,photo;
    private Video video;
    private boolean flag;
    private TextView video_name,type,director,actor,time,story,vscore;
    private String url;
    private Handler handler;
    private int pid,vid;
    private ImageView s01,s02,s03,s04,s05;
    private boolean f1,f2,f3,f4,f5;
    private String v_score;
    private int myscore;


    private EditText mywords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();
        //???UI???????????????Handler??????
        handler=new Handler();
        init();

//???????????????id?????????????????????
        Intent intent=getIntent();
        vid=intent.getIntExtra("vid",1);
        findVideoById(vid);

//???????????????id
        MyApp myApp= (MyApp) getApplication();
        vUser=myApp.getvUser();
        if(vUser!=null){
            pid=myApp.getvUser().getId();
            getMyscore(pid,vid);
            isStar(pid,vid);
        }
//?????????????????????
        getVscore(vid);

//??????????????????


        f1=false;
        f2=false;
        f3=false;
        f5=false;
        f4=false;


//??????flag??????


    }
    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    Runnable runnableVsore=new Runnable() {
        @Override
        public void run() {
            //????????????
//            v_score="10.00";
            vscore.setText(v_score);
        }
    };
    Runnable runnablePoint=new Runnable() {
        @Override
        public void run() {
            //????????????
            draw();
        }
    };
    Runnable runnableStar=new Runnable() {
        @Override
        public void run() {
            //????????????
            if(flag){     //???????????????????????????
                star_img.setImageResource(R.drawable.star3);
            }else{
                star_img.setImageResource(R.drawable.star2);
            }
        }
    };
    Runnable runnableUi=new Runnable() {
        @Override
        public void run() {
            //????????????
            url=video.getVurl();
            Glide.with(PlayActivity.this)
                    .load(video.getVimg())
                    .into(photo);
            type.setText(video.getVtype().getTname());
            video_name.setText(video.getVname());
//                video_name.setText();
            director.setText(video.getVdirector());
            actor.setText(video.getVactor());
            time.setText(video.getVtime());
            story.setText(video.getVdesc());
            JCVideoPlayerStandard jc = (JCVideoPlayerStandard) findViewById(R.id.videoplayer);
            jc.setUp(url, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, video.getVname());

        }
    };


    public void init(){
//        ????????????
            text1=findViewById(R.id.text1);
            text2=findViewById(R.id.text2);
            btn1=findViewById(R.id.btn1);
            btn2=findViewById(R.id.btn2);
            star_img=findViewById(R.id.star_img);
            photo=findViewById(R.id.photo);
            video_name=findViewById(R.id.video_name);
            type=findViewById(R.id.type);
            director=findViewById(R.id.director);
            actor=findViewById(R.id.actor);
            time=findViewById(R.id.time);
            story=findViewById(R.id.story);
            vscore=findViewById(R.id.vscore);

            mywords=findViewById(R.id.mywords);

            s01=findViewById(R.id.s01);
            s02=findViewById(R.id.s02);
            s03=findViewById(R.id.s03);
            s04=findViewById(R.id.s04);
            s05=findViewById(R.id.s05);

//????????????
            text2.setVisibility(View.GONE);
            text1.setVisibility(View.VISIBLE);
            btn1.setTextColor(Color.parseColor("#ff4500"));
            btn2.setTextColor(Color.parseColor("#000000"));

        }
    public void getVscore(int vid){
        //1.??????OkHttpClient??????
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.??????Request?????????????????????url??????,?????????????????????
        Request request = new Request.Builder().url("http://192.168.8.62:8080/videoplayer/video/getVscore?vid="+vid).method("get",null).build();
        //3.????????????call??????,????????????Request????????????
        Call call = okHttpClient.newCall(request);
        //4.???????????????????????????????????????
        call.enqueue(new Callback() {
            //???????????????????????????
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("????????????????????????");
            }
            //???????????????????????????
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str=response.body().string();      //???????????????json?????????
                System.out.println("?????????????????????"+str);
//                System.out.println(str.equals("false"));
                v_score=str;
                handler.post(runnableVsore);
                System.out.println("????????????????????????");
            }
        });
    }
    public void isStar(int pid,int vid){
        //1.??????OkHttpClient??????
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.??????Request?????????????????????url??????,?????????????????????
        Request request = new Request.Builder().url("http://192.168.8.62:8080/videoplayer/video/isStar?pid="+pid+"&vid="+vid).method("get",null).build();
        //3.????????????call??????,????????????Request????????????
        Call call = okHttpClient.newCall(request);
        //4.???????????????????????????????????????
        call.enqueue(new Callback() {
            //???????????????????????????
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("??????????????????");
            }
            //???????????????????????????
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str=response.body().string();      //???????????????json?????????
                System.out.println(str);
//                System.out.println(str.equals("false"));
                if (str.equals("true")){
                    flag=true;
                }else{
                    flag=false;
                }
                handler.post(runnableStar);

                System.out.println("??????????????????");
            }
        });

    }
    public void star(int pid,int vid){
        if(vUser == null){  //????????????????????????
            Intent intent=new Intent(PlayActivity.this,LoginActivity.class);
            startActivity(intent);
        }else{
            //1.??????OkHttpClient??????
            OkHttpClient okHttpClient = new OkHttpClient();
            //2.??????Request?????????????????????url??????,?????????????????????
            Request request = new Request.Builder().url("http://192.168.8.62:8080/videoplayer/video/star?pid="+pid+"&vid="+vid).method("get",null).build();
            //3.????????????call??????,????????????Request????????????
            Call call = okHttpClient.newCall(request);
            //4.???????????????????????????????????????
            call.enqueue(new Callback() {
                //???????????????????????????
                @Override
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();

                    Toast.makeText(PlayActivity.this,"????????????",Toast.LENGTH_SHORT).show();System.out.println("get????????????");
                    Looper.loop();
                }
                //???????????????????????????
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Looper.prepare();

                    Toast.makeText(PlayActivity.this,"????????????",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    System.out.println("get????????????");
                }
            });
        }

    }
    public void noStar(int pid,int vid){
        if(vUser == null){  //????????????????????????
            Intent intent=new Intent(PlayActivity.this,LoginActivity.class);
            startActivity(intent);
        }else{
            //1.??????OkHttpClient??????
            OkHttpClient okHttpClient = new OkHttpClient();
            //2.??????Request?????????????????????url??????,?????????????????????
            Request request = new Request.Builder().url("http://192.168.8.62:8080/videoplayer/video/noStar?pid="+pid+"&vid="+vid).method("get",null).build();
            //3.????????????call??????,????????????Request????????????
            Call call = okHttpClient.newCall(request);
            //4.???????????????????????????????????????
            call.enqueue(new Callback() {
                //???????????????????????????
                @Override
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();

                    Toast.makeText(PlayActivity.this,"??????????????????",Toast.LENGTH_SHORT).show();System.out.println("get????????????");
                    Looper.loop();
                }
                //???????????????????????????
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Looper.prepare();
                    Toast.makeText(PlayActivity.this,"??????????????????",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    System.out.println("get????????????");
                }
            });
        }


    }
    public void getMyscore(int pid,int vid){
        //1.??????OkHttpClient??????
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.??????Request?????????????????????url??????,?????????????????????
        Request request = new Request.Builder().url("http://192.168.8.62:8080/videoplayer/video/getMyscore?pid="+pid+"&vid="+vid).method("get",null).build();
        //3.????????????call??????,????????????Request????????????
        Call call = okHttpClient.newCall(request);
        //4.???????????????????????????????????????
        call.enqueue(new Callback() {
            //???????????????????????????
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(PlayActivity.this,"????????????",Toast.LENGTH_SHORT).show();System.out.println("get????????????");
                Looper.loop();
            }
            //???????????????????????????
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str=response.body().string();
                System.out.println(response);
                System.out.println(str);
                myscore=Integer.parseInt(str);
                System.out.println(myscore);
                handler.post(runnablePoint);

                //myscore=8;
                System.out.println("get????????????");
            }
        });

    }
    public void setMyscore(int pid,int vid,int myscore){
        if(vUser == null){  //????????????????????????
            Intent intent=new Intent(PlayActivity.this,LoginActivity.class);
            startActivity(intent);
        }else{
            draw();
            //1.??????OkHttpClient??????
            OkHttpClient okHttpClient = new OkHttpClient();
            //2.??????Request?????????????????????url??????,?????????????????????
            Request request = new Request.Builder().url("http://192.168.8.62:8080/videoplayer/video/setMyscore?pid="+pid+"&vid="+vid+"&score="+ myscore).method("get",null).build();
            //3.????????????call??????,????????????Request????????????
            Call call = okHttpClient.newCall(request);
            //4.???????????????????????????????????????
            call.enqueue(new Callback() {
                //???????????????????????????
                @Override
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    Toast.makeText(PlayActivity.this,"????????????",Toast.LENGTH_SHORT).show();
                    System.out.println("get????????????");
                    Looper.loop();
                }
                //???????????????????????????
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Looper.prepare();
                    Toast.makeText(PlayActivity.this,"????????????",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    System.out.println("get????????????");
                }
            });
        }

    }

    public void findVideoById( int vid){
        //1.??????OkHttpClient??????
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.??????Request?????????????????????url??????,?????????????????????
        Request request = new Request.Builder().url("http://192.168.8.62:8080/videoplayer/video/findVideoById?vid="+vid).method("get",null).build();
        //3.????????????call??????,????????????Request????????????
        Call call = okHttpClient.newCall(request);
        //4.???????????????????????????????????????
        call.enqueue(new Callback() {
            //???????????????????????????
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("get????????????");
            }
            //???????????????????????????
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str=response.body().string();      //???????????????json?????????
                Gson gson=new Gson();
                video= gson.fromJson(str,Video.class);         //??????????????????
                System.out.println(video);

                //?????????????????????????????????
                handler.post(runnableUi);
                //                System.out.println(str);
//                System.out.println(response.body());
//                System.out.println(response.getClass().toString());
                System.out.println("get????????????");
            }
        });
    }

    public void toIndex(View view){
        Intent intent=new Intent(PlayActivity.this,IndexActivity.class);
        startActivity(intent);
    }
    public void  findAllEva(View view){
        Intent intent=new Intent(PlayActivity.this,DiscussListActivity.class);
        intent.putExtra("vid",vid);
        startActivity(intent);
    }
    public void show1(View view){
        text2.setVisibility(View.GONE);
        text1.setVisibility(View.VISIBLE);
//        text1.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
//        btn1.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        btn1.setTextColor(Color.parseColor("#ff4500"));
        btn2.setTextColor(Color.parseColor("#000000"));
        btn2.getPaint().setFlags(0);

//        text1.setTextColor(Color.parseColor("#ff4500"));
    }
    public void show2(View view){
        text1.setVisibility(View.GONE);
        text2.setVisibility(View.VISIBLE);

//        text2.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        btn2.setTextColor(Color.parseColor("#ff4500"));
        btn1.setTextColor(Color.parseColor("#000000"));
//        btn2.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        btn1.getPaint().setFlags(0);
//        text2.setTextColor(Color.parseColor("#ff4500"));

    }
    public void star(View view){
        if(vUser == null){  //????????????????????????
            Intent intent=new Intent(PlayActivity.this,LoginActivity.class);
            startActivity(intent);
        }else{
            flag = !flag;
            if(flag){     //???????????????????????????
                star(pid,vid);
                star_img.setImageResource(R.drawable.star3);
            }else{
                noStar(pid,vid);
                star_img.setImageResource(R.drawable.star2);
            }
        }

    }

    public void draw(){
        switch (myscore){
            case 10:
                f1=false;
                f2=false;
                f3=false;
                f4=false;
                f5= !f5;
                if (f5){
                    s01.setImageResource(R.drawable.star3);
                    s02.setImageResource(R.drawable.star3);
                    s03.setImageResource(R.drawable.star3);
                    s04.setImageResource(R.drawable.star3);
                    s05.setImageResource(R.drawable.star3);
                }else{
                    s01.setImageResource(R.drawable.star2);
                    s02.setImageResource(R.drawable.star2);
                    s03.setImageResource(R.drawable.star2);
                    s04.setImageResource(R.drawable.star2);
                    s05.setImageResource(R.drawable.star2);
                }
                break;
            case 8:
                f1=false;
                f2=false;
                f3=false;
                f5=false;
                f4= !f4;
                if (f4){
                    clean();
                    s01.setImageResource(R.drawable.star3);
                    s02.setImageResource(R.drawable.star3);
                    s03.setImageResource(R.drawable.star3);
                    s04.setImageResource(R.drawable.star3);

                }else{
                    s01.setImageResource(R.drawable.star2);
                    s02.setImageResource(R.drawable.star2);
                    s03.setImageResource(R.drawable.star2);
                    s04.setImageResource(R.drawable.star2);

                }
                break;
            case 6:
                f1=false;
                f2=false;
                f5=false;
                f4=false;
                f3= !f3;
                if (f3){
                    clean();
                    s01.setImageResource(R.drawable.star3);
                    s02.setImageResource(R.drawable.star3);
                    s03.setImageResource(R.drawable.star3);
                }else{
                    s01.setImageResource(R.drawable.star2);
                    s02.setImageResource(R.drawable.star2);
                    s03.setImageResource(R.drawable.star2);
                }
                break;
            case 4:
                f1=false;
                f5=false;
                f3=false;
                f4=false;
                //???????????????
                f2= !f2;
                if (f2){
                    clean();
                    s01.setImageResource(R.drawable.star3);
                    s02.setImageResource(R.drawable.star3);

                }else{
                    s01.setImageResource(R.drawable.star2);
                    s02.setImageResource(R.drawable.star2);


                }
                break;
            case 2:
                f5=false;
                f2=false;
                f3=false;
                f4=false;
                //???????????????
                f1= !f1;
                if (f1){
                    clean();
                    s01.setImageResource(R.drawable.star3);

                }else{
                    s01.setImageResource(R.drawable.star2);

                }
                break;
            case 0:
                break;
        }
    }

    public void score01(View view){
        myscore=4;
        setMyscore(pid,vid,myscore);
//        draw();
    }
    public void score02(View view){
        myscore=4;

        setMyscore(pid,vid,myscore);
//        draw();
    }
    public void score03(View view){
        myscore=6;
        setMyscore(pid,vid,myscore);
//        draw();
    }
    public void score04(View view){
        myscore=8;

        setMyscore(pid,vid,myscore);
//        draw();
    }
    public void score05(View view){
        myscore=10;
        setMyscore(pid,vid,myscore);
//        draw();
    }
    public void clean(){
        s01.setImageResource(R.drawable.star2);
        s02.setImageResource(R.drawable.star2);
        s03.setImageResource(R.drawable.star2);
        s04.setImageResource(R.drawable.star2);
        s05.setImageResource(R.drawable.star2);
    }

    public void setmywords(View view){
        if(vUser == null){  //????????????????????????
            Intent intent=new Intent(PlayActivity.this,LoginActivity.class);
            startActivity(intent);
        }else{
            //1.????????????
            String words=mywords.getText().toString();
            words=words.trim();
            if (words.isEmpty()){
    //            Toast.makeText(PlayActivity.this,"",Toast.LENGTH_SHORT).show();
            }else{
                //2.??????
                mywords.setText("");
                //3.???????????????
                Discuss discuss=new Discuss();
                discuss.setPid(pid);
                discuss.setWords(words);
                discuss.setVid(vid);
                String url = "http://192.168.8.62:8080/videoplayer/video/eva";
                //??????????????????GSON
                Gson gson=new Gson();
                String jsonString;
                jsonString = gson.toJson(discuss);
                OkHttp3Utils.getInstance().doPostJson(url, jsonString, new GsonObjectCallback<ResultDTO>(){
                    @Override
                    public void onUi(ResultDTO resultDTO) {

                        Toast.makeText(PlayActivity.this,resultDTO.getMsg(),Toast.LENGTH_SHORT).show();
    //                finish();
    //                startActivity(getIntent());

                    }
                    @Override
                    public void onFailed(Call call, IOException e) {

                    }
                });
            }
        }
    }

}
