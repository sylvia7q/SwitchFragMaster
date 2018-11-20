package com.kanban.switchfragmaster.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;


import com.kanban.switchfragmaster.data.FeedersMaterielInfo;

import org.xclcharts.chart.BarChart;
import org.xclcharts.chart.BarData;
import org.xclcharts.chart.CustomLineData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.common.IFormatterDoubleCallBack;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.info.AnchorDataPoint;
import org.xclcharts.renderer.info.Legend;
import org.xclcharts.renderer.line.PlotDot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by LongQ on 2017/8/23.
 */

public class BarChartFeedersView extends DemoView{

    private BarChart chart = new BarChart();
    List<String> chartLabels = new LinkedList<String>();//标签轴
    List<BarData> BarDataSet = new LinkedList<BarData>();
    private List<CustomLineData> mCustomLineDataset = new LinkedList<CustomLineData>();
    private List<FeedersMaterielInfo> feederList;

    public BarChartFeedersView(Context context, List<FeedersMaterielInfo> feederList) {
        super(context);
        this.feederList = feederList;
        initview();
    }
    @Override
    public void render(Canvas canvas) {
        try {
            chart.render(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        chart.setChartRange(w, h);//图所占范围大小
    }

    private void initview() {
//        chartCustomLines();
        axisLabels();
        chartDataSet();
        chartRender();
    }

    /**
     * 设置X轴的标签
     */
    private void axisLabels() {
        if (feederList != null) {
            for (int i = 0; i < feederList.size(); i++) {
                String location = feederList.get(i).getFeeders_location();
                if (!TextUtils.isEmpty(location)) {
                    chartLabels.add(location);
                }
            }
        }
    }

    /**
     * 定制线/分界线
     */
    private void chartCustomLines() {
        CustomLineData line1 = new CustomLineData("及格线", 80d, Color.RED, 3);
        line1.setCustomLineCap(XEnum.DotStyle.PRISMATIC);
        line1.setLabelHorizontalPostion(Paint.Align.LEFT);
        line1.setLabelOffset(15);
        line1.getLineLabelPaint().setColor(Color.RED);
        mCustomLineDataset.add(line1);
    }

    /**
     * 绘制柱形图
     */
    private void chartDataSet() {

        //标签对应的柱形数据集
        List<Double> dataSeriesA = new LinkedList<Double>();
        //依数据值确定对应的柱形颜色.
        List<Integer> dataColorA = new LinkedList<Integer>();

        for (int i = 0; i < feederList.size(); i++) {
            double tempStand = Double.parseDouble(feederList.get(i).getFeeders_rejectRate().toString());
            dataSeriesA.add(tempStand);

//            if (tempStand > 100d) {
                dataColorA.add(Color.RED);
//            } else {
//                dataColorA.add(Color.GREEN);
//            }
        }
        //此地的颜色为Key值颜色及柱形的默认颜色
        BarData BarDataA = new BarData("", dataSeriesA, dataColorA,
                Color.rgb(0, 255, 0));

        BarDataSet.add(BarDataA);
    }


    private void drawLast() {
        //当值与轴最小值相等时，不显示轴
        chart.hideBarEqualAxisMin();

        //批注
        List<AnchorDataPoint> mAnchorSet = new ArrayList<AnchorDataPoint>();
        for (int i = 0; i < 14; i++) {
            AnchorDataPoint an = new AnchorDataPoint(0, i, XEnum.AnchorStyle.CAPRECT);
            an.setBgColor(Color.rgb(255, 145, 126));
            an.setAreaStyle(XEnum.DataAreaStyle.FILL);
//            an.setmCapRectWidth(15.f);
            mAnchorSet.add(an);
        }
        chart.setAnchorDataPoint(mAnchorSet);
        //设置柱形顶部标签在显示时的偏移距离
        chart.getBar().setItemLabelAnchorOffset(12);
    }

    /**
     * 显示右边说明
     */
    private void drawDyLegend() {

        Legend dyLegend = chart.getDyLegend();
        if (null == dyLegend) return;
        dyLegend.setPosition(0.92f, 0.15f);

        dyLegend.getBackgroundPaint().setColor(Color.WHITE);
        dyLegend.getBackgroundPaint().setAlpha(100);
//        dyLegend.setColSpan(20.f);
        dyLegend.setRowSpan(20.f);
        dyLegend.setMargin(15.f);
        dyLegend.setStyle(XEnum.DyInfoStyle.ROUNDRECT);

        Paint pDyLegend = new Paint(Paint.ANTI_ALIAS_FLAG);
        pDyLegend.setColor(Color.rgb(255,0 ,0));
        PlotDot dotDyLegend = new PlotDot();
        dotDyLegend.setDotStyle(XEnum.DotStyle.RECT);
        dyLegend.addLegend(dotDyLegend, "抛料率", pDyLegend);
    }

    //获取ArrayList中的最大值
    public double arrayListMax() {
        try {
            double maxDevation = 0.0;
            int totalCount = feederList.size();
            if (totalCount >= 1) {
                double max = Double.parseDouble(feederList.get(0).getFeeders_rejectRate().toString());
                for (int i = 0; i < totalCount; i++) {
                    double temp = Double.parseDouble(feederList.get(i).getFeeders_rejectRate().toString());
                    if (temp > max) {
                        max = temp;
                    }
                }
                maxDevation = max;
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
    //画轴
    private void chartRender() {
        try {

            //设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
            int[] ltrb = getBarLnDefaultSpadding();
            chart.setPadding(ltrb[0], ltrb[1], ltrb[2], DensityUtil.dip2px(getContext(), 10));

            //数据源
            chart.setDataSource(BarDataSet);
            chart.setCategories(chartLabels);
            chart.setCustomLines(mCustomLineDataset);
            //设置图的显示方向,即横向还是竖向显示柱形
            chart.setChartDirection(XEnum.Direction.VERTICAL);
            //图例
            chart.getAxisTitle().setLeftTitle("单位(%)");
            //显示轴标签
            chart.getDataAxis().showAxisLabels();
            //抛料率最大值
            double max = upToInteger();
            double steps = Math.ceil(max/5);
            //设置数据轴最大值
            chart.getDataAxis().setAxisMax(max);
            //设置数据轴最小值,默认为0
            chart.getDataAxis().setAxisMin(0);
            //设置数据轴步长
            chart.getDataAxis().setAxisSteps(steps);
            //指隔多少个轴刻度(即细刻度)后为主刻度
//            chart.getDataAxis().setDetailModeSteps(5);
            //背景网格
            chart.getPlotGrid().hideEvenRowBgColor();
            //竖向网格线
            chart.getPlotGrid().hideOddRowBgColor();
            //定义数据轴标签显示格式
            chart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack() {

                @Override
                public String textFormatter(String value) {
                    // TODO Auto-generated method stub
                    Double tmp = Double.parseDouble(value);
                    DecimalFormat df = new DecimalFormat("#0");
                    String label = df.format(tmp).toString();
                    return (label);
                }

            });

            //在柱形顶部显示值
            chart.getBar().setItemLabelVisible(true);
            chart.getBar().getItemLabelPaint().setTextSize(12);
            //指定数据轴标签旋转-45度显示
//            chart.getCategoryAxis().setTickLabelRotateAngle(-90f);
            //X轴圆柱标签的位置
            chart.getCategoryAxis().getTickLabelPaint().setTextAlign(Paint.Align.CENTER);
            chart.getCategoryAxis().getTickLabelPaint().setColor(Color.WHITE);
//            chart.getCategoryAxis().getTickLabelPaint().setTextSize(10);
            //设定格式
            chart.setItemLabelFormatter(new IFormatterDoubleCallBack() {
                @Override
                public String doubleFormatter(Double value) {
                    // TODO Auto-generated method stub
                    DecimalFormat df = new DecimalFormat("#0");
                    String label = df.format(value).toString();
                    return label;
                }
            });
            drawDyLegend();
            //隐藏Key
//            chart.getPlotLegend().hide();

            //让柱子间没空白
            chart.getBar().setBarInnerMargin(0.5f); //可尝试0.1或0.5各有啥效果噢

            //禁用平移模式
            chart.disablePanMode();
            //提高性能
            chart.disableHighPrecision();

            //柱形和标签居中方式
            chart.setBarCenterStyle(XEnum.BarCenterStyle.TICKMARKS);

        } catch (Exception e) {
            // TODO Auto-generated catch block
        }
    }

}
