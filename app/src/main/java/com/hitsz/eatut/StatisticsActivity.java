package com.hitsz.eatut;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hitsz.eatut.AAChartCoreLib.AAChartCreator.AAChartModel;
import com.hitsz.eatut.AAChartCoreLib.AAChartCreator.AASeriesElement;
import com.hitsz.eatut.AAChartCoreLib.AAChartEnum.AAChartType;

import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        Button time = (Button) findViewById(R.id.time);
        Button expense = (Button) findViewById(R.id.expense);
        Button favor = (Button) findViewById(R.id.favor);
        Button explore = (Button) findViewById(R.id.explore);
        com.hitsz.eatut.AAChartCoreLib.AAChartCreator.AAChartView aaChartView = findViewById(R.id.AAChartView);
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
                aaChartModel.chartType=AAChartType.Bar;
                aaChartView.aa_drawChartWithChartModel(aaChartModel);
            }
        });
        expense.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                aaChartModel.chartType=AAChartType.Area;
                aaChartView.aa_drawChartWithChartModel(aaChartModel);
            }
        });
        favor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                aaChartModel.chartType=AAChartType.Line;
                aaChartView.aa_drawChartWithChartModel(aaChartModel);
            }
        });
        explore.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                aaChartModel.chartType=AAChartType.Pie;
                aaChartView.aa_drawChartWithChartModel(aaChartModel);
            }
        });
    }
}
