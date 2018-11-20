package com.kanban.switchfragmaster;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;

import com.kanban.switchfragmaster.data.User;
import com.kanban.switchfragmaster.database.DBUtil;
import com.kanban.switchfragmaster.ui.HomeActivity;
import com.kanban.switchfragmaster.ui.activity.main.MainActivity;
import com.kanban.switchfragmaster.utils.SharedPreferencesUtils;


public class WelcomeActivity extends Activity {
    protected static final String TAG = "WelcomeActivity";
    private Context mContext;
    private ImageView mImageView;
    private User user;
    private String userId = "";
    private String passWord = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mContext = WelcomeActivity.this;
        findView();
        init();
    }

    private void findView() {
        mImageView = (ImageView) findViewById(R.id.iv_welcome);
    }

    private void init() {
        DBUtil dbUtil = new DBUtil(mContext);
        user = dbUtil.getLatestUser();
        if (user != null) {
            userId = user.getUserId();
            passWord = user.getPassword();
        }
        mImageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean isFirst = SharedPreferencesUtils.getFirstStart(getBaseContext(), "isFirst");
                if (!isFirst) {
                    SharedPreferencesUtils.saveValue(getBaseContext(), "isFirst", true);
                    Intent intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    if (!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(passWord)) {
                        Intent intent = new Intent(mContext, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (!TextUtils.isEmpty(userId) && TextUtils.isEmpty(passWord)) {
                        Intent intent = new Intent(mContext, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("userId", user.getUserId());
                        startActivity(intent);
                        finish();
                    }else {
                        Intent intent = new Intent(mContext, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        }, 2000);

    }
}
