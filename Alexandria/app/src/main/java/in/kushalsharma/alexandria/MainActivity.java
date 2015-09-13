package in.kushalsharma.alexandria;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import in.kushalsharma.fragments.BlankFragment;
import in.kushalsharma.fragments.ExploreFragment;


public class MainActivity extends AppCompatActivity implements ExploreFragment.MenuCallback {

    private ExploreFragment fragmentScan;
    private BlankFragment fragmentLibrary;
    private BlankFragment fragmentAbout;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentScan = new ExploreFragment();
        fragmentLibrary = new BlankFragment();
        fragmentAbout = new BlankFragment();

        fragmentScan.setMenuCallBack(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_placeholder, fragmentScan).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);

                        switch (menuItem.getItemId()) {
                            case R.id.nav_scan:
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_placeholder, fragmentScan).commit();
                                break;
                            case R.id.nav_library:
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_placeholder, fragmentLibrary).commit();
                                break;
                            case R.id.nav_about:
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_placeholder, fragmentAbout).commit();
                                break;
                        }

                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    public void menuPressed() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }
}
