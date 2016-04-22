package cn.wander.kFramework;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import cn.wander.ui.friends.FriendsFragment;
import cn.wander.ui.mine.MineFragment;
import cn.wander.ui.wave.WaveFragment;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private MainTabsAdapter mainTabsAdapter;
    private int currentPosition;
    private ImageView img_square;
    private ImageView img_friends;
    private ImageView img_mine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        mainTabsAdapter = new MainTabsAdapter(this);
        mainTabsAdapter.addTab("square", "声波", WaveFragment.class, null);
        mainTabsAdapter.addTab("friends", "知音", FriendsFragment.class, null);
        mainTabsAdapter.addTab("Mine", "我", MineFragment.class, null);
        viewPager.setAdapter(mainTabsAdapter);

        viewPager.setOffscreenPageLimit(4);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (currentPosition == position) {
                    return;
                }
                currentPosition = position;
                setIcon(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        initView();


    }

    private void initView() {
        img_square = (ImageView) findViewById(R.id.square);
        img_square.setOnClickListener(this);
        img_friends = (ImageView) findViewById(R.id.bosom);
        img_friends.setOnClickListener(this);
        img_mine = (ImageView) findViewById(R.id.mine);
        img_mine.setOnClickListener(this);

        setIcon(currentPosition);
    }

    private void setIcon(int position) {
        img_square.setSelected(0 == position);
        img_friends.setSelected(1 == position);
        img_mine.setSelected(2 == position);
    }

    @Override
    public MenuInflater getMenuInflater() {
        return super.getMenuInflater();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.square:
                viewPager.setCurrentItem(0);
                break;
            case R.id.bosom:
                viewPager.setCurrentItem(1);
                break;
            case R.id.mine:
                viewPager.setCurrentItem(2);
                break;
        }

    }
}
