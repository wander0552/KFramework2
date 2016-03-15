package cn.wander.ui.test;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.wander.bean.DownloadBean;
import cn.wander.bean.DownloadTask;
import cn.wander.kFramework.MainActivity;
import cn.wander.kFramework.R;
import cn.wander.presenter.TestPresenter;

public class TestActivity extends AppCompatActivity implements ITestView, SwipeRefreshLayout.OnRefreshListener {
    private TestPresenter testPresenter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private TestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        testPresenter = new TestPresenter(this, this);
        initView();
        testPresenter.loadData();

    }


    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "下载界面最小化", Snackbar.LENGTH_LONG)
                        .setAction("go", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(TestActivity.this, MainActivity.class));
                            }
                        }).show();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        refreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        refreshLayout.setOnRefreshListener(this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new TestAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListenre(new TestAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position, DownloadBean downloadBean) {
                Snackbar.make(refreshLayout, downloadBean.getName(), Snackbar.LENGTH_LONG).show();
            }
        });

    }


    @Override
    public void onRefresh() {
        testPresenter.refresh();
    }


    @Override
    public void refreshSuccess(List<DownloadBean> list) {
        adapter.addAll(list);
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void setAdapter(List<DownloadBean> list) {
        adapter.addAll(list);
    }

    @Override
    public void notifyState(DownloadTask task) {
        adapter.notifyState(task);
    }

    @Override
    public void notifyProgress(DownloadTask task) {
        adapter.notifyProgress(task);
    }

    @Override
    public void setTasks(List<DownloadTask> tasks) {
        adapter.setTasks(tasks);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        testPresenter.destroy();
    }
}
