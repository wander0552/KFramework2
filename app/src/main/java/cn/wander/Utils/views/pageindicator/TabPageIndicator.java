/*
 * Copyright (C) 2011 The Android Open Source Project
 * Copyright (C) 2011 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.wander.Utils.views.pageindicator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.wander.kFramework.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class TabPageIndicator extends HorizontalScrollView implements PageIndicator {


    public interface OnTabReselectedListener {
        /**
         * Callback when the selected tab has been reselected.
         *
         * @param position Position of the current center item.
         */
        void onTabReselected(int position);
    }

    private static final CharSequence EMPTY_TITLE = "";
    private static final int TYPE_SELECT = 1;
    private static final int TYPE_UNSELECT = 0;


    private final OnClickListener mTabClickListener = new OnClickListener() {
        public void onClick(View view) {
            TextView tabView = (TextView) view;
            final int newSelected = (Integer) tabView.getTag();
            mViewPager.setCurrentItem(newSelected, false);
        }
    };
    private final IcsLinearLayout mTabLayout;
    private OnTabReselectedListener mTabReselectedListener;
    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mListener;
    private float mLastRadio;
    private float mCurRadio;
    private float mUnSelectScale = 0.95f;
    private float mSelectScale = 1.0f;
    private int mSelectedTabIndex;
    private int mMaxTabWidth;
    private int mLastPosition;
    private int mCurPosition;
    private int mTabTextColorW;
    private int mTabTextColorG;
    private boolean mShowLogSended = false;

    public TabPageIndicator(Context context) {
        this(context, null);
    }

    public TabPageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        setHorizontalScrollBarEnabled(false);
        mTabLayout = new IcsLinearLayout(context, R.attr.vpiTabPageIndicatorStyle);
        mTabTextColorW = getResources().getColor(R.color.kw_common_cl_white);
        mTabTextColorG = getResources().getColor(R.color.kw_common_cl_white_alpha_50);
        addView(mTabLayout, new ViewGroup.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
    }

    public void setOnTabReselectedListener(OnTabReselectedListener listener) {
        mTabReselectedListener = listener;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final boolean lockedExpanded = widthMode == MeasureSpec.EXACTLY;
        setFillViewport(lockedExpanded);

        final int childCount = mTabLayout.getChildCount();
        if (childCount > 1 && (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST)) {
            if (childCount > 2) {
                mMaxTabWidth = (int) (MeasureSpec.getSize(widthMeasureSpec) * 0.4f);
            } else {
                mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;
            }
        } else {
            mMaxTabWidth = -1;
        }

        final int oldWidth = getMeasuredWidth();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int newWidth = getMeasuredWidth();

        if (lockedExpanded && oldWidth != newWidth) {
            // Recenter the tab display if we're at a new (scrollable) size.
            setCurrentItem(mSelectedTabIndex);
        }
    }

    private void addTab(int index, CharSequence text, int iconResId) {
        RelativeLayout tablayout = (RelativeLayout) View
                .inflate(getContext(), R.layout.tab_item_layout, null);

        final TextView tabView = (TextView) tablayout.findViewById(R.id.tab_item_title);
        tabView.setTag(index);
        tabView.setFocusable(true);
        tabView.setOnClickListener(mTabClickListener);
        tabView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 19);
        tabView.setText(text);
        tabView.setTextColor(getResources().getColor(R.color.kw_common_cl_white));
        if (iconResId != 0) {
            //             tabView.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0, 0);
            ImageView iconView = (ImageView) tablayout.findViewById(R.id.tab_item_icon);
            iconView.setImageResource(iconResId);
            iconView.setVisibility(View.VISIBLE);
        }

        mTabLayout.addView(tablayout, new LinearLayout.LayoutParams(0, MATCH_PARENT, 1));
    }

    @Override
    public void onPageScrollStateChanged(int pState) {
        if (mListener != null) {
            mListener.onPageScrollStateChanged(pState);
        }
        setSelectTabTextStyle();
    }

    @Override
    public void onPageScrolled(int position, float radio, int offset) {
        if (mListener != null) {
            mListener.onPageScrolled(position, radio, offset);
        }
        mLastRadio = mCurRadio;
        mCurRadio = radio;
        mLastPosition = mCurPosition;
        mCurPosition = position;
        float positionDx = mCurPosition - mLastPosition;
        float radioDx = mCurRadio - mLastRadio;
        if (positionDx == 0) {
            changeTabView(position, radio);
            if (radioDx != 0) {
                changeOtherTabView(position + 1, radio);
            }
        }
    }

    // Used to dynamically change the title font color =====================start

    private void changeOtherTabView(int position, float radio) {
        float scale = mUnSelectScale + (mSelectScale - mUnSelectScale) * radio;
        setTabTextView(position, scale, radio, TYPE_UNSELECT);
    }

    private void changeTabView(int position, float radio) {
        float scale = mSelectScale - (mSelectScale - mUnSelectScale) * radio;
        setTabTextView(position, scale, radio, TYPE_SELECT);
    }

    private void setTabTextView(int position, float scale, float radio, int type) {
        View view = mTabLayout.getChildAt(position);
        TextView tv = (TextView) view.findViewById(R.id.tab_item_title);
        setTextScale(tv, scale);
        if (TYPE_SELECT == type) {
            setColor(tv, getMiddleColor(mTabTextColorW, mTabTextColorG, radio));
        } else {
            setColor(tv, getMiddleColor(mTabTextColorG, mTabTextColorW, radio));
        }
    }

    @SuppressLint("NewApi")
	private void setTextScale(TextView tv, float scale) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            tv.setScaleX(scale);
            tv.setScaleY(scale);
        }
    }

    private void setColor(TextView tv, int color) {
        tv.setTextColor(color);
    }

    private int getMiddleValue(int prev, int next, float factor) {
        return Math.round(prev + (next - prev) * factor);
    }

    private int getMiddleColor(int preColor, int curColor, float factor) {
        if (preColor == curColor) {
            return curColor;
        }
        if (0f == factor) {
            return preColor;
        } else if (1f == factor) {
            return curColor;
        }
        int a = getMiddleValue(Color.alpha(preColor), Color.alpha(curColor), factor);
        int r = getMiddleValue(Color.red(preColor), Color.red(curColor), factor);
        int g = getMiddleValue(Color.green(preColor), Color.green(curColor), factor);
        int b = getMiddleValue(Color.blue(preColor), Color.blue(curColor), factor);
        return Color.argb(a, r, g, b);
    }

    // Used to dynamically change the title font color =====================end

    @Override
    public void onPageSelected(int arg0) {
        setCurrentItem(arg0);
        if (mListener != null) {
            mListener.onPageSelected(arg0);
        }
        if (mTabReselectedListener != null) {
        	mTabReselectedListener.onTabReselected(arg0);
        }
    }

    @Override
    public void setViewPager(ViewPager view) {
        if (mViewPager == view) {
            return;
        }
        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(null);
        }
        final PagerAdapter adapter = view.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mViewPager = view;
        view.setOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        mTabLayout.removeAllViews();
        PagerAdapter adapter = mViewPager.getAdapter();
        IconPagerAdapter iconAdapter = null;
        if (adapter instanceof IconPagerAdapter) {
            iconAdapter = (IconPagerAdapter) adapter;
        }
        final int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            CharSequence title = adapter.getPageTitle(i);
            if (title == null) {
                title = EMPTY_TITLE;
            }
            int iconResId = 0;
            if (iconAdapter != null) {
                iconResId = iconAdapter.getIconResId(i);
            }
            addTab(i, title, iconResId);
        }
        if (mSelectedTabIndex > count) {
            mSelectedTabIndex = count - 1;
        }
        setCurrentItem(mSelectedTabIndex);
        requestLayout();
    }

    @Override
    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    @Override
    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            return;
        }
        mSelectedTabIndex = item;
        mViewPager.setCurrentItem(item);
        setSelectTabTextStyle();
    }

    private void setSelectTabTextStyle() {
        if (mTabLayout == null) {
            return;
        }
        int count = mTabLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = mTabLayout.getChildAt(i);
            TextView text = (TextView) view.findViewById(R.id.tab_item_title);
            if (i == mSelectedTabIndex) {
                setTextScale(text, mSelectScale);
                setColor(text, getMiddleColor(mTabTextColorW, mTabTextColorG, 0));
            } else {
                setTextScale(text, mUnSelectScale);
                setColor(text, getMiddleColor(mTabTextColorG, mTabTextColorW, 0));
            }
        }
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mListener = listener;
    }

    public void setRedHotVisable(int index, int visable) {
        if (mTabLayout != null) {
            View view = mTabLayout.getChildAt(index);
            if (view != null) {
                View redHot = view.findViewById(R.id.tab_item_redhot);
                redHot.setVisibility(visable);
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    public int getTabItemLeft(int item) {
        final int tabCount = mTabLayout.getChildCount();
        if (item >= 0 && item < tabCount) {
            return mTabLayout.getChildAt(item).getLeft();
        }
        return -1;
    }

    public int getTabItemPaddingLeft(int item) {
        final int tabCount = mTabLayout.getChildCount();
        if (item >= 0 && item < tabCount) {
            return mTabLayout.getChildAt(item).getPaddingLeft();
        }
        return -1;
    }

    public int getTabItemPaddingRight(int item) {
        final int tabCount = mTabLayout.getChildCount();
        if (item >= 0 && item < tabCount) {
            return mTabLayout.getChildAt(item).getPaddingRight();
        }
        return -1;
    }

    public int getTabItemWidth(int item) {
        final int tabCount = mTabLayout.getChildCount();
        if (item >= 0 && item < tabCount) {
            return mTabLayout.getChildAt(item).getWidth();
        }
        return -1;
    }

    public int getTabItemHeight(int item) {
        final int tabCount = mTabLayout.getChildCount();
        if (item >= 0 && item < tabCount) {
            return mTabLayout.getChildAt(item).getHeight();
        }
        return -1;
    }
    ///////////////////////////////////////////////////////////////////////////

}
