package com.krawczyk.maciej.travellingsalesmanproblem.android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.krawczyk.maciej.travellingsalesmanproblem.R;
import com.krawczyk.maciej.travellingsalesmanproblem.data.Graph;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by maciejkrawczyk on 07.06.2017.
 */
public class HistoricallyRoutesFragment extends BaseFragment {

    @BindView(R.id.lv_historically_routes)
    ListView historicallyRoutes;
    ArrayList<String> strings;

    public static HistoricallyRoutesFragment newInstance() {
        return new HistoricallyRoutesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getView();
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_historically_routes, container, false);
        }

        for (int i = 0; i < getRealm().allObjects(Graph.class).size(); i++) {
            strings.add(i + " graph");
        }

        historicallyRoutes.setAdapter(new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_list_item_1,
                strings
        ));

        historicallyRoutes.setOnItemClickListener((adapterView, view1, i, l) -> {
            Toast.makeText(getContext(), "Item: " + i + " clicked", Toast.LENGTH_SHORT).show();
        });

        getMainActivity().setupMainActivityListener(this);

        return view;
    }

}
