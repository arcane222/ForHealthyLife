package com.team_comfortable.forhealthylife.ui.community;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team_comfortable.forhealthylife.R;
import com.team_comfortable.forhealthylife.ui.calendar.fragment.CalendarFragment;
import com.team_comfortable.forhealthylife.ui.calendar.fragment.ScheduleFragment;

import java.util.ArrayList;

public class CommunityFragment extends Fragment {

    private CommunityViewModel communityViewModel;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;


    public void initFirebase()
    {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);
        ListView listview = view.findViewById(R.id.community_board);
        Button sinupBtn = view.findViewById(R.id.btn_signup);
        Log.i("tag", "1");
        CustomList adapter = new CustomList((Activity) view.getContext());
        Log.i("tag", "2");
        listview.setAdapter(adapter);
        Log.i("tag", "5");

        return view;
    }
    static String[] riceName = {
            "쌀 밥  (300Kcal)",
            "보리밥  (300Kcal)",
            "현미밥  (320Kcal)",
            "오곡밥  (380Kcal)",
            "비빔밥  (600Kcal)",
            "새우볶음밥  (630Kcal)",
            "김치볶음밥  (590Kcal)",
            "오므라이스  (690Kcal)",
            "김 밥  (460Kcal)",
            "스파게티 (650Kcal)",
            "자장면 (700Kcal)"
    };


    public class CustomList extends ArrayAdapter<String>
    {
        private final Activity context;

        public CustomList(Activity context)
        {
            super(context, R.layout.schedule_item, riceName);
            Log.i("tag", "7");
            this.context = context;
            Log.i("tag", "8");
        }

        public View getView(int position, View view, ViewGroup parent)
        {
            Log.i("tag", "3");
            final int pos = position;

            if (view == null)
            {
                final Context context = parent.getContext();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.community_item, parent, false);
            }
            /*
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.community_item, null, true);*/
            Log.i("tag", "4");

            TextView titleList = (TextView) view.findViewById(R.id.communityTitle);
            TextView writerList = (TextView) view.findViewById(R.id.communityWriter);
            TextView timeList = (TextView) view.findViewById(R.id.writeTime);
            titleList.setText(riceName[pos]);
            writerList.setText(riceName[pos]);
            timeList.setText(riceName[pos]);

            Log.i("tag", riceName[pos]);
            return view;
        }

    }

}
