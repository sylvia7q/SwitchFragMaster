package com.kanban.switchfragmaster.ui.activity.report.product;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.Log;


import com.kanban.switchfragmaster.data.ProductTableInfo;
import com.kanban.switchfragmaster.view.DemoView;

import org.xclcharts.chart.BarChart;
import org.xclcharts.chart.BarData;
import org.xclcharts.chart.CustomLineData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.common.IFormatterDoubleCallBack;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.info.Legend;
import org.xclcharts.renderer.line.PlotDot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName BarChartProductProgressView
 * @Description  柱形图例子(竖向)
 *
 * @author long
 */

public class ProductChartView extends DemoView implements Runnable { //DemoView

    private String TAG = "ProductChartView";
    private BarChart chart = new BarChart();

    //标签轴
    private List<String> chartLabels = new ArrayList<String>();
    private List<BarData> chartData = new ArrayList<BarData>();
    private ArrayList<ProductTableInfo> productList;

    Paint mPaintToolTip = new Paint(Paint.ANTI_ALIAS_FLAG);
    PlotDot mDotToolTip = new PlotDot();

    private List<CustomLineData> mCustomLineDataset = new ArrayList<CustomLineData>();

    public ProductChartView(Context context, ArrayList<ProductTableInfo> productList) {
        super(context);
        // TODO Auto-generated constructor stub
        this.productList = productList;
        initView();
    }

    private void initView() {
        chartDataSet();
        chartLabels();
        chartRender();
        new Thread(this).start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //图所占范围大小
        chart.setChartRange(w,h); // + w * 0.5f
    }

    private void chartLabels(){

        if(productList!=null){
            for (int i = 0; i < productList.size(); i++) {
                String time = productList.get(i).getTime();
                String s[] = time.split("~");
                chartLabels.add(s[1]);
            }
        }
    }
    private void chartDataSet() {
        //标签对应的柱形数据集
        List<Double> dataSeriesA= new ArrayList<Double>();
        List<Double> dataSeriesB= new ArrayList<Double>();

        if(productList!=null){
            for (int i = 0; i < productList.size(); i++) {
                String standand = productList.get(i).getStandand();
                String really = productList.get(i).getReally();
                dataSeriesA.add(Double.valueOf(standand));
                dataSeriesB.add(Double.valueOf(really));
            }
            BarData BarDataA = new BarData("",dataSeriesA, Color.rgb(186, 20, 26));
            BarData BarDataB = new BarData("",dataSeriesB, Color.rgb(1, 188, 242));
            chartData.add(BarDataA);
            chartData.add(BarDataB);
        }
    }
    //获取ArrayList中的最大值
    public double arrayListMax(){
        try{
            double maxDevation = 0.0;
            int totalCount = productList.size();
            if (totalCount >= 1){
                double maxStand = Double.parseDouble(productList.get(0).getStandand().toString());
                double maxReally = Double.parseDouble(productList.get(0).getStandand().toString());
                for (int i = 0; i < totalCount; i++){
                    double tempStand = Double.parseDouble(productList.get(i).getStandand().toString());
                    double tempReally = Double.parseDouble(productList.get(i).getReally().toString());
                    if (tempStand > maxStand) {
                        maxStand = tempStand;
                    }
                    if (tempReally > maxReally) {
                        maxReally = tempReally;
                    }
                }
                maxDevation = maxStand > maxReally ? maxStand:maxReally;
            }
            return maxDevation;
        } catch (Exception ex) {
            throw ex;
        }
    }

    //获取ArrayList中的最小值
    public double arrayListMin()
    {
        try {
            double mixDevation = 0.0;
            int totalCount = productList.size();
            if (totalCount >= 1) {
                double min = Double.parseDouble(productList.get(0).getStandand().toString());
                for (int i = 0; i < totalCount; i++) {
                    double temp = Double.parseDouble(productList.get(i).getStandand().toString());
                    if (min > temp) {
                        min = temp;
                    }
                }
                mixDevation = min;
            }
            return mixDevation;
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
    private void chartRender() {
        try {
            //设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
            int [] ltrb = getBarLnDefaultSpadding();
            chart.setPadding(ltrb[0], ltrb[1], ltrb[2], DensityUtil.dip2px(getContext(), 10));

            //数据源
            chart.setDataSource(chartData);
            chart.setCategories(chartLabels);

            //轴标题
            chart.getAxisTitle().setRightTitle("单位(PCS/H)");

            double max = upToInteger();
            //数据轴
            chart.getDataAxis().setAxisMax(max);
            chart.getDataAxis().setAxisMin(0);
            double steps = max / 5;
            chart.getDataAxis().setAxisSteps(Math.ceil(steps));

            //指隔多少个轴刻度(即细刻度)后为主刻度
//            chart.getDataAxis().setDetailModeSteps(5);

            //X轴圆柱标签的位置
            chart.getCategoryAxis().getTickLabelPaint().setTextAlign(Align.CENTER);
            chart.getCategoryAxis().getTickLabelPaint().setColor(Color.BLACK);
            chart.getCategoryAxis().setTickLabelRotateAngle(-45f);
            //定义数据轴标签显示格式
            chart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack(){
                @Override
                public String textFormatter(String value) {
                    // TODO Auto-generated method stub
                    Double tmp = Double.parseDouble(value);
                    DecimalFormat df=new DecimalFormat("#0");
                    String label = df.format(tmp).toString();
                    return (label);
                }

            });
            //设定格式
            chart.setItemLabelFormatter(new IFormatterDoubleCallBack() {
                @Override
                public String doubleFormatter(Double value) {
                    // TODO Auto-generated method stub
                    DecimalFormat df=new DecimalFormat("#0");
                    String label = df.format(value).toString();
                    return label;
                }});
            //在柱形顶部显示值
            chart.getBar().setItemLabelVisible(true);
            //让柱子间没空白
            chart.getBar().setBarInnerMargin(0f);

            //颜色
            chart.getAxisTitle().getLeftTitlePaint().setColor(Color.BLACK);
            chart.getAxisTitle().getLowerTitlePaint().setColor(Color.BLACK);
            //柱子上标签的颜色和字的大小
            chart.getBar().getItemLabelPaint().setColor(Color.BLACK);
            chart.getBar().getItemLabelPaint().setTextSize(12);

            //显示十字交叉线
//            chart.showDyLine();
//            DyLine dyl = chart.getDyLine();
//            if( null != dyl)
//            {
//                dyl.setDyLineStyle(XEnum.DyLineStyle.Horizontal);
//                dyl.setLineDrawStyle(XEnum.LineStyle.DASH);
//            }

            //忽略Java的float计算误差，提高性能
            chart.disableHighPrecision();

            //柱形和标签居中方式
//             chart.setBarCenterStyle(XEnum.BarCenterStyle.TICKMARKS);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e(TAG, "chartRender():"+e.toString());
        }
    }

    @Override
    public void render(Canvas canvas) {
        try{
            chart.render(canvas);
        } catch (Exception e){
            Log.e(TAG, e.toString());
        }
    }


    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            chartAnimation();
        }
        catch(Exception e) {
            Thread.currentThread().interrupt();
        }
    }
    //动画效果
    private void chartAnimation() {
        try {
            chart.getDataAxis().hide();
            chart.getPlotLegend().hide();

            int [] ltrb = getBarLnDefaultSpadding();
            for(int i=8; i> 0 ;i--)
            {
                Thread.sleep(100);
                chart.setPadding(ltrb[0],i *  ltrb[1], ltrb[2], ltrb[3]);

                if(1 == i){
                    drawLast();
                    drawDyLegend();
                }
                postInvalidate();
            }

        }
        catch(Exception e) {
            Thread.currentThread().interrupt();
        }
    }

    private void drawLast() {
        //扩展横向显示范围,当数据太多时可用这个扩展实际绘图面积
        //chart.getPlotArea().extWidth(200f);

        //禁用平移模式
        chart.disablePanMode();
        //限制只能左右滑动
        //chart.setPlotPanMode(XEnum.PanMode.HORIZONTAL);

        //禁用双指缩放
        chart.disableScale();
        //显示轴(包含轴线，刻度线和标签)
        chart.getDataAxis().show();

        //当值与轴最小值相等时，不显示轴
        chart.hideBarEqualAxisMin();

        //批注
        /*List<AnchorDataPoint> mAnchorSet = new ArrayList<AnchorDataPoint>();
        for (int i = 0; i < chartData.size(); i++) {

            for (int j = 0; j < productList.size(); j++) {
                if(productList.get(j).getReally().toString().equals("0")){
                    return;
                }
                if(productList.get(j).getStandand().toString().equals("0")){
                    return;
                }
                AnchorDataPoint an = new AnchorDataPoint(i,j,XEnum.AnchorStyle.CAPRECT);
                an.setBgColor(Color.rgb(255, 145, 126));
                an.setTextColor(Color.BLACK);
                an.setAreaStyle(XEnum.DataAreaStyle.FILL);
                an.setmCapRectWidth(12.f);
                mAnchorSet.add(an);
            }
        }
        chart.setAnchorDataPoint(mAnchorSet);*/
        //设置柱形顶部标签在显示时的偏移距离
        chart.getBar().setItemLabelAnchorOffset(5);
        //绘制背景
//		chart.setApplyBackgroundColor(true);
//		chart.setBackgroundColor(XEnum.Direction.VERTICAL,Color.rgb(69, 117, 180),Color.rgb(224, 243, 248));  //Color.rgb(17, 162, 255),Color.rgb(163, 219, 254));//Color.WHITE);
        //设置线的颜色
        chart.getBorder().setBorderLineColor(Color.rgb(0, 255, 0));
        //设置线的粗细
        chart.getBorder().getLinePaint().setStrokeWidth(1);
        //隐藏边框
        chart.hideBorder();

        //绘制分界线
//        CustomLineData line1 = new CustomLineData("分界",80d, Color.rgb(255, 0, 0),3);
//        line1.setCustomLineCap(XEnum.DotStyle.HIDE);
//        line1.setLabelHorizontalPostion(Align.RIGHT);
//        //line1.setLabelOffset(15);
//        line1.getLineLabelPaint().setColor(Color.RED);
//        mCustomLineDataset.add(line1);
//        chart.setCustomLines(mCustomLineDataset);
    }

    //图表右边图文说明
    private void drawDyLegend() {

        Legend dyLegend = chart.getDyLegend();
        if(null == dyLegend) return;
        dyLegend.setPosition(0.92f,0.5f);
        if(chart.getPlotArea().getHeight() > chart.getPlotArea().getWidth()) {
            dyLegend.setPosition(0.6f,0.15f);
        }
        dyLegend.getBackgroundPaint().setColor(Color.WHITE);
        dyLegend.getBackgroundPaint().setAlpha(100);
        dyLegend.setColSpan(10.f);
//        dyLegend.setRowSpan(8.f);
        dyLegend.setMargin(10.f);
        dyLegend.setStyle(XEnum.DyInfoStyle.ROUNDRECT);


        Paint pDyLegend = new Paint(Paint.ANTI_ALIAS_FLAG);
        pDyLegend.setColor(Color.rgb(186, 20, 26));

        PlotDot dotDyLegend = new PlotDot();
        dotDyLegend.setDotStyle(XEnum.DotStyle.RECT);
        dyLegend.addLegend(dotDyLegend, "标准产能", pDyLegend);


        Paint pDyLegend2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        pDyLegend2.setColor(Color.rgb(1, 188, 242));

        PlotDot dotDyLegend2 = new PlotDot();
        dotDyLegend2.setDotStyle(XEnum.DotStyle.RECT);
        dyLegend.addLegend(dotDyLegend, "实际产能", pDyLegend2);
    }
}

