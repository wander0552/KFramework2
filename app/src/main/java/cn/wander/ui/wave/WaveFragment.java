package cn.wander.ui.wave;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import cn.wander.kFramework.R;
import cn.wander.ui.child.ChildActivity;
import cn.wander.ui.circle.CircleActivity;
import cn.wander.ui.encounter.EncounterActivity;
import cn.wander.ui.health.FitnessActivity;

public class WaveFragment extends Fragment implements View.OnClickListener {

    private LinearLayout encounter,child,health,circle;

    public WaveFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_wave, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        circle = (LinearLayout) view.findViewById(R.id.circle);
        encounter = (LinearLayout) view.findViewById(R.id.encounter);
        health = (LinearLayout) view.findViewById(R.id.health);
        child = (LinearLayout) view.findViewById(R.id.child);
        encounter.setOnClickListener(this);
        health.setOnClickListener(this);
        child.setOnClickListener(this);
        circle.setOnClickListener(this);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.child:
                jumpToActivity(ChildActivity.class);
                break;
            case R.id.circle:
                jumpToActivity(CircleActivity.class);
                break;
            case R.id.encounter:
                jumpToActivity(EncounterActivity.class);
                break;
            case R.id.health:
                jumpToActivity(FitnessActivity.class);
                break;
        }
    }

    private void jumpToActivity(Class<?> activity) {
        startActivity(new Intent(this.getActivity(), activity));
    }
}
