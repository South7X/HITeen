package com.hitsz.eatut;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hitsz.eatut.database.UserInfo;

import org.litepal.LitePal;

import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    // private TextView tv_main_title;//标题
    // private TextView tv_back;//返回按钮
    private Button btn_register;//注册按钮
    //用户名，密码，再次输入的密码的控件
    private EditText et_user_name,et_phoneNumber,et_studentNumber,et_psw,et_psw_again,et_addressDormitory;
    //用户名，密码，再次输入的密码的控件的获取值
    private String userName,phoneNumber,studentNumber,psw,spPsw,addressDormitory;
    //标题布局
    // private RelativeLayout rl_title_bar;

    //获取的用户名，手机号码，学号，密码，加密密码，宿舍楼号


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }

    private void init() {
        //从main_title_bar.xml 页面布局中获取对应的UI控件
//        tv_main_title=findViewById(R.id.tv_main_title);
//        tv_main_title.setText("注册");
//        tv_back=findViewById(R.id.tv_back);
        //布局根元素
//        rl_title_bar=findViewById(R.id.title_bar);
        // rl_title_bar.setBackgroundColor(Color.TRANSPARENT);
        //从activity_register.xml 页面中获取对应的UI控件
        btn_register=findViewById(R.id.btn_register);
        et_user_name=findViewById(R.id.et_user_name);
        et_psw=findViewById(R.id.et_psw);
        et_psw_again=findViewById(R.id.et_psw_again);
        et_phoneNumber=findViewById(R.id.et_phoneNumber);
        et_studentNumber=findViewById(R.id.et_studentNumber);
        et_addressDormitory=findViewById(R.id.et_addressDormitory);
//        tv_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //返回键
//                SignUpActivity.this.finish();
//            }
//        });
        //注册按钮
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取输入在相应控件中的字符串
                getEditString();
                View focusView = null;
                boolean cancel = false;
                //判断输入框内容
                if(TextUtils.isEmpty(userName)){
                    et_user_name.setError("昵称不能为空");
                    focusView = et_user_name;
                    cancel = true;
                } else if (!isUserNameValid(userName)){
                    et_user_name.setError("昵称字符长度不小于2位");
                    focusView = et_user_name;
                    cancel = true;
                }
                if (TextUtils.isEmpty(phoneNumber)){
                    et_phoneNumber.setError("请输入手机号码");
                    focusView = et_phoneNumber;
                    cancel = true;
                }else if (!isPhoneValid(phoneNumber)) {
                    et_phoneNumber.setError("手机号码格式错误");
                    focusView = et_phoneNumber;
                    cancel = true;
                }
                if (TextUtils.isEmpty(studentNumber)){
                    et_studentNumber.setError("请输入学号");
                    focusView = et_studentNumber;
                    cancel = true;
                } else if (!isStudentNumberValid(studentNumber)) {
                    et_studentNumber.setError("学号格式错误（请输入9位学号）");
                    focusView = et_studentNumber;
                    cancel = true;
                }
                if(TextUtils.isEmpty(psw)){
                    et_psw.setError("请输入密码");
                    focusView = et_psw;
                    cancel = true;
                } else if (!isPasswordValid(psw)){
                    et_psw.setError("密码长度不小于8位");
                    focusView = et_psw;
                    cancel = true;
                }
                if(TextUtils.isEmpty(spPsw)){
                    et_psw_again.setError("请再次输入密码");
                    focusView = et_psw_again;
                    cancel = true;
                } else if (!psw.equals(spPsw)) {
                    et_psw_again.setError("输入两次的密码不一样");
                    focusView = et_psw_again;
                    cancel = true;
                }

                if (TextUtils.isEmpty(addressDormitory)){
                    et_psw_again.setError("请输入宿舍楼号");
                    focusView = et_psw_again;
                    cancel = true;
                }
                if (cancel){
                    focusView.requestFocus();
                }
                else if(isUserExist(phoneNumber, studentNumber)){
                    Toast.makeText(SignUpActivity.this, "此账户已经存在", Toast.LENGTH_SHORT).show();
                    cancel = true;
                } else{
                    Toast.makeText(SignUpActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    // 向user数据库新增一个用户数据
                    saveRegisterInfo(userName,phoneNumber,studentNumber,psw,addressDormitory);
                    //注册成功后把账号传递到LoginActivity.java中
                    // 返回值到loginActivity显示
                    Intent data = new Intent();
                    data.putExtra("phoneNumber", phoneNumber);
                    setResult(RESULT_OK, data);
                    //RESULT_OK为Activity系统常量，状态码为-1，
                    // 表示此页面下的内容操作成功将data返回到上一页面，如果是用back返回过去的则不存在用setResult传递data值
                    SignUpActivity.this.finish();
                }
            }
        });
    }
    // TODO: move these function to BaseClass to let LoginActivity use
    private boolean isUserNameValid(String userName) {
        return userName.length() >= 2;
    }

    private boolean isStudentNumberValid(String studentNumber) {
        return studentNumber.length() == 9;
    }

    private boolean isPhoneValid(String phone) {
        return phone.length() == 11;
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8;
    }
    /**
     * 获取控件中的字符串
     */
    private void getEditString(){
        userName=et_user_name.getText().toString().trim();
        psw=et_psw.getText().toString().trim();
        spPsw=et_psw_again.getText().toString().trim();
        phoneNumber=et_phoneNumber.getText().toString().trim();
        studentNumber=et_studentNumber.getText().toString().trim();
        addressDormitory=et_addressDormitory.getText().toString().trim();
    }
    /**
     * 读取输入的用户名，判断SharedPreferences中是否有此用户名
     */
    private boolean isUserExist(String phoneNumber, String studentNumber){
        List<UserInfo> users_phone = LitePal.where("phoneNumber = ?", phoneNumber).find(UserInfo.class);
        List<UserInfo> users_student = LitePal.where("studentNumber = ?", studentNumber).find(UserInfo.class);
        return (!users_phone.isEmpty() | !users_student.isEmpty());
    }
    /**
     * 保存账号和密码到SharedPreferences中SharedPreferences
     */
    private void saveRegisterInfo(String userName, String phoneNumber,String studentNumber,
                                  String psw, String addressDormitory){
        // String md5Psw = MD5Utils.md5(psw);//把密码用MD5加密
        // TODO: MD5 encode
        //loginInfo表示文件名, mode_private SharedPreferences sp = getSharedPreferences( );
        UserInfo newUser = new UserInfo();
        newUser.setUserName(userName);
        newUser.setPassword(psw);
        newUser.setStudentNumber(studentNumber);
        newUser.setTelephoneNumber(phoneNumber);
        newUser.setAddressDormitory(addressDormitory);
        newUser.save();
    }

}