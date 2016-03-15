package cn.wander.ui.test;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import cn.wander.bean.DownloadBean;
import cn.wander.bean.DownloadTask;
import cn.wander.kFramework.R;
import cn.wander.mod.download.DownloadState;

/**
 * Created by wander on 2016/3/11.
 * email 805677461@qq.com
 */
public class TestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private List<DownloadBean> data;
    private onItemClickListener listener;
    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.mipmap.ic_launcher)
            .cacheInMemory(true)
            .cacheOnDisk(true).build();

    private List<DownloadTask> tasks = new ArrayList<>();

    public TestAdapter() {
        data = new ArrayList<>();
    }

    public void addAll(List<DownloadBean> elem) {
        data.addAll(elem);
        notifyDataSetChanged();
    }

    public void setTasks(List<DownloadTask> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public void remove(DownloadBean elem) {
        int i = data.indexOf(elem);
        if (i != -1) {
            data.remove(elem);
            notifyItemRangeRemoved(i, data.size());
        }
    }

    public void remove(int index) {
        data.remove(index);
        notifyItemRangeRemoved(index, data.size());
    }

    public void replaceAll(List<DownloadBean> elem) {
        data.clear();
        data.addAll(elem);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_download, parent, false);
        view.setOnClickListener(this);
        return new TestHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof TestHolder) {
            DownloadBean downloadBean = data.get(position);
            if (tasks != null && tasks.size() > position) {
                DownloadTask task = tasks.get(position);
                ((TestHolder) holder).name.setText(downloadBean.getName());
                ImageLoader.getInstance().displayImage(downloadBean.getImageUrl(), ((TestHolder) holder).imageView, options);
                ((TestHolder) holder).progressBar.setProgress((int) (task.progress * 100));
                task.param = holder;
                task.showIndex = position;
            }
        }
    }

    public void setOnItemClickListenre(onItemClickListener listenre) {
        this.listener = listenre;
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    private RecyclerView recycler;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        recycler = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        recycler = null;
    }

    @Override
    public void onClick(View v) {
        int position = recycler.getChildAdapterPosition(v);

        listener.onItemClick(position, data.get(position));
    }

    public void notifyProgress(DownloadTask task) {
        TestHolder testHolder = (TestHolder) task.param;
        testHolder.progressBar.setProgress((int) task.progress);
        testHolder.speed.setText((int) task.speed/1024+"KB/S");
    }

    public void notifyState(DownloadTask task) {
        TestHolder testHolder = (TestHolder) task.param;
        if (task.state == DownloadState.Finished) {
            testHolder.pause.setImageResource(R.drawable.menu_icon_delete_normal);
            testHolder.speed.setText("下载完成");
        }
    }

    class TestHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView name;
        private ProgressBar progressBar;
        private ImageView pause;
        private TextView speed;

        public TestHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.download_image);
            name = (TextView) itemView.findViewById(R.id.download_name);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress);
            progressBar.setMax(100);
            pause = (ImageView) itemView.findViewById(R.id.download_pause);
            speed = (TextView) itemView.findViewById(R.id.download_speed);
            pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, "是否暂停下载", Snackbar.LENGTH_SHORT).setAction("YES", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
                }
            });
        }
    }

    class SuccessHolde extends RecyclerView.ViewHolder {
        public SuccessHolde(View itemView) {
            super(itemView);
        }
    }

    public interface onItemClickListener {
        void onItemClick(int position, DownloadBean downloadBean);
    }
}
