package com.hitsz.eatut.ui.Main_ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hitsz.eatut.R;
import com.hitsz.eatut.RankActivity;
import com.hitsz.eatut.ShowPostActivity;
import com.hitsz.eatut.ViewVoteActivity;
import com.hitsz.eatut.adActivity;
import com.hitsz.eatut.adapter.CanteenAdapter;
import com.hitsz.eatut.adapter.canteen;
import com.hitsz.eatut.database.CanteenInfo;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;


public class MainFragment extends Fragment implements View.OnClickListener{
    private List<canteen> canteenList=new ArrayList<>();
    private RecyclerView recyclerView;
    private Button rankingEntryButton;
    private Button showPostButton;
    private Button viewVoteButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        showPostButton =root.findViewById(R.id.showpost_btn);
        showPostButton.setOnClickListener(this);
        rankingEntryButton = root.findViewById(R.id.ranking_entry_btn);
        rankingEntryButton.setOnClickListener(this);
        viewVoteButton = root.findViewById(R.id.viewvote_btn);
        viewVoteButton.setOnClickListener(this);
        recyclerView=root.findViewById(R.id.canteen_recycle);
        initRecycle();
        initCanteen();
        return root;
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.showpost_btn:
                Intent intent=new Intent(getActivity(), ShowPostActivity.class);
                startActivity(intent);
                break;
            case R.id.ranking_entry_btn:
                Intent intent1=new Intent(getActivity(), RankActivity.class);
                startActivity(intent1);
                break;
            case R.id.viewvote_btn:
                Intent intent2 = new Intent(getActivity(), ViewVoteActivity.class);
                startActivity(intent2);
                break;
        }
    }
    private void initRecycle(){
            LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            CanteenAdapter adapter=new CanteenAdapter(canteenList);
            recyclerView.setAdapter(adapter);
    }
    private void initCanteen(){
        //conteenList.clear();
        List<CanteenInfo> canteenInfos = LitePal.findAll(CanteenInfo.class);
        for (CanteenInfo canteenInfo:canteenInfos){
            String name = canteenInfo.getCanteenName();
            int image = canteenInfo.getImageID();
            float score = canteenInfo.getCanteenScore();
            byte[] canteenshot = canteenInfo.getCanteenshot();
            canteen can = new canteen(name, score, image, canteenshot);
            canteenList.add(can);
        }
    }
}