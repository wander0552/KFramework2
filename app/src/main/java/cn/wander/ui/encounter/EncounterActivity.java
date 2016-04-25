package cn.wander.ui.encounter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import cn.wander.kFramework.R;
import cn.wander.ui.base.BaseActivity;

/**
 * Created by wander on 2016/4/25.
 * email 805677461@qq.com
 */
public class EncounterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.encounter_activity);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EncounterActivity.this, EncounterPublishActivity.class));
            }
        });
    }

}
