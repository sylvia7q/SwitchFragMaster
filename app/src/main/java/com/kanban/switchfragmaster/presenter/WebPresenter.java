package com.kanban.switchfragmaster.presenter;

import android.content.Context;
import android.os.Handler;


import com.kanban.switchfragmaster.data.GetLineBoardEntity;

import java.util.List;

public class WebPresenter {
    private final static String TAG = WebPresenter.class.getName();
    private static WebPresenter mInitData;
    private Context mContext;
    private List<GetLineBoardEntity> lineBoardEntities;

    public WebPresenter(Context mContext) {
        this.mContext = mContext;
    }

    public static WebPresenter getInstance(Context mContext) {
        if (mInitData == null) {
            mInitData = new WebPresenter(mContext);
        }
        return mInitData;
    }

    private Handler fragProductStatusHandler;

    public Handler getFragProductStatusHandler() {
        return fragProductStatusHandler;
    }

    public void setFragProductStatusHandler(Handler fragProductStatusHandler) {
        this.fragProductStatusHandler = fragProductStatusHandler;
    }

    public List<GetLineBoardEntity> getLineBoardEntities() {
        return lineBoardEntities;
    }

    public void initProductStatusData(List<GetLineBoardEntity> getLineBoardEntities){
        if(getLineBoardEntities !=null){
            lineBoardEntities = getLineBoardEntities;
        }
    }
}
