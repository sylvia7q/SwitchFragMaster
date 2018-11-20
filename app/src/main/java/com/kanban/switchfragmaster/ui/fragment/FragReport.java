package com.kanban.switchfragmaster.ui.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.data.AppConstants;
import com.kanban.switchfragmaster.data.GetLineBoardEntity;
import com.kanban.switchfragmaster.presenter.WebPresenter;
import com.kanban.switchfragmaster.ui.activity.report.EquipmentActivity;
import com.kanban.switchfragmaster.ui.activity.report.ExceptionActivity;
import com.kanban.switchfragmaster.ui.activity.report.FeedMaterialActivity;
import com.kanban.switchfragmaster.ui.activity.report.OnlineWarnActivity;
import com.kanban.switchfragmaster.ui.activity.report.ProductActivity;
import com.kanban.switchfragmaster.utils.SharedPreferencesUtils;
import com.kanban.switchfragmaster.utils.ToastUtil;

import java.util.List;

import butterknife.OnClick;

public class FragReport extends BaseFragment {
    private String sLineNo = "";
    private String sWo = "";

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_frag_report;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initInject() {

    }

    @Override
    protected void loadData() {
        setState(AppConstants.STATE_SUCCESS);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @OnClick({R.id.report_rl_product, R.id.report_rl_equipement, R.id.report_rl_feeder_material
            ,R.id.report_rl_online_warn, R.id.report_rl_exception})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.report_rl_product:
                if (checkLineEmpty()) {
                    Intent chartIntent = new Intent(context, ProductActivity.class);
                    chartIntent.putExtra("sLineNo", sLineNo);
                    chartIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(chartIntent);
                }
                break;
            case R.id.report_rl_equipement:
                if (checkLineEmpty()) {
                    Intent equipmentIntent = new Intent(context, EquipmentActivity.class);
                    equipmentIntent.putExtra("sLineNo", sLineNo);
                    equipmentIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(equipmentIntent);
                }
                break;
            case R.id.report_rl_feeder_material:
                if (checkLineEmpty()) {
                    Intent feedIntent = new Intent(context, FeedMaterialActivity.class);
                    feedIntent.putExtra("sLineNo", sLineNo);
                    feedIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(feedIntent);
                }
                break;
            case R.id.report_rl_online_warn:
                if (checkLineEmpty() && checkWoEmpty()) {
                    Intent warnIntent = new Intent(context, OnlineWarnActivity.class);
                    warnIntent.putExtra("sWo", sWo);
                    warnIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(warnIntent);
                }
                break;
            case R.id.report_rl_exception:
                if (checkLineEmpty() && checkWoEmpty()) {
                    Intent warnIntent = new Intent(context, ExceptionActivity.class);
                    warnIntent.putExtra("sLineNo", sLineNo);
                    warnIntent.putExtra("sWo", sWo);
                    warnIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(warnIntent);
                }
                break;
        }
    }

    private boolean checkLineEmpty() {
        sLineNo = SharedPreferencesUtils.getValue(context, "LineNo", "");//获取线别代码
        if (TextUtils.isEmpty(sLineNo)) {
            ToastUtil.showShortToast(context, getString(R.string.set_line_please));
            return false;
        }
        return true;
    }
    private boolean checkWoEmpty() {
        List<GetLineBoardEntity> list = WebPresenter.getInstance(context).getLineBoardEntities();
        if(list!=null && list.size()>=1){
           sWo = list.get(0).getsWo();//获取制令单代码
            if (TextUtils.isEmpty(sWo)) {
                ToastUtil.showShortToast(context, getString(R.string.wo_is_empty));
                return false;
            }
            return true;
        }else{
            ToastUtil.showShortToast(context, getString(R.string.wo_is_empty));
            return false;
        }
    }
}
