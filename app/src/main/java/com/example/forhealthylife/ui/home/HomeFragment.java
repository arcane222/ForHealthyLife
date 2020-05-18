package com.example.forhealthylife.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.forhealthylife.MainActivity;
import com.example.forhealthylife.R;

public class HomeFragment extends Fragment
{

    private HomeViewModel homeViewModel;

    public interface OnBtnClickListener
    {
        public void onBtnClick(View V);
    }

    OnBtnClickListener mBtnClickListener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        /* Get Button in home fragment (Run, Eat, Exercise) and Implements three btn listener */
        mBtnClickListener = (MainActivity) getActivity();
        View.OnClickListener onHomeFragBtnClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mBtnClickListener.onBtnClick(v);
            }
        };

        /* Set Button Click Listener in running, eating, exercise button*/
        Button runFragBtn = (Button) root.findViewById(R.id.btn_run);
        Button eatFragBtn = (Button) root.findViewById(R.id.btn_eat);
        Button exerciseFragBtn = (Button) root.findViewById(R.id.btn_exercise);
        Button weightFragBtn = (Button) root.findViewById(R.id.btn_weight);

        runFragBtn.setOnClickListener(onHomeFragBtnClickListener);
        eatFragBtn.setOnClickListener(onHomeFragBtnClickListener);
        exerciseFragBtn.setOnClickListener(onHomeFragBtnClickListener);
        weightFragBtn.setOnClickListener(onHomeFragBtnClickListener);

        return root;
    }
}
