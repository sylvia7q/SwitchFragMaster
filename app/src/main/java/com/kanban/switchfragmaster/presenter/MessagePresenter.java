package com.kanban.switchfragmaster.presenter;


import com.kanban.switchfragmaster.data.MessageInfo;

public interface MessagePresenter {

    interface View extends BaseView<MessageInfo>{
    }

    interface Presenter{
        void fetchTopNewsList(int id);
    }
    interface ViewActivity extends View {
        void refreshActivityView(MessageInfo info);
    }
}
