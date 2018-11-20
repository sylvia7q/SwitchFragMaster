package com.kanban.switchfragmaster.ui.activity.myself;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.data.User;
import com.kanban.switchfragmaster.database.DBUtil;
import com.kanban.switchfragmaster.ui.activity.main.MainActivity;
import com.kanban.switchfragmaster.utils.DateUtils;
import com.kanban.switchfragmaster.view.TitleBarView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends Activity {

    @BindView(R.id.tv_news)
    TextView tvNews;
    @BindView(R.id.activity_setting_ll_news)
    LinearLayout ll_news;
    @BindView(R.id.tv_function)
    TextView tvFunction;
    @BindView(R.id.activity_setting_ll_function)
    LinearLayout ll_function;
    @BindView(R.id.tv_exit)
    TextView tvExit;
    @BindView(R.id.activity_setting_ll_exit)
    LinearLayout ll_exit;
    @BindView(R.id.activity_setting_titlebarview)
    TitleBarView titlebarview;
    private Context context;
    private String userId = "";
    private String userName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        context = SettingActivity.this;
        initTitleBar();
        initData();
    }
    private void initTitleBar(){
        titlebarview.setIvBackImage(View.VISIBLE);
        titlebarview.setBackOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titlebarview.setLeftText("设置");
    }

    private void initData(){
        Intent intent = getIntent();
        if(intent != null){
            userId = intent.getStringExtra("userId");
            userName = intent.getStringExtra("userName");
        }
    }

    @OnClick({R.id.activity_setting_ll_news, R.id.activity_setting_ll_function, R.id.activity_setting_ll_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_setting_ll_news:
                Intent newsIntent = new Intent(context, NewsRemindActivity.class);
                newsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(newsIntent);
                break;
            case R.id.activity_setting_ll_function:
                Intent functionIntent = new Intent(context, FuncSettingActivity.class);
                functionIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(functionIntent);
                break;
            case R.id.activity_setting_ll_exit:
                DBUtil dbUtil = new DBUtil(context);
                User updateUser = new User();
                updateUser.setUserId(userId);
                updateUser.setUserName(userName);
                updateUser.setPassword("");
                updateUser.setLoginTime(DateUtils.getDateTime());
                updateUser.setCurAccount("Y");
                dbUtil.saveUser(updateUser);
                Intent exitIntent = new Intent(context, MainActivity.class);
                exitIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(exitIntent);
                break;
        }
    }
}
