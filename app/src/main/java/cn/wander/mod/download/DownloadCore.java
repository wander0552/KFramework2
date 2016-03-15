package cn.wander.mod.download;

import android.os.AsyncTask;

import java.io.IOException;

import cn.wander.base.net.OkHttpUtil;
import cn.wander.bean.DownloadBean;
import cn.wander.bean.DownloadTask;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wander on 2016/3/14.
 * email 805677461@qq.com
 */
public class DownloadCore extends AsyncTask{

    DownloadTask task;
    DownloadMgrImpl downloadMgr;

    public DownloadCore(DownloadMgrImpl downloadMgr, DownloadTask task) {
        this.downloadMgr = downloadMgr;
        this.task = task;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        OkHttpUtil.download(task.downloadBean.getDownUrl(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
        return null;
    }
}
