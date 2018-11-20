package com.kanban.switchfragmaster.utils;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kanban.switchfragmaster.R;


/**
 * [弹出框]
 * Created by YANGT on 2017/11/21.
 */

public class DialogUtil {

    public static Dialog showStatusDialog(Context ctx, String title, String message){
        final Dialog dialog = new Dialog(ctx, R.style.lb_myDialog);
        dialog.setCancelable(false);
        LinearLayout layout = (LinearLayout) (LayoutInflater.from(ctx).inflate(R.layout.lb_dialog_reminder, null));
        TextView titleTextView = layout.findViewById(R.id.dialog_reminder_title);//标题
        TextView message1 = layout.findViewById(R.id.dialog_reminder_message);//消息
        Button confirm = layout.findViewById(R.id.dialog_reminder_btn_confirm);
        Button cancel = layout.findViewById(R.id.dialog_reminder_btn_cancel);
        cancel.setVisibility(View.GONE);
        dialog.setContentView(layout);
        titleTextView.setText(title);//标题
        message1.setText(message);//消息
        if(title.equalsIgnoreCase("OK")){
            titleTextView.setBackgroundResource(R.color.lb_blue);
            titleTextView.setTextColor(ctx.getResources().getColor(R.color.lb_whites));
            message1.setTextColor(ctx.getResources().getColor(R.color.lb_blue));
            confirm.setBackgroundResource(R.drawable.selector_btn);
            cancel.setBackgroundResource(R.drawable.selector_btn);
        }else if(title.equalsIgnoreCase("NG")||title.equalsIgnoreCase("ERROR")){
            titleTextView.setBackgroundResource(R.color.lb_red);
            titleTextView.setTextColor(ctx.getResources().getColor(R.color.lb_whites));
            message1.setTextColor(ctx.getResources().getColor(R.color.lb_red));
            confirm.setBackgroundResource(R.drawable.lb_selector_btn_error);
            cancel.setBackgroundResource(R.drawable.lb_selector_btn_error);
        }else {
            titleTextView.setBackgroundResource(R.color.lb_btn_warn);
            titleTextView.setTextColor(ctx.getResources().getColor(R.color.lb_whites));
            message1.setTextColor(ctx.getResources().getColor(R.color.lb_btn_warn));
            confirm.setBackgroundResource(R.drawable.lb_selector_btn_warn);
            cancel.setBackgroundResource(R.drawable.lb_selector_btn_warn);
        }
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        setDialogWidthHeight(ctx,dialog,0.9f);
        return dialog;
    }
    /**
     * 设置对话框的宽和高
     */
    public static void setDialogWidthHeight(Context context,Dialog sDialog, float f) {
        WindowManager.LayoutParams lp = sDialog.getWindow().getAttributes(); //获取对话框当前的参数值
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * f); // 宽度设置为屏幕的0.6
//        lp.height = (int) (d.heightPixels * 1);
        sDialog.getWindow().setAttributes(lp); //设置生效
    }
    private static Button confirm;
    private static Button cancel;

    public static void setConfirmOnclickListener(View.OnClickListener listener){
        confirm.setOnClickListener(listener);
    }

    public static void setCancelOnclickListener(View.OnClickListener listener){
        cancel.setOnClickListener(listener);
    }
    public static Dialog showConfirmDialog(Context ctx, String title, String message){
        final Dialog dialog = new Dialog(ctx,R.style.lb_myDialog);
        dialog.setCancelable(false);
        LinearLayout layout = (LinearLayout) (LayoutInflater.from(ctx).inflate(R.layout.lb_dialog_reminder, null));
        TextView titleTextView = layout.findViewById(R.id.dialog_reminder_title);//标题
        TextView message1 = layout.findViewById(R.id.dialog_reminder_message);//消息
        confirm = layout.findViewById(R.id.dialog_reminder_btn_confirm);
        cancel = layout.findViewById(R.id.dialog_reminder_btn_cancel);
        dialog.setContentView(layout);
        titleTextView.setText(title);//标题
        message1.setText(message);//消息
        titleTextView.setBackgroundResource(R.color.lb_blue);
        titleTextView.setTextColor(ctx.getResources().getColor(R.color.lb_whites));
        message1.setTextColor(ctx.getResources().getColor(R.color.lb_blue));
        confirm.setBackgroundResource(R.drawable.selector_btn);
        cancel.setBackgroundResource(R.drawable.selector_btn);
        dialog.show();
        setDialogWidthHeight(ctx,dialog,0.9f);
        return dialog;
    }

}
