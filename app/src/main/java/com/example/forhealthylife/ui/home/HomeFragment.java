package com.example.forhealthylife.ui.home;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.forhealthylife.MainActivity;
import com.example.forhealthylife.R;
import com.example.forhealthylife.ui.eating.EatingFragment;
import com.example.forhealthylife.ui.exercise.ExerciseFragment;
import com.example.forhealthylife.ui.running.RunningFragment;

import java.util.Stack;

public class HomeFragment extends Fragment
{

    private HomeViewModel homeViewModel;

    public interface OnBtnClikListener {
        public void onBtnClik(View V);
    }

    OnBtnClikListener mBtnClikListener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        //Fragment first = manager.findFragmentById(R.id.fragment_home);




        Button runBtn = (Button) root.findViewById(R.id.btn_run);
        runBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //mBtnClikListener.onBtnClik(v);
            }
        });

        mBtnClikListener = (MainActivity) getActivity();
        Button eatBtn = (Button) root.findViewById(R.id.btn_eat);
        eatBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mBtnClikListener.onBtnClik(v);
                /*Fragment menu = new EatingFragment();
                FragmentTransaction menuFt = getChildFragmentManager().beginTransaction();

                menuFt.replace(R.id.fragment_home, menu);
                menuFt.addToBackStack(null);
                menuFt.commit();*/
            }
        });

        Button exeBtn = (Button) root.findViewById(R.id.btn_exe);
        exeBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Fragment menu = new ExerciseFragment();
                FragmentTransaction menuFt = getChildFragmentManager().beginTransaction();
                menuFt.replace(R.id.fragment_home, menu);
                menuFt.addToBackStack(null);
                menuFt.commit();
            }
        });


        return root;

    }


}
