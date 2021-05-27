package com.neu.video;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.neu.video.pojo.MyApp;
import com.neu.video.pojo.ResultDTO;
import com.neu.video.pojo.VAdmin;
import com.neu.video.pojo.VUser;
import com.neu.video.utils.GsonObjectCallback;
import com.neu.video.utils.JsonUtil;
import com.neu.video.utils.NetWorkUtils;
import com.neu.video.utils.OkHttp3Utils;

import java.io.IOException;

import okhttp3.Call;

public class LoginActivity extends AppCompatActivity {

    private VUser vUser=new VUser();
    private VAdmin vAdmin=new VAdmin();
    private String jsonString;
    private String responseData;

    private EditText et_name,et_pwd;
    private TextView tv_msg_name,tv_msg_pwd;
    private RadioGroup rg_who;
    private RadioButton rb_user,rb_admin;
    private Button b_login;
    private int checked_id=2131165290;  //默认为 用户id

    private boolean name_flag=false,pwd_flag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        initComponent();
        init();

        boolean netWorkAvailable = NetWorkUtils.isNetWorkAvailable(this);
        if(!netWorkAvailable){

            Toast.makeText(this, "连网:"+netWorkAvailable, Toast.LENGTH_SHORT).show();
        }
    }

    private void initComponent(){
        et_name=findViewById(R.id.et_name);
        et_pwd=findViewById(R.id.et_pwd);
        tv_msg_name=findViewById(R.id.tv_msg_name);
        tv_msg_pwd=findViewById(R.id.tv_msg_pwd);
        rg_who=findViewById(R.id.rg_who);
        rb_user=findViewById(R.id.rb_user);
        rb_admin=findViewById(R.id.rb_admin);
        b_login=findViewById(R.id.b_login);
    }

    private void init(){
        //设置密码隐藏
        et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        et_name.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp=s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_msg_name.setText("");
                System.out.println("登陆时，用户输入的用户名："+temp.toString());
                if (temp.toString().isEmpty()){
                    tv_msg_name.setText("用户名不能为空");
                    tv_msg_name.setTextColor(getResources().getColor(R.color.tomato));
                    name_flag=false;
                }else{
                    tv_msg_name.setText("");
                    name_flag=true;
                }
            }
        });
        et_pwd.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp=s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_msg_pwd.setText("");
                System.out.println("登陆时，用户输入的密码："+temp.toString());
                if (temp.toString().isEmpty()){
                    tv_msg_pwd.setText("密码不能为空");
                    tv_msg_pwd.setTextColor(getResources().getColor(R.color.tomato));
                    pwd_flag=false;
                }else{
                    tv_msg_pwd.setText("");
                    pwd_flag=true;
                }
            }
        });
        rg_who.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==rb_user.getId()){
                    System.out.println("用户"+checkedId);
                }else{
                    System.out.println("管理员"+checkedId);
                }
                checked_id=checkedId;
            }
        });
    }

    public void login(View view){
        if(name_flag && pwd_flag) {   //用户名密码不为空
            if (checked_id == rb_user.getId()) {   //用户身份登陆
                //服务器url地址
                String url = "http://192.168.8.62:8080/videoplayer/vuser/login";
                //Android端用户输入的信息
                String uname = et_name.getText().toString();
                String upwd = et_pwd.getText().toString();
                //vuser声明成员变量
                vUser.setUname(uname);
                vUser.setUpwd(upwd);
                System.out.println(vUser.toString());
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create(); //设置日期格式
                jsonString = gson.toJson(vUser);
                //该方法需要发送json数据到后台   resultDTO是服务器返回给Android的json数据
                OkHttp3Utils.getInstance().doPostJson(url, jsonString, new GsonObjectCallback<ResultDTO>() {
                    @Override
                    public void onUi(ResultDTO resultDTO) {
                        //考虑将数据放入到组件中
                        System.out.println("状态码:" + resultDTO.getCode());
                        System.out.println("信息:" + resultDTO.getMsg());
                        System.out.println("单个对象数据:" + resultDTO.getData());
                        System.out.println("多个对象数据:" + resultDTO.getDatas());
                        Toast.makeText(LoginActivity.this, resultDTO.getMsg().toString(), Toast.LENGTH_SHORT).show();
                        if (1001 == resultDTO.getCode()) {    //1001为登陆成功状态码
                            //将GSON处理的单个对象转换为字符串
                            String info=com.alibaba.fastjson.JSONObject.toJSONString(resultDTO.getData());
                            //将字符串转换为vuser对象
                            VUser user= JsonUtil.getObject(info,VUser.class);
                            System.out.println("当前登陆用户信息："+user.toString());
                            //将当前登陆用户信息保存
                            MyApp myApp= (MyApp) getApplication();
                            myApp.setvUser(user);
                            System.out.println(myApp.getvUser().toString());
                            Intent intent = new Intent(LoginActivity.this, IndexActivity.class);
                            startActivity(intent);
                            LoginActivity.this.finish();
                        }
                    }

                    @Override
                    public void onFailed(Call call, IOException e) {
                        System.out.println(e);
                    }
                });


            } else {  //管理员身份登陆
                //服务器url地址
                String url = "http://192.168.8.62:8080/videoplayer/vadmin/login";
                //Android端用户输入的信息
                String aname = et_name.getText().toString();
                String apwd = et_pwd.getText().toString();
                //vadmin声明成员变量
                vAdmin.setAname(aname);
                vAdmin.setApwd(apwd);
                System.out.println(vAdmin.toString());
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create(); //设置日期格式
                jsonString = gson.toJson(vAdmin);
                //该方法需要发送json数据到后台   resultDTO是服务器返回给Android的json数据
                OkHttp3Utils.getInstance().doPostJson(url, jsonString, new GsonObjectCallback<ResultDTO>() {
                    @Override
                    public void onUi(ResultDTO resultDTO) {
                        //考虑将数据放入到组件中
                        System.out.println("状态码:" + resultDTO.getCode());
                        System.out.println("信息:" + resultDTO.getMsg());
                        System.out.println("单个对象数据:" + resultDTO.getData());
                        System.out.println("多个对象数据:" + resultDTO.getDatas());
                        Toast.makeText(LoginActivity.this, resultDTO.getMsg().toString(), Toast.LENGTH_SHORT).show();
                        if (1001 == resultDTO.getCode()) {    //1001为登陆成功状态码
                            //将GSON处理的单个对象转换为字符串
                            String info=com.alibaba.fastjson.JSONObject.toJSONString(resultDTO.getData());
                            //将字符串转换为vuser对象
                            VAdmin admin= JsonUtil.getObject(info,VAdmin.class);
                            System.out.println("当前登陆管理员信息："+admin.toString());
                            //将当前登陆用户信息保存
                            MyApp myApp= (MyApp) getApplication();
                            myApp.setvAdmin(admin);
                            System.out.println(myApp.getvAdmin().toString());
                            Intent intent = new Intent(LoginActivity.this, UserListActivity.class);
                            startActivity(intent);
                            //LoginActivity.this.finish();
                        }
                    }

                    @Override
                    public void onFailed(Call call, IOException e) {
                        System.out.println(e);
                    }
                });

            }
        }else{
            if(!name_flag && pwd_flag){  //name_flag=false name没有填
                Toast.makeText(LoginActivity.this, "请填写用户名！", Toast.LENGTH_SHORT).show();
            }else if(name_flag&&!pwd_flag){  //pwd_flag=false pwd没有填
                Toast.makeText(LoginActivity.this, "请填写密码！", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(LoginActivity.this, "请填写用户名和密码！", Toast.LENGTH_SHORT).show();
            }

        }

    }

    public void toRegist(View view){
        Intent intent=new Intent(LoginActivity.this,RegistActivity.class);
        startActivity(intent);
    }

   /* private void getData(){
        String url = "http://192.168.8.62:8080/8080/videoplayer/vuser/test";
        System.out.println("1234");
        OkHttp3Utils.getInstance().doGet(url, new GsonObjectCallback<ResultDTO>() {
            @Override
            public void onUi(ResultDTO resultDTO) {
                //考虑将数据放入到组件中
                System.out.println("状态码:"+resultDTO.getCode());
                System.out.println("信息:"+resultDTO.getMsg());
                System.out.println("单个对象数据:"+resultDTO.getData());
                System.out.println("多个对象数据:"+resultDTO.getDatas());
                tv_msg_name.setText(resultDTO.toString());
            }

            @Override
            public void onFailed(Call call, IOException e) {
                System.out.println(e);
            }
        });

    }*/

}
