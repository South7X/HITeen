package com.hitsz.eatut.managerActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.SharedPreferences;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;
import com.hitsz.eatut.R;
import com.hitsz.eatut.adapter.ManagerOrderAdapter;
import com.hitsz.eatut.adapter.OrderItem;
import com.hitsz.eatut.adapter.order;
import com.hitsz.eatut.database.ManagerOrderInfo;
import com.hitsz.eatut.database.orderFood;

import org.litepal.LitePal;
import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Lily
 */
public class OrderActivity extends AppCompatActivity {
    //判断菜品是否已完成，返回给用户信息
    public static boolean haveOrderPrepared(int id){
        boolean isprepared=true;
        List<ManagerOrderInfo> list= LitePal.findAll(ManagerOrderInfo.class);
        for(ManagerOrderInfo per:list){
            if(per.getId()==id){
                isprepared=false;
            }
        }
        return isprepared;
    }

    public static void saveOrderToManager(int OrderNo,String phone){
        List<orderFood> orderFoodsList=LitePal.findAll(orderFood.class);
        for(orderFood per:orderFoodsList){
            Log.d("manager", per.getName());
            ManagerOrderInfo info=new ManagerOrderInfo();
            info.setDishPrice(per.getDishPrice());
            info.setDishScore(per.getDishScore());
            info.setName(per.getName());
            info.setPhone(phone);
            info.setOrderNo(OrderNo);
            info.save();
        }
    }

    private RecyclerView recyclerView;
    private List<OrderItem> orderInfoList=new ArrayList<>();
    private ManagerOrderAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        findview();
        initOrder();
        initRecycle();
    }
    protected void findview(){
        recyclerView=(RecyclerView)findViewById(R.id.recycler_order);

    }
    //通过recyclerview显示litepal中内容
    private void initOrder(){
        List<ManagerOrderInfo> orderInfos = LitePal.findAll(ManagerOrderInfo.class);
        for(ManagerOrderInfo orderInfo : orderInfos){
            OrderItem orderItem=new OrderItem(orderInfo.getId(),orderInfo.getOrderNo(),orderInfo.getName());
            orderInfoList.add(orderItem);
        }
    }

    private void initRecycle(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new ManagerOrderAdapter(orderInfoList, new ManagerOrderAdapter.ofaListener() {
            @Override
            public void ofa(int position) {
                orderInfoList.remove(position);
//                OrderItem feedbackItem=orderInfoList.get(position);
//                for(int i=0;i<orderInfoList.size();i++){
//                    if(orderInfoList.get(i).getOrderno()==feedbackItem.getOrderno()){
//                        orderInfoList.remove(i);
//                    }
//                }
//                initOrder();
                initRecycle();
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
//TODO:连接队列

//    private Button button_neworder=null;
//    private Button button_deleorder=null;
//    private TextView tv_order=null;
//    private boolean IsReading=false;
//    Queue<Integer> queue = new Queue<>();
//    private int index=0;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_order);
//
//        tv_order = (TextView)findViewById(R.id.text_order);
//        button_neworder=(Button)findViewById(R.id.button_test_neworder);
//        button_deleorder=(Button)findViewById(R.id.button_test_deleorder);
//        button_neworder.setOnClickListener(listener);
//        button_deleorder.setOnClickListener(listener);
//
//        for(int i=0;i<=99;i++){
//            queue.offer(i);//初始队列1~100
//        }
//
//    }
//
//    public void RefreshData()
//    {
//        if(IsReading)
//        {
//            String s=(new Integer(queue.display(index))).toString();
//            tv_order.setText(s);
//        }
//    }
//
//    OnClickListener listener=new View.OnClickListener(){
//        @Override
//        public void onClick(View v)
//        {
//            switch(v.getId())
//            {
//                case R.id.button_test_neworder:
//                    IsReading=true;
//                    index = index + 1;
//
//                    RefreshData();
//                    break;
//                case R.id.button_test_deleorder:
//                    //TODO:
//                    break;
//                default:
//                    break;
//            }
//        }
//    };


