package com.hitsz.eatut;

import android.os.Bundle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hitsz.eatut.adapter.WindowAdapter;
import com.hitsz.eatut.adapter.window;
import com.hitsz.eatut.database.WindowInfo;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;


public class WindowActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<window> windowList=new ArrayList<>();
    private String canteenName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window);
        Intent intent = getIntent();
        canteenName = intent.getStringExtra("extra_data");
        findView();
        initWindows(canteenName);
        initRecycle();
    }

    protected void findView(){
        recyclerView=(RecyclerView)findViewById(R.id.window_recycle);

    }
    //初始化档口信息
    private void initWindows(String canteenName){
        Log.d("find_canteen", canteenName);
        List<WindowInfo> windowInfos = LitePal.where("belongToCanteenName like ?", canteenName).find(WindowInfo.class);
        for (WindowInfo windowInfo:windowInfos){
            String name = windowInfo.getWindowName();
            int image = windowInfo.getImageID();
            float score = windowInfo.getWindowScore();
            window addWindow = new window(name, score, image);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        windowList.clear();
        initWindows(canteenName);
        initRecycle();
    }
}

