package com.kanban.switchfragmaster.ui.fragment;


import android.content.Intent;
import android.text.TextUtils;
import android.view.View;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.data.AppConstants;
import com.kanban.switchfragmaster.ui.activity.board.GrakonLineBoardActivity;
import com.kanban.switchfragmaster.ui.activity.board.SmtLineBoardActivity;
import com.kanban.switchfragmaster.ui.activity.board.WarehouseActivity;
import com.kanban.switchfragmaster.ui.activity.board.WorkshopCharacterActivity;
import com.kanban.switchfragmaster.ui.activity.board.workshop.SmtProductionWorkshopKanbanActivity;
import com.kanban.switchfragmaster.utils.SharedPreferencesUtils;
import com.kanban.switchfragmaster.utils.ToastUtil;

import butterknife.OnClick;

/**
 * 看板
 */
public class FragBoard extends BaseFragment {

    private String sLineNo = "";
    private String sStationNoIn = "";
    private String sStationNameIn = "";
    private String sInterfaceType = "";

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_frag_board;
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
    private boolean checkLineEmpty() {
        sLineNo = SharedPreferencesUtils.getValue(context, "LineNo", "");//获取线别代码
        if (TextUtils.isEmpty(sLineNo)) {
            ToastUtil.showShortToast(context, getString(R.string.set_line_please));
            return false;
        }
        return true;
    }
    private boolean checkStation() {
        sStationNoIn = SharedPreferencesUtils.getValue(context, "StationNoIn", "");//获取工站代码
        sStationNameIn = SharedPreferencesUtils.getValue(context, "StationNameEnIn", "");//获取工站代码

        if (TextUtils.isEmpty(sStationNoIn)) {
            ToastUtil.showShortToast(context, getString(R.string.set_station_please));
            return false;
        }
        if (TextUtils.isEmpty(sStationNameIn)) {
            ToastUtil.showShortToast(context, getString(R.string.set_station_please));
            return false;
        }
        return true;
    }
    @OnClick({R.id.report_rl_smt_line_board, R.id.report_rl_hi_thread_board,
            R.id.report_rl_warehouse_board, R.id.report_rl_thread_board,
            R.id.report_rl_workshop_board,R.id.report_rl_workshop_board2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.report_rl_smt_line_board:
                sInterfaceType = "SMT";
                if (checkLineEmpty()&&checkStation()) {
                    Intent chartIntent = new Intent(context, SmtLineBoardActivity.class);
                    chartIntent.putExtra("sLineNo", sLineNo);
                    chartIntent.putExtra("sStationNoIn", sStationNoIn);
                    chartIntent.putExtra("sInterfaceType", sInterfaceType);
                    chartIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(chartIntent);
                }
                break;
            case R.id.report_rl_hi_thread_board:
                sInterfaceType = "HI";
                if (checkLineEmpty()) {
                    Intent chartIntent = new Intent(context, SmtLineBoardActivity.class);
                    chartIntent.putExtra("sLineNo", sLineNo);
                    chartIntent.putExtra("sInterfaceType", sInterfaceType);
                    chartIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(chartIntent);
                }
                break;
            case R.id.report_rl_warehouse_board:
                sInterfaceType = "WHKB";
                if (checkLineEmpty()) {
                    Intent chartIntent = new Intent(context, WarehouseActivity.class);
                    chartIntent.putExtra("sLineNo", sLineNo);
                    chartIntent.putExtra("sInterfaceType", sInterfaceType);
                    chartIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(chartIntent);
                }
                break;
            case R.id.report_rl_thread_board:
                sInterfaceType = "ASSY";
                if (checkLineEmpty()&&checkStation()) {
                    Intent chartIntent = new Intent(context, GrakonLineBoardActivity.class);
                    chartIntent.putExtra("sLineNo", sLineNo);
                    chartIntent.putExtra("sStationNoIn", sStationNoIn);
                    chartIntent.putExtra("sStationNameIn", sStationNameIn);
                    chartIntent.putExtra("sInterfaceType", sInterfaceType);
                    chartIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(chartIntent);
                }
                break;
            case R.id.report_rl_workshop_board:
                sInterfaceType = "SMT";
//                if (checkLineEmpty()) {
                    Intent workIntent = new Intent(context, SmtProductionWorkshopKanbanActivity.class);
//                    workIntent.putExtra("sInterfaceType", sInterfaceType);
                    workIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(workIntent);
//                }
                break;
            case R.id.report_rl_workshop_board2:
                sInterfaceType = "SMT";
                if (checkLineEmpty()) {
                    Intent chartIntent = new Intent(context, WorkshopCharacterActivity.class);
//                    chartIntent.putExtra("sInterfaceType", sInterfaceType);
                    chartIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(chartIntent);
                }
                break;
        }
    }
}
