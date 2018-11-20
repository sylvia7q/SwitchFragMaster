package com.kanban.switchfragmaster.ui.activity.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.data.MyApplication;
import com.kanban.switchfragmaster.utils.LogUtil;
import com.kanban.switchfragmaster.utils.SharedPreferencesUtils;
import com.kanban.switchfragmaster.utils.ToastUtil;
import com.kanban.switchfragmaster.view.TitleBarView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SocketIpSettingActivity extends Activity {

    @BindView(R.id.activity_socket_ip_setting_title_bar)
    TitleBarView titleBarView;
    @BindView(R.id.activity_socket_ip_setting_ed_ip_1)
    EditText ed_ip_1;
    @BindView(R.id.activity_socket_ip_setting_ed_ip_2)
    EditText ed_ip_2;
    @BindView(R.id.activity_socket_ip_setting_ed_ip_3)
    EditText ed_ip_3;
    @BindView(R.id.activity_socket_ip_setting_ed_ip_4)
    EditText ed_ip_4;
    @BindView(R.id.activity_socket_ip_setting_ed_port)
    EditText ed_port;
    @BindView(R.id.activity_socket_ip_setting_btn_save)
    Button btn_save;

    private Context context;
    private String sIP1 = "";
    private String sIP2 = "";
    private String sIP3 = "";
    private String sIP4 = "";
    private String sPort = "";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_ip_setting);
        ButterKnife.bind(this);
        context = SocketIpSettingActivity.this;
        checkAll(); //EditText全选(获得焦点时全选文本)
        initTitleView();
        editTextListener(); //输入IP监听
        enterListener(); //PDA Enter键监听
        setListener(); //监听事件
    }

    @OnClick(R.id.activity_socket_ip_setting_btn_save)
    public void onViewClicked() {
    }
    @Override
    protected void onStart() {
        super.onStart();
        initData(); //初始化控件值
    }

    //EditText全选(获得焦点时全选文本)
    private void checkAll() {
        ed_ip_1.setSelectAllOnFocus(true);
        ed_ip_2.setSelectAllOnFocus(true);
        ed_ip_3.setSelectAllOnFocus(true);
        ed_ip_4.setSelectAllOnFocus(true);
        ed_port.setSelectAllOnFocus(true);
    }

    private void initTitleView() {
        titleBarView.setIvBackImage(View.VISIBLE);
        titleBarView.setBackOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleBarView.setLeftText("SOCKET IP设置");
    }

    //监听事件
    private void setListener() {
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ipPortSave(); //IP/端口保存
            }
        });
    }

    //输入IP监听
    private void editTextListener() {
        initData(); //初始化控件值
        TextChangeListen[] mTextWatcher = new TextChangeListen[4];
        EditText[] editTexts_List = new EditText[4];
        editTexts_List[0] = ed_ip_1;
        editTexts_List[1] = ed_ip_2;
        editTexts_List[2] = ed_ip_3;
        editTexts_List[3] = ed_ip_4;

        //循环添加监听事件
        for (int i = 0; i < 4; i++) {
            mTextWatcher[i] = new TextChangeListen(editTexts_List[i]);
            editTexts_List[i].addTextChangedListener(mTextWatcher[i]);
        }
    }

    //初始化控件值
    private void initData() {
        String ip = SharedPreferencesUtils.getValue(getBaseContext(), "sSocketServerIp", "127.0.0.1");
        int sServerPort = SharedPreferencesUtils.getValue(getBaseContext(), "sSocketServerPort", 80);
        String IP[] = ip.split("\\.");
        ed_ip_1.setText(IP[0]);
        ed_ip_2.setText(IP[1]);
        ed_ip_3.setText(IP[2]);
        ed_ip_4.setText(IP[3]);
        ed_port.setText(String.valueOf(sServerPort));
        ed_ip_1.requestFocus();
    }

    //IP录入监听
    private class TextChangeListen implements TextWatcher {

        public EditText IP_Edit;

        public TextChangeListen(EditText IP_Edit) {
            super();
            this.IP_Edit = IP_Edit;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 3) {
                if (Integer.parseInt(s.toString()) <= 255) {
                    if (this.IP_Edit == ed_ip_1) {
                        ed_ip_2.requestFocus();
                    }
                    if (this.IP_Edit == ed_ip_2) {
                        ed_ip_3.requestFocus();
                    }
                    if (this.IP_Edit == ed_ip_3) {
                        ed_ip_4.requestFocus();
                    }
                } else {
                    ToastUtil.showShortToast(context,String.format(getString(R.string.socket_ip_error), s.toString().trim()));
                    this.IP_Edit.setText("0");
                }
            } else if (s.length() == 0) {
                if (this.IP_Edit == ed_ip_1) {
                    ed_ip_1.setText("0");
                }
                if (this.IP_Edit == ed_ip_2) {
                    ed_ip_1.requestFocus();
                    ed_ip_2.setText("0");
                }
                if (this.IP_Edit == ed_ip_3) {
                    ed_ip_2.requestFocus();
                    ed_ip_3.setText("0");
                }
                if (this.IP_Edit == ed_ip_4) {
                    ed_ip_3.requestFocus();
                    ed_ip_4.setText("0");
                }
            }
        }
    }

    //PDA Enter键监听
    public void enterListener() {
        ed_ip_1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (ed_ip_1.hasFocus() && keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    ed_ip_2.requestFocus();
                    LogUtil.i("Topcee", "------>ed_ip_1");
                    return true;
                } else {
                    return false;
                }

            }
        });

        ed_ip_2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (ed_ip_2.hasFocus() && keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    ed_ip_3.requestFocus();
                    LogUtil.i("Topcee", "------>ed_ip_2");
                    return true;
                } else {
                    return false;
                }

            }
        });

        ed_ip_3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (ed_ip_3.hasFocus() && keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    ed_ip_4.requestFocus();
                    LogUtil.i("Topcee", "------>ed_ip_3");
                    return true;
                } else {
                    return false;
                }
            }
        });

        ed_ip_4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (ed_ip_4.hasFocus() && keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    ed_port.requestFocus();
                    LogUtil.i("Topcee", "------>ed_ip_4");
                    return true;
                } else {
                    return false;
                }
            }
        });

        ed_port.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (ed_port.hasFocus() && keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    btn_save.requestFocus();
                    LogUtil.i("Topcee", "------>ed_port");
                    return true;
                } else {
                    return false;
                }
            }
        });

        btn_save.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (btn_save.hasFocus() && keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    ipPortSave(); //IP/端口保存
                    LogUtil.i("Topcee", "------>btn_save");
                    return true;
                } else {
                    return false;
                }
            }
        });

    }

    //IP/端口保存
    private void ipPortSave() {
        sIP1 = ed_ip_1.getText().toString().trim();
        sIP2 = ed_ip_2.getText().toString().trim();
        sIP3 = ed_ip_3.getText().toString().trim();
        sIP4 = ed_ip_4.getText().toString().trim();
        sPort = ed_port.getText().toString().trim();
        String sServerIp = sIP1 + "." + sIP2 + "." + sIP3 + "." + sIP4;
        Integer sServerPort = 0;
        if (TextUtils.isEmpty(sIP1) || TextUtils.isEmpty(sIP2) || TextUtils.isEmpty(sIP3) || TextUtils.isEmpty(sIP4)) {
            ToastUtil.showShortToast(context,getString(R.string.webservice_ip_error_whole));
            return;
        } else if (TextUtils.equals(sServerIp, "0.0.0.0") || TextUtils.equals(sServerIp, "00.00.00.00") || TextUtils.equals(sServerIp, "000.000.000.000") || TextUtils.equals(sServerIp, "255.255.255.255")) {
            ToastUtil.showShortToast(context,String.format(getString(R.string.webservice_ip_error_not_a_valid_address), sServerIp));
            return;
        } else if (TextUtils.isEmpty(sPort)) {
            ToastUtil.showShortToast(context,getString(R.string.webservice_port_error_whole));
            return;
        }
        try {
            sServerPort = Integer.parseInt(sPort);
        } catch (Exception e) {
            ToastUtil.showShortToast(context,getString(R.string.webservice_port_isnum));
            return;
        }
        //保存服务器IP、端口至本地文件中
        SharedPreferencesUtils.saveValue(getBaseContext(), "sSocketServerIp", sServerIp);
        SharedPreferencesUtils.saveValue(getBaseContext(), "sSocketServerPort", sServerPort);
        MyApplication.sSocketPort = sServerPort;
        MyApplication.sSocketIp = sServerIp;
        ToastUtil.showShortToast(context,getString(R.string.webservice_save_success));
        finish();
    }

    //返回
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
