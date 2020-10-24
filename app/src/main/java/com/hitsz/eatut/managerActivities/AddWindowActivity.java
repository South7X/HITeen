package com.hitsz.eatut.managerActivities;

import androidx.appcompat.app.AppCompatActivity;
import com.hitsz.eatut.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.hitsz.eatut.BaseClass.addNewWindow;
/**
 * @author Lily
 */
public class AddWindowActivity extends AppCompatActivity implements View.OnClickListener {

    EditText text_windowname = null;
    EditText text_windowaddress = null;
    EditText text_win_belongtocateen = null;
    EditText text_dishnum = null;

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.button_submit_win:
                String wname = text_windowname.getText().toString();
                String wadd = text_windowaddress.getText().toString();
                String win_belongtocanteen = text_win_belongtocateen.getText().toString();
                String dishnum_string = text_dishnum.getText().toString();
                int wnum = Integer.valueOf(dishnum_string);
                if(wnum>0) {
                    addNewWindow(wname, wadd, win_belongtocanteen, wnum);
                    Toast.makeText(this, "增加档口成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else Toast.makeText(this, "菜品数需大于零", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_window);
        Button button_edit=(Button)findViewById(R.id.button_submit_win);
        button_edit.setOnClickListener(this);

        text_windowname = (EditText) findViewById(R.id.text_windowname);
        text_windowaddress =(EditText) findViewById(R.id.text_windowaddress);
        text_win_belongtocateen =(EditText) findViewById(R.id.text_win_belongtocateen);
        text_dishnum =(EditText) findViewById(R.id.text_dishnum);
//        if(mDish.save()){
//            Toast.makeText(this,"增加成功",Toast.LENGTH_SHORT).show();
//        } else{
//            Toast.makeText(this,"增加失败，请重试",Toast.LENGTH_SHORT).show();
//        }
    }
}
