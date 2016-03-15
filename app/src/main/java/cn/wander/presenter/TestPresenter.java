package cn.wander.presenter;

import android.app.DownloadManager;
import android.content.Context;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

import cn.wander.bean.DownloadBean;
import cn.wander.bean.DownloadTask;
import cn.wander.core.messageMgr.MessageID;
import cn.wander.core.messageMgr.MessageManager;
import cn.wander.core.moduleMgr.ModMgr;
import cn.wander.core.observers.IDownloadMgrObserver;
import cn.wander.core.observers.ext.AppObserver;
import cn.wander.mod.download.IDownloadMgr;
import cn.wander.ui.test.ITestView;

/**
 * Created by wander on 2016/3/11.
 * email 805677461@qq.com
 */
public class TestPresenter {
    private final IDownloadMgr downloadMgr;
    private String[] url = new String[]{
            "http://media.cdn.kuwo.cn/resource/m2/ksing/2015/9/5/1441438646495_177653620.aac",
            "http://rb03.sycdn.kuwo.cn/resource/a2/68/93/2232646567.aac"
            , "http://media.cdn.kuwo.cn/resource/m2/ksing/2015/12/18/1450377813917_199137451.aac"
            , "http://media.cdn.kuwo.cn/resource/m3/ksing/2015/12/18/1450409659060_198899012.aac"
            , "http://file.mydrivers.com/DG2015Setup_1159E.exe"};
    private String[] imageUrl = new String[]{
            "http://cn.bing.com/hpwp/7745a211788fcac8541f8fc28577449e?FORM=Z9FD1",
            "https://camo.githubusercontent.com/dd69e725f30c30031dea279adc5a9d09ea3432f2/687474703a2f2f6665726e616e646f63656a61732e636f6d2f77702d636f6e74656e742f75706c6f6164732f323031342f30392f636c65616e5f617263686974656374757265312e706e67"
            , "http://www.fookwood.com/wp-content/uploads/2014/08/1408964085_thumb.jpeg"
            , "http://c.hiphotos.baidu.com/baike/w%3D268/sign=6768ccd691ef76c6d0d2fc2da517fdf6/810a19d8bc3eb135b14fa94ca71ea8d3fc1f44b8.jpg"
            , "http://image.baidu.com/search/down?tn=download&word=download&ie=utf8&fr=detail&url=http%3A%2F%2Fimage.tianjimedia.com%2FuploadImages%2F2015%2F131%2F31%2FZM1FK7K0858T.jpg&thumburl=http%3A%2F%2Fimg0.imgtn.bdimg.com%2Fit%2Fu%3D958027555%2C3942824769%26fm%3D21%26gp%3D0.jpg"
    };

    private Context context;
    private ITestView mTestView;
    private List<DownloadBean> list;

    public TestPresenter(Context context, ITestView mTestView) {
        this.context = context;
        this.mTestView = mTestView;
        list = new ArrayList<>();
        MessageManager.getInstance().attachMessage(MessageID.OBSERVER_APP, appObserver);
        MessageManager.getInstance().attachMessage(MessageID.OBSERVER_DOWNLOAD, downloadMgrObserver);
        downloadMgr = ModMgr.getDownloadMgr();
    }

    IDownloadMgrObserver downloadMgrObserver = new IDownloadMgrObserver() {
        @Override
        public void IDownloadObserver_OnStateChanged(DownloadTask task) {
            mTestView.notifyState(task);
        }

        @Override
        public void IDownloadObserver_OnProgressChanged(DownloadTask task) {
            mTestView.notifyProgress(task);
        }

        @Override
        public void IDownloadObserver_OnListChanged(List<DownloadTask> tasks) {
            mTestView.setTasks(tasks);
        }
    };

    public void refresh() {
        MessageManager.getInstance().asyncRun(2000, new MessageManager.Runner() {
            @Override
            public void call() {
                mTestView.refreshSuccess(loadMore());
            }
        });
    }

    public void loadData() {
        //mode 中数据加载逻辑
        initList();
        mTestView.setAdapter(list);
    }

    private void initList() {
        if (list == null) {
            return;
        }
        for (int i = 0; i < 3; i++) {
            DownloadBean downloadBean = new DownloadBean();
            downloadBean.setDownUrl(url[i]);
            downloadBean.setImageUrl(imageUrl[i]);
            downloadBean.setName("第" + (i + 1) + "个下载任务");
            downloadBean.setId(i);
            list.add(downloadBean);
        }
        downloadMgr.addTasks(list);
    }

    private List<DownloadBean> loadMore() {
        List<DownloadBean> beanList = new ArrayList<>();
        for (int i = 3; i < 5; i++) {
            DownloadBean downloadBean = new DownloadBean();
            downloadBean.setDownUrl(url[i]);
            downloadBean.setImageUrl(imageUrl[i]);
            downloadBean.setName("第" + (i + 1) + "个新加下载任务");
            downloadBean.setId(i);
            beanList.add(downloadBean);
        }
        downloadMgr.addTasks(beanList);
        return beanList;
    }

    AppObserver appObserver = new AppObserver() {
        @Override
        public void IAppObserver_OnBackground() {
            super.IAppObserver_OnBackground();
        }

        @Override
        public void IAppObserver_NetworkStateChanged(boolean state, boolean isWifi) {
            super.IAppObserver_NetworkStateChanged(state, isWifi);
        }
    };

    public void destroy() {
        MessageManager.getInstance().detachMessage(MessageID.OBSERVER_APP, appObserver);
        MessageManager.getInstance().detachMessage(MessageID.OBSERVER_DOWNLOAD, downloadMgrObserver);
    }

    public List<DownloadBean> getList() {
        return list;
    }
}
