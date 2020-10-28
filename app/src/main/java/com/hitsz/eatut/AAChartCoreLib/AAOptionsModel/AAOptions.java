package com.hitsz.eatut.AAChartCoreLib.AAOptionsModel;

public class AAOptions {
    public com.hitsz.eatut.AAChartCoreLib.AAOptionsModel.AAChart chart;
    public com.hitsz.eatut.AAChartCoreLib.AAOptionsModel.AATitle title;
    public com.hitsz.eatut.AAChartCoreLib.AAOptionsModel.AASubtitle subtitle;
    public com.hitsz.eatut.AAChartCoreLib.AAOptionsModel.AAXAxis xAxis;
    public com.hitsz.eatut.AAChartCoreLib.AAOptionsModel.AAYAxis yAxis;
    public com.hitsz.eatut.AAChartCoreLib.AAOptionsModel.AAXAxis[] xAxisArray;
    public com.hitsz.eatut.AAChartCoreLib.AAOptionsModel.AAYAxis[] yAxisArray;
    public com.example.anan.AAChartCore.AAChartCoreLib.AAOptionsModel.AATooltip tooltip;
    public com.hitsz.eatut.AAChartCoreLib.AAOptionsModel.AAPlotOptions plotOptions;
    public Object[] series;
    public com.hitsz.eatut.AAChartCoreLib.AAOptionsModel.AALegend legend;
    public com.example.anan.AAChartCore.AAChartCoreLib.AAOptionsModel.AAPane pane;
    public Object[] colors;
    public com.example.anan.AAChartCore.AAChartCoreLib.AAOptionsModel.AALang defaultOptions;
    public Boolean touchEventEnabled;

    public AAOptions chart(com.hitsz.eatut.AAChartCoreLib.AAOptionsModel.AAChart prop) {
        chart = prop;
        return this;
    }

    public AAOptions title(com.hitsz.eatut.AAChartCoreLib.AAOptionsModel.AATitle prop) {
        title = prop;
        return this;
    }

    public AAOptions subtitle(com.hitsz.eatut.AAChartCoreLib.AAOptionsModel.AASubtitle prop) {
        subtitle = prop;
        return this;
    }

    public AAOptions xAxis(com.hitsz.eatut.AAChartCoreLib.AAOptionsModel.AAXAxis prop) {
        xAxis = prop;
        return this;
    }

    public AAOptions yAxis(com.hitsz.eatut.AAChartCoreLib.AAOptionsModel.AAYAxis prop) {
        yAxis = prop;
        return this;
    }

    public AAOptions xAxisArray(com.hitsz.eatut.AAChartCoreLib.AAOptionsModel.AAXAxis[] prop) {
        xAxisArray = prop;
        return this;
    }

    public AAOptions yAxisArray(com.hitsz.eatut.AAChartCoreLib.AAOptionsModel.AAYAxis[] prop) {
        yAxisArray = prop;
        return this;
    }

    public AAOptions tooltip(com.example.anan.AAChartCore.AAChartCoreLib.AAOptionsModel.AATooltip prop) {
        tooltip = prop;
        return this;
    }

    public AAOptions plotOptions(com.hitsz.eatut.AAChartCoreLib.AAOptionsModel.AAPlotOptions prop) {
        plotOptions = prop;
        return this;
    }

    public AAOptions series(Object[] prop) {
        series = prop;
        return this;
    }

    public AAOptions legend(com.hitsz.eatut.AAChartCoreLib.AAOptionsModel.AALegend prop) {
        legend = prop;
        return this;
    }

    public AAOptions pane(com.example.anan.AAChartCore.AAChartCoreLib.AAOptionsModel.AAPane prop) {
        pane = prop;
        return this;
    }

    public AAOptions colors(Object[] prop) {
        colors = prop;
        return this;
    }

    public AAOptions defaultOptions(com.example.anan.AAChartCore.AAChartCoreLib.AAOptionsModel.AALang prop) {
        defaultOptions = prop;
        return this;
    }

    public AAOptions touchEventEnabled(Boolean prop) {
        touchEventEnabled = prop;
        return this;
    }

}
