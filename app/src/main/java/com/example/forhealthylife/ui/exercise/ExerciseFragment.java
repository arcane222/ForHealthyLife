package com.example.forhealthylife.ui.exercise;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterViewFlipper;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.forhealthylife.MainActivity;
import com.example.forhealthylife.R;
import com.example.forhealthylife.ui.eating.EatingFragment;
import com.example.forhealthylife.ui.eating.EatingViewModel;

import java.util.ArrayList;
import java.util.List;

public class ExerciseFragment extends Fragment {

    private ExerciseViewModel exerciseViewModel;
    private int pos;
    AdapterViewFlipper v_fllipper;
    List<Integer> imageId = new ArrayList<Integer>();

    EatingFragment.OnListSelectedListener mListSelListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        exerciseViewModel =
                ViewModelProviders.of(this).get(ExerciseViewModel.class);
        View root = inflater.inflate(R.layout.fragment_exercise, container, false);
        ListView listview = root.findViewById(R.id.exercise_list);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, DataExercise.exercise);
        listview.setAdapter(adapter);




        v_fllipper = (AdapterViewFlipper) root.findViewById(R.id.exerciseViewFlipper);



        mListSelListener = (MainActivity) getActivity();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListSelListener.onListSelected(position+5);
            }
        });

        /*
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imageId.add(R.drawable.image1);
                imageId.add(R.drawable.image2);
                v_fllipper.setAdapter(new galleryAdapter(getActivity()));

                v_fllipper.setFlipInterval(1000);

                v_fllipper.startFlipping();
            }
        });*/




        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        exerciseViewModel = ViewModelProviders.of(getActivity()).get(ExerciseViewModel.class);

        exerciseViewModel.getInteger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                pos = integer;

                onImage(pos);

           }
        });

    }

    public void onImage(Integer position){
        imageId.clear();
        switch(position){
            case 0:
                for(int i =0; i < DataExercise.stretchImage.length; i++){
                    imageId.add(DataExercise.stretchImage[i]);
                }
                break;
            case 1:
                imageId.add(R.drawable.childe_stretching1);
                imageId.add(R.drawable.childe_stretching2);
                imageId.add(R.drawable.childe_stretching3);
                break;
            case 2:
                imageId.add(R.drawable.twist_stretching1);
                imageId.add(R.drawable.twist_stretching2);
                imageId.add(R.drawable.twist_stretching3);
                imageId.add(R.drawable.twist_stretching4);
                break;

        }

        v_fllipper.setAdapter(new galleryAdapter(getActivity()));

        v_fllipper.setFlipInterval(1000);

        v_fllipper.startFlipping();

    }




    public class galleryAdapter extends BaseAdapter {

        private final Context mContext;
        LayoutInflater inflater;

        public galleryAdapter(Context c) {
            mContext = c;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return imageId.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = inflater.inflate(R.layout.exercise_item, parent, false);
            }
            ImageView imageView = (ImageView) convertView.findViewById(R.id.exerciseView);
            imageView.setImageResource(imageId.get(position));
            return convertView;
        }
    }








}
