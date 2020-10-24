package com.hitsz.eatut.AAChartCoreLib.AAOptionsModel;

import java.util.Map;

public class AASeries {
    public Float borderRadius;
    public com.example.anan.AAChartCore.AAChartCoreLib.AAOptionsModel.AAMarker marker;
    public String stacking;
    public com.example.anan.AAChartCore.AAChartCoreLib.AAOptionsModel.AAAnimation animation;
    public String[] keys;
    public Boolean colorByPoint;//决定了图表是否给每个数据列或每个点分配一个颜色，默认值是 false， 即默认是给每个数据类分配颜色，
    public Boolean connectNulls;//设置折线是否断点重连
    public Map events;
    public com.example.anan.AAChartCore.AAChartCoreLib.AAOptionsModel.AAShadow shadow;
    public com.hitsz.eatut.AAChartCoreLib.AAOptionsModel.AADataLabels dataLabels;

    public AASeries borderRadius(Float prop) {
        borderRadius = prop;
        return this;
    }

    public AASeries marker(com.example.anan.AAChartCore.AAChartCoreLib.AAOptionsModel.AAMarker prop) {
        marker = prop;
        return this;
    }

    public AASeries stacking(String prop) {
        stacking = prop;
        return this;
    }

    public AASeries animation(com.example.anan.AAChartCore.AAChartCoreLib.AAOptionsModel.AAAnimation prop) {
        animation = prop;
        return this;
    }

    public AASeries keys(String[] prop) {
        keys = prop;
        return this;
    }

    public AASeries colorByPoint(Boolean prop) {
        colorByPoint = prop;
        return this;
    }

    public AASeries connectNulls(Boolean prop) {
        connectNulls = prop;
        return this;
    }

    public AASeries events(Map prop) {
        events = prop;
        return this;
    }

    public AASeries shadow(com.example.anan.AAChartCore.AAChartCoreLib.AAOptionsModel.AAShadow prop) {
        shadow = prop;
        return this;
    }

    public AASeries dataLabels(com.hitsz.eatut.AAChartCoreLib.AAOptionsModel.AADataLabels prop) {
        dataLabels = prop;
        return this;
    }

}
