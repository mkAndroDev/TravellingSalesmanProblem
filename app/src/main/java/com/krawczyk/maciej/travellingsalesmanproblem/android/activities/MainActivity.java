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

        realm = Realm.getDefaultInstance();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        loadFragment(MapFragment.newInstance(), false);
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
    }

    private void loadFragment(Fragment fragment, boolean addToBacktack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fl_fragment_content, fragment);
        if (addToBacktack) {
            transaction.addToBackStack(fragment.getClass().getName());
        }
        transaction.commitAllowingStateLoss();
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.map:
                loadFragment(MapFragment.newInstance(), false);
            case R.id.nav_clear_map:
                if (mainActivityListener != null) {
                    mainActivityListener.onMenuItemClicked(R.id.nav_clear_map);
                }
                break;
            case R.id.nav_calculate_route:
                if (mainActivityListener != null) {
                    mainActivityListener.onMenuItemClicked(R.id.nav_calculate_route);
                }
                break;
            case R.id.nav_historical_routes:
                loadFragment(HistoricallyRoutesFragment.newInstance(), true);
            case R.id.nav_about_author:
                break;
            default:
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public Realm getRealm() {
        return realm;
    }

    public interface MainActivityListener {
        void onMenuItemClicked(int menuItemId);
    }
}
