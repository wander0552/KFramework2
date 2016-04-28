package cn.wander.Utils.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.Locale;

import cn.wander.kFramework.R;

/**
 * Created by wander on 2015/5/14.
 * Email 18955260352@163.com
 */
public class PagerSlidingLinearStrip extends LinearLayout {

    private Paint rectPaint;
    private Paint dividerPaint;
    private Locale locale;

    //默认导航栏分割线宽度
    private float dividerWidth = 1;
    private int tabPadding = 24;
    private int scrollOffset = 52;
    private int indicatorHeight = 8;
    private int underlineHeight = 2;
    private int dividerPadding = 12;
    private int indicatorColor = 0xFF666666;
    private int underlineColor = 0x1A000000;
    private int dividerColor = 0x1A000000;

    //内容样式
    private int tabTextSize = 18;
    private int tabTextColor = getResources().getColor(R.color.kw_common_cl_red);
    private Typeface tabTypeface = null;
    private int tabTypefaceStyle = Typeface.BOLD;
    private boolean textAllCaps = true;
    private int tabBackgroundResId = R.drawable.background_tab;
    private int selectColor = 0xFFFFFFFF;


    private LayoutParams defaultTabLayoutParams;
    private int lastScrollX = 0;

    public interface IconTabProvider {
        public int getPageIconResId(int position);
    }

    //存放默认参数
    // @formatter:off
    private static final int[] ATTRS = new int[]{
            android.R.attr.textSize,
            android.R.attr.textColor
    };
    // @formatter:on

    private final PageListener listener = new PageListener();
    //用于外界处理ViewPager滑动的事件
    public ViewPager.OnPageChangeListener delegatePageListener;

    private ViewPager pager;

    private int tabcount;
    private int currentPosition;
    private float currentPositionOffset = 0f;


    public PagerSlidingLinearStrip(Context context) {
        this(context, null);
    }

    public PagerSlidingLinearStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerSlidingLinearStrip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setWillNotDraw(false);

        //画导航线的矩形画笔
        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        //画导航栏下面的分割线
        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setStrokeWidth(dividerWidth);

        defaultTabLayoutParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f);

        if (locale == null) {
            locale=getResources().getConfiguration().locale;
        }

    }

    public void setViewPager(ViewPager pager){
        this.pager=pager;
        if (pager.getAdapter()==null){
            throw new IllegalStateException("ViewPage do not have adapter instance");
        }

        pager.setOnPageChangeListener(listener);

        notifyDataSetChange();
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener){
        this.delegatePageListener=listener;
    }

    public void notifyDataSetChange(){
        PagerAdapter adapter = pager.getAdapter();
        tabcount = adapter.getCount();
        for (int i = 0; i < tabcount; i++) {
            addTextTab(i, (String) adapter.getPageTitle(i));
        }
        updateTabStyles();

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                currentPosition = pager.getCurrentItem();
            }
        });
    }

    void addTextTab(final int position,String title){
        TextView tab=new TextView(getContext());
        tab.setText(title);
        tab.setGravity(Gravity.CENTER);
        tab.setSingleLine();

        addTab(position,tab);
    }

    void addTab(final  int position,View tab){
        tab.setFocusable(true);
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(position);
            }
        });
        int top = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics());
        tab.setPadding(0, top, 0, top);
        addView(tab, position, defaultTabLayoutParams);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode()||tabcount==0){
            return;
        }
        final  int height=getHeight();

        //画当前的indicator，
        rectPaint.setColor(indicatorColor);
        View currentTab = getChildAt(currentPosition);
        float lineLeft = currentTab.getLeft();
        float lineRight=currentTab.getRight();
        //如果不在第一个和最后一个标题下，则在中间绘制indicator
        if (currentPositionOffset>0f&&currentPosition<tabcount-1){

            View nextTab = getChildAt(currentPosition + 1);
            final float nextTabLeft = nextTab.getLeft();
            final float nextTabRight = nextTab.getRight();

            lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset) * lineLeft);
            lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset) * lineRight);
        }

        canvas.drawRect(lineLeft, height - indicatorHeight, lineRight, height, rectPaint);

        // draw underline

        rectPaint.setColor(underlineColor);
        canvas.drawRect(0, height - underlineHeight, getWidth(), height, rectPaint);

        // draw divider

        dividerPaint.setColor(dividerColor);
        for (int i = 0; i < tabcount - 1; i++) {
            View tab = getChildAt(i);
            canvas.drawLine(tab.getRight(), dividerPadding, tab.getRight(), height - dividerPadding, dividerPaint);
        }
    }


    private class PageListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            currentPosition=position;
            currentPositionOffset=positionOffset;
            invalidate();

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {
            updateTabStyles();
            if (delegatePageListener != null) {
                delegatePageListener.onPageSelected(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (delegatePageListener != null) {
                delegatePageListener.onPageScrollStateChanged(state);
            }
        }
    }

    private void updateTabStyles() {

        for (int i = 0; i < tabcount; i++) {

            View v = getChildAt(i);

            v.setBackgroundResource(tabBackgroundResId);

            if (v instanceof TextView) {

                TextView tab = (TextView) v;
                tab.setTextSize(TypedValue.COMPLEX_UNIT_SP, tabTextSize);
//                tab.setTypeface(tabTypeface, tabTypefaceStyle);
                if (i == pager.getCurrentItem()) {
                    tab.setTextColor(selectColor);
                } else {
                    tab.setTextColor(tabTextColor);
                }

                // setAllCaps() is only available from API 14, so the upper case is made manually if we are on a
                // pre-ICS-build
                if (textAllCaps) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        tab.setAllCaps(true);
                    } else {
                        tab.setText(tab.getText().toString().toUpperCase(locale));
                    }
                }
            }
        }

    }
    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        currentPosition = savedState.currentPosition;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = currentPosition;
        return savedState;
    }


    //保存状态
    static class SavedState extends BaseSavedState {

        int currentPosition;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    public int getIndicatorColor() {
        return indicatorColor;
    }

    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
    }

    public int getUnderlineColor() {
        return underlineColor;
    }

    public void setUnderlineColor(int underlineColor) {
        this.underlineColor = underlineColor;
    }

    @Override
    public int getDividerPadding() {
        return dividerPadding;
    }

    @Override
    public void setDividerPadding(int dividerPadding) {
        this.dividerPadding = dividerPadding;
    }


    public int getUnderlineHeight() {
        return underlineHeight;
    }

    public void setUnderlineHeight(int underlineHeight) {
        this.underlineHeight = underlineHeight;
    }

    public int getIndicatorHeight() {
        return indicatorHeight;
    }

    public void setIndicatorHeight(int indicatorHeight) {
        this.indicatorHeight = indicatorHeight;
    }

    public int getTabTextColor() {
        return tabTextColor;
    }

    public void setTabTextColor(int tabTextColor) {
        this.tabTextColor = tabTextColor;
    }

    public int getTabTextSize() {
        return tabTextSize;
    }

    public void setTabTextSize(int tabTextSize) {
        this.tabTextSize = tabTextSize;
    }

    public Typeface getTabTypeface() {
        return tabTypeface;
    }

    public void setTabTypeface(Typeface tabTypeface) {
        this.tabTypeface = tabTypeface;
    }

    public int getTabTypefaceStyle() {
        return tabTypefaceStyle;
    }

    public void setTabTypefaceStyle(int tabTypefaceStyle) {
        this.tabTypefaceStyle = tabTypefaceStyle;
    }

    public int getTabBackgroundResId() {
        return tabBackgroundResId;
    }

    public void setTabBackgroundResId(int tabBackgroundResId) {
        this.tabBackgroundResId = tabBackgroundResId;
    }

    public int getSelectColor() {
        return selectColor;
    }

    public void setSelectColor(int selectColor) {
        this.selectColor = selectColor;
    }
}
