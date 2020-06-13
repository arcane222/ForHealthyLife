package com.team_comfortable.forhealthylife;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.team_comfortable.forhealthylife.ui.eating.DrinkFragment;
import com.team_comfortable.forhealthylife.ui.eating.EatingFragment;
import com.team_comfortable.forhealthylife.ui.eating.FastFoodFragment;
import com.team_comfortable.forhealthylife.ui.eating.RiceFragment;
import com.team_comfortable.forhealthylife.ui.eating.SideDishFragment;
import com.team_comfortable.forhealthylife.ui.eating.SoupFragment;
import com.team_comfortable.forhealthylife.ui.exercise.BackFragment;
import com.team_comfortable.forhealthylife.ui.exercise.ExerciseFragment;
import com.team_comfortable.forhealthylife.ui.home.HomeFragment;
import com.team_comfortable.forhealthylife.ui.running.RunningFragment;
import com.team_comfortable.forhealthylife.ui.weight.WeightFragment;
import com.team_comfortable.forhealthylife.ui.exercise.ChestFragment;
import com.team_comfortable.forhealthylife.ui.exercise.CoreFragment;
import com.team_comfortable.forhealthylife.ui.exercise.StretchingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity implements EatingFragment.OnListSelectedListener, HomeFragment.OnBtnClickListener
{
    private static final int PERMISSIONS_REQUEST_CODE = 1000;
    private static final String[] REQUEST_PERMISSIONS = {Manifest.permission.ACTIVITY_RECOGNITION};
    private AppBarConfiguration mAppBarConfiguration;
    private static final int REQUEST_RECOGNITION  = 1;
    private View permissionBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initBottomNavMenu();
        setProfile();
    }

    public void setProfile()
    {
        NavigationView navigationView = (NavigationView) findViewById(R.id.side_nav_view);
        View header = navigationView.getHeaderView(0);

        TextView userNameView = (TextView) header.findViewById(R.id.userName);
        TextView userEmailView = (TextView) header.findViewById(R.id.userEmail);
        TextView userIdView = (TextView) header.findViewById(R.id.userId);
        ImageView userImgView = (ImageView) header.findViewById(R.id.user_img);
        Button logOutBtn = (Button) header.findViewById(R.id.btn_logout);
        logOutBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
                mAuth.signOut(); // Firebase Sign out
                mGoogleSignInClient.signOut(); // Google Sign out
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                Toast.makeText(getApplicationContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }
        });

        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");
        String userEmail = intent.getStringExtra("userEmail");
        String userId = intent.getStringExtra("userId");
        String userImgUrl = intent.getStringExtra("userImgUrl");

        userNameView.setText("사용자 : " + userName);
        userEmailView.setText("이메일 : " + userEmail);
        userIdView.setText("사용자UID : " + userId);
        Glide.with(this).load(userImgUrl).into(userImgView);
    }

    public void onTouchCaneldarPanel(View v)
    {
        v.setBackgroundColor(Color.GREEN);
    }


    // 상단 툴바 초기설정 메소드
    public void initToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.side_nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.side_menu_profile, R.id.side_menu_analysis, R.id.side_menu_notice,
                R.id.side_menu_settings, R.id.bottom_menu_home, R.id.bottom_menu_schedule,
                R.id.bottom_menu_community).setDrawerLayout(drawer).build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }


    // 하단 메뉴 설정 메소드
    public void initBottomNavMenu()
    {
        BottomNavigationView navView = findViewById(R.id.bottom_nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.bottom_menu_home, R.id.bottom_menu_schedule, R.id.bottom_menu_community)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public void onListSelected(int position)
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (position){
            case 0:
                RiceFragment riceFragment = new RiceFragment();
                transaction.replace(R.id.eat_list_layout, riceFragment);
                break;
            case 1:
                SoupFragment soupFragment = new SoupFragment();
                transaction.replace(R.id.eat_list_layout, soupFragment);
                break;
            case 2:
                SideDishFragment sidedishFragment = new SideDishFragment();
                transaction.replace(R.id.eat_list_layout, sidedishFragment);
                break;
            case 3:
                FastFoodFragment fastfoodFragment = new FastFoodFragment();
                transaction.replace(R.id.eat_list_layout, fastfoodFragment);
                break;
            case 4:
                DrinkFragment drinkFragment = new DrinkFragment();
                transaction.replace(R.id.eat_list_layout, drinkFragment);
                break;
            case 5:
                StretchingFragment stretchFragment = new StretchingFragment();
                transaction.replace(R.id.exercise_list_layout, stretchFragment);
                break;
            case 6:
                ChestFragment chestFragment = new ChestFragment();
                transaction.replace(R.id.exercise_list_layout, chestFragment);
                break;
            case 7:
                BackFragment backFragment = new BackFragment();
                transaction.replace(R.id.exercise_list_layout, backFragment);
                break;
            case 8:
                CoreFragment coreFragment = new CoreFragment();
                transaction.replace(R.id.exercise_list_layout, coreFragment);
                break;

        }
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /* Home Fragment Button Click Listener (Eating, Running, Exercise) */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void onBtnClick(View v)
    {
        Fragment eatingFragment = new EatingFragment();
        Fragment runningFragment = new RunningFragment();
        Fragment exerciseFragment = new ExerciseFragment();
        Fragment weightFragment = new WeightFragment();

        FragmentTransaction menuFragTransaction = getSupportFragmentManager().beginTransaction();
        switch(v.getId())
        {
            case R.id.btn_run:
                permissionBtn = v;
                boolean bool = checkPermission();
                if(bool) menuFragTransaction.replace(R.id.fragment_home, runningFragment);
                break;
            case R.id.btn_eat:
                menuFragTransaction.replace(R.id.fragment_home, eatingFragment);
                break;
            case R.id.btn_exercise:
                menuFragTransaction.replace(R.id.fragment_home, exerciseFragment);
                break;
            case R.id.btn_weight:
                menuFragTransaction.replace(R.id.fragment_home, weightFragment);
                break;
            default:
                break;
        }
        menuFragTransaction.addToBackStack(null);
        menuFragTransaction.commit();
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public boolean checkPermission()
    {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION);
        if(permissionCheck != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACTIVITY_RECOGNITION))
            {
                Snackbar.make(permissionBtn, "이 기능을 실행하려면 신체활동 접근 권한이 필요합니다.",
                        Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        // 사용자게에 퍼미션 요청. 요청 결과는 onRequestPermissionResult 에 수신.
                        ActivityCompat.requestPermissions(MainActivity.this, REQUEST_PERMISSIONS,
                                PERMISSIONS_REQUEST_CODE);
                    }
                }).show();
            }
            else
            {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(this, REQUEST_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        if ( requestCode == PERMISSIONS_REQUEST_CODE && grantResults.length == 1) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            boolean check_result = true;
            // 모든 퍼미션을 허용했는지 체크합니다.
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if(!check_result){
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUEST_PERMISSIONS[0]))
                {
                    // 사용자가 거부만 선택한 경우에는 앱을 다시 실행하여 허용을 선택하면 앱을 사용할 수 있습니다.
                    Snackbar.make(permissionBtn, "접근권한이 거부되었습니다. \n앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view) {
                        }
                    }).show();
                }
                else
                {
                    // “다시 묻지 않음”을 사용자가 체크하고 거부를 선택한 경우에는 설정(앱 정보)에서 퍼미션을 허용해야 앱을 사용할 수 있습니다.
                    Snackbar.make(permissionBtn, "접근권한이 거부되었습니다. \n설정(앱 정보)에서 신체활동권한을 허용해야 합니다. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    }).show();
                }
            }
        }
    }


    /*
    public interface onKeyBackPressedListener { void onBackKey(); }
    private onKeyBackPressedListener mOnKeyBackPressedListener;
    public void setOnKeyBackPressedListener(onKeyBackPressedListener listener) {
        mOnKeyBackPressedListener = listener;
    }
    //EndToast endToast = new EndToast(this);
    @Override public void onBackPressed() {
        if (mOnKeyBackPressedListener != null) {
            mOnKeyBackPressedListener.onBackKey();
        } else {
            if(getSupportFragmentManager().getBackStackEntryCount()==0){
                //endToast.showEndToast("종료하려면 한번 더 누르세요.");
            }
            else{ super.onBackPressed();
            }
        }
    }
    */
}
