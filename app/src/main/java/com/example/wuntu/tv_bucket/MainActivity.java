package com.example.wuntu.tv_bucket;

import android.app.SearchManager;
import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wuntu.tv_bucket.Adapters.ExpandListAdapter;
import com.example.wuntu.tv_bucket.Adapters.SearchAdapter;
import com.example.wuntu.tv_bucket.Fragments.MoviesMainFragment;
import com.example.wuntu.tv_bucket.Fragments.SearchFragment;
import com.example.wuntu.tv_bucket.Fragments.TvMainFragment;
import com.example.wuntu.tv_bucket.Utils.UrlConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ArrayList<String> listDataHeader;
    HashMap<String,List<String>> listDataChild;
    ExpandableListView expListView;
    ExpandListAdapter listAdapter;
    UrlConstants urlConstants = UrlConstants.getSingletonRef();
    MoviesMainFragment moviesMainFragment = null;
    TvMainFragment tvMainFragment = null;
    DrawerLayout drawer;
    SearchView searchView = null;
    ActionBarDrawerToggle toggle;
    SearchFragment searchFragment;
    FrameLayout frameLayout;
    AppBarLayout.LayoutParams params;
    private int lastExpandedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frameLayout = (FrameLayout) findViewById(R.id.framelayout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();

        searchFragment = new SearchFragment();

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("TV Bucket");
        getSupportActionBar().setElevation(0);

        enableExpandableList();

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        toggle.syncState();

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }



    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        moviesMainFragment = new MoviesMainFragment();
        tvMainFragment = new TvMainFragment();
        adapter.addFragment(moviesMainFragment, "Movies");
        adapter.addFragment(tvMainFragment, "TV Series");
        viewPager.setAdapter(adapter);
    }



    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);


        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
        }

        final ImageView searchClose = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);



        searchView.setQueryHint(getResources().getString(R.string.search_hint));

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                viewPager.setVisibility(GONE);
                tabLayout.setVisibility(GONE);
                //Toast.makeText(MainActivity.this, "Now Opened", Toast.LENGTH_SHORT).show();
                toggle.setDrawerIndicatorEnabled(false);

                frameLayout.setVisibility(View.VISIBLE);
                params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP);
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, searchFragment).commit();
            }
        });

        searchClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "Now CLosed", Toast.LENGTH_SHORT).show();
                viewPager.setVisibility(View.VISIBLE);
                tabLayout.setVisibility(View.VISIBLE);
                toggle.setDrawerIndicatorEnabled(true);
                params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                        | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
                if (getSupportFragmentManager().getBackStackEntryCount()>0)
                {
                    getSupportFragmentManager().popBackStack();
                }
                getSupportFragmentManager().beginTransaction().remove(searchFragment).commit();
                getSupportActionBar().setDisplayShowTitleEnabled(true);

                frameLayout.setVisibility(GONE);

                searchView.onActionViewCollapsed();

            }
        });




        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do your search

                searchFragment = new SearchFragment();
                String query1 = query.replaceAll(" ","%20");
                //Toast.makeText(MainActivity.this,query1+ "", Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("QUERY",query1);
                searchFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, searchFragment).commit();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty())
                {
                    //Toast.makeText(MainActivity.this, "Empty", Toast.LENGTH_SHORT).show();
                    searchFragment = new SearchFragment();
                    String query1 = newText.replaceAll(" ","%20");
                    Bundle bundle = new Bundle();
                    bundle.putString("QUERY",query1);
                    searchFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, searchFragment).commit();
                }
                else
                {
                    searchFragment = new SearchFragment();
                    String query1 = newText.replaceAll(" ","%20");
                    Bundle bundle = new Bundle();
                    bundle.putString("QUERY",query1);
                    searchFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, searchFragment).commit();
                    // Toast.makeText(MainActivity.this, query1+"", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {


        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (frameLayout.getVisibility() == View.VISIBLE)
        {
            viewPager.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);
            toggle.setDrawerIndicatorEnabled(true);
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                    | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);

            getSupportActionBar().setDisplayShowTitleEnabled(true);

            frameLayout.setVisibility(GONE);

            searchView.onActionViewCollapsed();
        }
        else {
            super.onBackPressed();
        }
    }

    private void enableExpandableList() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        expListView = (ExpandableListView) findViewById(R.id.left_drawer);

        prepareListData(listDataHeader, listDataChild);
        listAdapter = new ExpandListAdapter(this, listDataHeader, listDataChild);
        // setting list adapter
        expListView.setAdapter(listAdapter);

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {

                return false;
            }

        });
        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });


        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition)
            {
            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {


                if (groupPosition == 0)
                {
                    switch (childPosition) {
                        case 0:
                            moviesMainFragment.prepareOnlineData(urlConstants.URL_popular_movies, 1);
                            drawer.closeDrawer(GravityCompat.START);
                            break;
                        case 1:
                            moviesMainFragment.prepareOnlineData(urlConstants.URL_top_rated_movies, 1);
                            drawer.closeDrawer(GravityCompat.START);
                            break;
                        case 2:
                            moviesMainFragment.prepareOnlineData(urlConstants.URL_upcoming_movies, 1);
                            drawer.closeDrawer(GravityCompat.START);
                            break;
                        case 3:
                            moviesMainFragment.prepareOnlineData(urlConstants.URL_now_playing_movies, 1);
                            drawer.closeDrawer(GravityCompat.START);
                            break;
                        default:
                            moviesMainFragment.prepareOnlineData(urlConstants.URL_popular_movies,1);
                            drawer.closeDrawer(GravityCompat.START);
                            break;
                    }
                }
                else if (groupPosition == 1)
                {
                    switch (childPosition)
                    {
                        case 0:
                            drawer.closeDrawer(GravityCompat.START);
                            tvMainFragment.prepareOnlineData(urlConstants.URL_popular_tv_shows,1);
                            break;
                        case 1:
                            tvMainFragment.prepareOnlineData(urlConstants.URL_top_rated_tv_shows,1);
                            drawer.closeDrawer(GravityCompat.START);
                            break;
                        case 2:
                            drawer.closeDrawer(GravityCompat.START);
                            tvMainFragment.prepareOnlineData(urlConstants.URL_tv_on_air,1);
                            break;
                        case 3:
                            drawer.closeDrawer(GravityCompat.START);
                            tvMainFragment.prepareOnlineData(urlConstants.URL_tv_airing_today,1);
                            break;
                    }
                }

                return false;
            }
        });}

    private void prepareListData(List<String> listDataHeader, Map<String,
            List<String>> listDataChild) {


        // Adding child data
        listDataHeader.add("Movies");
        listDataHeader.add("TV Shows");
        listDataHeader.add("People");

        // Adding child data
        List<String> top = new ArrayList<String>();
        top.add("Popular");
        top.add("Top Rated");
        top.add("Upcoming");
        top.add("Now Playing");


        List<String> mid = new ArrayList<String>();
        mid.add("Popular");
        mid.add("Top Rated");
        mid.add("On TV");
        mid.add("Airing Today");

        List<String> bottom = new ArrayList<String>();
        bottom.add("Popular People");



        listDataChild.put(listDataHeader.get(0), top);
        listDataChild.put(listDataHeader.get(1), mid);
        listDataChild.put(listDataHeader.get(2), bottom);
    }
}
