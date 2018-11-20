package com.kanban.switchfragmaster.ui.activity.report.equipment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;


import com.kanban.switchfragmaster.data.EquipmentInfo;
import com.kanban.switchfragmaster.view.DemoView;

import org.xclcharts.chart.BarData;
import org.xclcharts.chart.StackBarChart;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.common.IFormatterDoubleCallBack;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.info.Legend;
import org.xclcharts.renderer.line.PlotDot;
import org.xclcharts.view.GraphicalView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EquipmentChartView extends DemoView {
    private StackBarChart chart = new StackBarChart();//堆叠图基类
    private List<String> chartLabels = new LinkedList<String>();//标签轴
    private List<BarData> BarDataSet = new LinkedList<BarData>();
    private ArrayList<EquipmentInfo> equipmentList;

    public EquipmentChartView(Context context, ArrayList<EquipmentInfo> equipmentList) {
        super(context);
        this.equipmentList = equipmentList;
        initView();
    }
    /**
     * 初始化图表
     */
    private void initView(){
        chartRender();
        axisLabels();
        chartDataSet();
    }
    /**
     * 设置X轴的标签
     */
    private void axisLabels() {
        if(equipmentList.size()>=1){
            for (int i = 0; i < equipmentList.size(); i++) {
                String time = equipmentList.get(i).getTime();
                String s[] = time.split("~");
                chartLabels.add(s[1]);
            }
        }
    }

    private void chartDataSet(){
        //标签对应的柱形数据集
        List<Double> dataSeriesA= new ArrayList<Double>();
        List<Double> dataSeriesB= new ArrayList<Double>();

        if(equipmentList.size()>=1){
            for (int i = 0; i < equipmentList.size(); i++) {
                String run = equipmentList.get(i).getRunPercent();
                String stop = equipmentList.get(i).getStopPercent();
                dataSeriesA.add(Double.parseDouble(run));
                dataSeriesB.add(Double.parseDouble(stop));
            }
            BarData BarDataA = new BarData("",dataSeriesA, Color.rgb(0, 255, 0));
            BarData BarDataB = new BarData("",dataSeriesB, Color.rgb(255, 0, 0));
            BarDataSet.add(BarDataA);
            BarDataSet.add(BarDataB);
        }
    }
    //获取ArrayList中的最大值
    private double arrayListMax(){
        try{
            double maxDevation = 0.0;
            int totalCount = equipmentList.size();
            if (totalCount >= 1){
                double maxRun = Double.parseDouble(equipmentList.get(0).getRunPercent().toString());
                double maxStop = Double.parseDouble(equipmentList.get(0).getStopPercent().toString());
                for (int i = 0; i < totalCount; i++){
                    double tempRun = Double.parseDouble(equipmentList.get(i).getRunPercent().toString());
                    double tempStop = Double.parseDouble(equipmentList.get(i).getStopPercent().toString());
                    if (tempRun > maxRun) {
                        maxRun = tempRun;
                    }
                    if (tempStop > maxStop) {
                        maxStop = tempStop;
                    }
                }
                maxDevation = maxRun + maxStop;
            }
            return maxDevation;
        } catch (Exception ex) {
            throw ex;
        }
    }

    //向上取整
    private double upToInteger(){
        int num = (int) Math.ceil(arrayListMax());
        int a = 1;
        while(num >= 10){
            num = num / 10;
            a++;
        }
        num = num + 1;
        StringBuffer buffer = new StringBuffer();
        buffer.append(num);
        for (int i = 0; i < a - 1; i++) {
            buffer.append("0");
        }
        double end;
        try {
            end = Double.parseDouble(buffer.toString());
        }catch (Exception e){
            end = arrayListMax();
        }
        return end;
    }
    /**
     * 初始化轴框架
     */
    private void chartRender() {
        try {
            //显示边框
            chart.hideBorder();
            //设置是否在柱形的最上方，显示汇总标签
            chart.setTotalLabelVisible(false);
            //定义柱形上标签显示格式
            chart.getBar().setItemLabelVisible(true);

            //设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
            int [] ltrb = getBarLnDefaultSpadding();
//            chart.setPadding(ltrb[0], ltrb[1], ltrb[2], DensityUtil.dip2px(getContext(), 20));
            chart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);

            chart.setChartDirection(XEnum.Direction.VERTICAL);
            //数据源
            chart.setCategories(chartLabels);
            chart.setDataSource(BarDataSet);

            double max = upToInteger();
            double steps = max / 5;
            chart.getDataAxis().setAxisMax(max);
            chart.getDataAxis().setAxisMin(0);
            chart.getDataAxis().setAxisSteps(steps);
            //指定数据轴标签旋转-45度显示
            chart.getCategoryAxis().setTickLabelRotateAngle(-45f);
            Paint labelPaint = chart.getCategoryAxis().getTickLabelPaint();
            labelPaint.setTextAlign(Paint.Align.CENTER);//X轴圆柱标签的位置
            labelPaint.setColor(Color.rgb(0, 0, 0));
            //轴标题
            chart.getAxisTitle().setRightTitle("单位(%)");

            //背景网格
            chart.getPlotGrid().hideEvenRowBgColor();
            chart.getPlotGrid().hideOddRowBgColor();

            //定义数据轴标签显示格式
            chart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack(){

                @Override
                public String textFormatter(String value) {
                    // TODO Auto-generated method stub

                    DecimalFormat df=new DecimalFormat("#0");
                    Double tmp = Double.parseDouble(value);
                    String label = df.format(tmp).toString();
                    return label;
                }

            });

            //定义标签轴标签显示格式
            /*chart.getCategoryAxis().setLabelFormatter(new IFormatterTextCallBack(){

                @Override
                public String textFormatter(String value) {
                    // TODO Auto-generated method stub
                    String label = "IP-["+value+"]";
                    return label;
                }

            });*/


            chart.setItemLabelFormatter(new IFormatterDoubleCallBack() {
                @Override
                public String doubleFormatter(Double value) {
                    // TODO Auto-generated method stub
                    DecimalFormat df=new DecimalFormat("#0");
                    String label = df.format(value).toString();
                    return label;
                }});
            //显示右边说明
            drawDyLegend();
            //定义柱形上标签显示颜色
            chart.getBar().getItemLabelPaint().setColor(Color.rgb(0, 0, 0));
            chart.getBar().getItemLabelPaint().setTextSize(12);
            chart.getBar().getItemLabelPaint().setTypeface(Typeface.DEFAULT_BOLD);

            //激活点击监听
//            chart.ActiveListenItemClick();
//            chart.showClikedFocus();
//            chart.setPlotPanMode(XEnum.PanMode.HORIZONTAL);
            //设置柱形居中位置,依刻度线居中或依刻度中间点居中
            chart.setBarCenterStyle(XEnum.BarCenterStyle.TICKMARKS);
            chart.disablePanMode();

        } catch (Exception e) {
            // TODO Auto-generated catch block
        }
    }

    /**
     * 显示右边说明
     */
    private void drawDyLegend(){

        Legend dyLegend = chart.getDyLegend();
        if(null == dyLegend) return;
        dyLegend.setPosition(0.7f,0.12f);
        dyLegend.getBackgroundPaint().setColor(Color.WHITE);
        dyLegend.getBackgroundPaint().setAlpha(100);
//        dyLegend.setColSpan(20.f);
        dyLegend.setRowSpan(10.f);
        dyLegend.setMargin(10.f);
        dyLegend.setStyle(XEnum.DyInfoStyle.ROUNDRECT);

        Paint pDyLegend = new Paint(Paint.ANTI_ALIAS_FLAG);
        pDyLegend.setColor(Color.rgb(0, 255,0));
        PlotDot dotDyLegend = new PlotDot();
        dotDyLegend.setDotStyle(XEnum.DotStyle.RECT);
        dyLegend.addLegend(dotDyLegend, "运行", pDyLegend);

        Paint pDyLegend2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        pDyLegend2.setColor(Color.rgb(255, 0, 0));
        PlotDot dotDyLegend2 = new PlotDot();
        dotDyLegend2.setDotStyle(XEnum.DotStyle.RECT);
        dyLegend.addLegend(dotDyLegend2, "停止", pDyLegend2);

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        chart.setChartRange(w,h);//图所占范围大小
    }

    @Override
    public void render(Canvas canvas) {
        try {
            chart.render(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
