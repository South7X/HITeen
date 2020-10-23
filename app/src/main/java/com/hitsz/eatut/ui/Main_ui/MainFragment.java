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
import com.hitsz.eatut.adActivity;
import com.hitsz.eatut.adapter.CanteenAdapter;
import com.hitsz.eatut.adapter.canteen;
import com.hitsz.eatut.database.CanteenInfo;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;


public class MainFragment extends Fragment {
    private List<canteen> canteenList=new ArrayList<>();
    private RecyclerView recyclerView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        // 跳转至 what’s new
        final Button adButton=root.findViewById(R.id.button_whatsnew);
        recyclerView=root.findViewById(R.id.canteen_recycle);
        initRecycle();
        initCanteen();
        adButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), adActivity.class);
                startActivity(intent);
            }
        });
        return root;
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
            canteen can = new canteen(name, score, image);
            canteenList.add(can);
        }
    }
}