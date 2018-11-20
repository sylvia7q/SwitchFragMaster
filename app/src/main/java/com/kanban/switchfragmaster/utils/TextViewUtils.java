package com.kanban.switchfragmaster.utils;

import android.graphics.Color;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.kanban.switchfragmaster.R;


/**
 * Created by LongQ on 2018/1/11.
 */

public class TextViewUtils {

    //设置控件可点击并且获取焦点
    public static void setClickableShow(Button button, boolean isClickable){
        button.setEnabled(isClickable);
        if(isClickable){
            button.setBackgroundResource(R.drawable.selector_btn);
        }else{
            button.setBackgroundResource(R.color.devide_line);
        }
    }
    /**
     * 判断是true 或者 false
     * @param params
     * @return
     */
    public static boolean yes(String params){
        if(params.equals("Y")){
            return true;
        }else if(params.equals("N")){
            return false;
        }
        return false;
    }

    /**
     * 转换数量
     * @param params
     * @return
     */
    public static long getNumber(String params){
        long num = 0;
        if(!TextUtils.isEmpty(params)){
            return Long.parseLong(params);
        }else{
            return num;
        }
    }
}
