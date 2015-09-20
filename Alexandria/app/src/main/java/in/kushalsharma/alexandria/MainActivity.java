package in.kushalsharma.alexandria;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import in.kushalsharma.fragments.AboutFragment;
import in.kushalsharma.fragments.ExploreFragment;
import in.kushalsharma.fragments.LibraryFragment;


public class MainActivity extends AppCompatActivity implements ExploreFragment.MenuCallback, LibraryFragment.MenuCallback, AboutFragment.MenuCallback {

    private ExploreFragment fragmentScan;
    private LibraryFragment fragmentLibrary;
    private AboutFragment fragmentAbout;
    private DrawerLayout mDrawerLayout;

    private boolean mTwoPane;

    private int fragmentState;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("alexandria", Context.MODE_PRIVATE);


        fragmentScan = new ExploreFragment();
        fragmentLibrary = new LibraryFragment();
        fragmentAbout = new AboutFragment();

        fragmentScan.setMenuCallBack(this);
        fragmentLibrary.setMenuCallBack(this);
        fragmentAbout.setMenuCallBack(this);

        if (findViewById(R.id.drawer_layout) != null) {
            mTwoPane = false;
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        } else {
            mTwoPane = true;
        }

        if (savedInstanceState != null) {
            fragmentState = savedInstanceState.getInt("fragmentState");
        } else {
            fragmentState = sharedPreferences.getInt("startFragmentState", 0);
            mDrawerLayout.openDrawer(GravityCompat.START);
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        setFragmentFromState(fragmentState);
    }

    private void setFragmentFromState(int fragmentState) {
        switch (fragmentState) {
            case 0:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_placeholder, fragmentScan).commit();
                break;
            case 1:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_placeholder, fragmentLibrary).commit();
                break;
            case 2:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_placeholder, fragmentAbout).commit();
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("fragmentState", fragmentState);
    }


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        switch (menuItem.getItemId()) {
                            case R.id.nav_scan:
                                fragmentState = 0;
                                setFragmentFromState(fragmentState);

                                if (!mTwoPane) {
                                    mDrawerLayout.closeDrawers();
                                }
                                break;
                            case R.id.nav_library:
                                fragmentState = 1;
                                setFragmentFromState(fragmentState);

                                if (!mTwoPane) {
                                    mDrawerLayout.closeDrawers();
                                }
                                break;
                            case R.id.nav_about:
                                fragmentState = 2;
                                setFragmentFromState(fragmentState);

                                if (!mTwoPane) {
                                    mDrawerLayout.closeDrawers();
                                }
                                break;
                            case R.id.nav_setting:
                                Intent mIntent = new Intent(MainActivity.this, SettingsActivity.class);
                                startActivity(mIntent);
                                break;
                        }
                        return true;
                    }
                });
    }

    @Override
    public void menuPressed() {
        if (!mTwoPane) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void libraryMenuPressed() {
        if (!mTwoPane) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void aboutMenuPressed() {
        if (!mTwoPane) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }
}
