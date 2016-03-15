package cn.wander.core.observers;


import java.util.List;

import cn.wander.bean.DownloadTask;
import cn.wander.core.messageMgr.IObserverBase;

public interface IDownloadMgrObserver extends IObserverBase {

	//由于java语言的限制，接口不能空实现。会导致需要实现该接口的地方有大量的
	//空实现函数，这里为了减少函数个数，将各种状态合并到一个接口中
	//当某下载任务状态发生变化时触发该消息，例如从等待状态变为下载状态
	//当前的状态存在于task中的state字段，参数oldState一般不用考虑
	//by haiping @20140306:ext里的实现已经解决了大量空实现的问题
	void IDownloadObserver_OnStateChanged(final DownloadTask task);
		
	//当某下载任务的进度发生变化时触发该消息，下载大小，进度等信息都在task中保存
	void IDownloadObserver_OnProgressChanged(final DownloadTask task);
	
	//当某下载列表发生变化时触发该消息，比如增删操作，列表中下载任务的个数发生变化
	void IDownloadObserver_OnListChanged(List<DownloadTask> tasks);
}