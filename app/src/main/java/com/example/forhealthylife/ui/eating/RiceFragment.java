package com.example.forhealthylife.ui.eating;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.forhealthylife.MainActivity;
import com.example.forhealthylife.R;

public class RiceFragment extends Fragment {

    //private EatingViewModel eatingViewModel;

    public static RiceFragment newInstance() {
        return new RiceFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rice, container, false);
        ListView listview = view.findViewById(R.id.riceName);
        CustomList adapter = new CustomList((Activity) view.getContext());
        listview.setAdapter(adapter);
       //listview.setAdapter(new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_checked, Data.riceName));
        /*mWordSelListener = (MainActivity) getActivity();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mWordSelListener.onWordSelected(position);
            }
        });*/

        /*listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mEatModel.setInteger(position);
            }
        });*/


        return view;

    }
    /*
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eatingViewModel = ViewModelProviders.of(this).get(EatingViewModel.class);

    }
    */

    public class CustomList extends ArrayAdapter<String> {
        private final Activity context;
        public CustomList(Activity context) {
            super(context, R.layout.food_item, Data.riceName);
            this.context = context;
        }

        public View getView(int position, View view, ViewGroup parent) {
            final int pos = position;

            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.food_item, null, true);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.riceImage);
            TextView name = (TextView) rowView.findViewById(R.id.riceName);
            name.setText(Data.riceName[position]);
            imageView.setImageResource(Data.riceImage[position]);


            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //eatingViewModel.setInteger(pos);
                    Toast.makeText(context, pos + "번째 이미지 선택", Toast.LENGTH_SHORT).show();

                    /*EatingFragment eatF = new EatingFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("position",pos);
                    eatF.setArguments(bundle);*/
                }
            });

            return rowView;

        }
    }

    /*
    @Override
    public void onBackKey() {
        MainActivity activity = (MainActivity) getActivity();
        activity.setOnKeyBackPressedListener(null);
        activity.onBackPressed();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity)context).setOnKeyBackPressedListener(this);
    }
    */
}
