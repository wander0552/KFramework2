package cn.wander.core.moduleMgr;


//所有模块基础接口
public interface IModuleBase {
	
	//在模块被按需加载的时候自动调用，主线程
	void init();

	//退出时候被调用，主线程
	//切记别的模块可能已经release，此时不允许访问其它模块了，否则请使用IAppObserver_PrepareExitApp
	void release();
	
}
