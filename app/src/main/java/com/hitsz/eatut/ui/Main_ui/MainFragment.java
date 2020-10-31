package com.hitsz.eatut.ui.Main_ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hitsz.eatut.R;
import com.hitsz.eatut.RankActivity;
import com.hitsz.eatut.RecentVisitActivity;
import com.hitsz.eatut.ShowPostActivity;
import com.hitsz.eatut.ViewVoteActivity;
import com.hitsz.eatut.adActivity;
import com.hitsz.eatut.adapter.CanteenAdapter;
import com.hitsz.eatut.adapter.canteen;
import com.hitsz.eatut.adapter.recentVisit;
import com.hitsz.eatut.adapter.recentVisitAdapter;
import com.hitsz.eatut.database.CanteenInfo;
import com.hitsz.eatut.database.DishInfo;
import com.hitsz.eatut.database.MyOrder;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class MainFragment extends Fragment implements View.OnClickListener{
    private List<canteen> canteenList=new ArrayList<>();
    private List<recentVisit> recentVisitList = new ArrayList<>();
    private RecyclerView recyclerView;
    private Button rankingEntryButton;
    private Button showPostButton;
    private Button viewVoteButton;

    private RecyclerView recentRecyclerView;
    private Button recentVisitEntry;

    private int userID;

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
        recentVisitEntry=root.findViewById(R.id.recent_visit_entry);
        recentVisitEntry.setOnClickListener(this);

        recentRecyclerView=root.findViewById(R.id.main_recent_visit_recycle);

        SharedPreferences pref2=getActivity().getSharedPreferences("currentID",MODE_PRIVATE);
        userID = pref2.getInt("userID", -1);
        initRecentVisit();
        initCanteen();
        initRecycle();
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecentVisit();
        initRecycle();
    }


    @Override
    public void onResume() {
        super.onResume();
        initRecentVisit();
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
            case R.id.recent_visit_entry:
                Intent intent3 = new Intent(getActivity(), RecentVisitActivity.class);
                startActivity(intent3);
                break;
        }
    }
    private void initRecycle(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        CanteenAdapter adapter=new CanteenAdapter(canteenList);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager1=new LinearLayoutManager(getContext());
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recentRecyclerView.setLayoutManager(layoutManager1);
        recentVisitAdapter  adapter1 = new recentVisitAdapter(recentVisitList);
        recentRecyclerView.setAdapter(adapter1);
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
    private void initRecentVisit(){
        //取10个菜就好啦
        int showNum = 10;
        List<MyOrder> myOrderList = LitePal.where("userID = ?", "" + userID).find(MyOrder.class);
        //按下单时间降序排序
        if(!myOrderList.isEmpty()) {
            Collections.sort(myOrderList, new Comparator<MyOrder>() {
                @Override
                public int compare(MyOrder m1, MyOrder m2) {
                    long orderTime1 = m1.getOrderTime();
                    long orderTime2 = m2.getOrderTime();
                    return (int) (orderTime2 - orderTime1);
                }
            });
            List<Integer> recentDishIDs = new ArrayList<>();
            for (MyOrder one : myOrderList) {
                if (recentDishIDs.size() >= showNum) {
                    break;
                }
                List<Integer> dishIDs = one.getDishID_III();
                for (Integer i : dishIDs) {
                    //如果已经有这道菜就跳过
                    if (!recentDishIDs.contains(i))
                        recentDishIDs.add(i);
                }
            }
            recentVisitList.clear();
            int i = 0;
            while(i < showNum && i < recentDishIDs.size()){
                DishInfo dishInfo = LitePal.where("id = ?", "" + recentDishIDs.get(i)).find(DishInfo.class).get(0);
                String name = dishInfo.getDishName();
                int image = dishInfo.getImageID();
                byte[] imgs = dishInfo.getDishshot();
                recentVisit recentVisit = new recentVisit(dishInfo.getBelongToCanteen() + "-" + dishInfo.getBelongToWindow(), name, image, imgs);
                recentVisitList.add(recentVisit);
                i+=1;
            }
        }

    }

}