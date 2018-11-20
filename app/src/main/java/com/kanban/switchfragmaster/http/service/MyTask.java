package com.kanban.switchfragmaster.http.service;

import android.os.AsyncTask;

public class MyTask extends AsyncTask<String,Void,String> {

    private TaskListener taskListener ;

    public MyTask() {
    }

    public MyTask setTaskListener(TaskListener taskListener ){
        this.taskListener = taskListener ;
        return this ;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if ( taskListener != null ){
            taskListener.start();
        }
    }

    @Override
    protected String doInBackground(String[] params) {
        cancle();
        if ( taskListener != null ){
            return taskListener.doInBackground(params);
        }
        return null;
    }

    @Override
    protected void onPostExecute(String params) {
        super.onPostExecute(params);
        cancle();
        if ( taskListener != null ){
            taskListener.result( params );
        }
    }


    public interface TaskListener{
        void start() ;
        String doInBackground(String[] params);
        void result(String json);
    }

    /**
     * 取消一个正在执行的任务
     */
    public void cancle(){
        if ( !isCancelled() ){
            cancel( true ) ;
        }
    }
}
