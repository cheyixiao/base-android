package com.autoforce.common.view.refresh;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.autoforce.common.R;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xialihao on 2018/12/8.
 * description: An RecyclerView's adapter for showing  different status.
 */
public abstract class StatusAdapter<T extends StatusTypeInterface> extends RecyclerView.Adapter<BaseHolder<T>> {

    private static final int NO_NETWORK = -1;
    private static final int NO_DATA = -2;
    private static final int TOP_MARGIN_DEFAULT = 90;
    private static final int TOP_MARGIN_CENTER = -1;

    private int imageTopMargin = TOP_MARGIN_DEFAULT;

    private List<T> mInfos = new ArrayList<>();
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    /**
     * 创建 {@link BaseHolder}
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public BaseHolder<T> onCreateViewHolder(ViewGroup parent, final int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == NO_NETWORK) {
            View itemView = inflater.inflate(getNoNetworkLayoutId(), parent, false);
            return new NoNetworkHolder(itemView);
        } else if (viewType == NO_DATA) {
            View itemView = inflater.inflate(getNoDataLayoutId(), parent, false);
            return new EmptyHolder(itemView);
        }

        View view = inflater.inflate(getLayoutId(viewType), parent, false);

        BaseHolder<T> mHolder = getHolder(view, viewType);
        mHolder.setOnItemClickListener(new BaseHolder.OnViewClickListener() {//设置Item点击事件
            @Override
            public void onViewClick(View view, int position) {
                if (mOnItemClickListener != null && mInfos.size() > 0) {
                    mOnItemClickListener.onItemClick(view, viewType, mInfos.get(position), position);
                }
            }
        });
        return mHolder;
    }

    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(BaseHolder<T> holder, int position) {

        holder.bindData(mInfos.get(position), position);
    }


    /**
     * 返回数据的个数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mInfos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mInfos.get(position).getViewType();
    }

    @NonNull
    public List<T> getInfos() {
        return mInfos;
    }

    public void setInfos(List<T> infos) {
        if (infos != null) {
            mInfos.clear();
            mInfos.addAll(infos);
            notifyDataSetChanged();
        }
    }

    public void appendInfos(List<T> infos) {

        if (infos != null) {
            int size = mInfos.size();
            mInfos.addAll(infos);
            notifyItemRangeInserted(size, infos.size());
        }
    }

    public void showNoNetWork() {
        setInfos(getStatusList(NO_NETWORK));
    }

    public void showNoNetWork(int topMargin) {
        this.imageTopMargin = topMargin;
        setInfos(getStatusList(NO_NETWORK));
    }

    private List<T> getStatusList(int viewType) {
        List<T> list = new ArrayList<>();

        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        try {
            T entity = entityClass.newInstance();
            entity.setViewType(viewType);
            list.add(entity);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void showEmpty() {
        setInfos(getStatusList(NO_DATA));
    }

//    public void showEmpty(int topMargin) {
//        this.imageTopMargin = topMargin;
//        setInfos(getStatusList(NO_DATA));
//    }
//
//    public void showEmptyCenter() {
//        this.imageTopMargin = TOP_MARGIN_CENTER;
//        setInfos(getStatusList(NO_DATA));
//    }

    /**
     * 获得某个 {@code position} 上的 item 的数据
     *
     * @param position
     * @return
     */
    public T getItem(int position) {
        return mInfos == null ? null : mInfos.get(position);
    }

    /**
     * 让子类实现用以提供 {@link BaseHolder}
     *
     * @param v
     * @param viewType
     * @return
     */
    public abstract BaseHolder<T> getHolder(View v, int viewType);

    /**
     * 提供用于 {@code item} 布局的 {@code layoutId}
     *
     * @param viewType
     * @return
     */
    public abstract int getLayoutId(int viewType);

    protected int getNoDataLayoutId() {
        return R.layout.viewstub_list_empty;
    }

    protected int getNoNetworkLayoutId() {
        return R.layout.viewstub_list_error;
    }


    /**
     * 遍历所有{@link BaseHolder},释放他们需要释放的资源
     *
     * @param recyclerView
     */
    public static void releaseAllHolder(RecyclerView recyclerView) {
        if (recyclerView == null) return;
        for (int i = recyclerView.getChildCount() - 1; i >= 0; i--) {
            final View view = recyclerView.getChildAt(i);
            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
            if (viewHolder != null && viewHolder instanceof BaseHolder) {
                ((BaseHolder) viewHolder).onRelease();
            }
        }
    }

    public interface OnRecyclerViewItemClickListener<T> {
        /**
         * item点击回调
         *
         * @param view
         * @param viewType
         * @param data
         * @param position
         */
        void onItemClick(View view, int viewType, T data, int position);
    }

    public void setOnItemClickListener(StatusAdapter.OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    class NoNetworkHolder extends BaseHolder<T> {

        ImageView imageView;

        public NoNetworkHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }

        @Override
        public void bindData(T data, int position) {
            resizeTopMargin(itemView, imageView);
        }
    }

    class EmptyHolder extends BaseHolder<T> {

        ImageView imageView;

        public EmptyHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }

        @Override
        public void bindData(T data, int position) {

            resizeTopMargin(itemView, imageView);
        }


    }

    protected void resizeTopMargin(View itemView, ImageView imageView) {
//        if (imageTopMargin != TOP_MARGIN_DEFAULT) {
//            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
//            if (imageTopMargin == TOP_MARGIN_CENTER) {
//                params.topMargin = -DeviceUtil.dip2px(itemView.getContext(), 50);
//                params.addRule(RelativeLayout.CENTER_IN_PARENT);
//            }else {
//                params.topMargin = DeviceUtil.dip2px(itemView.getContext(), imageTopMargin);
//            }
//            imageView.setLayoutParams(params);
//
//        }
    }
}