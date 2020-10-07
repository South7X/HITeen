package com.hitsz.eatut.managerActivities;

import androidx.appcompat.app.AppCompatActivity;
import com.hitsz.eatut.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.hitsz.eatut.BaseClass.addNewDish;
/**
 * @author Lily
 */
public class AdddishActivity extends AppCompatActivity implements View.OnClickListener {
    EditText text_dishname = null;
    EditText text_dishprice = null;
    EditText text_belongToCanteenName = null;
    EditText text_belogToWindowName = null;
    EditText text_tags = null;

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.button_submit:
                String dn = text_dishname.getText().toString();
                String btc = text_belongToCanteenName.getText().toString();
                String btw = text_belogToWindowName.getText().toString();
                String dp_string = text_dishprice.getText().toString();
                String tags = text_tags.getText().toString();
                float dp = Float.valueOf(dp_string);
                addNewDish(dn, btc, btw, dp, tags);
                Toast.makeText(this, "增加菜品成功", Toast.LENGTH_SHORT).show();
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddish);
        Button button_edit=(Button)findViewById(R.id.button_submit);
        button_edit.setOnClickListener(this);

        text_dishname = (EditText) findViewById(R.id.text_dishname);
        text_belongToCanteenName =(EditText) findViewById(R.id.text_belongtocateen);
        text_belogToWindowName =(EditText) findViewById(R.id.text_belongtowindow);
        text_dishprice = (EditText) findViewById(R.id.text_dishprice);
        text_tags =(EditText) findViewById(R.id.text_tags);
//        if(mDish.save()){
//            Toast.makeText(this,"增加成功",Toast.LENGTH_SHORT).show();
//        } else{
//            Toast.makeText(this,"增加失败，请重试",Toast.LENGTH_SHORT).show();
//        }
    }
}
