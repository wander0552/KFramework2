package cn.wander.bean;


import cn.wander.mod.download.DownloadState;

// 根据需要这里面的字段会增减
public class DownloadTask {
    // 由DownloadProxy分配的id
    public long id;
    /**
     * 当前下载的任务
     */
    public  DownloadBean downloadBean;

    // 当前下载任务的状态
    public DownloadState state;

    // 当前下载任务的速度，单位 b/s
    public float speed;

    // 当前下载的进度百分百
    public float progress;

    // 运行时需要的参数，不需要持久保存
    public Object param;

    // 运行时需要的参数，不需要持久保存
    public int showIndex;
    //下载任务来源类型，用于区分是酷我自己的下载任务，还是接收外部暴风等应用的下载请求。这决定了状态回报的方式不一样，1是内部，100及以后的值是外部应用，目前是100的暴风
    public int downFrom;
    //保存用于状态回报的，外来app的广播Action字符串，不用序列化到数据库，只需要运行时进行回报。
    public String downFromAction;

    // 运行时需要的参数，不需要持久保存
    public int StateEx;            // 0:手动停止，1:自动停止

    // 运行时需要的参数，不需要持久保存
    public int retryTimes;        // wifi下自动尝试下载次数

    //运行时记录下载失败的错误码
    public int downFailedResult;

    public DownloadTask() {
        id = -1;
        StateEx = 0;
        retryTimes = 0;
        state = DownloadState.Waiting;
    }

    public boolean isAddByClick = false;//是否通过点击任务列表添加

}
