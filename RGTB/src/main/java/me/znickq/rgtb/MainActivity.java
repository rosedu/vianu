package me.znickq.rgtb;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity implements ListView.OnItemClickListener {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.initialize(this, "8h4BOTLUlhUBGHKOpgwzQmmxKyJOco6xn19dcy5l", "LjzCzA3KgWvP19QBEPXkTkizu6G74CQjcA8naVMP");
        ParseAnalytics.trackAppOpened(getIntent());

        PassHandler.init(this);

        try {
        SettingsFragment.loadData(this);
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);


        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, NavHandler.getMenusForAdapter()));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(this);

        // Create a new fragment and specify the planet to show based on position
        Fragment fragment = NavHandler.getFragmentForMenu(0);

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Create a new fragment and specify the planet to show based on position
        Fragment fragment = NavHandler.getFragmentForMenu(position);

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private CharSequence mTitle;
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            PassHandler.save(this);
            SettingsFragment.saveData(this);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
