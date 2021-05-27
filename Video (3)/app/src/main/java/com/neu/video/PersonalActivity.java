package com.neu.video;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.neu.video.pojo.MyApp;
import com.neu.video.pojo.VUser;

public class PersonalActivity extends AppCompatActivity {

    private VUser vUser=new VUser();
    private TextView tv_name;
    private ImageView iv_userImage;
    private SimpleAdapter userAdapter;
    private Button bt_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        initComponent();
        init();
    }
    private void initComponent(){
        tv_name=findViewById(R.id.tv_name);
        iv_userImage=findViewById(R.id.iv_userImage);
        bt_out=findViewById(R.id.bt_out);
    }
    private void init(){
        MyApp myApp= (MyApp) getApplication();
        vUser=myApp.getvUser();
        if (vUser!=null){
            //载入图片
            String uimg= vUser.getUimg();
            System.out.println(uimg);
            Glide.with(PersonalActivity.this).load(uimg).into(iv_userImage);
            //获取用户名
            tv_name.setText(vUser.getUname());
            bt_out.setVisibility(View.VISIBLE);
        }else{
            bt_out.setVisibility(View.GONE);
        }
    }

    public void toIndex(View view){
        Intent intent=new Intent(PersonalActivity.this,IndexActivity.class);
        startActivity(intent);
    }

    public void toClass(View view){
        Intent intent=new Intent(PersonalActivity.this,ClassActivity.class);
        startActivity(intent);
    }

    public void toPersonal(View view){
        Intent intent=new Intent(PersonalActivity.this,PersonalActivity.class);
        startActivity(intent);
    }

    public void toChange(View view){
        MyApp myApp= (MyApp) getApplication();
        vUser=myApp.getvUser();
        if (vUser!=null){
            Intent intent =new Intent(PersonalActivity.this,ChangePersonalActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(PersonalActivity.this,"请先登录账号！",Toast.LENGTH_SHORT).show();
        }
    }

    public void toChangePwd(View view){
        MyApp myApp= (MyApp) getApplication();
        vUser=myApp.getvUser();
        if (vUser!=null){
        Intent intent=new Intent(PersonalActivity.this,ChangePwdActivity.class);
        startActivity(intent);
        }else{
            Toast.makeText(PersonalActivity.this,"请先登录账号！",Toast.LENGTH_SHORT).show();
        }
    }

    public void Out(View view){
        MyApp myApp= (MyApp) getApplication();
        vUser=myApp.getvUser();
        if (vUser!=null){
            bt_out.setVisibility(View.VISIBLE);
            System.out.println("退出当前登陆账号："+myApp.getvUser().toString());
            //清空myApp中存储的user信息
            myApp.setvUser(null);
            Intent intent=new Intent(PersonalActivity.this,PersonalActivity.class);
            startActivity(intent);
            this.finish();
        }
    }

    public void toMyStar(View view){
        Intent intent=new Intent(PersonalActivity.this,MyStarActivity.class);
        startActivity(intent);
    }
}
