package com.hitsz.eatut.ui.Search_ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popupwindowlibrary.bean.FiltrateBean;
import com.example.popupwindowlibrary.view.ScreenPopWindow;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hitsz.eatut.CheckActivity;
import com.hitsz.eatut.R;
import com.hitsz.eatut.adapter.DishAdapter;
import com.hitsz.eatut.adapter.dish;
import com.hitsz.eatut.database.DishInfo;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.hitsz.eatut.BaseClass.createLinkedListFromDatabase;
import static com.hitsz.eatut.BaseClass.getUnitedList;
import static com.hitsz.eatut.BaseClass.traversalWholeLinkedList;


public class SearchFragment extends Fragment implements View.OnClickListener{
    private ScreenPopWindow screenPopWindow;
    private List<dish> foodsList=new ArrayList<>();
    private List<FiltrateBean> filterList = new ArrayList<>();
    private List<FiltrateBean> sortList = new ArrayList<>();

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private Button sortbtn;
    private Button filterbtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView=root.findViewById(R.id.supplier_recycle_lv);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            //设置fab下滑消失上滑显示
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0 && fab.getVisibility() == View.VISIBLE){
                    fab.hide();
                }else if(dy < 0 && fab.getVisibility() != View.VISIBLE){
                    fab.show();
                }
            }
        });
        fab=root.findViewById(R.id.fab);
        fab.setOnClickListener(this);
        sortbtn=root.findViewById(R.id.sort_btn);
        filterbtn=root.findViewById(R.id.filter_btn);

        createLinkedListFromDatabase();
        initfoods();
        initRecycle();
        initParam();
        initView();
        return root;

    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.fab:
                Intent intent=new Intent(getActivity(), CheckActivity.class);
                startActivity(intent);
                break;
        }
    }
    private void initfoods(){
        List<DishInfo> dishInfos= LitePal.findAll(DishInfo.class);
        for (DishInfo dishInfo:dishInfos){
            String name = dishInfo.getDishName();
            int image = dishInfo.getImageID();
            float price = dishInfo.getDishPrice();
            float score = dishInfo.getDishScore();
            int dishID=dishInfo.getId();
            dish addDish = new dish(name, image, price, score,dishID);
            foodsList.add(addDish);
        }
    }
    private void initRecycle(){
        SharedPreferences pref2 = getActivity().getSharedPreferences("currentID",MODE_PRIVATE);
        int userID = pref2.getInt("userID",-1);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        DishAdapter adapter=new DishAdapter(userID, foodsList, new DishAdapter.daListener() {
            @Override
            public void da() {
                foodsList.clear();
                initfoods();
                initRecycle();
                initView();
            }
        });
        recyclerView.setAdapter(adapter);
    }
    private void initParam() {
        String[] filter = {"全部","酸","甜","苦","辣","咸","清淡","油炸"};
        String[] sort = {"默认(评分降序)","价格升序","价格降序","评分升序","评分降序"};

        FiltrateBean fb1 = new FiltrateBean();
        fb1.setTypeName("口味");
        List<FiltrateBean.Children> childrenList0 = new ArrayList<>();
        for (String afilter : filter) {
            FiltrateBean.Children cd = new FiltrateBean.Children();
            cd.setValue(afilter);
            childrenList0.add(cd);
        }
        fb1.setChildren(childrenList0);

        FiltrateBean fb2 = new FiltrateBean();
        fb2.setTypeName("排序方式");
        List<FiltrateBean.Children> childrenList1 = new ArrayList<>();
        for (String asort : sort) {
            FiltrateBean.Children cd = new FiltrateBean.Children();
            cd.setValue(asort);
            childrenList1.add(cd);
        }
        fb2.setChildren(childrenList1);

        filterList.add(fb1);
        sortList.add(fb2);
    }
    private int getMiddle(int sign,int flag,List<dish> list,int begin ,int end){
        int index_i=begin,index_j=end;
        dish temp=list.get(begin);
        if(sign==0){//升序
            if(flag==0){//价格
                while(index_i<index_j){
                    while(index_i<index_j&&list.get(index_j).getDishPrice()>=temp.getDishPrice())
                        index_j--;
                    if(index_i<index_j){
                        list.set(index_i,list.get(index_j));
                        index_i++;
                    }
                    while(index_i<index_j&&list.get(index_i).getDishPrice()<temp.getDishPrice())
                        index_i++;
                    if(index_i<index_j){
                        list.set(index_j,list.get(index_i));
                        index_j--;
                    }
                }
            }else if(flag==1){
                while(index_i<index_j){
                    while(index_i<index_j&&list.get(index_j).getDishScore()>=temp.getDishScore())
                        index_j--;
                    if(index_i<index_j){
                        list.set(index_i,list.get(index_j));
                        index_i++;
                    }
                    while(index_i<index_j&&list.get(index_i).getDishScore()<temp.getDishScore())
                        index_i++;
                    if(index_i<index_j){
                        list.set(index_j,list.get(index_i));
                        index_j--;
                    }
                }
            }
        }else if(sign==1){//降序
            if(flag==0){//价格
                while(index_i<index_j){
                    while(index_i<index_j&&list.get(index_j).getDishPrice()<=temp.getDishPrice())
                        index_j--;
                    if(index_i<index_j){
                        list.set(index_i,list.get(index_j));
                        index_i++;
                    }
                    while(index_i<index_j&&list.get(index_i).getDishPrice()>temp.getDishPrice())
                        index_i++;
                    if(index_i<index_j){
                        list.set(index_j,list.get(index_i));
                        index_j--;
                    }
                }
            }else if(flag==1){
                while(index_i<index_j){
                    while(index_i<index_j&&list.get(index_j).getDishScore()<=temp.getDishScore())
                        index_j--;
                    if(index_i<index_j){
                        list.set(index_i,list.get(index_j));
                        index_i++;
                    }
                    while(index_i<index_j&&list.get(index_i).getDishScore()>temp.getDishScore())
                        index_i++;
                    if(index_i<index_j){
                        list.set(index_j,list.get(index_i));
                        index_j--;
                    }
                }
            }
        }
        list.set(index_i,temp);
        return index_i;
    }
    private void quickSort(int sign,int flag,List<dish> list,int begin,int end){
        if(list.isEmpty()||begin>=end)//检查是否为空
            return;
        int mid=getMiddle(sign,flag,list,begin,end);
        quickSort(sign,flag,list,begin,mid-1);
        quickSort(sign,flag,list,mid+1,end);
    }
    private void initView() {
        sortbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screenPopWindow = new ScreenPopWindow(getActivity(), sortList);
                //默认单选，因为共用的一个bean，这里调用reset重置下数据
                screenPopWindow.reset().build();
                screenPopWindow.showAsDropDown(sortbtn);
                screenPopWindow.setOnConfirmClickListener(new ScreenPopWindow.OnConfirmClickListener() {
                    @Override
                    public void onConfirmClick(List<String> list) {
                        if(list.size()==0)//没有选择的情况
                        {
                            quickSort(1,1,foodsList,0,foodsList.size()-1);
                            initRecycle();
                        }else {
                            String str=list.get(0);
                            Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
                            switch (str) {
                                case "价格升序":
                                    quickSort(0, 0, foodsList, 0, foodsList.size() - 1);
                                    initRecycle();
                                    break;
                                case "价格降序":
                                    quickSort(1, 0, foodsList, 0, foodsList.size() - 1);
                                    initRecycle();
                                    break;
                                case "评分升序":
                                    quickSort(0, 1, foodsList, 0, foodsList.size() - 1);
                                    initRecycle();
                                    break;
                                case "默认(评分降序)":
                                case "评分降序":
                                    quickSort(1, 1, foodsList, 0, foodsList.size() - 1);
                                    initRecycle();
                                    break;
                            }
                        }
                    }
                });

            }
        });

        filterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screenPopWindow = new ScreenPopWindow(getActivity(), filterList);
                //设置多选，因为共用的一个bean，这里调用reset重置下数据
                screenPopWindow.setSingle(false).reset().build();
                screenPopWindow.showAsDropDown(filterbtn);
                screenPopWindow.setOnConfirmClickListener(new ScreenPopWindow.OnConfirmClickListener() {
                    @Override
                    public void onConfirmClick(List<String> list) {
                        foodsList.clear();
                        List<Integer> perId;
                        List<DishInfo> dishInfos= LitePal.findAll(DishInfo.class);
                        DishInfo dishInfo;
                        if(list.size()==0||list.contains("全部")){
                            perId=traversalWholeLinkedList();//return 所有菜品
                        }else{
                            perId=getUnitedList(list);
                        }
                        for(int i=0;i<perId.size();i++){
                            Log.d("United", String.valueOf(perId.get(i)));
                            dishInfo=dishInfos.get(perId.get(i) - 1);
                            String name = dishInfo.getDishName();
                            int image = dishInfo.getImageID();
                            float price = dishInfo.getDishPrice();
                            float score = dishInfo.getDishScore();
                            int dishID=dishInfo.getId();
                            dish addFood=new dish(name,image,price,score,dishID);
                            foodsList.add(addFood);
                        }
                        initRecycle();

                        StringBuilder str = new StringBuilder();
                        for (int i=0;i<list.size();i++) {
                            str.append(list.get(i)).append(" ");
                        }
                        Toast.makeText(getActivity(), str.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}