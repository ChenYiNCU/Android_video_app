package com.neu.video;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.neu.video.pojo.ResultDTO;
import com.neu.video.pojo.VAdmin;
import com.neu.video.pojo.VUser;
import com.neu.video.utils.FileUtils;
import com.neu.video.utils.GsonObjectCallback;
import com.neu.video.utils.OkHttp3Utils;
import com.neu.video.utils.PermissionUtil;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;

public class RegistActivity extends AppCompatActivity {

    private VUser vUser=new VUser();
    private String jsonString;
    private String responseData;

    private EditText et_name,et_pwd,et_repwd,et_age,et_tel;
    private TextView tv_msg_name,tv_msg_pwd,tv_msg_repwd,tv_msg_age,tv_msg_tel;
    private RadioButton rb_male,rb_female;
    private RadioGroup rg_sex;
    private boolean name_flag=false,pwd_flag=false,age_flag=false,tel_flag=false,img_flag=false;

    //上传图片
    private ImageView imageView;
    private String fileName="";
    private String imgurl=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        PermissionUtil.verifyStoragePermissions(RegistActivity.this);

        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        initComponent();
        init();
    }
    //绑定组件
    private void initComponent(){
        et_name=findViewById(R.id.et_name);
        et_pwd=findViewById(R.id.et_pwd);
        et_repwd=findViewById(R.id.et_repwd);
        et_age=findViewById(R.id.et_age);
        et_tel=findViewById(R.id.et_tel);

        tv_msg_name=findViewById(R.id.tv_msg_name);
        tv_msg_pwd=findViewById(R.id.tv_msg_pwd);
        tv_msg_repwd=findViewById(R.id.tv_msg_repwd);
        tv_msg_age=findViewById(R.id.tv_msg_age);
        tv_msg_tel=findViewById(R.id.tv_msg_tel);

        rg_sex=findViewById(R.id.rg_sex);
        rb_female=findViewById(R.id.rb_female);
        rb_male=findViewById(R.id.rb_male);

        imageView=findViewById(R.id.imageview);

    }

    public void init(){
        //设置密码隐藏
        et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        et_repwd.setTransformationMethod(PasswordTransformationMethod.getInstance());

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
                System.out.println("注册时用户名："+temp.toString());
                if (temp.toString().matches(nameMode)){
                    String uname=temp.toString();
                    String url = "http://192.168.8.62:8080/videoplayer/vuser/findUserByName";
                    vUser.setUname(uname);
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
                }else{
                    tv_msg_pwd.setText("密码不可用");
                    tv_msg_pwd.setTextColor(getResources().getColor(R.color.tomato));
                }
            }
        });

        //两次密码比较
        et_repwd.addTextChangedListener(new TextWatcher() {
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
                tv_msg_repwd.setText("");
                System.out.println("注册时重复密码："+temp.toString());
                if(temp.toString().equals(et_pwd.getText().toString())){
                    tv_msg_repwd.setText("密码一致");
                    tv_msg_repwd.setTextColor(getResources().getColor(R.color.limegreen));
                    pwd_flag=true;
                    vUser.setUpwd(temp.toString());
                }else{
                    tv_msg_repwd.setText("密码不一致");
                    tv_msg_repwd.setTextColor(getResources().getColor(R.color.tomato));
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
                    vUser.setUage(Integer.parseInt(temp.toString()));
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
                    vUser.setUtel(temp.toString());
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
                    vUser.setUsex("男");;
                }else{
                    System.out.println("注册选择性别：女");
                    vUser.setUsex("女");
                }
            }
        });

        //头像上传
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
                fileName = FileUtils.getRealFilePath(RegistActivity.this,uri);
                System.out.println(fileName);

            }
        }
    }

    public void regist(View view){
        if(true==name_flag && true==pwd_flag && true==age_flag && true== tel_flag && true==img_flag){
            File currentFile = new File(fileName);
            String url = "http://192.168.8.62:8080/videoplayer/vuser/UploadOnePic";
            String currentFileName = currentFile.getName();
            System.out.println(currentFileName);
            //调用okhttp3上传图片方法
            OkHttp3Utils.uploadPic(url,currentFile,currentFileName,new GsonObjectCallback<ResultDTO>(){
                @Override
                public void onUi(ResultDTO resultDTO) {
                    System.out.println(resultDTO.getMsg());
                    vUser.setUimg(resultDTO.getMsg());

                    //服务器url地址
                    String url = "http://192.168.8.62:8080/videoplayer/vuser/regist";
                    System.out.println("注册用户信息："+vUser.toString());
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
                            if (1001 == resultDTO.getCode()) {    //1001为注册成功状态码
                                Toast.makeText(RegistActivity.this, resultDTO.getMsg().toString(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegistActivity.this, LoginActivity.class);
                                startActivity(intent);
                                // RegistActivity.this.finish();
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

        }else{
            Toast.makeText(this, "请认真填写各项信息！", Toast.LENGTH_SHORT).show();
        }

       /* Intent intent=new Intent(RegistActivity.this,IndexActivity.class);
        startActivity(intent);*/
    }

    public void toLogin(View view){
        Intent intent=new Intent(RegistActivity.this,LoginActivity.class);
        startActivity(intent);
    }
}
