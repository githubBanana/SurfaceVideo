package com.xs.surfacevideo.viewmodel;

import android.databinding.BaseObservable;

import com.xs.surfacevideo.model.BaseModel;

/**
 * @version V1.0 <描述当前版本功能>
 * @author: Xs
 * @date: 2016-08-22 10:54
 * @email Xs.lin@foxmail.com
 */
public class BaseViewModel<T extends BaseModel> extends BaseObservable {

    protected T model;
    public BaseViewModel(T model) {
        setModel(model);
    }

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }
}
