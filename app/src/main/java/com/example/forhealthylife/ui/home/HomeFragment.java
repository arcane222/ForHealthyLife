package com.example.forhealthylife.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.forhealthylife.R;
import com.example.forhealthylife.ui.running.RunningFragment;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        Button button = (Button) root.findViewById(R.id.btn_running);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Fragment fg;
                //view.findViewById(R.id.btn_running)
                if(v.getId() == R.id.btn_running) {
                    fg = new RunningFragment();

                    FragmentTransaction ft = getChildFragmentManager().beginTransaction();

                    ft.replace(R.id.child_fragment_container, fg);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            }
        });



        return root;




    }



}
