package com.kanban.switchfragmaster.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.data.AppConstants;
import com.kanban.switchfragmaster.data.MyApplication;
import com.kanban.switchfragmaster.data.User;
import com.kanban.switchfragmaster.database.DBUtil;
import com.kanban.switchfragmaster.ui.activity.myself.AboutActivity;
import com.kanban.switchfragmaster.ui.activity.myself.SettingActivity;
import com.kanban.switchfragmaster.utils.SharedPreferencesUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class FragMyself extends BaseFragment {

    @BindView(R.id.activity_myself_user_name)
    TextView tvUserName;
    @BindView(R.id.activity_myself_web_ip_port)
    TextView tvWebIpPort;
    @BindView(R.id.activity_myself_socket_ip_port)
    TextView tvSocketIpPort;
    private User user;
    private String userId = "";
    private String userName = "";

    @Override
    protected void loadData() {
        setState(AppConstants.STATE_SUCCESS);
        initData();
    }
    @Override
    protected int getLayoutId() {

        return R.layout.fragment_frag_myself;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initInject() {

    }
    @Override
    public void onResume() {
        super.onResume();

    }
    private void initData(){
        DBUtil dbUtil = new DBUtil(context);
        user = dbUtil.getLatestUser();
        if(user != null){
            userId = user.getUserId();
            userName = user.getUserName();
        }
        tvUserName.setText(userName);
        String ip = SharedPreferencesUtils.getValue(context, "sServerIp", "127.0.0.1");
        int sServerPort = SharedPreferencesUtils.getValue(context, "sServerPort", 80);
        tvWebIpPort.setText("WEBSERVICE  " + ip + ":" + sServerPort);
        tvSocketIpPort.setText("SOCKET  " + MyApplication.sSocketIp + ":" + MyApplication.sSocketPort);
    }

    @OnClick({R.id.myself_rl_setting, R.id.myself_rl_about})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.myself_rl_setting:
                Intent setIntent = new Intent(context, SettingActivity.class);
                setIntent.putExtra("userId",userId);
                setIntent.putExtra("userName",userName);
                setIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(setIntent);
                break;
            case R.id.myself_rl_about:
                Intent aboutIntent = new Intent(context, AboutActivity.class);
                aboutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(aboutIntent);
                break;
        }
    }

}
