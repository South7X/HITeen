package com.hitsz.eatut.managerActivities;

import com.hitsz.eatut.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static com.hitsz.eatut.BaseClass.deleteWindowFromDatabase;

/**
 * @author Lily
 */
public class DeletewindowActivity extends AppCompatActivity implements View.OnClickListener {

    EditText text_delete_windowname = null;
    EditText text_delete_window_belongtocanteen = null;
    boolean delete_window=false;

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.button_delete_window:
                String delete_wn=text_delete_windowname.getText().toString();
                String delete_win_belongcanteen=text_delete_window_belongtocanteen.getText().toString();
                delete_window=deleteWindowFromDatabase(delete_wn, delete_win_belongcanteen);
                if(delete_window)
                    Toast.makeText(this,"删除档口成功",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this,"删除档口失败",Toast.LENGTH_SHORT).show();
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deletewindow);
        Button button_edit=(Button)findViewById(R.id.button_delete_window);
        button_edit.setOnClickListener(this);
        text_delete_windowname = (EditText) findViewById(R.id.text_delete_windowname);
        text_delete_window_belongtocanteen = (EditText) findViewById(R.id.text_delete_window_belongtocanteen);
//        if(mDish.save()){
//            Toast.makeText(this,"增加成功",Toast.LENGTH_SHORT).show();
//        } else{
//            Toast.makeText(this,"增加失败，请重试",Toast.LENGTH_SHORT).show();
//        }
    }
}
