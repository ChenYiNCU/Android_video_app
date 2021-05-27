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

public class ChangePersonalActivity extends AppCompatActivity {
    private VUser vUser=new VUser();
    MyApp myApp;
    private String jsonString;
    private String responseData;

    private EditText et_name,et_age,et_tel;
    private TextView tv_msg_name,tv_msg_age,tv_msg_tel;
    private RadioButton rb_male,rb_female;
    private RadioGroup rg_sex;
    private boolean name_flag=true,age_flag=true,tel_flag=true,img_flag=false;

    //上传图片
    private ImageView userImage,imageView;
    private String fileName="";
    private String imgurl=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_personal);

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

        tv_msg_name=findViewById(R.id.tv_msg_name);
        tv_msg_age=findViewById(R.id.tv_msg_age);
        tv_msg_tel=findViewById(R.id.tv_msg_tel);

        rg_sex=findViewById(R.id.rg_sex);
        rb_female=findViewById(R.id.rb_female);
        rb_male=findViewById(R.id.rb_male);

        userImage=findViewById(R.id.user_image);
        imageView=findViewById(R.id.imageview);

    }
    public void init(){
        myApp= (MyApp) getApplication();
        VUser user=myApp.getvUser();
        et_name.setText(user.getUname());
        et_age.setText(user.getUage()+"");
        et_tel.setText(user.getUtel());

        if(rb_male.getText()== user.getUsex()){
            rg_sex.check(rb_male.getId());
        }else{
            rg_sex.check(rb_female.getId());
        }
        Glide.with(this).load(user.getUimg()).into(userImage);
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
                    myApp= (MyApp) getApplication();
                    if(uname.equals(myApp.getvUser().getUname())){  //没有修改用户名
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
                fileName = FileUtils.getRealFilePath(ChangePersonalActivity.this,uri);
                System.out.println(fileName);

            }
        }
    }
    public void update(View view){
        if(true==name_flag && true==age_flag && true== tel_flag){
            //为vUser属性赋值
            myApp= (MyApp) getApplication();
            vUser.setId(myApp.getvUser().getId());
            vUser.setUname(et_name.getText().toString());
            vUser.setUage(Integer.parseInt(et_age.getText().toString()));
            vUser.setUtel(et_tel.getText().toString());
            if(rg_sex.getCheckedRadioButtonId()==rb_male.getId()){
                vUser.setUsex("男");
            }else{
                vUser.setUsex("女");
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
                        vUser.setUimg(resultDTO.getMsg());
                        //调用修改用户接口
                        //服务器url地址
                        String url = "http://192.168.8.62:8080/videoplayer/vuser/update";
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
                                    Toast.makeText(ChangePersonalActivity.this, resultDTO.getMsg().toString(), Toast.LENGTH_SHORT).show();
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
                                                Toast.makeText(ChangePersonalActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                                                //将GSON处理的单个对象转换为字符串
                                                String info=com.alibaba.fastjson.JSONObject.toJSONString(resultDTO.getData());
                                                //将字符串转换为vuser对象
                                                VUser user= JsonUtil.getObject(info,VUser.class);
                                                System.out.println("修改后用户信息："+user.toString());
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
                                    Intent intent = new Intent(ChangePersonalActivity.this, PersonalActivity.class);
                                    startActivity(intent);
                                    ChangePersonalActivity.this.finish();
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
                String url = "http://192.168.8.62:8080/videoplayer/vuser/update2";
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
                           // Toast.makeText(ChangePersonalActivity.this, resultDTO.getMsg().toString(), Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(ChangePersonalActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                                        //将GSON处理的单个对象转换为字符串
                                        String info=com.alibaba.fastjson.JSONObject.toJSONString(resultDTO.getData());
                                        //将字符串转换为vuser对象
                                        VUser user= JsonUtil.getObject(info,VUser.class);
                                        System.out.println("修改后用户信息："+user.toString());
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
                            Intent intent = new Intent(ChangePersonalActivity.this, PersonalActivity.class);
                            startActivity(intent);
                            ChangePersonalActivity.this.finish();
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

    public void toPersonal(View view){
        Intent intent=new Intent(ChangePersonalActivity.this,PersonalActivity.class);
        startActivity(intent);
        ChangePersonalActivity.this.finish();
    }
}
