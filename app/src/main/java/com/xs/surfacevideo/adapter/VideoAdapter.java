package com.xs.surfacevideo.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xs.surfacevideo.R;
import com.xs.surfacevideo.databinding.ItemVideoBinding;
import com.xs.surfacevideo.model.VideoModel;
import com.xs.surfacevideo.ui.ListActivity;
import com.xs.surfacevideo.viewmodel.VideoViewModel;
import com.xs.videoplay.MediaHelp;
import com.xs.videoplay.VideoSuperPlayer;
import com.xs.videoplay.activity.FullVideoActivity;

/**
 * @version V1.0 <描述当前版本功能>
 * @author: Xs
 * @date: 2016-08-22 10:56
 * @email Xs.lin@foxmail.com
 */
public class VideoAdapter extends BaseAdapter<VideoModel> implements View.OnClickListener {
    private static final String TAG = "VideoAdapter";

    private Activity            mAct;
    private OnItemClickListener<VideoViewModel> mListener;
    private  Handler mHandler;
    public VideoAdapter(Activity mAct, Handler handler, OnItemClickListener<VideoViewModel> listener){
        super();
        this.mAct = mAct;
        this.mListener = listener;
        this.mHandler = handler;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mAct).inflate(R.layout.item_video,parent,false);
        return new VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        VideoHolder h = (VideoHolder) holder;
        h.itemView.setTag(h);
        h.setData(getItem(position),position);
        h.itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        VideoHolder holder = (VideoHolder) view.getTag();
        Toast.makeText(mAct,holder.viewModel.getUrl().toString()+"  "+String.valueOf(holder.position),Toast.LENGTH_SHORT).show();
    }

    class VideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ItemVideoBinding    binding ;
        private VideoViewModel      viewModel;
        private int                 position;
        public VideoHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            binding.playBtn.setOnClickListener(this);
            binding.playBtn.setTag(this);
        }

        public void setData(VideoModel model,int position) {
            viewModel = new VideoViewModel(model);
            binding.setVideoViewModel(viewModel);
            this.position = position;
            if (ListActivity.indexPostion == position) {
                viewModel.setVideoVisibility(View.VISIBLE);
            } else {
                viewModel.setVideoVisibility(View.GONE);
                binding.video.close();
            }
        }

        @Override
        public void onClick(View view) {
            VideoHolder holder = (VideoHolder) view.getTag();
            switch (view.getId()) {
                case R.id.play_btn:
                    ListActivity.indexPostion = position;
                    if (mListener != null)
                        mListener.onItemClick(itemView,holder.viewModel,holder.position);
                    Log.e(TAG, "onClick: 666 "+holder.viewModel.getUrl() );
                    binding.icon.setVisibility(View.GONE);
                    binding.video.setVisibility(View.VISIBLE);
                    binding.playBtn.setVisibility(View.GONE);
                    MediaHelp.release();
                    binding.video.loadAndPlay(MediaHelp.getInstance(), holder.viewModel.getUrl(), 0, false);
                    binding.video.setVideoPlayCallback(new MyVideoPlayCallback(holder));
                    notifyDataSetChanged();
                    break;
            }
        }

        class MyVideoPlayCallback implements VideoSuperPlayer.VideoPlayCallbackImpl {

            private VideoHolder _h;
            public MyVideoPlayCallback(VideoHolder holder){
                this._h = holder;
            }
            @Override
            public void onCloseVideo() {
                closeVideo();
            }

            @Override
            public void onSwitchPageType() {
                Log.e(TAG, "onSwitchPageType: " +mAct.getRequestedOrientation());
                if (mAct.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    Log.e(TAG, "onSwitchPageType 666: " );

                    Intent intent = new Intent(new Intent(mAct,
                            FullVideoActivity.class));
                    Intent intent1 = new Intent(mAct,FullVideoActivity.class);
                    intent1.putExtra("url", viewModel.getUrl());
                    intent1.putExtra("position", position);
                    mAct.startActivityForResult(intent1, 1);
                }
            }

            @Override
            public void onPlayFinish() {
                closeVideo();
            }

            private void closeVideo() {
                _h.binding.video.close();
                MediaHelp.release();
                _h.binding.icon.setVisibility(View.VISIBLE);
                _h.binding.playBtn.setVisibility(View.VISIBLE);
                _h.binding.video.setVisibility(View.GONE);
                mHandler.sendEmptyMessage(0);
            }
        }

    }
}
