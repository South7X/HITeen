package com.hitsz.eatut;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppComponentFactory;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hitsz.eatut.adapter.order;
import com.hitsz.eatut.adapter.orderFoodAdapter;
import com.hitsz.eatut.database.orderFood;
import com.hitsz.eatut.ui.Order_ui.OrderFragment;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

import static com.hitsz.eatut.managerActivities.OrderActivity.saveOrderToManager;

/**
 * @author zhang
 */
public class CheckActivity extends AppCompatActivity implements View.OnClickListener{
    private RecyclerView recyclerView;
    private List<order> orderFoodsList=new ArrayList<>();
    private Button confirmbtn;
    private AlertDialog.Builder builder;
    private TextView orderNO;
    private TextView phonenum;
    private String currentPhone;
    private int i;
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_check);
        findView();
        initOrder();
        initRecycle();
    }
    public void onClick(View v){
        switch(v.getId()){
            case R.id.confirm_btn:
                initOrder();
                if(orderFoodsList.isEmpty())
                {
                    DialogEmpty();
                }
                else {
                    i+=1;
                    Toast.makeText(v.getContext(),String.valueOf(i),Toast.LENGTH_SHORT).show();
                    SharedPreferences pref=this.getSharedPreferences("OrderCount",MODE_PRIVATE);
                    SharedPreferences.Editor editor=pref.edit();
                    editor.putInt("count",i);
                    editor.apply();
                    for(order one:orderFoodsList){
                        Log.d("order", one.getName());
                    }
                    OrderFragment.saveOrderToMyOrder(i,currentPhone);
                    saveOrderToManager(i,currentPhone);
                    LitePal.deleteAll(orderFood.class);
                    DialogSuccess();
                }
                break;

        }
    }
    protected void findView(){
        recyclerView=(RecyclerView)findViewById(R.id.check_recycle);
        confirmbtn=(Button)findViewById(R.id.confirm_btn);
        builder=new AlertDialog.Builder(CheckActivity.this);
        confirmbtn.setOnClickListener(this);
        orderNO=findViewById(R.id.orderno_textview);
        phonenum=findViewById(R.id.phone_textview);

        SharedPreferences pref2=this.getSharedPreferences("CurrentPhone",MODE_PRIVATE);
        currentPhone=pref2.getString("Phone","Get_Phone_Fail");
        SharedPreferences pref=this.getSharedPreferences("OrderCount",MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        i=pref.getInt("count",-1);

        orderNO.setText(String.valueOf(i+1));
        phonenum.setText(currentPhone);

    }
    private void DialogEmpty(){
        builder.setTitle("不想吃饭？");
        builder.setMessage("您还没有选择菜品惹。");
        builder.setPositiveButton("没饭吃=A=", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AlertDialog dialog=builder.show();
                dialog.dismiss();
                finish();
            }
        });
        builder.show();
    }
    private void DialogSuccess(){
        builder.setTitle("NICE!!!");
        builder.setMessage("订单提交成功！");
        builder.setPositiveButton("有饭吃！www", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AlertDialog dialog=builder.show();
                dialog.dismiss();
                finish();
            }
        });
        builder.show();
    }
    private void initOrder(){
        List<orderFood> orderFoods = LitePal.findAll(orderFood.class);
        orderFoodsList.clear();
        for (orderFood orderfood:orderFoods){
            order newOrder = new order(orderfood.getName(), orderfood.getDishPrice(),
                    orderfood.getDishScore(), orderfood.getId());
            orderFoodsList.add(newOrder);
        }
    }
    private void initRecycle(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        orderFoodAdapter adapter=new orderFoodAdapter(orderFoodsList, new orderFoodAdapter.ofaListener() {
            @Override
            public void ofa(int position) {
                orderFoodsList.remove(position);
                initRecycle();
            }
        });
        recyclerView.setAdapter(adapter);
    }

}

