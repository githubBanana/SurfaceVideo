package com.xs.surfacevideo.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @version V1.0 <描述当前版本功能>
 * @author: Xs
 * @date: 2016-08-22 10:18
 * @email Xs.lin@foxmail.com
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter {
    private static final String TAG = "BaseAdapter";
    protected List<T>           _list;
    private Object              _lock = new Object();
    protected BaseAdapter() {
        _list = new ArrayList<>();
    }
    protected List<T> getList() {
        return _list;
    }

    public void add(int position,T item) {
        synchronized (_lock) {
            getList().add(position,item);
            notifyItemInserted(position);
        }
    }

    public boolean add(T item) {
        synchronized (_lock) {
            if (getList().add(item)) {
                int position = getItemCount();
                notifyItemInserted(position - 1);
                return true;
            }
            return false;
        }
    }

    public boolean addAll(T... items) {
        synchronized (_lock) {
            int size = getItemCount();
            if (Collections.addAll(getList(),items)) {
                notifyItemRangeInserted(size,items.length);
                return true;
            }
            return false;
        }
    }

    public boolean addAll(Collection<T> items) {
        synchronized (_lock) {
            int size = getItemCount();
            if (getList().addAll(items)) {
                notifyItemRangeInserted(size,items.size());
                return true;
            }
            return false;
        }
    }

    public boolean addAll(int position,Collection<T> items) {
        synchronized (_lock) {
            if (getList().addAll(position,items)) {
                notifyItemRangeInserted(position,items.size());
                return true;
            }
            return false;
        }
    }

    protected T getItem(int position) {
        if (position >= getItemCount()) {
            Log.e(TAG, "getItem: this 'position' out of range!" );
            return null;
        }
        return getList().get(position);
    }

    /**
     * 情况数组
     */
    private void clear() {
        synchronized (_lock) {
            int size = getItemCount();
            getList().clear();
            notifyItemRangeRemoved(0,size);
        }
    }

    public void initList(Collection<T> items) {
        synchronized (_lock) {
//            clear();
            addAll(items);
        }
    }

    protected boolean isEmpty() {
        return getItemCount() == 0;
    }


    @Override
    public int getItemCount() {
        return getList().size();
    }

    /**
     * 监听item点击事件
     * @param <T>
     */
    public interface OnItemClickListener<T> {
        void onItemClick(View view, T t, int position);
    }

}
