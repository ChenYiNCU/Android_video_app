package com.neu.video;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.neu.video.pojo.MyApp;
import com.neu.video.pojo.ResultDTO;
import com.neu.video.pojo.VUser;
import com.neu.video.utils.FileUtils;
import com.neu.video.utils.GsonObjectCallback;
import com.neu.video.utils.JsonUtil;
import com.neu.video.utils.OkHttp3Utils;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;

public class UserDescActivity extends AppCompatActivity {
    //当前查找到的user
    private VUser vUser=new VUser();
    //修改时user
    private VUser newUser=new VUser();
    private int id=0;
    MyApp myApp;
    private String jsonString;
    private String responseData;

    private EditText et_name,et_age,et_tel,et_pwd;
    private TextView tv_msg_name,tv_msg_age,tv_msg_tel,tv_msg_pwd;
    private RadioButton rb_male,rb_female;
    private RadioGroup rg_sex;
    private boolean name_flag=true,age_flag=true,tel_flag=true,img_flag=false,pwd_flag=true;

    //上传图片
    private ImageView userImage,imageView;
    private String fileName="";
    private String imgurl=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_desc);

        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        initComponent();
        init();
        check();
    }
    //绑定组件
    private void initComponent(){
        et_name=findViewById(R.id.et_name);
        et_age=findViewById(R.id.et_age);
        et_tel=findViewById(R.id.et_tel);
        et_pwd=findViewById(R.id.et_pwd);

        tv_msg_name=findViewById(R.id.tv_msg_name);
        tv_msg_age=findViewById(R.id.tv_msg_age);
        tv_msg_tel=findViewById(R.id.tv_msg_tel);
        tv_msg_pwd=findViewById(R.id.tv_msg_pwd);

        rg_sex=findViewById(R.id.rg_sex);
        rb_female=findViewById(R.id.rb_female);
        rb_male=findViewById(R.id.rb_male);

        userImage=findViewById(R.id.user_image);
        imageView=findViewById(R.id.imageview);

    }
    public void init(){
        Intent intent=getIntent();
        id=(int)intent.getSerializableExtra("id");
        String url = "http://192.168.8.62:8080/videoplayer/vuser/findUserById";
        VUser user1=new VUser();
        user1.setId(id);
        System.out.println("9999999999999"+id);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create(); //设置日期格式
        jsonString = gson.toJson(user1);
        //该方法需要发送json数据到后台   resultDTO是服务器返回给Android的json数据
        OkHttp3Utils.getInstance().doPostJson(url, jsonString, new GsonObjectCallback<ResultDTO>() {
            @Override
            public void onUi(ResultDTO resultDTO) {
                //考虑将数据放入到组件中
                System.out.println("状态码:" + resultDTO.getCode());
                System.out.println("信息:" + resultDTO.getMsg());
                System.out.println("单个对象数据:" + resultDTO.getData());
                System.out.println("多个对象数据:" + resultDTO.getDatas());
                if (1001 == resultDTO.getCode()) {    //1001说明用户存在 成功
                    //将GSON处理的单个对象转换为字符串
                    String info=com.alibaba.fastjson.JSONObject.toJSONString(resultDTO.getData());
                    //将字符串转换为vuser对象
                    vUser= JsonUtil.getObject(info,VUser.class);
                    System.out.println("当前修改用户信息："+vUser.toString());
                    et_name.setText(vUser.getUname());
                    et_pwd.setText(vUser.getUpwd());
                    et_age.setText(vUser.getUage()+"");
                    et_tel.setText(vUser.getUtel());
                    if(rb_male.getText()== vUser.getUsex()){
                        rg_sex.check(rb_male.getId());
                    }else{
                        rg_sex.check(rb_female.getId());
                    }
                    if(vUser.getUimg()!=null){
                        Glide.with(UserDescActivity.this).load(vUser.getUimg()).into(userImage);
                    }
                }
            }

            @Override
            public void onFailed(Call call, IOException e) {
                System.out.println(e);
            }
        });

    }

    public void check(){
        et_name.addTextChangedListener(new TextWatcher() {
            //用户名验证  1-10位 A-Z a-z 0-9组合
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
                String nameMode="^[\\w]{1,10}$";
                System.out.println("修改时用户名："+temp.toString());
                if (temp.toString().matches(nameMode)){
                    String uname=temp.toString();
                    if(uname.equals(vUser.getUname())){  //没有修改用户名
                        tv_msg_name.setText("");
                        name_flag=true;
                    }else{ //修改了用户名
                        vUser.setUname(uname);
                        String url = "http://192.168.8.62:8080/videoplayer/vuser/findUserByName";
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create(); //设置日期格式
                        jsonString = gson.toJson(vUser);
                        //该方法需要发送json数据到后台   resultDTO是服务器返回给Android的json数据
                        OkHttp3Utils.getInstance().doPostJson(url, jsonString, new GsonObjectCallback<ResultDTO>() {
                            @Override
                            public void onUi(ResultDTO resultDTO) {
                                //考虑将数据放入到组件中
                            /*System.out.println("状态码:" + resultDTO.getCode());
                            System.out.println("信息:" + resultDTO.getMsg());
                            System.out.println("单个对象数据:" + resultDTO.getData());
                            System.out.println("多个对象数据:" + resultDTO.getDatas());*/
                                if (1001 == resultDTO.getCode()) {    //1001为成功状态
                                    //判断是否重复
                                    tv_msg_name.setText("当前用户名可用");
                                    tv_msg_name.setTextColor(getResources().getColor(R.color.limegreen));
                                    name_flag=true;
                                    vUser.setUname(temp.toString());
                                }else{
                                    tv_msg_name.setText("当前用户名已存在");
                                    tv_msg_name.setTextColor(getResources().getColor(R.color.tomato));
                                    name_flag=false;
                                    vUser.setUname(null);
                                }
                            }

                            @Override
                            public void onFailed(Call call, IOException e) {
                                System.out.println(e);
                            }
                        });
                    }
                }else{
                    tv_msg_name.setText("当前用户名不可用");
                    tv_msg_name.setTextColor(getResources().getColor(R.color.tomato));
                    name_flag=false;
                }
            }
        });

        //密码验证 6-20所有字符
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
                String pwdMode="^.{6,20}$";
                System.out.println("注册时密码："+temp.toString());
                if(temp.toString().matches(pwdMode)){
                    tv_msg_pwd.setText("密码可用");
                    tv_msg_pwd.setTextColor(getResources().getColor(R.color.limegreen));
                    pwd_flag=true;
                }else{
                    tv_msg_pwd.setText("密码不可用");
                    tv_msg_pwd.setTextColor(getResources().getColor(R.color.tomato));
                    pwd_flag=false;
                }
            }
        });

        //年龄验证 非0正整数
        et_age.addTextChangedListener(new TextWatcher() {
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
                tv_msg_age.setText("");
                String ageMode="^[1-9]\\d*$";
                System.out.println("注册时年龄："+temp.toString());
                if(temp.toString().matches(ageMode)){
                    tv_msg_age.setText("");
                    age_flag=true;
                }else{
                    tv_msg_age.setText("年龄格式不正确");
                    tv_msg_age.setTextColor(getResources().getColor(R.color.tomato));
                    age_flag=false;

                }
            }
        });

        //手机号验证
        et_tel.addTextChangedListener(new TextWatcher() {
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
                tv_msg_tel.setText("");
                String pwdMode="^(13[0-9]|14[5|7]|15[0|1|2|3|4|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";
                System.out.println("注册时手机号："+temp.toString());
                if(temp.toString().matches(pwdMode)){
                    tv_msg_tel.setText("");
                    tel_flag=true;
                }else{
                    tv_msg_tel.setText("手机号格式不正确");
                    tv_msg_tel.setTextColor(getResources().getColor(R.color.tomato));
                    tel_flag=false;
                }
            }
        });

        //性别选择
        vUser.setUsex("男");
        rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==rb_male.getId()){
                    System.out.println("注册选择性别：男");
                }else{
                    System.out.println("注册选择性别：女");
                }
            }
        });

    }

    //选择图片
    public void choosePic(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, 2);
        img_flag=true;
    }

    //Intent页面数据回显
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();

                imageView.setImageURI(uri);
                //将URI 处理为图片地址
                fileName = FileUtils.getRealFilePath(UserDescActivity.this,uri);
                System.out.println(fileName);

            }
        }
    }

    public void update1(View view){
        if(true==name_flag && true==age_flag && true== tel_flag && true== pwd_flag){
            //为vUser属性赋值
            newUser.setId(vUser.getId());
            newUser.setUname(et_name.getText().toString());
            newUser.setUpwd(et_pwd.getText().toString());
            newUser.setUage(Integer.parseInt(et_age.getText().toString()));
            newUser.setUtel(et_tel.getText().toString());
            if(rg_sex.getCheckedRadioButtonId()==rb_male.getId()){
                newUser.setUsex("男");
            }else{
                newUser.setUsex("女");
            }
            if(true == img_flag){  //修改了图片
                System.out.println("1111111111111111");
                //上传图片接口
                File currentFile = new File(fileName);
                String url = "http://192.168.8.62:8080/videoplayer/vuser/UploadOnePic";
                String currentFileName = currentFile.getName();
                System.out.println(currentFileName);
                //调用okhttp3上传图片方法
                OkHttp3Utils.uploadPic(url,currentFile,currentFileName,new GsonObjectCallback<ResultDTO>(){
                    @Override
                    public void onUi(ResultDTO resultDTO) {
                        System.out.println(resultDTO.getMsg());
                        //为img赋值
                        newUser.setUimg(resultDTO.getMsg());
                        //调用修改用户接口
                        //服务器url地址
                        String url = "http://192.168.8.62:8080/videoplayer/vuser/update3";
                        System.out.println("修改用户信息："+newUser.toString());
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create(); //设置日期格式
                        jsonString = gson.toJson(newUser);
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
                                    Toast.makeText(UserDescActivity.this, resultDTO.getMsg().toString(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(UserDescActivity.this, UserListActivity.class);
                                    startActivity(intent);
                                    UserDescActivity.this.finish();
                                }
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
            }else{  //没有修改图片
                System.out.println("22222222222222");
                String url = "http://192.168.8.62:8080/videoplayer/vuser/update4";
                System.out.println("修改用户信息："+newUser.toString());
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create(); //设置日期格式
                jsonString = gson.toJson(newUser);
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
                            Toast.makeText(UserDescActivity.this, resultDTO.getMsg().toString(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UserDescActivity.this, UserListActivity.class);
                            startActivity(intent);
                            UserDescActivity.this.finish();
                        }
                    }

                    @Override
                    public void onFailed(Call call, IOException e) {
                        System.out.println(e);
                    }
                });
            }
        }else{ //填写数据有错误
            System.out.println("333333333");
            Toast.makeText(this, "请认真填写各项信息！", Toast.LENGTH_SHORT).show();
        }
    }

    public void toUserList(View view){
        Intent intent=new Intent(UserDescActivity.this,UserListActivity.class);
        startActivity(intent);
        UserDescActivity.this.finish();
    }
}
