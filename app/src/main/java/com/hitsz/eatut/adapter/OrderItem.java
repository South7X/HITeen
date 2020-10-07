package com.hitsz.eatut.adapter;
/**
 * @author Lily
 */
public class OrderItem {
    private int id;
    private int orderno;        //订单号（队列编号）
    private String ordername;   //菜名

    public OrderItem(int id , int orderno, String ordername){
        this.id=id;
        this.orderno=orderno;
        this.ordername=ordername;
    }

    public void setId(int id){
        this.id=id;
    }
    public void setOrdername(String ordername){
        this.ordername=ordername;
    }
    public void setOrderno(int orderno){
        this.orderno=orderno;
    }

    public int getId(){
        return id;
    }
    public int getOrderno(){
        return orderno;
    }

    public String getOrdername(){
        return ordername;
    }
}
