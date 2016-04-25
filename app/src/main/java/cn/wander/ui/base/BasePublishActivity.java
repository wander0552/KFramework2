package cn.wander.ui.base;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

import cn.wander.base.utils.ScreenUtility;
import cn.wander.kFramework.R;

public class BasePublishActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_publish);
        initView();
        initToolBar();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
    }


    void initToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitleBarHeight();
    }

    private void setTitleBarHeight() {
        //api19以上如若做透明状态栏需要调整头部高度
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && toolbar!= null) {
            ViewGroup.LayoutParams params = toolbar.getLayoutParams();
            int h = ScreenUtility.dip2px(25);
            params.height += h;
            toolbar.setPadding(0, h, 0, 0);
            toolbar.setLayoutParams(params);
        }
    }


}
