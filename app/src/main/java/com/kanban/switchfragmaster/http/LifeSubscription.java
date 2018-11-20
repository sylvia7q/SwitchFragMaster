package com.kanban.switchfragmaster.http;

import rx.Subscription;

/**
 * Created by LQ on 2017/3/21.
 */

public interface LifeSubscription {
    void bindSubscription(Subscription subscription);
}

