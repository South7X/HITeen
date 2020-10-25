package com.hitsz.eatut.managerActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.hitsz.eatut.R;
import com.hitsz.eatut.database.VoteInfo;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.view.View;
import android.widget.Toast;


// 创建投票
public class AddVoteActivity extends AppCompatActivity implements View.OnClickListener {
    EditText text_votename;
    private Button submit_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addvote);
        Button submit_btn=(Button)findViewById(R.id.button_submit_vote);
        text_votename = (EditText)findViewById(R.id.text_votename);
        submit_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.button_submit_vote:
                String voteName = text_votename.getText().toString();
                VoteInfo newvote = new VoteInfo();
                newvote.setVoteName(voteName);
                newvote.InitNum();
                newvote.save();
                //addNewVote(voteName);
                Toast.makeText(this, "已添加投票", Toast.LENGTH_SHORT).show();
                finish();
                break;
            default:
                Toast.makeText(this, "失败", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}