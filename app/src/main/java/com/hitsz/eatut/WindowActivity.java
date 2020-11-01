package com.hitsz.eatut;

import android.os.Bundle;

import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hitsz.eatut.adapter.FeedbackItem;
import com.hitsz.eatut.adapter.WindowAdapter;
import com.hitsz.eatut.adapter.window;
import com.hitsz.eatut.database.CanteenInfo;
import com.hitsz.eatut.database.DishInfo;
import com.hitsz.eatut.database.MyOrder;
import com.hitsz.eatut.database.WindowInfo;
import com.hitsz.eatut.datepicker.DateFormatUtils;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.hitsz.eatut.BaseClass.getCanteenCommentKeyword;


public class WindowActivity extends AppCompatActivity implements View.OnClickListener{
    private RecyclerView recyclerView;
    private TextView canIntroName;
    private TextView canIntroScore;
    private TextView canIntroSale;
    private  TextView canIntroAddr;
    private Button canIntroComment;
    private TextView canWindowKeyWord1;
    private TextView canWindowKeyWord2;
    private TextView canWindowKeyWord3;
    private TextView canWindowKeyWord4;
    private TextView canWindowKeyWord5;

    private List<window> windowList=new ArrayList<>();
    private String canteenName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window);
        Intent intent = getIntent();
        canteenName = intent.getStringExtra("extra_data");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        findView();
        initCanteenIntro();
        initWindows(canteenName);
        initRecycle();
    }
    public void onClick(View v){
        if(v.getId() == R.id.canteen_intro_comment){
            Intent intent = new Intent(v.getContext(), CommentActivity.class);
            intent.putExtra("extra_data", canteenName);
            v.getContext().startActivity(intent);
        }
    }
    protected void findView(){
        recyclerView=(RecyclerView)findViewById(R.id.window_recycle);
        canIntroName=findViewById(R.id.canteen_intro_name);
        canIntroScore=findViewById(R.id.canteen_intro_score);
        canIntroSale=findViewById(R.id.canteen_intro_monthly_sale);
        canIntroAddr=findViewById(R.id.canteen_intro_address);
        canIntroComment=findViewById(R.id.canteen_intro_comment);
        canIntroComment.setOnClickListener(this);
        canWindowKeyWord1=findViewById(R.id.canteen_window_keyword_1);
        canWindowKeyWord2=findViewById(R.id.canteen_window_keyword_2);
        canWindowKeyWord3=findViewById(R.id.canteen_window_keyword_3);
        canWindowKeyWord4=findViewById(R.id.canteen_window_keyword_4);
        canWindowKeyWord5=findViewById(R.id.canteen_window_keyword_5);
    }
    //初始化食堂介绍文本信息：食堂名称、本月到目前为止的销量、评分、地址
    private float canteenMonthlySale(){
        //返回该食堂本月到目前为止的销量
        float sale = 0;
        //本月的所有订单
        long beginDayOfThisMonth = DateUtils.getBeginDayOfMonth(DateUtils.getNowMonth());//当月第一天开始时间戳
        Log.d("CommentTest", "本月第一天开始时间点: " + DateFormatUtils.long2Str(beginDayOfThisMonth, true));
        List<MyOrder> myOrderList = LitePal.where("endTime>?", ""+beginDayOfThisMonth).find(MyOrder.class);
        for(MyOrder one:myOrderList){
            List<Integer> dishIDs = one.getDishID_III();
            for(int i=0;i<dishIDs.size();i++){
                //遍历某订单中所有菜品
                DishInfo dishInfo = LitePal.where("id=?", "" + dishIDs.get(i)).find(DishInfo.class).get(0);
                if(canteenName.equals(dishInfo.getBelongToCanteen()))
                    //如果该菜品属于这个食堂
                    sale+=dishInfo.getDishPrice();
            }
        }
        return sale;
    }
    private void initCanteenIntro(){
        canIntroName.setText(canteenName);
        CanteenInfo canteenInfo = LitePal.where("canteenName=?", canteenName).find(CanteenInfo.class).get(0);
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        canIntroScore.setText(decimalFormat.format(canteenInfo.getCanteenScore()));
        canIntroAddr.setText(canteenInfo.getCanteenAddress());
        String sale = "￥" + decimalFormat.format(canteenMonthlySale());
        canIntroSale.setText(sale);
        initKeyWords();
    }

    //初始化档口信息
    private void initWindows(String canteenName){
        Log.d("find_canteen", canteenName);
        List<WindowInfo> windowInfos = LitePal.where("belongToCanteenName like ?", canteenName).find(WindowInfo.class);
        for (WindowInfo windowInfo:windowInfos){
            String name = windowInfo.getWindowName();
            int image = windowInfo.getImageID();
            float score = windowInfo.getWindowScore();
            byte[] winshot = windowInfo.getWindowshot();
            window addWindow = new window(name, score, image, winshot);
            windowList.add(addWindow);
        }
    }

    private void initRecycle(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        WindowAdapter adapter=new WindowAdapter(windowList);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
    @Override
    protected void onRestart() {
        super.onRestart();
        windowList.clear();
        initCanteenIntro();
        initWindows(canteenName);
        initRecycle();
    }
    private void initKeyWords(){
        String AllkeyWord = getCanteenCommentKeyword(canteenName);
        if(!AllkeyWord.isEmpty()) {
            String[] keyWords = AllkeyWord.split(" ");
            switch (keyWords.length) {
                case 1:
                    canWindowKeyWord1.setText(keyWords[0]);
                    canWindowKeyWord1.setBackgroundResource(R.drawable.radius_text_view);
                    break;
                case 2:
                    canWindowKeyWord1.setText(keyWords[0]);
                    canWindowKeyWord2.setText(keyWords[1]);
                    canWindowKeyWord1.setBackgroundResource(R.drawable.radius_text_view);
                    canWindowKeyWord2.setBackgroundResource(R.drawable.radius_text_view);
                    break;
                case 3:
                    canWindowKeyWord1.setText(keyWords[0]);
                    canWindowKeyWord2.setText(keyWords[1]);
                    canWindowKeyWord3.setText(keyWords[2]);
                    canWindowKeyWord1.setBackgroundResource(R.drawable.radius_text_view);
                    canWindowKeyWord2.setBackgroundResource(R.drawable.radius_text_view);
                    canWindowKeyWord3.setBackgroundResource(R.drawable.radius_text_view);
                    break;
                case 4:
                    canWindowKeyWord1.setText(keyWords[0]);
                    canWindowKeyWord2.setText(keyWords[1]);
                    canWindowKeyWord3.setText(keyWords[2]);
                    canWindowKeyWord4.setText(keyWords[3]);
                    canWindowKeyWord1.setBackgroundResource(R.drawable.radius_text_view);
                    canWindowKeyWord2.setBackgroundResource(R.drawable.radius_text_view);
                    canWindowKeyWord3.setBackgroundResource(R.drawable.radius_text_view);
                    canWindowKeyWord4.setBackgroundResource(R.drawable.radius_text_view);
                    break;
                case 5:
                    canWindowKeyWord1.setText(keyWords[0]);
                    canWindowKeyWord2.setText(keyWords[1]);
                    canWindowKeyWord3.setText(keyWords[2]);
                    canWindowKeyWord4.setText(keyWords[3]);
                    canWindowKeyWord5.setText(keyWords[4]);
                    canWindowKeyWord1.setBackgroundResource(R.drawable.radius_text_view);
                    canWindowKeyWord2.setBackgroundResource(R.drawable.radius_text_view);
                    canWindowKeyWord3.setBackgroundResource(R.drawable.radius_text_view);
                    canWindowKeyWord4.setBackgroundResource(R.drawable.radius_text_view);
                    canWindowKeyWord5.setBackgroundResource(R.drawable.radius_text_view);
                    break;
                default:
                    break;
            }
        }
    }
}

