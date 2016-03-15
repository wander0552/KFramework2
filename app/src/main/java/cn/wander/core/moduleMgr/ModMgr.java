package cn.wander.core.moduleMgr;

import java.util.Iterator;
import java.util.LinkedList;

import cn.wander.base.utils.KwDebug;
import cn.wander.mod.download.DownloadMgrImpl;
import cn.wander.mod.download.IDownloadMgr;

public final class ModMgr {

    private ModMgr() {
    }

    private static LinkedList<IModuleBase> allModules = new LinkedList<IModuleBase>();

    public static void releaseAll() {
        int moduleNum = allModules.size();
        for (Iterator<IModuleBase> itemIter = allModules.iterator(); itemIter.hasNext(); ) {
            IModuleBase item = (IModuleBase) itemIter.next();
            try {
                item.release();
            } catch (Throwable e) {
                KwDebug.classicAssert(false, e);
            }
            KwDebug.classicAssert(allModules.size() == moduleNum, "模块release函数里只能释放自己占用的资源，不允许访问其它模块" + item.toString());
        }
        allModules.clear();
        allModules = null;
    }

    private static void addModule(final IModuleBase m) {
        m.init();
        allModules.add(m);
    }

    // 下载模块
    private static IDownloadMgr downloadMgr = null;

    public static IDownloadMgr getDownloadMgr() {
        KwDebug.mustMainThread();
        if (downloadMgr == null) { // 请忽略findbugs提示，主线程接口，没有多线程问题
            downloadMgr = new DownloadMgrImpl();
            addModule(downloadMgr);
        }
        return downloadMgr;
    }



}
