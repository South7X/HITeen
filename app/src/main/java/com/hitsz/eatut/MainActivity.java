package com.hitsz.eatut;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hitsz.eatut.adapter.canteen;
import com.hitsz.eatut.database.CanteenInfo;
import com.hitsz.eatut.database.DishInfo;
import com.hitsz.eatut.database.WindowInfo;
import com.hitsz.eatut.managerActivities.FeedbackActivity;

import java.util.ArrayList;
import java.util.List;

import static com.hitsz.eatut.BaseClass.addNewCanteen;
import static com.hitsz.eatut.BaseClass.addNewDish;
import static com.hitsz.eatut.BaseClass.addNewWindow;
import static com.hitsz.eatut.BaseClass.buildTreeFromDatabase;
import static com.hitsz.eatut.BaseClass.createLinkedListFromDatabase;
import static com.hitsz.eatut.BaseClass.deleteCanteenFromDatabase;
import static com.hitsz.eatut.BaseClass.deleteDishFromDatabase;
import static com.hitsz.eatut.BaseClass.deleteWindowFromDatabase;
import static com.hitsz.eatut.BaseClass.traversalWholeLinkedList;
import static com.hitsz.eatut.BaseClass.traversalWholeTreeNodes;

/**
 * @author lixiang
 */
public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<canteen> canteenList=new ArrayList<>();
    private BottomNavigationView navView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //test();
        navView=findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_search, R.id.navigation_main, R.id.navigation_order)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }

    private void test(){
        String tags1 = "tag1$tag2$tag3$tag4$tag5";
        String tags2 = "tag1$tag2$tag3";
        String tags3 = "$tag3$tag4$tag5";
        String tags4 = "tag2$tag3$tag4";
        //测试-----------------------------
        //食堂
        CanteenInfo canteen1 = new CanteenInfo();
        canteen1.setCanteenName("canteen1");
        canteen1.setCanteenWindowNumber(15);
        canteen1.save();
        CanteenInfo canteen2 = new CanteenInfo();
        canteen2.setCanteenName("canteen2");
        canteen2.setCanteenWindowNumber(5);
        canteen2.save();
        CanteenInfo canteen3 = new CanteenInfo();
        canteen3.setCanteenName("canteen3");
        canteen3.setCanteenWindowNumber(10);
        canteen3.save();
        //档口
        WindowInfo window1 = new WindowInfo();
        window1.setWindowName("window1");
        window1.setBelongToCanteenName("canteen3");
        window1.setWindowDishNumber(15);
        window1.save();
        WindowInfo window2 = new WindowInfo();
        window2.setWindowName("window2");
        window2.setBelongToCanteenName("canteen2");
        window2.setWindowDishNumber(10);
        window2.save();
        WindowInfo window3 = new WindowInfo();
        window3.setWindowName("window3");
        window3.setBelongToCanteenName("canteen3");
        window3.setWindowDishNumber(12);
        window3.save();
        //菜品
        DishInfo dish1 = new DishInfo();
        dish1.setDishName("dish1");
        dish1.setBelongToWindow("window1");
        dish1.setBelongToCanteen("canteen3");
        dish1.setDishTags(tags1);
        dish1.setDishPrice(12);
        dish1.setImageID(R.drawable.dumplings);
        dish1.save();
        DishInfo dish2 = new DishInfo();
        dish2.setDishName("dish2");
        dish2.setBelongToWindow("window1");
        dish2.setBelongToCanteen("canteen3");
        dish2.setDishTags(tags2);
        dish2.setDishPrice(10);
        dish2.save();
        DishInfo dish3 = new DishInfo();
        dish3.setDishName("dish3");
        dish3.setBelongToWindow("window1");
        dish3.setBelongToCanteen("canteen3");
        dish3.setDishTags(tags3);
        dish3.setDishPrice(1);
        dish3.save();
        DishInfo dish4 = new DishInfo();
        dish4.setDishName("dish4");
        dish4.setBelongToWindow("window3");
        dish4.setBelongToCanteen("canteen3");
        dish4.setDishTags(tags4);
        dish4.setDishPrice(12);
        dish4.save();

        //建树
        buildTreeFromDatabase();
        //遍历输出
        Log.d("TreeNode", "第一次建树");
        traversalWholeTreeNodes();




        //生成嵌套链表索引
        createLinkedListFromDatabase();
        Log.d("LinkedList", "生成链表");
        traversalWholeLinkedList();

        //增加结点
        //addNewCanteen("canteen4", "***", 30);
        //addNewWindow("window1","**", "canteen4", 10);
        //addNewDish("dish1","canteen4", "window1", 2, tags1);

        //遍历输出
        Log.d("LinkedList", "增加结点后");
        traversalWholeTreeNodes();
        traversalWholeLinkedList();

        //删除菜品结点
        deleteDishFromDatabase("dish1", "canteen4", "window1");
        deleteDishFromDatabase("dish2", "canteen3", "window1");

        //遍历输出
        Log.d("TreeNode", "删除菜品结点后");
        traversalWholeTreeNodes();
        traversalWholeLinkedList();


        //删除档口结点
        deleteWindowFromDatabase("window1", "canteen3");

        Log.d("TreeNOde", "删除档口结点后");
        traversalWholeTreeNodes();
        //删除食堂结点
        deleteCanteenFromDatabase("canteen1");
        Log.d("TreeNode", "删除食堂结点后");
        traversalWholeTreeNodes();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.feedback: {
                Intent feedback_intent = new Intent(MainActivity.this, FeedbackActivity.class);
                startActivity(feedback_intent);
                Toast.makeText(MainActivity.this, "跳转至设置",
                        Toast.LENGTH_SHORT).show();
            }
            default:
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_main, R.id.navigation_order, R.id.navigation_search)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }
}
