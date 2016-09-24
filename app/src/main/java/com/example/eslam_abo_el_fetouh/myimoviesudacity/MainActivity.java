package com.example.eslam_abo_el_fetouh.myimoviesudacity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    MainFragment fragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        AppBarLayout appBar = (AppBarLayout) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        if (findViewById(R.id.fragment_pane) != null){
            appBar.setVisibility(View.GONE);
            mTwoPane = true;
            fragment = new MainFragment();
            LandScapeView fragment1 = new LandScapeView();
            DetailsFragment fragment2 = new DetailsFragment();
                fragment2.setID(0);
                FragmentManager fragmentManager = getSupportFragmentManager();
                if (findViewById(R.id.fragment_pane) != null){
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment, fragment1);
                    fragmentTransaction.commit();
                }else {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment, fragment2);
                    fragmentTransaction.commit();
                }

            }else {
            mTwoPane = false;
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                if (fragment != null) {
                    fragmentTransaction.remove(fragment);
                }
                fragment = new MainFragment();
                fragmentTransaction.replace(R.id.fragment, fragment);
                fragmentTransaction.commit();


        }
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



//    @Override
//    public void onBackPressed() {
//        if (fragmentManager.getBackStackEntryCount() == 1) {
//            this.finish();
//        } else {
//            fragmentManager.popBackStack();
//        }
//    }
}
