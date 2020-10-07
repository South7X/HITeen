package com.hitsz.eatut.managerActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.hitsz.eatut.R;
import com.hitsz.eatut.adapter.FeedbackAdapter;
import com.hitsz.eatut.adapter.FeedbackItem;
import com.hitsz.eatut.database.FeedInfo;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Lily
 */
//查看反馈信息,显示数据库中反馈的内容
public class FeedInfoActivity extends AppCompatActivity{
    private RecyclerView recyclerView;
    private List<FeedbackItem> feedInfoList=new ArrayList<>();
    private FeedbackAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_info);
        findview();
        initFeedback();
        initRecycle();
    }

    protected void findview(){
        recyclerView=(RecyclerView)findViewById(R.id.recycler_feedback);
    }

    //通过recyclerview显示litepal中内容
    private void initFeedback(){
        List<FeedInfo> feedInfos = LitePal.findAll(FeedInfo.class);
        feedInfoList.clear();
        for(FeedInfo feedInfo : feedInfos){
            FeedbackItem feedbackItem=new FeedbackItem(feedInfo.getId(),feedInfo.getFeedInfo());
            feedInfoList.add(feedbackItem);
            //feedbackItem.setUserID(feedInfo.getId());
        }
    }

    private void initRecycle(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new FeedbackAdapter(feedInfoList, new FeedbackAdapter.ofaListener() {
            @Override
            public void ofa(int position) {
                feedInfoList.remove(position);
                initRecycle();
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
