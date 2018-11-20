package com.kanban.switchfragmaster.ui.activity.myself;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.ui.activity.main.SocketIpSettingActivity;
import com.kanban.switchfragmaster.ui.activity.main.WebserviceIpSettingActivity;
import com.kanban.switchfragmaster.ui.activity.myself.func.LineSettingActivity;
import com.kanban.switchfragmaster.ui.activity.myself.func.MorelineSettingActivity;
import com.kanban.switchfragmaster.ui.activity.myself.func.StationSettingActivity;
import com.kanban.switchfragmaster.view.TitleBarView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FuncSettingActivity extends Activity {

    @BindView(R.id.activity_func_setting_titlebarview)
    TitleBarView titlebarview;
    @BindView(R.id.activity_func_setting_ll_line)
    LinearLayout ll_line;
    @BindView(R.id.activity_func_more_setting_ll_line)
    LinearLayout ll_more_line;
    @BindView(R.id.activity_func_setting_ll_station)
    LinearLayout ll_station;
    @BindView(R.id.activity_func_setting_ll_terminal)
    LinearLayout ll_terminal;
    @BindView(R.id.activity_func_setting_ll_wo)
    LinearLayout ll_wo;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func_setting);
        ButterKnife.bind(this);
        context = FuncSettingActivity.this;
        initTitleBar();
    }

    private void initTitleBar() {
        titlebarview.setIvBackImage(View.VISIBLE);
        titlebarview.setBackOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titlebarview.setLeftText("功能设置");
    }

    @OnClick({R.id.activity_func_setting_ll_webservice_ip, R.id.activity_func_setting_ll_socket_ip
            , R.id.activity_func_setting_ll_line
            , R.id.activity_func_more_setting_ll_line
            , R.id.activity_func_setting_ll_station
            , R.id.activity_func_setting_ll_terminal
            , R.id.activity_func_setting_ll_wo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_func_setting_ll_webservice_ip:
                Intent webIntent = new Intent(context, WebserviceIpSettingActivity.class);
                webIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(webIntent);
                break;
            case R.id.activity_func_setting_ll_socket_ip:
                Intent socketIntent = new Intent(context, SocketIpSettingActivity.class);
                socketIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(socketIntent);
                break;
            case R.id.activity_func_setting_ll_line:
                Intent lineIntent = new Intent(context, LineSettingActivity.class);
                lineIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(lineIntent);
                break;
            case R.id.activity_func_more_setting_ll_line:
                Intent moreLineIntent = new Intent(context, MorelineSettingActivity.class);
                moreLineIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(moreLineIntent);
                break;
            case R.id.activity_func_setting_ll_station:
                Intent stationIntent = new Intent(context, StationSettingActivity.class);
                stationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(stationIntent);
                break;
            case R.id.activity_func_setting_ll_terminal:
                break;
            case R.id.activity_func_setting_ll_wo:
                break;
        }
    }
}
