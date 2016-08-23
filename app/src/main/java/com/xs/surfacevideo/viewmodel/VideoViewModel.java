package com.xs.surfacevideo.viewmodel;

import android.view.View;

import com.xs.surfacevideo.model.VideoModel;

/**
 * @version V1.0 <描述当前版本功能>
 * @author: Xs
 * @date: 2016-08-22 11:09
 * @email Xs.lin@foxmail.com
 */
public class VideoViewModel extends BaseViewModel<VideoModel>{
    private static final String TAG = "VideoViewModel";


    public String url ;

    public int playVisibility  = View.VISIBLE;
    public int videoVisibility = View.GONE;
    public int bgVisibility    = View.VISIBLE;

    public VideoViewModel(VideoModel model) {
        super(model);
    }

    public String getUrl() {
        return getModel().url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPlayVisibility() {
        return playVisibility;
    }

    public void setPlayVisibility(int playVisibility) {
        this.playVisibility = playVisibility;
    }

    public int getVideoVisibility() {
        return videoVisibility;
    }

    public void setVideoVisibility(int videoVisibility) {
        this.videoVisibility = videoVisibility;
    }

    public int getBgVisibility() {
        return bgVisibility;
    }

    public void setBgVisibility(int bgVisibility) {
        this.bgVisibility = bgVisibility;
    }
}
