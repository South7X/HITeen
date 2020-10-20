package com.hitsz.eatut.database;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class UserInfo extends LitePalSupport {
    private int id;             //数据ID

    @Column(defaultValue = "unknown")
    private String userName;    //用户名

    @Column(defaultValue = "unknown")
    private String password;    //密码

    @Column(defaultValue = "unknown")
    private String studentNumber;   //学号

    @Column(defaultValue = "unknown")
    private String addressDormitory;    //宿舍楼号

    @Column(defaultValue = "unknown")
    private String telephoneNumber;//手机号码

    /**
     * getting method
     *
     */
    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public String getAddressDormitory() {
        return addressDormitory;
    }
    /**
     * setting method
     *
     */
    public void setId(int id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTelephoneNumber(String  telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public void setAddressDormitory(String addressDormitory) {
        this.addressDormitory = addressDormitory;
    }
}
