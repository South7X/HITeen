package com.hitsz.eatut;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.hitsz.eatut.ui.Search_ui.SearchFragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import com.example.popupwindowlibrary.view.ScreenPopWindow;
import com.hitsz.eatut.AAChartCoreLib.AAChartCreator.AAChartModel;
import com.hitsz.eatut.AAChartCoreLib.AAChartCreator.AASeriesElement;
import com.hitsz.eatut.AAChartCoreLib.AAChartEnum.AAChartType;
import com.example.popupwindowlibrary.bean.FiltrateBean;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {
    private ScreenPopWindow screenPopWindow;
    private List<FiltrateBean> expenseList = new ArrayList<>();
    private List<FiltrateBean> favorList = new ArrayList<>();
    public StatisticData statisticData =new StatisticData();
    public HashMap<String, Integer> taste_hash=new HashMap<>();
    public HashMap<String, Integer> window_hash=new HashMap<>();
    public Object[] times;
    public int userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        SharedPreferences pref2 = this.getSharedPreferences("currentID",MODE_PRIVATE);
        userID = pref2.getInt("userID", -1);
        taste_hash=statisticData.PreferStatistic(userID,1);
        window_hash=statisticData.PreferStatistic(userID,0);
        super.onCreate(savedInstanceState);
        Activity statis=this;
        setContentView(R.layout.activity_statistics);
        Button time = (Button) findViewById(R.id.time);
        Button expense = (Button) findViewById(R.id.expense);
        Button favor = (Button) findViewById(R.id.favor);
        com.hitsz.eatut.AAChartCoreLib.AAChartCreator.AAChartView aaChartView = findViewById(R.id.AAChartView);
        initParam();
        time.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AAChartModel aaChartModel = new AAChartModel();
                aaChartView.aa_drawChartWithChartModel(aaChartModel);
                aaChartModel.backgroundColor="#fffff0";
                String[] s=new String[]{"6:00-7:00", "7:00-8:00", "11:00-12:00", "12:00-13:00", "17:00-18:00","18:00-19:00","其他"};
                aaChartModel.dataLabelsEnabled     = false;
                aaChartModel.colorsTheme           = new String[]{"#fe117c","#ffc069","#06caf4","#7dffc0"};
                aaChartModel.xAxisTickInterval=0;
                aaChartModel.polar=Boolean.FALSE;
                aaChartModel.title("就餐时间展示")
                        .subtitle("妈妈叫我吃饭啦！");
                aaChartModel.chartType=AAChartType.Bar;
                aaChartModel.categories(s);
                times=statisticData.EndTimeStatistic(userID);
                Object[]data =new Object[]{0,0,0,0,0,0,0};
                for (Object o : times) {
                    if (o.toString().equals("06")) {
                        data[0] = (int) data[0] + 1;
                        continue;
                    }
                    if (o.toString().equals("07")) {
                        data[1] = (int) data[1] + 1;
                        continue;
                    }
                    if (o.toString().equals("11")) {
                        data[2] = (int) data[2] + 1;
                        continue;
                    }
                    if (o.toString().equals("12")) {
                        data[3] = (int) data[3] + 1;
                        continue;
                    }
                    if (o.toString().equals("17")) {
                        data[4] = (int) data[4] + 1;
                        continue;
                    }
                    if (o.toString().equals("18")) {
                        data[5] = (int) data[5] + 1;
                        continue;
                    }
                    data[6] = (int) data[6] + 1;
                }
                aaChartModel.series(new AASeriesElement[]{
                        new AASeriesElement()
                                .name("时段用餐次数")
                                .data(data)
                });
                aaChartView.aa_drawChartWithChartModel(aaChartModel);
            }
        });

        expense.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AAChartModel aaChartModel = new AAChartModel();
                aaChartView.aa_drawChartWithChartModel(aaChartModel);
                aaChartModel.backgroundColor="#fffff0";
                aaChartModel.colorsTheme = new String[]{"#00ffff","#fe117c","#ffc069","#06caf4","#3CB371","#FFFF00","#FF4500","#9400D3","#FF00FF","#FFB6C1","#00008B","#FFD700"};
                screenPopWindow = new ScreenPopWindow(statis, expenseList);
                //设置多选，因为共用的一个bean，这里调用reset重置下数据
                screenPopWindow.setSingle(true).reset().build();
                screenPopWindow.showAsDropDown(expense);
                screenPopWindow.setOnConfirmClickListener(new ScreenPopWindow.OnConfirmClickListener() {
                    @Override
                    public void onConfirmClick(List<String> list) {
                        if (list.isEmpty()==Boolean.FALSE)
                        {
                            String str=list.get(0);
                            aaChartModel.title("花销展示")
                                    .subtitle("钱包不大，胃口不小");
                            switch (str){
                                case"周": aaChartModel.xAxisTickInterval=0;
                                    aaChartModel.dataLabelsEnabled     = false;
                                    aaChartModel.chartType = AAChartType.Line;
                                    aaChartModel.polar=Boolean.FALSE;
                                    aaChartModel.categories(new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday","Saturday","Sunday"});
                                    aaChartModel.series(new AASeriesElement[]{
                                            new AASeriesElement()
                                                    .name("本周花销")
                                                    .data(statisticData.weekCost(userID))
                                    });
                                    aaChartView.aa_drawChartWithChartModel(aaChartModel);
                                    break;
                                case"月":
                                    //Object data1[]={15,2,3,6,5,4,8,3,9,11};
                                    //Obiect data2[]={22,20,29,24,17,10,7,17,23,8};
                                    //Object data3[]={11,14,15,20,19,8,5,11,16,10,29};
                                    aaChartModel.polar=Boolean.FALSE;
                                    aaChartModel.dataLabelsEnabled     = false;
                                    aaChartModel.xAxisTickInterval=0;
                                    Object[] total =statisticData.monthCost(userID);
                                    for(int i=0;i<31;i++){
                                        Log.d("StatisticTest", "StatisticAc调用得到的total: " + i + ": "+ total[i]);
                                    }
                                    Object[] data1= Arrays.copyOfRange(total,0,10);
                                    for(int i=0;i<10;i++){
                                        Log.d("StatisticTest", "data 1" + i + ": "+ data1[i]);
                                    }
                                    Object[] data2= Arrays.copyOfRange(total,10,20);
                                    for(int i=0;i<10;i++){
                                        Log.d("StatisticTest", "data 2" + i + ": "+ data2[i]);
                                    }

                                    Object[] data3= Arrays.copyOfRange(total,20,31);
                                    for(int i=0;i<11;i++){
                                        Log.d("StatisticTest", "data 3" + i + ": "+ data3[i]);
                                    }

                                    Calendar now = Calendar.getInstance();
                                    String[] Str=new String[11];
                                    for(int i=1;i<=10;i++)
                                    {
                                        String a="+";
                                        String s=String.valueOf(i);
                                        s=a.concat(s);
                                        s=s.concat("日");
                                        Str[i-1]=s;
                                    }
                                    Str[10]="31日";
                                    aaChartModel.chartType = AAChartType.Line;
                                    aaChartModel.categories(Str);
                                    Log.d("StatisticTest", "Calendar value" + now.get(Calendar.DAY_OF_MONTH));
                                    switch (now.get(Calendar.DAY_OF_MONTH)/10){
                                        case 0:
                                            data1=Arrays.copyOfRange(data1,0,now.get(Calendar.DAY_OF_MONTH));
                                            aaChartModel.series(new AASeriesElement[]{
                                                    new AASeriesElement().name("0+").data(data1)});
                                        case 1:
                                            data2=Arrays.copyOfRange(data2,0,now.get(Calendar.DAY_OF_MONTH)%10);
                                            aaChartModel.series(new AASeriesElement[]{
                                                    new AASeriesElement().name("0+").data(data1),
                                                    new AASeriesElement().name("10+").data(data2)});
                                        case 2:
                                            data3=Arrays.copyOfRange(data3,0,now.get(Calendar.DAY_OF_MONTH)%10);
                                            aaChartModel.series(new AASeriesElement[]{
                                                    new AASeriesElement().name("0+").data(data1),
                                                    new AASeriesElement().name("10+").data(data2),
                                                    new AASeriesElement().name("20+").data(data3)});
                                    }
                                    aaChartView.aa_drawChartWithChartModel(aaChartModel);
                                    break;
                                case"年":
                                    aaChartModel.chartType = AAChartType.Column;
                                    aaChartModel.dataLabelsEnabled=Boolean.TRUE;
                                    aaChartModel.polar=Boolean.TRUE;
                                    aaChartModel.categories(new String[]{"1月", "2月", "3月", "4月", "5月","6月","7月", "8月", "9月", "10月", "11月","12月"});
                                    aaChartModel.series(new AASeriesElement[]{
                                            new AASeriesElement()
                                                    .name("年度花销")
                                                    .data(statisticData.yearCost(userID))
                                    });
                                    aaChartView.aa_drawChartWithChartModel(aaChartModel);
                                    break;
                            }
                        }

                    }
                });
            }
        });
        favor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AAChartModel aaChartModel = new AAChartModel();
                aaChartView.aa_drawChartWithChartModel(aaChartModel);
                aaChartModel.backgroundColor="#fffff0";
                aaChartModel.xAxisTickInterval=0;
                aaChartModel.dataLabelsEnabled= false;
                aaChartModel.polar=Boolean.FALSE;
                aaChartModel.colorsTheme=new String[]{"#9400D3"};
                screenPopWindow = new ScreenPopWindow(statis, favorList);
                //设置多选，因为共用的一个bean，这里调用reset重置下数据
                screenPopWindow.setSingle(true).reset().build();
                screenPopWindow.showAsDropDown(favor);
                screenPopWindow.setOnConfirmClickListener(new ScreenPopWindow.OnConfirmClickListener() {
                    @Override
                    public void onConfirmClick(List<String> list) {
                        if (list.isEmpty()==Boolean.FALSE)
                        {   String str=list.get(0);
                            aaChartModel.title("偏好展示")
                                    .subtitle("你问我为什么顽固而彻底");
                            switch (str)
                            {
                                case"口味偏好": {
                                    String[]s=new String[]{"酸","甜","苦","辣","咸","清淡","油炸"};
                                    Object[]data=new Object[7];
                                    for(int i=0;i<7;i++)
                                    {
                                        data[i]=taste_hash.get(s[i]);
                                    }
                                    aaChartModel.chartType = AAChartType.Column;
                                    //aaChartModel.polar=Boolean.TRUE;
                                    aaChartModel.categories(s);
                                    aaChartModel.series(new AASeriesElement[]{
                                            new AASeriesElement()
                                                    .name("品尝次数")
                                                    .data(data)
                                    });
                                    aaChartView.aa_drawChartWithChartModel(aaChartModel);
                                    break;
                                }
                                case"档口偏好": {
                                    String[]s=new String[]{"荔园三食堂-乐记水饺","荔园三食堂-开饭了","荔园三食堂-兰州拉面","荔园三食堂-粤式烧腊"};
                                    Object[]data=new Object[4];
                                    for(int i=0;i<4;i++)
                                    {
                                        data[i]=window_hash.get(s[i]);
                                    }
                                    aaChartModel.chartType = AAChartType.Column;
                                    aaChartModel.categories(s);
                                    aaChartModel.series(new AASeriesElement[]{
                                            new AASeriesElement()
                                                    .name("光顾次数")
                                                    .data(data)});
                                    aaChartView.aa_drawChartWithChartModel(aaChartModel);
                                    break;
                                }
                            }
                        }

                    }
                });
            }
        });
    }
    private void initParam() {
        String[] filter = {"周","月","年"};
        String[] sort = {"口味偏好","档口偏好"};

        FiltrateBean fb1 = new FiltrateBean();
        fb1.setTypeName("查看类型");
        List<FiltrateBean.Children> childrenList0 = new ArrayList<>();
        for (String afilter : filter) {
            FiltrateBean.Children cd = new FiltrateBean.Children();
            cd.setValue(afilter);
            childrenList0.add(cd);
        }
        fb1.setChildren(childrenList0);

        FiltrateBean fb2 = new FiltrateBean();
        fb2.setTypeName("查看类型");
        List<FiltrateBean.Children> childrenList1 = new ArrayList<>();
        for (String asort : sort) {
            FiltrateBean.Children cd = new FiltrateBean.Children();
            cd.setValue(asort);
            childrenList1.add(cd);
        }
        fb2.setChildren(childrenList1);

        expenseList.add(fb1);
        favorList.add(fb2);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}