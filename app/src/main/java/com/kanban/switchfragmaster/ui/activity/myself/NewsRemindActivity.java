package com.kanban.switchfragmaster.ui.activity.myself;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.view.TitleBarView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsRemindActivity extends Activity {

    @BindView(R.id.activity_news_remind_titlebarview)
    TitleBarView titlebarview;
    @BindView(R.id.activity_news_remind_switch_accept_news)
    Switch switchAcceptNews;
    @BindView(R.id.activity_news_remind_ll_news)
    LinearLayout llNews;
    @BindView(R.id.activity_news_remind_switch_voice)
    Switch switchVoice;
    @BindView(R.id.activity_news_remind_ll_voice)
    LinearLayout llVoice;
    @BindView(R.id.activity_news_remind_switc_vibration)
    Switch switcVibration;
    @BindView(R.id.activity_news_remind_ll_vibration)
    LinearLayout llVibration;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_remind);
        ButterKnife.bind(this);
        context = NewsRemindActivity.this;
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
        titlebarview.setLeftText("新消息提醒");
    }

    @OnClick({R.id.activity_news_remind_switch_accept_news, R.id.activity_news_remind_ll_news, R.id.activity_news_remind_switch_voice, R.id.activity_news_remind_ll_voice, R.id.activity_news_remind_ll_vibration})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_news_remind_switch_accept_news:
                break;
            case R.id.activity_news_remind_ll_news:
                break;
            case R.id.activity_news_remind_switch_voice:
                break;
            case R.id.activity_news_remind_ll_voice:
                break;
            case R.id.activity_news_remind_ll_vibration:
                break;
        }
    }
}
