package com.android.tandur;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.tandur.bottom_navigation.favorite.FavoriteFragment;
import com.android.tandur.bottom_navigation.home.HomeFragment;
import com.android.tandur.bottom_navigation.profile.ProfileFragment;
import com.android.tandur.bottom_navigation.transaction.TransactionFragment;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.tandur.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends Activity implements View.OnClickListener{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.card1).setOnClickListener(this);
        findViewById(R.id.card2).setOnClickListener(this);
        findViewById(R.id.card3).setOnClickListener(this);
        findViewById(R.id.card4).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.card1 || view.getId() == R.id.card2 || view.getId() == R.id.card3 || view.getId() == R.id.card4){
            Intent intent = new Intent(MainActivity.this, LahanActivity.class);
            startActivity(intent);
        }

    }
}

//public class MainActivity extends AppCompatActivity{
//
//    private BottomNavigationView botnav;
//
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
//        super.onCreate(savedInstanceState, persistentState);
//
//


//        //bottom navigation
//        botnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                Fragment fragment;
//                switch (item.getItemId()){
//                    case R.id.navigation_home:
//                        fragment = new HomeFragment();
//                        loadFragment(fragment);
//                        return true;
//                    case R.id.navigation_favorite:
//                        fragment = new FavoriteFragment();
//                        loadFragment(fragment);
//                        return true;
//                    case R.id.navigation_transaction:
//                        fragment = new TransactionFragment();
//                        loadFragment(fragment);
//                        return true;
//                    case R.id.navigation_profile:
//                        fragment = new ProfileFragment();
//                        loadFragment(fragment);
//                        return true;
//                }
//                return false;
//            }
//        });

//    }

//    //load fragment
//    private void loadFragment(Fragment fragment){
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.fragment_container, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
////    }
//}
