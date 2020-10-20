package com.hitsz.eatut;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hitsz.eatut.database.UserInfo;

public class ForgetPassword extends AppCompatActivity {

    private Button btn_change_psw;
    //用户名，密码，再次输入的密码的控件
    private EditText et_phoneNumber,et_psw,et_psw_again;
    //用户名，密码，再次输入的密码的控件的获取值
    private String phoneNumber,psw,spPsw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        init();
    }
    private void init() {
        btn_change_psw=findViewById(R.id.btn_change_psw);
        et_psw=findViewById(R.id.psw);
        et_psw_again=findViewById(R.id.psw_again);
        et_phoneNumber=findViewById(R.id.phoneNumber);
        //注册按钮
        btn_change_psw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取输入在相应控件中的字符串
                getEditString();
                View focusView = null;
                boolean cancel = false;
                //判断输入框内容
                if (TextUtils.isEmpty(phoneNumber)){
                    et_phoneNumber.setError("请输入手机号码");
                    focusView = et_phoneNumber;
                    cancel = true;
                }else if (!BaseClass.isPhoneValid(phoneNumber)) {
                    et_phoneNumber.setError("手机号码格式错误");
                    focusView = et_phoneNumber;
                    cancel = true;
                }
                if(TextUtils.isEmpty(psw)){
                    et_psw.setError("请输入新密码");
                    focusView = et_psw;
                    cancel = true;
                } else if (!BaseClass.isPasswordValid(psw)){
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
                if (cancel){
                    focusView.requestFocus();
                } else if(!BaseClass.isUserExist(phoneNumber)){
                    Toast.makeText(ForgetPassword.this, "此账户不存在，请前往注册", Toast.LENGTH_SHORT).show();
                    cancel = true;
                } else{
                    Toast.makeText(ForgetPassword.this, "修改密码成功", Toast.LENGTH_SHORT).show();
                    // 向user数据库更新密码信息
                    updateInfo(phoneNumber,psw);
                    //RESULT_OK为Activity系统常量，状态码为-1，
                    // 表示此页面下的内容操作成功将data返回到上一页面，如果是用back返回过去的则不存在用setResult传递data值
                    ForgetPassword.this.finish();
                }
            }
        });
    }

    /**
     * 获取控件中的字符串
     */
    private void getEditString(){
        psw=et_psw.getText().toString().trim();
        spPsw=et_psw_again.getText().toString().trim();
        phoneNumber=et_phoneNumber.getText().toString().trim();
    }

    /**
     * 保存账号和密码到SharedPreferences中SharedPreferences
     */
    private void updateInfo(String phoneNumber, String psw){
        // String md5Psw = MD5Utils.md5(psw);//把密码用MD5加密
        // TODO: MD5 encode
        UserInfo newUser = new UserInfo();
        newUser.setPassword(psw);
        newUser.updateAll("telephoneNumber = ?", phoneNumber);
    }
}