package com.krawczyk.maciej.travellingsalesmanproblem.android.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.krawczyk.maciej.travellingsalesmanproblem.R;
import com.krawczyk.maciej.travellingsalesmanproblem.android.fragments.HistoricallyRoutesFragment;
import com.krawczyk.maciej.travellingsalesmanproblem.android.fragments.MapFragment;
import com.krawczyk.maciej.travellingsalesmanproblem.data.Route;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MainActivityListener mainActivityListener;
    private DrawerLayout drawer;
    Realm realm;

    public void setupMainActivityListener(MainActivityListener listener) {
        mainActivityListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupRealm();

        setupViews();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        loadFragment(MapFragment.newInstance(), false, MapFragment.class.getSimpleName());
    }

    private void setupViews() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    private void setupRealm() {
        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .name("graphdb.realm")
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(1)
                .build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_show_map:
                if (getSupportFragmentManager().findFragmentByTag(MapFragment.class.getSimpleName()) != null
                        && !getSupportFragmentManager().findFragmentByTag(MapFragment.class.getSimpleName()).isVisible()) {
                    loadFragment(MapFragment.newInstance(), false, MapFragment.class.getSimpleName());
                }
            case R.id.nav_clear_map:
                if (mainActivityListener != null) {
                    mainActivityListener.onMenuItemClicked(R.id.nav_clear_map);
                }
            case R.id.nav_calculate_route:
                if (mainActivityListener != null) {
git                     mainActivityListener.onMenuItemClicked(R.id.nav_calculate_route);
                }
                break;
            case R.id.nav_historical_routes:

                HistoricallyRoutesFragment.OnHistoricallyRoutesFragmentListener onHistoricallyRoutesFragmentListener = route -> {
                    if (mainActivityListener != null) {
                        mainActivityListener.showRouteOnMap(route);
                    }
                };

                loadFragment(HistoricallyRoutesFragment.newInstance(onHistoricallyRoutesFragmentListener), true, HistoricallyRoutesFragment.class.getSimpleName());
            case R.id.nav_about_author:
                break;
            default:
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    public Realm getRealm() {
        return realm;
    }

    public void loadFragment(Fragment fragment, boolean addToBackStack, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fl_fragment_content, fragment, tag);
        if (addToBackStack) {
            transaction.addToBackStack(fragment.getClass().getName());
        }
        transaction.commitAllowingStateLoss();
    }

    public interface MainActivityListener {
        void onMenuItemClicked(int menuItemId);

        void showRouteOnMap(Route route);
    }
}
