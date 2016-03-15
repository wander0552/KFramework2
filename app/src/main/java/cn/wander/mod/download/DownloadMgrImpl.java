package cn.wander.mod.download;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.okhttpfinal.FileDownloadCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.wander.base.utils.SDCardUtils;
import cn.wander.bean.DownloadBean;
import cn.wander.bean.DownloadTask;
import cn.wander.core.messageMgr.IObserverBase;
import cn.wander.core.messageMgr.MessageID;
import cn.wander.core.messageMgr.MessageManager;
import cn.wander.core.observers.IDownloadMgrObserver;
import cn.wander.core.observers.ext.AppObserver;
import cn.wander.kFramework.App;

public class DownloadMgrImpl implements IDownloadMgr {
    private static final String TAG = "DownloadMgrImpl";

    private Context context;
    private DownloadManager downloadManager;
    private DownloadChangeObserver downloadChangeObserver;
    private int count;

    public static final Uri CONTENT_URI = Uri.parse("content://downloads/my_downloads");
    private List<DownloadTask> tasks = new ArrayList<>();
    private File saveDir;

    private void notifyListChange() {
        MessageManager.getInstance().asyncNotify(MessageID.OBSERVER_DOWNLOAD, new MessageManager.Caller<IDownloadMgrObserver>() {
            @Override
            public void call() {
                ob.IDownloadObserver_OnListChanged(getAllTasks());
            }
        });

    }

    private void notifyStateChange(final DownloadTask task) {
        MessageManager.getInstance().asyncNotify(MessageID.OBSERVER_DOWNLOAD, new MessageManager.Caller<IDownloadMgrObserver>() {
            @Override
            public void call() {
                ob.IDownloadObserver_OnStateChanged(task);
            }
        });
    }

    /**
     * 通知当前下载进度
     *
     * @param task
     */
    private void notifyProgressChange(final DownloadTask task) {
        MessageManager.getInstance().asyncNotify(MessageID.OBSERVER_DOWNLOAD, new MessageManager.Caller<IDownloadMgrObserver>() {
            @Override
            public void call() {
                ob.IDownloadObserver_OnProgressChanged(task);
            }
        });
    }

    public DownloadMgrImpl() {
        context = App.getInstance().getApplicationContext();
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    @Override
    public boolean addTask(DownloadBean downloadBean) {
        final DownloadTask task = new DownloadTask();
        task.downloadBean = downloadBean;
        String downUrl = downloadBean.getDownUrl();
        File file = new File(saveDir.getAbsolutePath(), downUrl.substring(downUrl.lastIndexOf("/")));
        FileDownloadCallback downloadCallback = new FileDownloadCallback(){
            @Override
            public void onStart() {
                super.onStart();
                task.state = DownloadState.Downloading;
                notifyStateChange(task);
            }

            @Override
            public void onProgress(int progress, long networkSpeed) {
                super.onProgress(progress, networkSpeed);
                task.progress = progress;
                task.speed = networkSpeed;
                notifyProgressChange(task);
            }

            @Override
            public void onDone() {
                task.state = DownloadState.Finished;
                notifyStateChange(task);
                super.onDone();
            }
        };
        HttpRequest.download(downUrl, file , downloadCallback);
        task.id = downloadBean.getId();
        task.state = DownloadState.Waiting;
        count++;
        tasks.add(task);
        notifyListChange();
        return true;
    }



    @Override
    public boolean addTasks(List<DownloadBean> downloadBeanList) {
        if (downloadBeanList != null && downloadBeanList.size() > 0) {
            for (DownloadBean downloadBean : downloadBeanList) {
                addTask(downloadBean);
            }
        }
        return false;
    }

    class DownloadChangeObserver extends ContentObserver {
        DownloadTask task;

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public DownloadChangeObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
        }

        public DownloadTask getTask() {
            return task;
        }

        public void setTask(DownloadTask task) {
            this.task = task;
        }
    }

    @Override
    public final List<DownloadTask> getAllTasks() {
        ArrayList<DownloadTask> temp = new ArrayList<DownloadTask>();
        if (!temp.isEmpty()) {
            temp.clear();
        }
        if (tasks != null) {
            temp.addAll(tasks);
        }

        return temp;
    }

    @Override
    public boolean deleteAllTasks() {
        return false;
    }

    @Override
    public int getTaskCount() {
        return 0;
    }

    @Override
    public boolean startAllTasks(boolean isUserAction) {
        return false;
    }

    @Override
    public boolean pauseAllTasks(boolean autoPause) {
        return false;
    }

    @Override
    public boolean changeDownloadPath(String path) {
        return false;
    }

    @Override
    public void init() {
        saveDir = new File(Environment.getExternalStorageDirectory(),"Framework33");
        if (!saveDir.exists()){
            saveDir.mkdir();
        }
        MessageManager.getInstance().attachMessage(MessageID.OBSERVER_APP, appObserver);
//        MessageManager.getInstance().attachMessage(MessageID.OBSERVER_DOWNLOAD, IDownloadMgrObserver);

    }

    private AppObserver appObserver = new AppObserver() {
        @Override
        public void IAppObserver_NetworkStateChanged(boolean state, boolean isWifi) {
            super.IAppObserver_NetworkStateChanged(state, isWifi);

        }
    };

    @Override
    public void release() {
        MessageManager.getInstance().detachMessage(MessageID.OBSERVER_APP, appObserver);
        context.getContentResolver().unregisterContentObserver(downloadChangeObserver);
    }
}
