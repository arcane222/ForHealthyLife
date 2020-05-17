package com.example.forhealthylife.ui.eating;

import androidx.fragment.app.FragmentTransaction;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.forhealthylife.MainActivity;
import com.example.forhealthylife.R;
import com.example.forhealthylife.ui.home.HomeFragment;


public class EatingFragment extends Fragment
{

    private EatingViewModel eatingViewModel;
    private TextView kcalView;
    private int kcalSum;
    private int pos;
    private int opr;

    public interface OnListSelectedListener
    {
        public void onListSelected(int position);
    }

    OnListSelectedListener mListSelListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {

        View root = inflater.inflate(R.layout.fragment_eating, container, false);


        ListView listview = root.findViewById(R.id.eat_list);
        kcalView = root.findViewById(R.id.calculKcal);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Data.eats);
        listview.setAdapter(adapter);
        mListSelListener = (MainActivity) getActivity();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListSelListener.onListSelected(position);
            }
        });
        /*
        if(savedInstanceState != null){
            int pos = getArguments().getInt("position");
            textview.setText(Data.riceKcal[pos]+"Kcal");
        }
        */


        //listview.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Data.eats));
        /*mWordSelListener = (MainActivity) getActivity();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mWordSelListener.onWordSelected(position);

            }
        });*/
        return root;
    }
/*
    public class CustomList extends ArrayAdapter<String> {
        private final Activity context;
        public CustomList(Activity context) {
            super(context, R.layout.food_item, Data.riceName);
            this.context = context;
        }
    }*/

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {

        super.onActivityCreated(savedInstanceState);
        eatingViewModel =  ViewModelProviders.of(getActivity()).get(EatingViewModel.class);

        eatingViewModel.getInteger().observe(this, new Observer<Integer>()
        {
            @Override
            public void onChanged(Integer integer)
            {
                pos = integer;
            }
        });
        eatingViewModel.getOperation().observe(this, new Observer<Integer>()
        {
            @Override
            public void onChanged(Integer integer)
            {
                opr = integer;
            }
        });
        eatingViewModel.getCount().observe(this, new Observer<Integer>()
        {

            @Override
            public void onChanged(Integer integer)
            {
                if(opr == 1){
                    if(integer > 0){
                        kcalSum += Data.riceKcal[pos];
                        kcalView.setText(String.valueOf(kcalSum));
                    }
                }

                else{
                    if(integer >= 0){
                        kcalSum -= Data.riceKcal[pos];
                        kcalView.setText(String.valueOf(kcalSum));
                    }
                }

            }
        });
    }

    public void onFoodBtnClicked(View view)
    {

    }

    /*
    public void onStart() {
        super.onStart();

        Bundle args = getArguments();
        if (args != null) {
            int pos = args.getInt("position");
            textview.setText(Data.riceKcal[pos]+"Kcal");
        }
    }
    */
}
