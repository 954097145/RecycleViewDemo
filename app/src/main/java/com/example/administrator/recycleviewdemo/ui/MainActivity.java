package com.example.administrator.recycleviewdemo.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.administrator.recycleviewdemo.R;
import com.example.administrator.recycleviewdemo.adapter.NetEaseAdapter;
import com.example.administrator.recycleviewdemo.biz.Xhttp;
import com.example.administrator.recycleviewdemo.entity.NetEase;
import com.example.administrator.recycleviewdemo.view.RecycleViewDivider;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;


import java.util.List;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView mRecyclerView1;
    //1.ButterKnife ,2,网络,Volley,XUtil3,Retrofit,3,RecyclerView
    String url = "http://c.m.163.com/nc/article/list/T1348647909107/0-20.html";
    // String url = "http://c.m.163.com/nc/article/list/T1370583240249/0-20.html";
    NetEaseAdapter mNetEaseAdapter;
    @BindView(R.id.swipe1)
    SwipeRefreshLayout mSwipe1;
    Handler mHandler;
    private String tid = "T1348647909107";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mHandler = new Handler();
        mSwipe1.setOnRefreshListener(this);
        mRecyclerView1.addOnScrollListener(lis);
        Xhttp.getNewsList(url, tid, listener);
        // Xhttp.getNewsList(url, "T1370583240249", listener);
    }

    private RecyclerView.OnScrollListener lis = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (!mSwipe1.isRefreshing()) {
                int lastItemPosition = layoutManager.findLastVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastItemPosition == mNetEaseAdapter.getItemCount() - 1) {
                    //加载数据
                    mNetEaseAdapter.setCurrentState(NetEaseAdapter.FOOTER_PULLING);

                    //获取新数据，url
                    Xhttp.getNewsList(url, tid, new Xhttp.OnSuccessListener() {
                        @Override
                        public void setNewsList(List<NetEase> neteaseNews) {
                            mNetEaseAdapter.addDataList(neteaseNews);
                            mNetEaseAdapter.notifyDataSetChanged();
                            if (neteaseNews.size() == 0) {
                                mNetEaseAdapter.setCurrentState(NetEaseAdapter.FOOTER_PULL_NO_DATA);
                            } else {
                                mNetEaseAdapter.setCurrentState(NetEaseAdapter.FOOTER_PULL_FINISHED);
                            }
                        }
                    });
                }
            }

        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };
    private LinearLayoutManager layoutManager;
    private Xhttp.OnSuccessListener listener = new Xhttp.OnSuccessListener() {
        @Override
        public void setNewsList(List<NetEase> neteaseNews) {
            //适配器的工作：将集合添加到适配器
            mNetEaseAdapter = new NetEaseAdapter(neteaseNews);
            mRecyclerView1.setAdapter(mNetEaseAdapter);
            //必须要设置一个布局管理器 //listview,gridview,瀑布流
            layoutManager = new LinearLayoutManager(MainActivity.this);
            mRecyclerView1.setLayoutManager(layoutManager);
            //   mRecyclerView1.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
            // mRecyclerView1.setLayoutManager(new GridLayoutManager(MainActivity.this, 2, GridLayoutManager.HORIZONTAL, false));
            //   mRecyclerView1.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            //分割线
            mRecyclerView1.addItemDecoration(new RecycleViewDivider(MainActivity.this, LinearLayoutManager.HORIZONTAL));

        }
    };

    @Override
    public void onRefresh() {
        //新数据的获取，加到列表的顶端
        Runnable runable = new TimerTask() {
            @Override
            public void run() {
                NetEase netEase = mNetEaseAdapter.getDataList().get(1);
                mNetEaseAdapter.addData(1, netEase);
                mNetEaseAdapter.notifyItemInserted(1);
                Toast.makeText(MainActivity.this, "加载数据完毕", Toast.LENGTH_SHORT).show();
                mSwipe1.setRefreshing(false);

            }
        };
        mHandler.postDelayed(runable, 2000);
    }
}
