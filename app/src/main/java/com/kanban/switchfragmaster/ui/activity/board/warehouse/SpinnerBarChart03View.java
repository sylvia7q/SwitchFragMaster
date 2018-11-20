/**
 * Copyright 2014  XCL-Charts
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 	
 * @Project XCL-Charts 
 * @Description Android图表基类库
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @Copyright Copyright (c) 2014 XCL-Charts (www.xclcharts.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 1.0
 */
package com.kanban.switchfragmaster.ui.activity.board.warehouse;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;


import com.kanban.switchfragmaster.data.WoInfoWhEntity;
import com.kanban.switchfragmaster.view.DemoView;

import org.xclcharts.chart.BarChart;
import org.xclcharts.chart.BarData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.common.IFormatterDoubleCallBack;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.renderer.XEnum;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName SpinnerBarChart01View
 * @Description  柱形图同数据源不同柱形图切换的例子
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 */
public class SpinnerBarChart03View extends DemoView {

	private String TAG = "SpinnerBarChart01View";

	private int mChartStyle = 0;
	private int mOffsetHeight = 0;
	private int mOffsetHeight2 = 0;
	private BarChart mChart = null;
	//标签轴
	private List<String> chartLabels = new LinkedList<String>();
	private List<BarData> chartData = new LinkedList<BarData>();
	private List<WoInfoWhEntity> listWoInfo;

	public SpinnerBarChart03View(Context context, int chartStyle, List<WoInfoWhEntity> listWoInfo) {
		super(context);
		// TODO Auto-generated constructor stub
		
		this.mChartStyle = chartStyle;
		this.listWoInfo = listWoInfo;
		chartLabels();
		chartDataSet();
		chartRender();
	}
		
	private void initChart(int chartStyle)
	{
		switch(chartStyle)
		{
		case 0: //竖向柱形图
			mChart = new BarChart();
			//图例
			mChart.getAxisTitle().setLeftTitle("");
			break;
		case 1:	//横向柱形图
			mChart = new BarChart();
			mChart.setChartDirection(XEnum.Direction.HORIZONTAL);
			break;
		}
		
		
	}
	
	@Override  
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {  
        super.onSizeChanged(w, h, oldw, oldh);  
       //图所占范围大小
        //mChart.setChartRange(w,h);
    }

	public void chartRender()
	{
		try {
			
			initChart(mChartStyle);
			
			//设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....		
			int [] ltrb = getBarLnDefaultSpadding();
			mChart.setPadding(DensityUtil.dip2px(getContext(), 50),DensityUtil.dip2px(getContext(), 20), ltrb[2],  ltrb[3]);
 				
			
			//数据源
			mChart.setDataSource(chartData);
			mChart.setCategories(chartLabels);

			//数据轴
			mChart.getDataAxis().setAxisMax(100);
			mChart.getDataAxis().setAxisMin(0);
			mChart.getDataAxis().setAxisSteps(20);
			//数据轴最大值
//			double max = upToInteger();
//			double steps = Math.ceil(max/5);
//			//设置数据轴最大值
//			mChart.getDataAxis().setAxisMax(max);
//			//设置数据轴最小值,默认为0
//			mChart.getDataAxis().setAxisMin(0);
//			//设置数据轴步长
//			mChart.getDataAxis().setAxisSteps(steps);
			
			//定义数据轴标签显示格式
			mChart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack(){

				@Override
				public String textFormatter(String value) {
					// TODO Auto-generated method stub
					Double tmp = Double.parseDouble(value);
					DecimalFormat df = new DecimalFormat("#0");
					String label = df.format(tmp).toString();
					return label;
				}

			});
			//定义柱形上标签显示格式
			mChart.getBar().setItemLabelVisible(true);
			mChart.getBar().getItemLabelPaint().setColor(Color.rgb(72, 61, 139)); 
			mChart.getBar().getItemLabelPaint().setFakeBoldText(true);

			mChart.setItemLabelFormatter(new IFormatterDoubleCallBack() {
				@Override
				public String doubleFormatter(Double value) {
					// TODO Auto-generated method stub
					DecimalFormat df=new DecimalFormat("#0");
					String label = df.format(value).toString();
					return label;
				}});

			mChart.getCategoryAxis().setTickLabelRotateAngle(-45f);
			mChart.getCategoryAxis().getTickLabelPaint().setTextSize(10);

			mChart.DeactiveListenItemClick();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		}
	}
	private void chartDataSet()
	{
		//标签对应的柱形数据集
		List<Double> dataSeriesA= new LinkedList<Double>();
		List<Double> dataSeriesB= new LinkedList<Double>();
		double qty = 0.0;
		double woPlanQty = 0.0;
		for (int i = 0; i < listWoInfo.size(); i++) {
			String sQty = listWoInfo.get(i).getQtyTotal().toString();
			String sWoPlanQty = listWoInfo.get(i).getWoPlanQty().toString();
			Double dlAchievingRate = 0.0;
			if(TextUtils.isEmpty(sQty) || TextUtils.isEmpty(sWoPlanQty)){
				dlAchievingRate = 0.0;
			}else {
				qty = Double.parseDouble(sQty);
				woPlanQty = Double.parseDouble(sWoPlanQty);
				dlAchievingRate = (qty/woPlanQty)*100;
			}
			dataSeriesA.add(dlAchievingRate);
		}
//		dataSeriesA.add(50d);
//		dataSeriesA.add(25d);
//		dataSeriesA.add(20d);
		BarData BarDataA = new BarData("制令单数量",dataSeriesA,Color.rgb(73, 135, 218));
				

//		dataSeriesB.add(35d);
//		dataSeriesB.add(65d);
//		dataSeriesB.add(75d);
		BarData BarDataB = new BarData("工单完成率",dataSeriesB,Color.rgb(224, 4, 0));
		
//		List<Double> dataSeriesC= new LinkedList<Double>();
//		dataSeriesC.add(15d);
//		dataSeriesC.add(10d);
//		dataSeriesC.add(5d);
//		BarData BarDataC = new BarData("Bing",dataSeriesC,Color.rgb(255, 185, 0));
		
		chartData.add(BarDataA);
		chartData.add(BarDataB);
//		chartData.add(BarDataC);
	}
	
	private void chartLabels()
	{
//		chartLabels.add("路人甲");
//		chartLabels.add("路人乙");
//		chartLabels.add("路人丙");
		if(listWoInfo!=null){
			for (int i = 0; i < listWoInfo.size(); i++) {
				chartLabels.add(listWoInfo.get(i).getLineNo());
			}
		}
	}
	//获取ArrayList中的最大值
	public double arrayListMax(){
		try{
			double maxDevation = 0.0;
			int totalCount = listWoInfo.size();
			if (totalCount >= 1){
				double qty = Double.parseDouble(listWoInfo.get(0).getQtyTotal().toString());
				double woPlanQty = Double.parseDouble(listWoInfo.get(0).getWoPlanQty().toString());
				Double maxRate = (qty/woPlanQty)*100;
				for (int i = 0; i < totalCount; i++){
					double tempQty = Double.parseDouble(listWoInfo.get(i).getQtyTotal().toString());
					double tempWoPlanQty = Double.parseDouble(listWoInfo.get(i).getWoPlanQty().toString());
					Double tempRate = (tempQty/tempWoPlanQty)*100;
					if (tempRate > maxRate) {
						maxRate = tempRate;
					}
				}
				maxDevation = maxRate;
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
	@Override
    public void render(Canvas canvas) {
        try{
        	
//        	mChart.setChartRange(0.0f, mOffsetHeight, this.getWidth(),this.getHeight() - mOffsetHeight);
        	mChart.setChartRange(this.getMeasuredWidth(), this.getMeasuredHeight());
        	mChart.render(canvas);
        } catch (Exception e){
        	Log.e(TAG, e.toString());
        }
    }
}
