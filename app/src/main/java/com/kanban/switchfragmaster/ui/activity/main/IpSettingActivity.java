package com.kanban.switchfragmaster.ui.activity.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.view.TitleBarView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yangt on 2017-12-12.
 * APP-IP设置
 */

public class IpSettingActivity extends Activity {

    @BindView(R.id.activity_ip_setting_titlebarview)
    TitleBarView titlebarview;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_setting);
        ButterKnife.bind(this);
        context = IpSettingActivity.this;
        initTitleBar();

    }
    private void initTitleBar(){
        titlebarview.setIvBackImage(View.VISIBLE);
        titlebarview.setBackOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titlebarview.setLeftText("IP设置");
    }

    @OnClick({R.id.activity_ip_setting_ll_webservice_ip, R.id.activity_ip_setting_ll_socket_ip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_ip_setting_ll_webservice_ip:
                Intent webIntent = new Intent(context, WebserviceIpSettingActivity.class);
                webIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(webIntent);
                break;
            case R.id.activity_ip_setting_ll_socket_ip:
                Intent socketIntent = new Intent(context, SocketIpSettingActivity.class);
                socketIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(socketIntent);
                break;
        }
    }
}
