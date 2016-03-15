package cn.wander.mod.download;

//by wang meng
public enum DownloadState {
    Waiting,		//正在等待
    Preparing,	    //准备缓存，未正式开始缓存文件，仅仅请求URL等，外部调用者不需要考虑该状态
    Downloading,	//正在缓存
    Paused,			//暂停缓存
    Finished,		//已经缓存
    Failed;		    //缓存失败
    
    @Override
    public String toString() {
		switch (this){ 
		case Waiting: 
			return "等待下载"; 
		case Downloading: 
			return "下载中"; 
		case Paused: 
			return "暂停下载"; 
		case Finished: 
			return "下载完成"; 
		case Failed: 
			return "下载失败"; 
		default: 
			return "未知状态"; 
		} 
	} 
}
