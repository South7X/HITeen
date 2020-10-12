package com.hitsz.eatut.managerActivities;

import com.hitsz.eatut.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static com.hitsz.eatut.BaseClass.addNewCanteen;

/**
 * @author Lily
 */
public class AddcanteenActivity extends AppCompatActivity implements View.OnClickListener {

    EditText text_canteenname = null;
    EditText text_canteenaddress = null;
    EditText text_windownum = null;

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.button_submit_canteen:
                String cn = text_canteenname.getText().toString();
                String ca = text_canteenaddress.getText().toString();
                String wn_string = text_windownum.getText().toString();
                int wn = Integer.valueOf(wn_string);
                if(wn>0) {
                    addNewCanteen(cn, ca, wn);
                    Toast.makeText(this, "增加食堂成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else Toast.makeText(this, "档口数需大于零", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcanteen);
        Button button_edit=(Button)findViewById(R.id.button_submit_canteen);
        button_edit.setOnClickListener(this);

        text_canteenname = (EditText) findViewById(R.id.text_canteenname);
        text_canteenaddress =(EditText) findViewById(R.id.text_canteenaddress);
        text_windownum =(EditText) findViewById(R.id.text_windownum);
//        if(mDish.save()){
//            Toast.makeText(this,"增加成功",Toast.LENGTH_SHORT).show();
//        } else{
//            Toast.makeText(this,"增加失败，请重试",Toast.LENGTH_SHORT).show();
//        }
    }
}
