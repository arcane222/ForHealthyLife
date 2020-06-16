package com.team_comfortable.forhealthylife.ui.home;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.team_comfortable.forhealthylife.MainActivity;
import com.team_comfortable.forhealthylife.R;


public class HomeFragment extends Fragment
{
    public interface OnViewClickListener
    {
        public void onViewClick(View view);
    }
    private OnViewClickListener mOnViewClickListener;
    private ImageView iv_healthInformation1, iv_healthInformation2,
            iv_healthInformation3, iv_healthInformation4, iv_healthInformation5;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        initData(root);
        return root;
    }

    private void initData(View root)
    {
        /* Get Button in home fragment (Run, Eat, Exercise) and Implements three btn listener */
        mOnViewClickListener = (MainActivity) getActivity();
        View.OnClickListener onHomeFragBtnClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mOnViewClickListener.onViewClick(v);
            }
        };

        /* Set Button Click Listener in running, eating, exercise button*/
        Button runFragBtn = (Button) root.findViewById(R.id.btn_run);
        Button eatFragBtn = (Button) root.findViewById(R.id.btn_eat);
        Button exerciseFragBtn = (Button) root.findViewById(R.id.btn_exercise);
        Button weightFragBtn = (Button) root.findViewById(R.id.btn_weight);
        iv_healthInformation1 = root.findViewById(R.id.iv_home_healthInformation1);
        iv_healthInformation2 = root.findViewById(R.id.iv_home_healthInformation2);
        iv_healthInformation3 = root.findViewById(R.id.iv_home_healthInformation3);
        iv_healthInformation4 = root.findViewById(R.id.iv_home_healthInformation4);
        iv_healthInformation5 = root.findViewById(R.id.iv_home_healthInformation5);

        runFragBtn.setOnClickListener(onHomeFragBtnClickListener);
        eatFragBtn.setOnClickListener(onHomeFragBtnClickListener);
        exerciseFragBtn.setOnClickListener(onHomeFragBtnClickListener);
        weightFragBtn.setOnClickListener(onHomeFragBtnClickListener);
        iv_healthInformation1.setOnClickListener(onHomeFragBtnClickListener);
        iv_healthInformation2.setOnClickListener(onHomeFragBtnClickListener);
        iv_healthInformation3.setOnClickListener(onHomeFragBtnClickListener);
        iv_healthInformation4.setOnClickListener(onHomeFragBtnClickListener);
        iv_healthInformation5.setOnClickListener(onHomeFragBtnClickListener);
    }
}
