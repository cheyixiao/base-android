package com.autoforce.common.view.refresh;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import com.autoforce.common.R;
import com.autoforce.common.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.List;

/**
 * Created by xlh on 2019/4/17.
 * description: This abstract class can be inherited by subclass for showing  recyclerView in a variety of status.
 */
public abstract class AutoForceRecyclerView<T extends StatusTypeInterface> extends LinearLayoutCompat implements OnDataLoadCallback<T> {

    protected SmartRefreshLayout mRefreshLayout;
    protected RecyclerView mRecyclerView;
    protected StatusAdapter<T> mAdapter = getAdapter();
    protected IRefreshDataModel mDataModel = generateDataModel(this);

    public AutoForceRecyclerView(Context context) {
        this(context, null);
    }

    public AutoForceRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context);
    }

    private void initLayout(Context context) {

        addChildView(context);
        bindViews();
        initRefreshLayout();
        initRecyclerView();
    }

    private void bindViews() {

        mRefreshLayout = findViewById(R.id.refresh_layout);
        mRecyclerView = findViewById(R.id.recycler_view);
    }

    protected void initRefreshLayout() {

        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        });

        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> {
            //指定为经典Footer，默认是 BallPulseFooter
            ClassicsFooter footer = new ClassicsFooter(context).setDrawableSize(20);
            footer.setFinishDuration(0);
            return footer;
        });

        ClassicsFooter.REFRESH_FOOTER_FINISH = "";

        mRefreshLayout.setOnRefreshListener((it) -> {
            it.setNoMoreData(false);
            if (mDataModel != null) {
                mDataModel.loadData(false);
            }
        });

        mRefreshLayout.setOnLoadMoreListener((it) -> {
            if (mDataModel != null) {
                mDataModel.loadData(true);
            }
        });
    }

    public void loadData() {

        if (mDataModel != null) {
            mDataModel.loadData(false);
        }
    }

    @Override
    public void onDataGot(List<T> data, boolean isLoadMore) {

        if (isLoadMore) {
            mRecyclerView.stopScroll();

            if (data != null && !data.isEmpty()) {
                mRefreshLayout.finishLoadMore();
                mAdapter.appendInfos(data);
            } else {
                mRefreshLayout.finishLoadMoreWithNoMoreData();
            }
        } else {

            if (mRefreshLayout.isRefreshing()) {
                mRefreshLayout.finishRefresh();
            }

            if (data == null || data.isEmpty()) {
                mAdapter.showEmpty();
                mRefreshLayout.setEnableLoadMore(false);
            } else {
                mAdapter.setInfos(data);
            }
        }

    }

    @Override
    public void onDataError(boolean isLoadMore) {

        if (isLoadMore) {
            mRefreshLayout.finishLoadMore();
            ToastUtil.showToast(R.string.load_more_error);
        } else {

            mRefreshLayout.finishRefresh(false);
            List<T> infos = mAdapter.getInfos();

            if (infos.isEmpty()) {
                mAdapter.showNoNetWork();
            }

        }
    }

    private void initRecyclerView() {

        mRecyclerView.setLayoutManager(getLayoutManager());
        mRecyclerView.setAdapter(mAdapter);
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    protected abstract StatusAdapter<T> getAdapter();

    protected abstract IRefreshDataModel generateDataModel(OnDataLoadCallback<T> callback);

    private void addChildView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_recycler, this);
    }
}
