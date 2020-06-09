package com.common.paylibrary.listener;

import com.common.paylibrary.model.PayExtModel;

public interface PayCallback {
    void payResult(PayExtModel extModel);
}