package com.krawczyk.maciej.travellingsalesmanproblem.android.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.krawczyk.maciej.travellingsalesmanproblem.R;
import com.krawczyk.maciej.travellingsalesmanproblem.android.fragments.MapFragment;

public class MainActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(MapFragment.newInstance(), false);
    }

    private void loadFragment(Fragment fragment, boolean addToBacktack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fl_fragment_content, fragment);
        if (addToBacktack) {
            transaction.addToBackStack(fragment.getClass().getName());
        }
        transaction.commitAllowingStateLoss();
    }
}
