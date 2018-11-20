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
package com.kanban.switchfragmaster.ui.activity.board.workshop;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;


import com.kanban.switchfragmaster.data.SevenusSmtWorkshopEntity;
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
public class SpinnerBarChart01View extends DemoView {
	
	private String TAG = "SpinnerBarChart01View";
	
	private int mChartStyle = 0;
	private int mOffsetHeight = 0;
	private int mOffsetHeight2 = 0;
	private BarChart mChart = null;
	//标签轴
	private List<String> chartLabels = new LinkedList<String>();
	private List<BarData> chartData = new LinkedList<BarData>();
	private List<SevenusSmtWorkshopEntity> workshopEntities;
	
	public SpinnerBarChart01View(Context context, int chartStyle, List<SevenusSmtWorkshopEntity> workshopEntities) {
		super(context);
		// TODO Auto-generated constructor stub
		
		this.mChartStyle = chartStyle;
		this.workshopEntities = workshopEntities;
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
//			mChart.getDataAxis().setAxisMax(100);
//			mChart.getDataAxis().setAxisMin(0);
//			mChart.getDataAxis().setAxisSteps(20);
			//数据轴最大值
			double max = upToInteger();
			double steps = Math.ceil(max/5);
			//设置数据轴最大值
			mChart.getDataAxis().setAxisMax(max);
			//设置数据轴最小值,默认为0
			mChart.getDataAxis().setAxisMin(0);
			//设置数据轴步长
			mChart.getDataAxis().setAxisSteps(steps);
			
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
			mChart.getCategoryAxis().getTickLabelPaint().setTextSize(15);

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

		for (int i = 0; i < workshopEntities.size(); i++) {
			double qty = Double.parseDouble(workshopEntities.get(i).getsMoPlanQty().toString());
			double finishRate = Double.parseDouble(workshopEntities.get(i).getsMoFinishingRate().toString());
			dataSeriesA.add(qty);
			dataSeriesB.add(finishRate);
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
		if(workshopEntities!=null){
			for (int i = 0; i < workshopEntities.size(); i++) {
				chartLabels.add(workshopEntities.get(i).getsLineNo());
			}
		}
	}
	//获取ArrayList中的最大值
	public double arrayListMax(){
		try{
			double maxDevation = 0.0;
			int totalCount = workshopEntities.size();
			if (totalCount >= 1){
				double maxStand = Double.parseDouble(workshopEntities.get(0).getsMoFinishingRate().toString());
				double maxReally = Double.parseDouble(workshopEntities.get(0).getsMoPlanQty().toString());
				for (int i = 0; i < totalCount; i++){
					double tempStand = Double.parseDouble(workshopEntities.get(i).getsMoFinishingRate().toString());
					double tempReally = Double.parseDouble(workshopEntities.get(i).getsMoPlanQty().toString());
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
