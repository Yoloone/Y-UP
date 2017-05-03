package com.yy.demo.Yup;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.yy.demo.Yup.Entity.Habit;
import com.yy.demo.Yup.Entity.day_record;
import com.yy.demo.Yup.Entity.month_record;
import com.yy.demo.Yup.Entity.week_record;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * Created by Administrator on 2017/4/9.
 */

public class StatisticFrame extends Fragment{
    private Activity context;
    private MyApplication myApplication;
    private List<Habit> habitList;
    private List<String> habitNameList = new ArrayList<>();
    private Spinner habitSpinner, dateSpinner;
    private LineChartView lineChartView;
    private PieChartView pieChartView;
    private LineChartData chartData;
    private Axis axisX, axisY;
    private PieChartData pieChartData;
    private day_record dayRecord;
    private week_record weekRecord;
    private month_record monthRecord;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.statistic_frame,container,false);
        context = this.getActivity();
        myApplication = (MyApplication)context.getApplication();
        habitList = myApplication.getHabitList();
       // Log.i("app",""+ myApplication.getHabitList().size());
        habitSpinner = (Spinner) view.findViewById(R.id.habit_spinner);
        dateSpinner = (Spinner) view.findViewById(R.id.date_spinner);
        TextView tvCountDays = (TextView) view.findViewById(R.id.tv_count_days);

        int countDays = 0;
        for(Habit habit : habitList){
            habitNameList.add(habit.getHabit_name());
            countDays += habit.getLasted_days();
        }
        tvCountDays.setText(countDays+"");
        //线性图表视图
        lineChartView = (LineChartView) view.findViewById(R.id.charts);
        initChartsView();
        generateData(habitList.get(0),0);

        //饼图视图
        pieChartView = (PieChartView) view.findViewById(R.id.pie_charts);
        initPieChartData();

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,habitNameList);
        habitSpinner.setAdapter(spinnerAdapter);
//
        habitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
             //   Toast.makeText(context, habitList.get(i).getHabit_name()+" "+i,Toast.LENGTH_SHORT).show();
                generateData(habitList.get(i),dateSpinner.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
//
        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                generateData(habitList.get(habitSpinner.getSelectedItemPosition()),i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }
//    数据获取并显示
    private void generateData(final Habit habit, int i){
        switch(i){
            case 0:
                BmobQuery<day_record> query = new BmobQuery<>();
                query.addWhereEqualTo("habit",habit);
                query.findObjects(new FindListener<day_record>() {
                    @Override
                    public void done(List<day_record> list, BmobException e) {
                        if(e == null){
                            dayRecord = list.get(0);
                            //Toast.makeText(context,list.get(0).getObjectId(),Toast.LENGTH_SHORT).show();
                            initChartsData(0);
                        }else{
                            Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });
                break;
            case 1:
                BmobQuery<week_record> wquery = new BmobQuery<>();
                wquery.addWhereEqualTo("habit",habit);
                wquery.findObjects(new FindListener<week_record>() {
                    @Override
                    public void done(List<week_record> list, BmobException e) {
                        if(e == null){
                            weekRecord = list.get(0);
                           // Toast.makeText(context,list.get(0).getObjectId(),Toast.LENGTH_SHORT).show();
                            initChartsData(1);
                        }else{
                            Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });
                break;
            case 2:
                BmobQuery<month_record> mquery = new BmobQuery<>();
                mquery.addWhereEqualTo("habit",habit);
                mquery.findObjects(new FindListener<month_record>() {
                    @Override
                    public void done(List<month_record> list, BmobException e) {
                        if(e == null){
                            monthRecord = list.get(0);
                          //  Toast.makeText(context,list.get(0).getObjectId(),Toast.LENGTH_SHORT).show();
                            initChartsData(2);
                        }else{
                            Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });
                break;
        }
//
        }
//    图表配置
    private void initChartsView(){
        lineChartView.setInteractive(true);//可交互
        //        横纵坐标
        axisX = new Axis();
        axisY = new Axis().setHasLines(true);//显示Y轴网格
        //        axisX.setName("日期");
        axisY.setName("打卡数");
        axisX.setLineColor(ChartUtils.COLOR_GREEN);
        axisY.setLineColor(ChartUtils.COLOR_GREEN);
        axisX.setTextColor(ChartUtils.COLOR_GREEN);
        axisY.setTextColor(ChartUtils.COLOR_GREEN);
        axisX.setHasTiltedLabels(true);//x轴文字向左旋转45度
        axisX.setTextSize(10);
    }
//    图表生成

 private void initChartsData(int i) {

            //        坐标刻度值
              //  List<AxisValue> axisValuesX = new ArrayList<>();
                List<AxisValue> axisValuesY = new ArrayList<>();
                List<PointValue> pointValues = new ArrayList<>();

            // 图表点集设置
                switch(i){
                    case 0:
                       setDayPoint(pointValues);
                       break;
                    case 1:
                        setWeekPoint(pointValues);
                        break;
                    case 2:
                        setMonthPoint(pointValues);
                        break;
                }

              //  axisX.setValues(axisValuesX);
            //        axisY.setValues(axisValuesY);
            //        设置折线集
                List<Line> lines = new ArrayList<>();
                Line line = new Line(pointValues);
                line.setColor(ChartUtils.COLOR_ORANGE);
                line.setCubic(false);//设置曲线是否平滑
            //        line.setHasLabelsOnlyForSelected(true);
                line.setHasLabels(true);//是否显示节点数据
                lines.add(line);

            //        设置数据集
                chartData = new LineChartData();
                chartData.setLines(lines);
                chartData.setAxisXBottom(axisX);
                chartData.setAxisYLeft(axisY);
                lineChartView.setLineChartData(chartData);
            }
    private void initPieChartData(){
        pieChartView.setViewportCalculationEnabled(true);//视图自适应
        List<SliceValue> sliceList = new ArrayList<>();
        for(Habit habit : habitList){
            sliceList.add(new SliceValue(habit.getLasted_days(),ChartUtils.pickColor()).setLabel(habit.getHabit_name()));

        }
        pieChartData = new PieChartData();
        pieChartData.setHasLabels(true);
        pieChartData.setValues(sliceList);
        pieChartView.setPieChartData(pieChartData);
    }
    private void setDayPoint(List<PointValue> pointValues){
        List<AxisValue> axisValuesX = new ArrayList<>();
        pointValues.add(new PointValue(0,dayRecord.getOne_ago()));
        pointValues.add(new PointValue(1,dayRecord.getTwo_ago()));
        pointValues.add(new PointValue(2,dayRecord.getThree_ago()));
        pointValues.add(new PointValue(3,dayRecord.getFour_ago()));
        pointValues.add(new PointValue(4,dayRecord.getFive_ago()));
        pointValues.add(new PointValue(5,dayRecord.getSix_ago()));
        pointValues.add(new PointValue(6,dayRecord.getSeven_ago()));
        Date d = new Date();
        SimpleDateFormat df = new SimpleDateFormat("MM-dd");
        for(int i = 6; i >= 0; i--){
            String date = df.format(new Date(d.getTime() - i * 24 * 60 * 60 * 1000));
            axisValuesX.add(new AxisValue(6-i).setLabel(date));
        }

        axisX.setValues(axisValuesX);
    }

    private void setWeekPoint(List<PointValue> pointValues){
        List<AxisValue> axisValuesX = new ArrayList<>();
        pointValues.add(new PointValue(0,weekRecord.getOne_ago()));
        pointValues.add(new PointValue(1,weekRecord.getTwo_ago()));
        pointValues.add(new PointValue(2,weekRecord.getThree_ago()));
        pointValues.add(new PointValue(3,weekRecord.getFour_ago()));
        pointValues.add(new PointValue(4,weekRecord.getFive_ago()));
        pointValues.add(new PointValue(5,weekRecord.getSix_ago()));
        for(int i = 5; i > 0; i--){
            axisValuesX.add(new AxisValue(5-i).setLabel(i + "周前"));
        }
        axisValuesX.add(new AxisValue(5).setLabel("当周"));
        axisX.setValues(axisValuesX);
        }
    private void setMonthPoint(List<PointValue> pointValues){
        List<AxisValue> axisValuesX = new ArrayList<>();
        pointValues.add(new PointValue(0,monthRecord.getOne_ago()));
        pointValues.add(new PointValue(1,monthRecord.getTwo_ago()));
        pointValues.add(new PointValue(2,monthRecord.getThree_ago()));
        pointValues.add(new PointValue(3,monthRecord.getFour_ago()));
        pointValues.add(new PointValue(4,monthRecord.getFive_ago()));
        pointValues.add(new PointValue(5,monthRecord.getSix_ago()));
        pointValues.add(new PointValue(6,monthRecord.getSeven_ago()));
        pointValues.add(new PointValue(7,monthRecord.getEight_ago()));
        pointValues.add(new PointValue(8,monthRecord.getNine_ago()));
        pointValues.add(new PointValue(9,monthRecord.getTen_ago()));
        pointValues.add(new PointValue(10,monthRecord.getEleven_ago()));
        pointValues.add(new PointValue(11,monthRecord.getTwelve_ago()));
        String []month = {"一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"};
        Calendar c = Calendar.getInstance();
        int begin = c.get(Calendar.MONTH);
        for(int i = 0; i < 12; i++){

            axisValuesX.add(new AxisValue(i).setLabel(month[(begin + i + 1)%12]));
        }
        axisX.setValues(axisValuesX);
    }
}
