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

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team_comfortable.forhealthylife.R;


public class FastFoodFragment extends Fragment {

    private EatingViewModel eatingViewModel;

    public static FastFoodFragment newInstance() {
        return new FastFoodFragment();
    }

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    private void initFirebase()
    {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
    }
    private int kcalSum;

    public void setKcalFastFood(int kcal, int check){
        final int Kcal = kcal;
        final int Check = check;
        initFirebase();
        final DatabaseReference kcalInDB = mReference.child("UserList").child(mUser.getUid());
        kcalInDB.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String key = data.getKey();
                    if(key.equals("userKcal")){
                        if(Check == 0){
                            kcalSum = Integer.parseInt(data.getValue().toString());
                            kcalSum += Kcal;
                            kcalInDB.child(key).setValue(kcalSum);
                            eatingViewModel.setInteger(Kcal);
                            break;
                        }
                        else if(Check == 1){
                            kcalSum = Integer.parseInt(data.getValue().toString());
                            kcalSum -= Kcal;
                            kcalInDB.child(key).setValue(kcalSum);
                            eatingViewModel.setInteger(Kcal);
                            break;
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_fast_food, container, false);
        ListView listview = view.findViewById(R.id.fastfoodName);
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
            super(context, R.layout.food_item, Data.fastFoodName);
            this.context = context;
        }

        public View getView(int position, View view, ViewGroup parent)
        {
            final int pos = position;


            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.food_item, null, true);

            ImageView imageView = (ImageView) rowView.findViewById(R.id.riceImage);
            TextView foodName = (TextView) rowView.findViewById(R.id.riceName);
            Button incrButton = (Button) rowView.findViewById(R.id.incrAmountBtn);
            imageView.setImageResource(Data.fastFoodImage[position]);
            foodName.setText(Data.fastFoodName[position]);


            incrButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    final int kcal = Data.fastFoodKcal[pos];
                    setKcalFastFood(kcal,0);
                    Snackbar.make(v, Data.fastFoodName[pos]+" 이 추가되었습니다.",
                            Snackbar.LENGTH_LONG).setAction("되돌리기", new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            setKcalFastFood(kcal,1);
                        }
                    }).show();


                }
            });



            return rowView;
        }
    }
}
