package com.youzan.datashow.service;

import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.axis.AxisLabel;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.*;
import com.github.abel533.echarts.data.PointData;
import com.github.abel533.echarts.feature.MagicType;
import com.github.abel533.echarts.series.Bar;
import com.youzan.datashow.domain.BugReportStatusObject;
import com.youzan.datashow.domain.BugSeverityObject;
import com.youzan.datashow.mapper.BugSeverityMapper;
import com.youzan.datashow.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by adrian on 16/5/17.
 */
@Service
public class BugSeverityService {

    @Autowired
    private BugSeverityMapper bugSeverityMapper;

    List<BugSeverityObject> getList(String startDate, String endDate,String project){
        return bugSeverityMapper.getList(startDate,endDate,project);
    };


    public Option selectBarPicture(String startDate,String endDate,String project)  {
        List<BugSeverityObject> bugSeverityList;

        bugSeverityList=this.getList(startDate,endDate,project);
        List yList=new ArrayList<>();
        List xList=new ArrayList<>();

        for (BugSeverityObject bugSeverity:bugSeverityList){
            yList.add(bugSeverity.getBugNum());
            xList.add(bugSeverity.getBugSeverity());
        }
        Option option = new Option();
        option.legend().x(X.left).data("BUG量");
        option.tooltip().trigger(Trigger.axis);
        option.toolbox().show(true).feature(Tool.dataView, new MagicType(Magic.line, Magic.bar).show(true), Tool.saveAsImage);
        option.calculable(true);
        CategoryAxis xaxis = new CategoryAxis().data(xList.toArray());
        AxisLabel axisLabel = new AxisLabel();
        axisLabel.setInterval(0);
        axisLabel.setRotate(30);
        xaxis.axisLabel(axisLabel);
        option.xAxis(xaxis);
        option.yAxis(new ValueAxis());
        Bar bar = new Bar("BUG量");
        bar.setData(yList);
        bar.itemStyle().normal().color("#1780C7").label().show(true).textStyle().color("#DD002C");
        bar.markPoint().data(new PointData().type(MarkType.max).name("最大值"), new PointData().type(MarkType.min).name("最小值"));
        bar.markLine().data(new PointData().type(MarkType.average).name("平均值"));
        option.series(bar);

        Option optionNull = new Option();

        if(xList.isEmpty()){
            return optionNull;

        }else {
            return option;
        }
    }
}
