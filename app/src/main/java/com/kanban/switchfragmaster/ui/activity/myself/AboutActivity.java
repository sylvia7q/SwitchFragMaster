package com.kanban.switchfragmaster.ui.activity.myself;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.autoupdate.UpdateManager;
import com.kanban.switchfragmaster.utils.AppUtil;
import com.kanban.switchfragmaster.view.TitleBarView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends Activity {

    @BindView(R.id.activity_about_titlebarview)
    TitleBarView titlebarview;
    @BindView(R.id.activity_about_tv_versoin)
    TextView tvVersoin;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        context = AboutActivity.this;
        initTitleBar();
        initData();
    }

    private void initTitleBar() {
        titlebarview.setIvBackImage(View.VISIBLE);
        titlebarview.setBackOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titlebarview.setLeftText("关于");
    }

    private void initData() {
        tvVersoin.setText(AppUtil.getAppName(context) + "  " + AppUtil.getVersionName(context));
    }

    //检查app是否需要更新
    private void AutoUpdateManager() {
        UpdateManager mUpdateManager = new UpdateManager(this);
        mUpdateManager.checkUpdateInfo(false);//获取APP版本(本地与服务器)比对，是否需要更新
    }

    @OnClick({R.id.activity_about_ll_func_content, R.id.activity_about_ll_check_new_version})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_about_ll_func_content:
                break;
            case R.id.activity_about_ll_check_new_version:
                AutoUpdateManager(); //检查app是否需要更新
                break;
        }
    }
}
