package com.krawczyk.maciej.travellingsalesmanproblem.android.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.krawczyk.maciej.travellingsalesmanproblem.R;
import com.krawczyk.maciej.travellingsalesmanproblem.data.AdjacencyPoint;
import com.krawczyk.maciej.travellingsalesmanproblem.data.GraphPoint;
import com.krawczyk.maciej.travellingsalesmanproblem.data.Route;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by maciek on 09.06.17.
 */
public class CalculatedRouteFragment extends BaseFragment {

    private final static int METERS_TO_KILOMETERS = 1000;
    private final static String KILOMETERS_PATTERN = "km";

    @BindView(R.id.lv_calculated_route)
    ListView calculatedRouteLV;
    @BindView(R.id.tv_distance)
    TextView distanceTV;

    private OnCalculatedRouteFragmentListener listener;
    private Route route;

    public static CalculatedRouteFragment newInstance(Route route, OnCalculatedRouteFragmentListener listener) {
        CalculatedRouteFragment calculatedRouteFragment = new CalculatedRouteFragment();
        calculatedRouteFragment.listener = listener;
        calculatedRouteFragment.route = route;
        return calculatedRouteFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getView();
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_calculated_route, container, false);
        }

        ButterKnife.bind(this, view);

        setupViews();

        return view;
    }

    private void setupViews() {
        String preparedDistance = String.format(Locale.GERMAN, "%.2f", ((double) route.getDistanceForRoute() / METERS_TO_KILOMETERS)) + KILOMETERS_PATTERN;
        distanceTV.setText(preparedDistance);

        calculatedRouteLV.setAdapter(new ArrayAdapter<GraphPoint>(
                getContext(),
                android.R.layout.simple_list_item_2,
                android.R.id.text1,
                route.getPoints()) {

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(route.getPoints().get(position).getName());
                text2.setText(route.getPoints().get(position).getLon() + ", " + route.getPoints().get(position).getLon());
                return view;
            }
        });

        calculatedRouteLV.setOnItemClickListener((parent, view, position, id) -> {
            for (AdjacencyPoint adjacencyPoint : route.getPoints().get(position).getAdjacencyPoints()) {
                Log.d(CalculatedRouteFragment.class.getSimpleName(), adjacencyPoint.toString());
            }
        });
    }

    @OnClick(R.id.btn_show_on_map)
    void onShowOnMapClicked() {
        if (listener != null) {
            getMainActivity().onBackPressed();
            listener.onShowOnMapClicked(route);
        }
    }

    public interface OnCalculatedRouteFragmentListener {
        void onShowOnMapClicked(Route route);
    }

}
