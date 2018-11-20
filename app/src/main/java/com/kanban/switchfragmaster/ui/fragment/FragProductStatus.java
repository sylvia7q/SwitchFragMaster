package com.kanban.switchfragmaster.ui.fragment;

import android.text.TextUtils;
import android.widget.TextView;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.data.AppConstants;
import com.kanban.switchfragmaster.data.GetLineBoardEntity;

import java.util.List;

import butterknife.BindView;

public class FragProductStatus extends BaseFragment {

    //制令单-相关数据
    private String sWo = ""; //制令单
    private String sPartNo = "";  //产品号
    private String sPartDesc = ""; //产品描述
    private String sQtyPlan = ""; //计划数量
    private String sBoardCount = ""; //拼板
    private String sSide = ""; //板面
    private String sPlanProcessingDate = ""; //计划开始时间
    private String sPlanFinishedDate = ""; //计划结束时间
    private String sCustomerNo = ""; //客户信息
    private String sNextWo = ""; //下一制令单
    private String sWorkingSchedule = ""; //生产进度
    private String sWorkingSchedulePanel = ""; //板面是正反面
    private String sFinishingRate = ""; //完成率
    private String sCO = ""; //总产时间
    private String sOE = ""; //异常时间
    private String sFTQ = ""; //直通率
    private String nothing = "无";

    private List<GetLineBoardEntity> listLineBoardInfo;
    private boolean flag;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_frag_product_status;
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

}

