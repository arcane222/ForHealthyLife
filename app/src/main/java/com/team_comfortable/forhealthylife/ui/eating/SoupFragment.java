package com.team_comfortable.forhealthylife.ui.eating;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.team_comfortable.forhealthylife.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SoupFragment extends Fragment {

    private EatingViewModel eatingViewModel;

    public static SoupFragment newInstance() {
        return new SoupFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_soup, container, false);
        ListView listview = view.findViewById(R.id.soupName);
        CustomList adapter = new CustomList((Activity) view.getContext());
        listview.setAdapter(adapter);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        eatingViewModel = ViewModelProviders.of(getActivity()).get(EatingViewModel.class);
    }

    public class CustomList extends ArrayAdapter<String>
    {
        private final Activity context;

        public CustomList(Activity context)
        {
            super(context, R.layout.food_item, Data.soupName);
            this.context = context;
        }

        public View getView(int position, View view, ViewGroup parent)
        {
            final int pos = position;
            eatingViewModel.setVersion(1);

            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.food_item, null, true);

            ImageView imageView = (ImageView) rowView.findViewById(R.id.riceImage);
            TextView foodName = (TextView) rowView.findViewById(R.id.riceName);
            final TextView amount = (TextView) rowView.findViewById(R.id.foodAmount);
            Button incrButton = (Button) rowView.findViewById(R.id.incrAmountBtn);
            Button decrButton = (Button) rowView.findViewById(R.id.decrAmountBtn);

            imageView.setImageResource(Data.soupImage[position]);
            foodName.setText(Data.soupName[position]);
            amount.setText(Data.amountCount[position]+"");

            incrButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    eatingViewModel.setInteger(pos);
                    eatingViewModel.setOperation(1);
                    if(Integer.parseInt(amount.getText().toString()) < 9){
                        amount.setText(String.valueOf(Integer.parseInt(amount.getText().toString()) + 1));
                        eatingViewModel.setCount(Integer.parseInt(amount.getText().toString()));
                    }

                }
            });

            decrButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    eatingViewModel.setInteger(pos);
                    eatingViewModel.setOperation(0);
                    if(Integer.parseInt(amount.getText().toString()) > 0) {
                        amount.setText(String.valueOf(Integer.parseInt(amount.getText().toString()) - 1));
                        eatingViewModel.setCount(Integer.parseInt(amount.getText().toString()));
                    }
                }
            });

            return rowView;
        }
    }
}
