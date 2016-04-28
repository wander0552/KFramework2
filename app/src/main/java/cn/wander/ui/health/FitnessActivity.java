package cn.wander.ui.health;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import cn.wander.Utils.views.ColorArcProgressBar;
import cn.wander.base.utils.DialogUtils;
import cn.wander.kFramework.R;
import cn.wander.ui.base.BaseActivity;

public class FitnessActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(LayoutInflater.from(this).inflate(R.layout.activity_fitness, null, false));
        initView();
    }

    public void initView() {
        ColorArcProgressBar colorArcProgressBar = (ColorArcProgressBar) findViewById(R.id.stepBar);
        colorArcProgressBar.setCurrentValues(80);


    }

    public void fitClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                DialogUtils.showDialog_NOResponse(this, "设置接收声音的范围、性别、年龄...");
                break;
            case R.id.button2:
                DialogUtils.showDialog_NOResponse(this, "跳转到播放器歌单，正在播放，列表选择");
                break;
            case R.id.button3:
                //周边的人
                break;
            case R.id.button4:
                //开启雷达
                break;
            case R.id.button5:
                //吼一声
                break;
        }
    }
}
