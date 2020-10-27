package com.hitsz.eatut.managerActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.hitsz.eatut.R;
import com.hitsz.eatut.database.VoteInfo;
import com.hitsz.eatut.datepicker.CustomDatePicker;
import com.hitsz.eatut.datepicker.DateFormatUtils;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.view.View;
import android.widget.DatePicker;
import android.app.DatePickerDialog;
import android.widget.TextView;
import android.widget.Toast;


// 创建投票
public class AddVoteActivity extends AppCompatActivity implements View.OnClickListener {
    EditText text_votename;
    private Button submit_btn;
    //时间选择
    private TextView mTvSelectedTime;
    private CustomDatePicker mTimerPicker;
    private long mTimeStamp;//时间戳


    public AddVoteActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addvote);
        submit_btn=(Button)findViewById(R.id.button_submit_vote);
        text_votename = (EditText)findViewById(R.id.text_votename);
        mTvSelectedTime = (TextView)findViewById(R.id.tv_selected_vote_time);
        submit_btn.setOnClickListener(this);
        findViewById(R.id.vot_ddl).setOnClickListener(this);
        initTimerPicker();
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.vot_ddl:
                //日期格式为yyyy-MM-dd HH:mm
                mTimerPicker.show(mTvSelectedTime.getText().toString());
                break;
            case R.id.button_submit_vote:
                String voteName = text_votename.getText().toString();
                VoteInfo newvote = new VoteInfo();
                newvote.setVoteName(voteName);
                newvote.InitNum();
                newvote.initVotedId();
//                newvote.setVotedId();
                newvote.setVoteDdl(mTimeStamp);
//                newvote.initVotedId();
                boolean save_flag = newvote.save();
                //addNewVote(voteName);
                if (save_flag){
                    Toast.makeText(this, "已添加投票", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(this, "失败", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                Toast.makeText(this, "失败", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void initTimerPicker() {
        /*时间选择控件初始化*/
        //时间范围：[beginTime, endTime]
        String beginTime = "2020-1-1 00:00";
        String endTime = "2021-12-31 00:00";
        String currentTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);
        //默认初始时间为当前时间
        mTvSelectedTime.setText(currentTime);


        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimerPicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                Log.d("CheckActivityTimeStamp", Long.toString(timestamp));//时间戳
                mTimeStamp = timestamp;
                mTvSelectedTime.setText(DateFormatUtils.long2Str(timestamp, true));
            }
        }, beginTime, endTime, currentTime);
        // 允许点击屏幕或物理返回键关闭
        mTimerPicker.setCancelable(true);
        // 显示时和分
        mTimerPicker.setCanShowPreciseTime(true);
        // 允许循环滚动
        mTimerPicker.setScrollLoop(true);
        // 不允许滚动动画
        mTimerPicker.setCanShowAnim(false);
    }
}