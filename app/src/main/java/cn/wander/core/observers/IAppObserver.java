package cn.wander.core.observers;


import cn.wander.core.messageMgr.IObserverBase;

public interface IAppObserver extends IObserverBase {

	// Service层初始化完毕，这之后service.Proxy接口才生效
	void IAppObserver_InitFinished();

	// 启动画面显示完毕，系统保证发生在InitFinished之后
	void IAppObserver_WelcomePageDisappear();

	// 网络状态发生变化。网络可用state为true, 网络为wifi时type为true
	void IAppObserver_NetworkStateChanged(boolean state, boolean isWifi);

	// 进入前台
	void IAppObserver_OnForground();

	// 转入后台
	void IAppObserver_OnBackground();

	// 用户主动退出，请抓紧时间保存数据，请活着的Activity自杀
	void IAppObserver_PrepareExitApp();
	
	// sd卡插拔
	void IAppObserver_SDCardStateChanged(final boolean mounted);

	// 正在播放弹出收起(被挡住算收起，遮挡离开算弹出)
	void IAppObserver_OnNowplayingShow(final boolean show);
	
	// 内存不足，请释放所有能缓存的内存
	void IAppObserver_OnLowMemory();

    //升级数据库通知
    void IAppObserver_OnUpdateDatabase();
    
    //播放状态刷新
    void IAppObserver_PlayStateUpdate();
}
