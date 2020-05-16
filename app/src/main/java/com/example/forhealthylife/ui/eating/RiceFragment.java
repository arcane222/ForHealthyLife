package com.example.forhealthylife.ui.eating;

import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.forhealthylife.R;

public class RiceFragment extends Fragment {

    private RiceViewModel mViewModel;

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
       // listview.setAdapter(new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_checked, Data.riceName));
        /*mWordSelListener = (MainActivity) getActivity();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mWordSelListener.onWordSelected(position);
            }
        });*/

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RiceViewModel.class);
        // TODO: Use the ViewModel
    }

    public class CustomList extends ArrayAdapter<String> {
        private final Activity context;
        public CustomList(Activity context) {
            super(context, R.layout.food_item, Data.riceName);
            this.context = context;
        }

        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.food_item, null, true);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.riceImage);
            TextView name = (TextView) rowView.findViewById(R.id.riceName);
            name.setText(Data.riceName[position]);
            imageView.setImageResource(Data.riceImage[position]);

            return rowView;





        }
    }

}
