package com.xs.surfacevideo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.xs.surfacevideo.R;
import com.xs.surfacevideo.adapter.BaseAdapter;
import com.xs.surfacevideo.adapter.VideoAdapter;
import com.xs.surfacevideo.model.VideoModel;
import com.xs.surfacevideo.viewmodel.VideoViewModel;
import com.xs.videoplay.MediaHelp;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private VideoAdapter mAdapter;
    private boolean isPlaying;
    public static int indexPostion = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.rcv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new VideoAdapter(this, mHandler,new BaseAdapter.OnItemClickListener<VideoViewModel>() {
            @Override
            public void onItemClick(View view, VideoViewModel videoViewModel, int position) {
                isPlaying = true;
                indexPostion = position;
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.initList(getReource());


            mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
//                    Log.e("info", "onScrollStateChanged: "+newState );
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    checkPosition(recyclerView);
                }
            });

    }
    private List<VideoModel> getReource() {
        List<VideoModel> modelList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            VideoModel model = new VideoModel();
            model.setUrl("http://112.253.22.157/17/z/z/y/u/zzyuasjwufnqerzvyxgkuigrkcatxr/hc.yinyuetai.com/D046015255134077DDB3ACA0D7E68D45.flv");
            modelList.add(model);
        }
        return modelList;
    }

    private void checkPosition(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
            int lastItemPosition = linearManager.findLastVisibleItemPosition();
            int firstItemPosition = linearManager.findFirstVisibleItemPosition();
            if ((indexPostion < firstItemPosition || indexPostion > lastItemPosition) && isPlaying) {
                Log.e("info", "checkPosition: "+firstItemPosition+" "+lastItemPosition );
                indexPostion = -1;
                isPlaying = false;
                mAdapter.notifyDataSetChanged();
                MediaHelp.release();
            }
        }
    }

    @Override
    protected void onDestroy() {
        MediaHelp.release();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        MediaHelp.resume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        MediaHelp.pause();
        super.onPause();
    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    isPlaying = false;
                    indexPostion = -1;
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MediaHelp.getInstance().seekTo(data.getIntExtra("position", 0));
    }

}
