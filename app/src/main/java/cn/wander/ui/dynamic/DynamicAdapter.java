package cn.wander.ui.dynamic;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import cn.wander.base.utils.DialogUtils;
import cn.wander.kFramework.R;

/**
 * Created by wander on 2016/4/28.
 * email 805677461@qq.com
 */
public class DynamicAdapter extends RecyclerView.Adapter<DynamicAdapter.DynamicHolder> {
    private Context context;
    private List<String> data;

    String[] headerIcons = {"http://www.5djiaren.com/uploads/2015-04/17-115301_29.jpg",
            "http://img1.dzwww.com:8080/tupian_pl/20160106/32/4152697013403556460.jpg",
            "http://c.hiphotos.baidu.com/zhidao/pic/item/72f082025aafa40f191362cfad64034f79f019ce.jpg",
            "http://img.article.pchome.net/new/w600/00/35/15/66/pic_lib/wm/122532981493137o3iegiyx.jpg",
            "http://img0.imgtn.bdimg.com/it/u=3382799710,1639843170&fm=21&gp=0.jpg",
            "http://i2.sinaimg.cn/travel/2014/0918/U7398P704DT20140918143217.jpg",
            "http://photo.l99.com/bigger/21/1415193165405_4sg3ds.jpg",
            "http://img.pconline.com.cn/images/upload/upc/tx/photoblog/1305/15/c2/20949108_20949108_1368599174341.jpg",
            "http://pic29.nipic.com/20130501/12558275_114724775130_2.jpg",
            "http://photo.l99.com/bigger/20/1415193157174_j2fa5b.jpg"};

    public DynamicAdapter(Context context) {
        this.context = context;
    }

    public void addAll(List<String> list) {
        if (data == null) {
            data = new ArrayList<>();
        }
        data.addAll(list);
    }

    @Override
    public DynamicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_item, parent, false);

        return new DynamicHolder(view);
    }

    @Override
    public void onBindViewHolder(DynamicHolder holder, final int position) {
        final String des = data.get(position);
        holder.content.setText(des);
        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(position, des);
            }
        });

        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.showDialog_NOResponse(context,"作品提供打赏");
            }
        });

        ImageLoader.getInstance().displayImage(headerIcons[position % headerIcons.length], holder.header);

    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public void setOnclickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    private onItemClickListener listener;

    public interface onItemClickListener {
        void onItemClick(int position, String content);
    }

    class DynamicHolder extends RecyclerView.ViewHolder {
        private ImageView header;
        private TextView content;
        private ImageView play;
        private ImageView more;

        public DynamicHolder(View itemView) {
            super(itemView);

            header = (ImageView) itemView.findViewById(R.id.header);
            content = (TextView) itemView.findViewById(R.id.content);
            play = (ImageView) itemView.findViewById(R.id.dynamic_play);


            more = (ImageView) itemView.findViewById(R.id.dynamic_more);

        }
    }
}
