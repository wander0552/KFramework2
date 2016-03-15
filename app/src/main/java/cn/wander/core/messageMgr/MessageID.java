package cn.wander.core.messageMgr;


import cn.wander.core.observers.IAppObserver;
import cn.wander.core.observers.IDownloadMgrObserver;

public enum MessageID {

	OBSERVER_ID_RESERVE {
		public Class<? extends IObserverBase> getObserverClass() {
			return null;
		}
	}
	,
	OBSERVER_APP {
		public Class<? extends IObserverBase> getObserverClass() {
			return IAppObserver.class;
		}
	}
	,
	OBSERVER_DOWNLOAD{
		@Override
		Class<? extends IObserverBase> getObserverClass() {
			return IDownloadMgrObserver.class;
		}
	};
	abstract Class<? extends IObserverBase> getObserverClass();
}