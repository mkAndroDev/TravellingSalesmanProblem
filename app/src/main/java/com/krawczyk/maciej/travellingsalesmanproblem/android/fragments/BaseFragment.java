package com.krawczyk.maciej.travellingsalesmanproblem.android.fragments;

import android.support.v4.app.Fragment;

import com.krawczyk.maciej.travellingsalesmanproblem.android.activities.MainActivity;

import io.realm.Realm;

/**
 * Created by maciejkrawczyk on 07.06.2017.
 */

public class BaseFragment extends Fragment implements MainActivity.MainActivityListener {

    public MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

    public Realm getRealm() {
        return getMainActivity().getRealm();
    }

    @Override
    public void onMenuItemClicked(int menuItemId) {

    }
}
