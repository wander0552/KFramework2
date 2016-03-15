package cn.wander.ui.test;

import java.util.List;

import cn.wander.bean.DownloadBean;
import cn.wander.bean.DownloadTask;
import cn.wander.presenter.BasePresenter;

/**
 * Created by wander on 2016/3/11.
 * email 805677461@qq.com
 */
public interface ITestView extends BasePresenter {

    void refreshSuccess(List<DownloadBean> list);

    void setAdapter(List<DownloadBean> list);

    void notifyState(DownloadTask task);

    void notifyProgress(DownloadTask task);

    void setTasks(List<DownloadTask> tasks);


}
