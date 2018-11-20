package com.kanban.switchfragmaster.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.kanban.switchfragmaster.R;

import org.xclcharts.common.DensityUtil;


/**
 * Created by YangT on 2017/6/9.
 */

public class TitleBarView extends RelativeLayout {
    private static final String TAG = "TitleBarView";
    private Context mContext;
    private TextView tvLeftTitle;
    private TextView tvCenterTitle;
    private Button btnRight;
    private Button btnBackTitle;
    private View llBackTitleDivide;

    public TitleBarView(Context context) {
        super(context);
        this.mContext=context;
        initView();
    }

    public TitleBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext=context;
        initView();
    }

    public TitleBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext=context;
        initView();
    }
    private void initView(){
        View view = LayoutInflater.from(mContext).inflate(R.layout.common_title_bar, this);
        tvLeftTitle = view.findViewById(R.id.title_tv_left);
        tvCenterTitle = view.findViewById(R.id.title_tv_center);
        btnRight = view.findViewById(R.id.title_btn_right);
        btnBackTitle = view.findViewById(R.id.title_btn_menu_back);
        llBackTitleDivide = view.findViewById(R.id.title_ll_menu_back_divide);
        setIvBackImage(View.GONE);
        btnRight.setVisibility(View.GONE);
    }

    public void setIvBackImage(int visibility){
        btnBackTitle.setVisibility(visibility);
        llBackTitleDivide.setVisibility(visibility);
        if(View.VISIBLE == visibility){
            setBtnLeft(R.drawable.titlebar_left_icon);
        }
    }
    public void setBtnRightClicable(boolean isClicable){
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setEnabled(isClicable);
        if(isClicable){
           setBtnRight(R.drawable.titlebar_save);
        }else {
            setBtnRight(R.mipmap.save_press);
        }
    }
    public void setBtnRightClickable(int visibility){
        btnRight.setVisibility(visibility);
    }
    public void setBackOnclickListener(OnClickListener listener){
        btnBackTitle.setOnClickListener(listener);
    }
    public void setBtnRightOnclickListener(OnClickListener listener){
        btnRight.setOnClickListener(listener);
    }
    public void setLeftText(int txtRes){
        tvLeftTitle.setText(txtRes);
    }
    public void setCenterText(int txtRes){
        tvCenterTitle.setText(txtRes);
    }
    public void setRightText(int txtRes){
        btnRight.setText(txtRes);
    }
    public void setLeftText(String txtRes){
        tvLeftTitle.setText(txtRes);
    }
    public void setCenterText(String txtRes){
        tvCenterTitle.setText(txtRes);
    }
    public void setRightText(String txtRes){
        btnRight.setText(txtRes);
    }
    public void setBtnLeft(int icon){
        Drawable img=mContext.getResources().getDrawable(icon);
        int height= DensityUtil.dip2px(mContext, 30);
        int width=img.getIntrinsicWidth()*height/img.getIntrinsicHeight();
        img.setBounds(0, 0, width, height);
        btnBackTitle.setText("");
        btnBackTitle.setCompoundDrawables(img, null, null, null);
    }

    public void setBtnRight(int icon,int txtRes){

        if (txtRes != 0)
            btnRight.setText(txtRes);
        if (icon == 0) {
            btnRight.setHeight(40);
            btnRight.setWidth(90);
        } else {
            Drawable img = mContext.getResources().getDrawable(icon);
            int height = DensityUtil.dip2px(mContext, 30);
            int width = img.getIntrinsicWidth() * height / img.getIntrinsicHeight();
            img.setBounds(0, 0, width, height);
            btnRight.setCompoundDrawables(img, null, null, null);
        }


    }
    public void setBtnRight(int icon){
        Drawable img = mContext.getResources().getDrawable(icon);
        int height = DensityUtil.dip2px(mContext, 25);
        int width = img.getIntrinsicWidth()*height/img.getIntrinsicHeight();
        img.setBounds(0, 0, width, height);
        btnRight.setCompoundDrawables(img, null, null, null);
    }

}
