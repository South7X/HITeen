package com.hitsz.eatut.managerActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.hitsz.eatut.R;
/**
 * @author Lily
 */
public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //进入增加食堂界面
        Button button_addcanteen=(Button)findViewById(R.id.button_addcanteen);
        button_addcanteen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(EditActivity.this,AddcanteenActivity.class);
                startActivity(intent);
            }
        });

        //进入增加档口界面
        Button button_addwin=(Button)findViewById(R.id.button_addwindow);
        button_addwin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(EditActivity.this,AddWindowActivity.class);
                startActivity(intent);
            }
        });

        //进入增加菜品界面
        Button button_add=(Button)findViewById(R.id.button_adddish);
        button_add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(EditActivity.this,AdddishActivity.class);
                startActivity(intent);
            }
        });
        //进入删除食堂界面
        Button button_delete_canteen=(Button)findViewById(R.id.button_deletecanteen);
        button_delete_canteen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(EditActivity.this,DeletecanteenActivity.class);
                startActivity(intent);
            }
        });
        //进入删除档口界面
        Button button_delete_win=(Button)findViewById(R.id.button_deletewindow);
        button_delete_win.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(EditActivity.this,DeletewindowActivity.class);
                startActivity(intent);
            }
        });
        //进入删除菜品界面
        Button button_delete=(Button)findViewById(R.id.button_deletedish);
        button_delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(EditActivity.this,DeletedishActivity.class);
                startActivity(intent);
            }
        });
    }
}
