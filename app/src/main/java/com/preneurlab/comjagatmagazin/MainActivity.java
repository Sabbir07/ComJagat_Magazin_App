package com.preneurlab.comjagatmagazin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.preneurlab.comjagatmagazin.R;
import com.preneurlab.fragments_navigation.NavItem;
import com.preneurlab.fragments_navigation.NavListAdapter;
import com.preneurlab.fragments_navigation.MyAbout;
import com.preneurlab.fragments_navigation.MyHome;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    RelativeLayout drawerPane;
    ListView lvNav;
    ActionBarDrawerToggle actionBarDrawerToggle;

    List<NavItem> listNavItems;
    List<Fragment> listFragments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerPane = (RelativeLayout) findViewById(R.id.drawer_pane);
        lvNav = (ListView) findViewById(R.id.nav_list);

        listNavItems = new ArrayList<NavItem>();
        listNavItems.add(new NavItem("Home", "প্রথম পাতা",
                R.drawable.ic_action_home));
        listNavItems.add(new NavItem("About", "প্রস্তুতকারীর তথ্য",
                R.drawable.ic_action_about));

        NavListAdapter navListAdapter = new NavListAdapter(
                getApplicationContext(), R.layout.item_nav_list, listNavItems);

        lvNav.setAdapter(navListAdapter);

        listFragments = new ArrayList<Fragment>();
        listFragments.add(new MyHome());
        listFragments.add(new MyAbout());

        // load first fragment as default:
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_content, listFragments.get(0)).commit();

//        setTitle(listNavItems.get(0).getTitle());
        lvNav.setItemChecked(0, true);
        drawerLayout.closeDrawer(drawerPane);

        // set listener for navigation items:
        lvNav.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // replace the fragment with the selection correspondingly:
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.main_content, listFragments.get(position))
                        .addToBackStack(null).commit();

//                setTitle(listNavItems.get(position).getTitle());
                lvNav.setItemChecked(position, true);
                drawerLayout.closeDrawer(drawerPane);

            }
        });

        // create listener for drawer layout
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.drawer_opened, R.string.drawer_closed) {

            @Override
            public void onDrawerOpened(View drawerView) {
                // TODO Auto-generated method stub
                invalidateOptionsMenu();
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                // TODO Auto-generated method stub
                invalidateOptionsMenu();
                super.onDrawerClosed(drawerView);
            }

        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the MyHome/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

//        switch (item.getItemId()) {
//            case R.id.action_settings:
//                Toast.makeText(getApplicationContext(), "Its on under developing...", Toast.LENGTH_LONG).show();
//
////                UpdateApp  atualizaApp = new UpdateApp();
////                atualizaApp.setContext(getApplicationContext());
////                atualizaApp.execute("http://serverurl/appfile.apk");
//
//                //check app version
//                WVersionManager versionManager = new WVersionManager(this);
//                versionManager.setVersionContentUrl("{\n" +
//                        "    \"is_run_mode\":\"true\",\n" +
//                        "    \"name\":\"MAHUpdater Sample\",\n" +
//                        "    \"uri_current\":\"com.mobapphome.mahandroidupdater.sample\",\n" +
//                        "    \"version_code_current\":\"2\",\n" +
//                        "    \"version_code_min\":\"1\",\n" +
//                        "    \"update_info\":\"On version 1.0 we added bla bla\",\n" +
//                        "    \"update_date\":\"16/07/2016\"\n" +
//                        "}"); // your update content url, see the response format below
//                versionManager.setUpdateNowLabel("Custom update now label");
//                versionManager.setRemindMeLaterLabel("Custom remind me later label");
//                versionManager.setIgnoreThisVersionLabel("Custom ignore this version");
//                versionManager.setUpdateUrl("https://play.google.com/store/apps/details?id=com.preneurlab.addatv"); // this is the link will execute when update now clicked. default will go to google play based on your package name.
//                versionManager.setReminderTimer(10);
//                versionManager.checkVersion();
//
//
////                versionManager.askForRate();
////
////                versionManager.setTitle("Please rate us"); // optional
////                versionManager.setMessage("We need your help to rate this app!"); // optional
////                versionManager.setAskForRatePositiveLabel("OK"); // optional
////                versionManager.setAskForRateNegativeLabel("Not now"); // optional
//
////                versionManager.setOnReceiveListener(new OnReceiveListener() {
////                    @Override
////                    public boolean onReceive(int status, String result) {
////                        // implement your own compare logic here
////                        Toast.makeText(getApplicationContext(), "err", Toast.LENGTH_LONG).show();
////                        return false; // return true if you want to use library's default logic & dialog
////                    }
////                });
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }

    }
}
