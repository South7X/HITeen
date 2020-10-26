package com.hitsz.eatut.managerActivities;

import com.hitsz.eatut.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static com.hitsz.eatut.BaseClass.deleteDishFromDatabase;
/**
 * @author Lily
 */
public class DeletedishActivity extends AppCompatActivity implements View.OnClickListener {

    EditText text_delete_dishwname = null;
    EditText text_delete_dish_belongtocanteen = null;
    EditText text_delete_dish_belongtowin = null;
    boolean delete_dish=false;

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.button_delete_dish:
                String delete_dn=text_delete_dishwname.getText().toString();
                String delete_dish_belongtocanteen=text_delete_dish_belongtocanteen.getText().toString();
                String delete_dish_belongtowin=text_delete_dish_belongtowin.getText().toString();
                delete_dish=deleteDishFromDatabase(delete_dn, delete_dish_belongtocanteen,delete_dish_belongtowin);
                if(delete_dish)
                    Toast.makeText(this,"删除菜品成功",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this,"删除菜品失败，请确认输入",Toast.LENGTH_SHORT).show();
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deletedish);
        Button button_edit=(Button)findViewById(R.id.button_delete_dish);
        button_edit.setOnClickListener(this);
        text_delete_dishwname = (EditText) findViewById(R.id.text_delete_windowname);
        text_delete_dish_belongtocanteen = (EditText) findViewById(R.id.text_delete_dish_belongtocanteen);
        text_delete_dish_belongtowin = (EditText) findViewById(R.id.text_delete_dish_belongtowin);
//        if(mDish.save()){
//            Toast.makeText(this,"增加成功",Toast.LENGTH_SHORT).show();
//        } else{
//            Toast.makeText(this,"增加失败，请重试",Toast.LENGTH_SHORT).show();
//        }
    }
}
