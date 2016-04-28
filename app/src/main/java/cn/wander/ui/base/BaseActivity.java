package cn.wander.ui.base;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import cn.wander.base.utils.ScreenUtility;
import cn.wander.kFramework.R;

public class BaseActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private View.OnClickListener clickListener;
    public FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base2);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        loadData();
//        setTitleBarHeight();
    }

    public void loadData() {

    }

    public void setView(View contentLayout) {
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.activity_content);
        if (frameLayout != null) {
            frameLayout.addView(contentLayout);
        }
    }

    public void setView(int resource) {
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.activity_content);
        View view = LayoutInflater.from(this).inflate(resource, null, false);
        if (frameLayout != null && view != null) {
            frameLayout.addView(view);
        }
    }

    private void setTitleBarHeight() {
        //api19以上如若做透明状态栏需要调整头部高度
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && toolbar != null) {
            ViewGroup.LayoutParams params = toolbar.getLayoutParams();
            int h = ScreenUtility.dip2px(25);
            params.height += h;
            toolbar.setPadding(0, h, 0, 0);
            toolbar.setLayoutParams(params);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
