package com.neu.video;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.neu.video.pojo.MyApp;
import com.neu.video.pojo.ResultDTO;
import com.neu.video.pojo.VUser;
import com.neu.video.utils.GsonObjectCallback;
import com.neu.video.utils.JsonUtil;
import com.neu.video.utils.OkHttp3Utils;

import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;

public class ChangePwdActivity extends AppCompatActivity {
    private VUser vUser=new VUser();
    MyApp myApp;
    private String jsonString;
    private String responseData;

    private EditText et_new_pwd,et_new_repwd;
    private TextView tv_msg_new_pwd,tv_msg_new_repwd;

    private boolean pwd_flag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);

        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        initComponent();
        init();
    }

    private void initComponent(){
        et_new_pwd=findViewById(R.id.et_new_pwd);
        et_new_repwd=findViewById(R.id.et_new_repwd);
        tv_msg_new_pwd=findViewById(R.id.tv_msg_new_pwd);
        tv_msg_new_repwd=findViewById(R.id.tv_msg_new_repwd);
    }
    private void init(){
        //设置密码隐藏
        et_new_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        et_new_repwd.setTransformationMethod(PasswordTransformationMethod.getInstance());

        //密码验证 6-20所有字符
        et_new_pwd.addTextChangedListener(new TextWatcher() {
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
                tv_msg_new_pwd.setText("");
                String pwdMode="^.{6,20}$";
                System.out.println("注册时密码："+temp.toString());
                if(temp.toString().matches(pwdMode)){
                    tv_msg_new_pwd.setText("密码可用");
                    tv_msg_new_pwd.setTextColor(getResources().getColor(R.color.limegreen));
                }else{
                    tv_msg_new_pwd.setText("密码不可用");
                    tv_msg_new_pwd.setTextColor(getResources().getColor(R.color.tomato));
                }
            }
        });

        //两次密码比较
        et_new_repwd.addTextChangedListener(new TextWatcher() {
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
                tv_msg_new_repwd.setText("");
                System.out.println("注册时重复密码："+temp.toString());
                if(temp.toString().equals(et_new_pwd.getText().toString())){
                    tv_msg_new_repwd.setText("密码一致");
                    tv_msg_new_repwd.setTextColor(getResources().getColor(R.color.limegreen));
                    pwd_flag=true;
                    vUser.setUpwd(temp.toString());
                }else{
                    tv_msg_new_repwd.setText("密码不一致");
                    tv_msg_new_repwd.setTextColor(getResources().getColor(R.color.tomato));
                    pwd_flag=false;
                }
            }
        });
    }

    public void updatePwd(View view){
        if(true==pwd_flag){
            myApp= (MyApp) getApplication();
            vUser.setId(myApp.getvUser().getId());
            System.out.println("修改密码用户id="+myApp.getvUser().getId());
            System.out.println("新密码："+vUser.getUpwd());
            String url = "http://192.168.8.62:8080/videoplayer/vuser/updatePwd";
            System.out.println("修改用户信息："+vUser.toString());
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
                    //Toast.makeText(RegistActivity.this, resultDTO.getMsg().toString(), Toast.LENGTH_SHORT).show();
                    if (1001 == resultDTO.getCode()) {    //1001为修改成功状态码
                       // Toast.makeText(ChangePwdActivity.this, resultDTO.getMsg().toString(), Toast.LENGTH_SHORT).show();
                        //重新查找用户 登陆用户信息重置
                        String url = "http://192.168.8.62:8080/videoplayer/vuser/findUserById";
                        System.out.println("查找用户id："+vUser.getId()+"");
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create(); //设置日期格式
                        jsonString = gson.toJson(vUser);
                        OkHttp3Utils.getInstance().doPostJson(url, jsonString, new GsonObjectCallback<ResultDTO>() {
                            @Override
                            public void onUi(ResultDTO resultDTO) {
                                //考虑将数据放入到组件中
                                System.out.println("状态码:" + resultDTO.getCode());
                                System.out.println("信息:" + resultDTO.getMsg());
                                System.out.println("单个对象数据:" + resultDTO.getData());
                                System.out.println("多个对象数据:" + resultDTO.getDatas());
                                //Toast.makeText(RegistActivity.this, resultDTO.getMsg().toString(), Toast.LENGTH_SHORT).show();
                                if (1001 == resultDTO.getCode()) {    //1001为查找成功状态码
                                    Toast.makeText(ChangePwdActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                                    //将GSON处理的单个对象转换为字符串
                                    String info=com.alibaba.fastjson.JSONObject.toJSONString(resultDTO.getData());
                                    //将字符串转换为vuser对象
                                    VUser user= JsonUtil.getObject(info,VUser.class);
                                    System.out.println("修改密码后用户信息："+user.toString());
                                    //将当前登陆用户信息保存
                                    myApp= (MyApp) getApplication();
                                    myApp.setvUser(user);
                                    System.out.println(myApp.getvUser().toString());
                                }
                            }

                            @Override
                            public void onFailed(Call call, IOException e) {
                                System.out.println(e);
                            }
                        });
                        Intent intent = new Intent(ChangePwdActivity.this, PersonalActivity.class);
                        startActivity(intent);
                        ChangePwdActivity.this.finish();
                    }
                }

                @Override
                public void onFailed(Call call, IOException e) {
                    System.out.println(e);
                }
            });
        }
    }

    public void toPersonal(View view){
        Intent intent=new Intent(ChangePwdActivity.this,PersonalActivity.class);
        startActivity(intent);
        ChangePwdActivity.this.finish();
    }
}
