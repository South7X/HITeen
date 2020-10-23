package com.hitsz.eatut;

import androidx.appcompat.app.AppCompatActivity;
import com.hitsz.eatut.ui.Search_ui.SearchFragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
    public Object[] time;
    public int userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        time=statisticData.EndTimeStatistic(userID);
        //System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh"+time[0]);
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
        Button explore = (Button) findViewById(R.id.explore);
        com.hitsz.eatut.AAChartCoreLib.AAChartCreator.AAChartView aaChartView = findViewById(R.id.AAChartView);
        initParam();
        AAChartModel aaChartModel = new AAChartModel()
                .chartType(AAChartType.Column)
                .title("THE HEAT OF PROGRAMMING LANGUAGE")
                .subtitle("Virtual Data")
                .backgroundColor("#fffacd")
                .categories(new String[]{"Java", "Swift", "Python", "Ruby", "PHP", "Go", "C", "C#", "C++"})
                .dataLabelsEnabled(false)
                .yAxisGridLineWidth(0f)
                .series(new AASeriesElement[]{
                        new AASeriesElement()
                                .name("Tokyo")
                                .data(new Object[]{7.0, 6.9, 9.5, 14.5}),
                        new AASeriesElement()
                                .name("NewYork")
                                .data(new Object[]{0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5}),
                        new AASeriesElement()
                                .name("London")
                                .data(new Object[]{0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0}),
                        new AASeriesElement()
                                .name("Berlin")
                                .data(new Object[]{3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8})
                });
        aaChartView.aa_drawChartWithChartModel(aaChartModel);
        time.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                aaChartModel.xAxisTickInterval=0;
                aaChartModel.polar=Boolean.FALSE;
                aaChartModel.title("就餐时间展示")
                        .subtitle("妈妈叫我回家吃饭啦！");
                aaChartModel.chartType=AAChartType.Bar;
                aaChartView.aa_drawChartWithChartModel(aaChartModel);
            }
        });

        expense.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                screenPopWindow = new ScreenPopWindow(statis, expenseList);
                //设置多选，因为共用的一个bean，这里调用reset重置下数据
                screenPopWindow.setSingle(true).reset().build();
                screenPopWindow.showAsDropDown(expense);
                screenPopWindow.setOnConfirmClickListener(new ScreenPopWindow.OnConfirmClickListener() {
                    @Override
                    public void onConfirmClick(List<String> list) {
                        String str=list.get(0);
                        aaChartModel.title("花销展示")
                                .subtitle("钱包不大，胃口不小");
                        switch (str){
                            case"周": aaChartModel.xAxisTickInterval=0;
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
                                // data2[]={22,20,29,24,17,10,7,17,23,8};
                                //Object data3[]={11,14,15,20,19,8,5,11,16,10,29};
                                aaChartModel.polar=Boolean.FALSE;
                                aaChartModel.xAxisTickInterval=0;
                                Object[] total =statisticData.monthCost(userID);
                                Object[] data1= Arrays.copyOfRange(total,0,10);
                                Object[] data2= Arrays.copyOfRange(total,10,20);
                                Object[] data3= Arrays.copyOfRange(total,20,31);
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
                                //aaChartModel.polar=Boolean.TRUE;
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
                });
            }
        });
        favor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                aaChartModel.xAxisTickInterval=0;
                aaChartModel.polar=Boolean.FALSE;
                screenPopWindow = new ScreenPopWindow(statis, favorList);
                //设置多选，因为共用的一个bean，这里调用reset重置下数据
                screenPopWindow.setSingle(true).reset().build();
                screenPopWindow.showAsDropDown(favor);
                screenPopWindow.setOnConfirmClickListener(new ScreenPopWindow.OnConfirmClickListener() {
                    @Override
                    public void onConfirmClick(List<String> list) {
                        String str=list.get(0);
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
                });
            }
        });
        explore.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                aaChartModel.title("最近浏览展示")
                        .subtitle("不买最贵的，只买最对的");
                aaChartModel.chartType=AAChartType.Pie;
                aaChartView.aa_drawChartWithChartModel(aaChartModel);
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
}