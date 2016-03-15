package cn.wander.core.observers.ext;


import cn.wander.core.observers.IAppObserver;

public class AppObserver implements IAppObserver {
	@Override
	public void IAppObserver_InitFinished() {
	}

	@Override
	public void IAppObserver_WelcomePageDisappear() {
	}

	@Override
	public void IAppObserver_NetworkStateChanged(boolean state, boolean isWifi) {
	}

	@Override
	public void IAppObserver_OnForground() {
	}

	@Override
	public void IAppObserver_OnBackground() {
	}

	@Override
	public void IAppObserver_PrepareExitApp() {
	}

	@Override
	public void IAppObserver_SDCardStateChanged(boolean mounted) {
	}

	@Override
	public void IAppObserver_OnNowplayingShow(final boolean show) {
	}
	
	@Override
	public void IAppObserver_OnLowMemory() {
	}

    @Override
    public void IAppObserver_OnUpdateDatabase(){
    }
    
	@Override
	public void IAppObserver_PlayStateUpdate() {
	}
}
