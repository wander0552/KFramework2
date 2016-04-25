package cn.wander.kFramework;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Handler;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

import cn.finalteam.okhttpfinal.OkHttpFinal;
import cn.finalteam.okhttpfinal.OkHttpFinalConfiguration;
import cn.wander.base.utils.DeviceInfo;
import cn.wander.base.utils.SDCardUtils;
import cn.wander.base.utils.ScreenUtility;

/**
 * Created by wander on 2016/3/10.
 * email 805677461@qq.com
 */
public class App extends Application {
    private static App instance;

    public static App getInstance() {
        return instance;
    }

    private static Handler mainThreadHandler = new Handler();
    private static long mainThreadID = Thread.currentThread().getId();
    private static volatile boolean isExiting;
    private static boolean mainProgress;


    public static long getMainThreadID() {
        return mainThreadID;
    }

    public static Handler getMainThreadHandler() {
        return mainThreadHandler;
    }

    public static boolean isExiting() {
        return isExiting;
    }

    public static boolean isMainProgress() {
        return mainProgress;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initImageLoader(this);
        SDCardUtils.init();
        OkHttpFinalConfiguration.Builder builder = new OkHttpFinalConfiguration.Builder().setTimeout(20000);
        OkHttpFinal.getInstance().init(builder.build());
        DeviceInfo.initScreenInfo(this);
    }

    public static void initImageLoader(Context context) {
        int memoryCacheSize;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            int memClass = ((ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE))
                    .getMemoryClass();
            memoryCacheSize = (memClass); // 1/8 of app memory limit
        } else {
            memoryCacheSize = 2;
        }
        int m = 2;
        if (memoryCacheSize > 200) {
            m = 10;
        } else if (memoryCacheSize > 150) {
            m = 6;
        } else if (memoryCacheSize >= 64) {
            m = 3;
        } else {
            m = 2;
        }
        System.out.println("memoryCacheSize  " + memoryCacheSize + "M");
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        File cacheDir = StorageUtils.getCacheDirectory(context);
        // File cacheDir = StorageUtils.getOwnCacheDirectory(context,
        // DirectoryManager.getDirPath(DirContext.PICTURE));
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800)
                // default = device screen dimensions
                // .discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75)
                // //设置缓存的详细信息，最好不要设置这个
                .threadPoolSize(3)
                // default
                .threadPriority(Thread.NORM_PRIORITY - 1)
                // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(m * 1024 * 1024))
//				.memoryCache(new WeakMemoryCache())
                // default
                .memoryCacheSize(m * 1024 * 1024)
                .diskCache(new UnlimitedDiskCache(cacheDir))
                // default
                .diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(context)) // default
                .tasksProcessingOrder(QueueProcessingType.LIFO) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }
}
