package com.hitsz.eatut.ui.UserCenter_ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hitsz.eatut.BaseClass;
import com.hitsz.eatut.LoginActivity;
import com.hitsz.eatut.R;
import com.hitsz.eatut.database.UserInfo;

import org.litepal.LitePal;

import java.util.List;

public class ChangeUserInfoActivity extends AppCompatActivity {

    private Button btn_change;//注册按钮
    private Button btn_logout;
    //用户名，学号，宿舍楼地址的控件
    private EditText ch_user_name,ch_studentNumber,ch_addressDormitory;
    //用户名，学号，宿舍楼地址的控件的获取值
    private String userName,studentNumber,addressDormitory;
    private String phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_info);
        Intent intent = getIntent();
        phoneNumber = intent.getStringExtra("phoneNumber");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        init();
    }
    private void init() {

        btn_change = findViewById(R.id.btn_change);
        btn_logout = findViewById(R.id.btn_logout);
        ch_user_name=findViewById(R.id.ch_user_name);
        ch_studentNumber=findViewById(R.id.ch_studentNumber);
        ch_addressDormitory=findViewById(R.id.ch_addressDormitory);

        List<UserInfo> userInfo = LitePal.where("telephoneNumber = ?", phoneNumber).find(UserInfo.class);
        for (UserInfo user: userInfo){
            ch_user_name.setText(user.getUserName());
            ch_studentNumber.setText(user.getStudentNumber());
            ch_addressDormitory.setText(user.getAddressDormitory());
        }
        btn_logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ChangeUserInfoActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });
        //注册按钮
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取输入在相应控件中的字符串
                getEditString();
                View focusView = null;
                boolean cancel = false;
                //判断输入框内容
                if(TextUtils.isEmpty(userName)){
                    ch_user_name.setError("昵称不能为空");
                    focusView = ch_user_name;
                    cancel = true;
                } else if (!BaseClass.isUserNameValid(userName)){
                    ch_user_name.setError("昵称字符长度不小于2位");
                    focusView = ch_user_name;
                    cancel = true;
                }

                if (TextUtils.isEmpty(studentNumber)){
                    ch_studentNumber.setError("请输入学号");
                    focusView = ch_studentNumber;
                    cancel = true;
                } else if (!BaseClass.isStudentNumberValid(studentNumber)) {
                    ch_studentNumber.setError("学号格式错误（请输入9位学号）");
                    focusView = ch_studentNumber;
                    cancel = true;
                }

                if (TextUtils.isEmpty(addressDormitory)){
                    ch_addressDormitory.setError("请输入宿舍楼号");
                    focusView = ch_addressDormitory;
                    cancel = true;
                }
                if (cancel){
                    focusView.requestFocus();
                }
                Toast.makeText(ChangeUserInfoActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                // 向user数据库新增一个用户数据
                saveRegisterInfo(userName,phoneNumber,studentNumber,addressDormitory);
                // 成功后把账号传递到Activity.java中
                // 返回值到Activity显示
                Intent data = new Intent();
                data.putExtra("userName", userName);
                setResult(RESULT_OK, data);
                //RESULT_OK为Activity系统常量，状态码为-1，
                // 表示此页面下的内容操作成功将data返回到上一页面，如果是用back返回过去的则不存在用setResult传递data值
                ChangeUserInfoActivity.this.finish();
            }
        });
    }

    /**
     * 获取控件中的字符串
     */
    private void getEditString(){
        userName=ch_user_name.getText().toString().trim();
        studentNumber=ch_studentNumber.getText().toString().trim();
        addressDormitory=ch_addressDormitory.getText().toString().trim();
    }

    /**
     * 保存账号和密码到SharedPreferences中SharedPreferences
     */
    private void saveRegisterInfo(String userName, String phoneNumber,String studentNumber,
                                  String addressDormitory){
        UserInfo newUser = new UserInfo();
        newUser.setUserName(userName);
        newUser.setStudentNumber(studentNumber);
        newUser.setAddressDormitory(addressDormitory);
        newUser.updateAll("telephoneNumber = ?", phoneNumber);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}