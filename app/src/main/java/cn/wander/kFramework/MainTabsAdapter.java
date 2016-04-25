package cn.wander.kFramework;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by wander on 2016/4/22.
 * email 805677461@qq.com
 */
public class MainTabsAdapter extends FragmentPagerAdapter {

    private final ArrayList<TabInfo> mTabs = new ArrayList<>();
    private Context mContext;

    class TabInfo {
        private final Class<?> clss;
        private final Bundle args;
        public String title;
        public Fragment f;

        TabInfo(String _tag, String _title, Class<?> _class, Bundle _args) {
            clss = _class;
            args = _args;
            title = _title;
            f = null;
        }
    }

    public MainTabsAdapter(FragmentActivity activity) {
        super(activity.getSupportFragmentManager());
        mContext = activity;
    }

    public void Resume() {
        for (TabInfo info : mTabs) {
            if (info != null && info.f != null && info.f.isAdded()) {
                info.f.onResume();
            }
        }
    }

    public void Pause() {
        for (TabInfo info : mTabs) {
            if (info != null && info.f != null && info.f.isAdded()) {
                info.f.onPause();
            }
        }
    }

    public void addTab(String tag, String title, Class<?> clss, Bundle args) {
        mTabs.add(new TabInfo(tag, title, clss, args));
        notifyDataSetChanged();
    }

    public Fragment getFragment(int position) {
        if (position > mTabs.size() || position < 0) {
            return null;
        }
        TabInfo info = mTabs.get(position);

        if (info.f == null) {
            return info.f;
        }
        return null;
    }

    public TabInfo getInfo(int position) {
        if (position > mTabs.size() || position < 0) {
            return null;
        }
        TabInfo info = mTabs.get(position);
        return info;
    }

    @Override
    public Fragment getItem(int position) {
        if (position > mTabs.size() || position < 0) {
            return null;
        }
        TabInfo info = mTabs.get(position);

        if (info.f == null) {
            info.f = Fragment.instantiate(mContext, info.clss.getName(), info.args);
        }

        return info.f;
    }

    @Override
    public int getCount() {
        return mTabs != null ? mTabs.size() : 0;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
