package com.hitsz.eatut.managerActivities;

import com.hitsz.eatut.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static com.hitsz.eatut.BaseClass.deleteCanteenFromDatabase;

/**
 * @author Lily
 */
public class DeletecanteenActivity extends AppCompatActivity implements View.OnClickListener {

    EditText text_delete_canteenname = null;
    boolean delete_canteen=false;

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.button_delete_canteen:
                String delete_cn=text_delete_canteenname.getText().toString();
                delete_canteen=deleteCanteenFromDatabase(delete_cn);
                if(delete_canteen)
                    Toast.makeText(this,"删除食堂成功",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this,"删除食堂失败，请确认输入",Toast.LENGTH_SHORT).show();
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deletecanteen);
        Button button_edit=(Button)findViewById(R.id.button_delete_canteen);
        button_edit.setOnClickListener(this);

        text_delete_canteenname = (EditText) findViewById(R.id.text_delete_canteenname);
//        if(mDish.save()){
//            Toast.makeText(this,"增加成功",Toast.LENGTH_SHORT).show();
//        } else{
//            Toast.makeText(this,"增加失败，请重试",Toast.LENGTH_SHORT).show();
//        }
    }
}
