package com.hitsz.eatut;

import androidx.appcompat.app.AppCompatActivity;
import com.hitsz.eatut.ui.Search_ui.SearchFragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import com.example.popupwindowlibrary.view.ScreenPopWindow;
import com.hitsz.eatut.AAChartCoreLib.AAChartCreator.AAChartModel;
import com.hitsz.eatut.AAChartCoreLib.AAChartCreator.AASeriesElement;
import com.hitsz.eatut.AAChartCoreLib.AAChartEnum.AAChartType;
import com.example.popupwindowlibrary.bean.FiltrateBean;
import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {
    private ScreenPopWindow screenPopWindow;
    private List<FiltrateBean> expenseList = new ArrayList<>();
    private List<FiltrateBean> favorList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

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
                                .data(new Object[]{7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6}),
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
                            case"周":
                            aaChartModel.chartType = AAChartType.Line;
                            aaChartModel.categories(new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday","Saturday","Sunday"});
                            aaChartView.aa_drawChartWithChartModel(aaChartModel);
                            break;
                            case"月":
                                 aaChartModel.chartType = AAChartType.Line;
                                 aaChartModel.categories(new String[]{"第一周", "第二周", "第三周", "第四周", "第五周"});
                                 aaChartView.aa_drawChartWithChartModel(aaChartModel);
                                 break;
                            case"年":
                                aaChartModel.chartType = AAChartType.Line;
                                aaChartModel.categories(new String[]{"1月", "2月", "3月", "4月", "5月","6月","7月", "8月", "9月", "10月", "11月","12月"});
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
                                aaChartModel.chartType = AAChartType.Line;
                                aaChartModel.categories(new String[]{"酸","甜","苦","辣","咸","清淡","油炸"});
                                aaChartView.aa_drawChartWithChartModel(aaChartModel);
                                break;
                            }
                            case"档口偏好": {
                                aaChartModel.chartType = AAChartType.Line;
                                aaChartModel.categories(new String[]{"酸","甜","苦","辣","咸","清淡"});
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