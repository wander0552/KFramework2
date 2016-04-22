package cn.wander.ui.friends;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import java.util.ArrayList;

import cn.wander.Utils.views.pageindicator.TabPageIndicator;


/**
 * Created by wander on 2016/4/21.
 * email 805677461@qq.com
 */
class TabsAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {
    private final Context mContext;
    private final TabPageIndicator mPageIndicator;
    private final ViewPager mViewPager;
    private final ArrayList<TabInfo> mTabs = new ArrayList<>();

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

    public TabsAdapter(FragmentActivity activity, TabPageIndicator pageIndicator, ViewPager pager) {
        super(activity.getSupportFragmentManager());
        mContext = activity;
        mPageIndicator = pageIndicator;
        mViewPager = pager;
        mViewPager.setAdapter(this);
        mPageIndicator.setOnPageChangeListener(this);
    }

    public void addTab(String tag, String title, Class<?> clss, Bundle args) {
        mTabs.add(new TabInfo(tag, title, clss, args));
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mTabs.size() == 0) {
            return null;
        }
        return mTabs.get(position % mTabs.size()).title;
    }

    public Fragment getFragment(int position) {
        if (position >= mTabs.size() || position < 0) {
            return null;
        }
        TabInfo info = mTabs.get(position);
        if (info != null)
            return info.f;
        return null;
    }

    @Override
    public Fragment getItem(int position) {
        if (position >= mTabs.size()) {
            return null;
        }

        TabInfo info = mTabs.get(position);

        if (info.f == null) {
            info.f = Fragment.instantiate(mContext, info.clss.getName(), info.args);
        }

        return info.f;
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

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    //		int oldPosition = 0;
    @Override
    public void onPageSelected(final int position) {
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
