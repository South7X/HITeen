package com.hitsz.eatut;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hitsz.eatut.adapter.DishAdapter;
import com.hitsz.eatut.adapter.dish;
import com.hitsz.eatut.database.DishInfo;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class DishActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private List<dish> foodsList=new ArrayList<>();
    private FloatingActionButton fab;
    private String windowName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish);
        Intent intent = getIntent();
        windowName = intent.getStringExtra("extra_data");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        findView();
        initfoods(windowName);
        initRecycle();
    }
    public void onClick(View v){
        switch(v.getId()){
            case R.id.fab:
                Intent intent=new Intent(DishActivity.this,CheckActivity.class);
                startActivity(intent);
                break;

        }
    }

    protected void findView(){
        recyclerView=(RecyclerView)findViewById(R.id.dish_recycle);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            //设置fab下滑消失上滑显示
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0 && fab.getVisibility() == View.VISIBLE){
                    fab.hide();
                }else if(dy < 0 && fab.getVisibility() != View.VISIBLE){
                    fab.show();
                }
            }
        });
        fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }
    //初始化菜品信息
    private void initfoods(String windowName){
        List<DishInfo> dishInfos = LitePal.where("belongToWindow like ?", windowName).find(DishInfo.class);
        for (DishInfo dishInfo:dishInfos){
            String name = dishInfo.getDishName();
            int image = dishInfo.getImageID();
            float price = dishInfo.getDishPrice();
            float score = dishInfo.getDishScore();
            int dishID=dishInfo.getId();
            byte[] dishshot= dishInfo.getDishshot();
            dish addDish = new dish(name, image, price, score,dishID, dishshot);
            foodsList.add(addDish);
        }
    }

    private void initRecycle(){
        SharedPreferences pref2 = this.getSharedPreferences("currentID",MODE_PRIVATE);
        int userID = pref2.getInt("userID", -1);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DishAdapter adapter=new DishAdapter(userID, foodsList, new DishAdapter.daListener() {
            @Override
            public void da() {
                foodsList.clear();
                initfoods(windowName);
                initRecycle();
            }
        });
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
}
