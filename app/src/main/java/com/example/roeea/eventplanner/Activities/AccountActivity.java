package com.example.roeea.eventplanner.Activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.roeea.eventplanner.ViewModels.AccountViewModel;
import com.example.roeea.eventplanner.ViewModels.FragmentViewModel;
import com.example.roeea.eventplanner.ObjectClasses.Event;
import com.example.roeea.eventplanner.ObjectClasses.User;
import com.example.roeea.eventplanner.ObjectClasses.get;
import com.example.roeea.eventplanner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class AccountActivity extends AppCompatActivity {

    private static final String TAG = "Account Activity";
    private FirebaseAuth fAuth;
    private static List<String> guestIn_Name = new ArrayList<>();
    private static List<String> MangerOf_Name = new ArrayList<>();
    private static List<String> InvitedTo_Name = new ArrayList<>();
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    private ArrayList<List<String>> lists = new ArrayList<List<String>>();
    private static Event tempEvent = new Event();
    private AccountViewModel accountViewModel;


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        accountViewModel.loadUseer();

        int numberOfTabs = 3;
        String eventID = getIntent().getStringExtra("eventID");
        for (int i = 0; i < numberOfTabs; i++)
            lists.add(new ArrayList<String>());

        // Receiving invite
        if (eventID != null) {
            startActivity(new Intent(this, EventInvitationActivity.class)
                    .putExtra("eventID", eventID));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(AccountActivity.this, EventCreationActivity.class));
            }
        });


        fAuth = FirebaseAuth.getInstance();
//       fAuth.getInstance().signOut();
        if (fAuth.getCurrentUser() == null) {
            Intent loginIntent = new Intent(this, MainActivity.class);
            startActivity(loginIntent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(getBaseContext(), UserSetting.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private View rootView;
        private static PlaceholderFragment fragment;
        private ListView listView;
        private static ArrayList<String>  keys;
        private TextView textView;
        private FirebaseAuth mAuth;
        private User user = new User();
        private AccountViewModel accountViewModel;
        private FragmentViewModel fragmentViewModel;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onResume() {
            super.onResume();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_account, container, false);
            textView = (TextView) rootView.findViewById(R.id.section_label);
            listView = (ListView) rootView.findViewById(R.id.accountListView);
            final int index = getArguments().getInt(ARG_SECTION_NUMBER);
            accountViewModel = ViewModelProviders.of(getActivity()).get(AccountViewModel.class);
            fragmentViewModel = ViewModelProviders.of(this).get(FragmentViewModel.class);
            final ArrayList<List<String>> lists = new ArrayList<>();
            accountViewModel.getUser().observe(this, new Observer<User>() {
                @Override
                public void onChanged(@Nullable User user) {
                    if(user != null) {
                        PlaceholderFragment.this.user = user;
                        lists.add(user.getManagerOf().getEvents());
                        lists.add(user.getGuestIn().getEvents());
                        lists.add(user.getInvitedTo().getInviteeEvent());
                        fragmentViewModel.loadEvents(lists.get(getArguments().getInt(ARG_SECTION_NUMBER)));
                    }
                }
            });
            fragmentViewModel.getEvent().observe(this, new Observer<List<Event>>() {
                @Override
                public void onChanged(@Nullable List<Event> events) {
                    if (events != null) {
                        ArrayList<String> names = new ArrayList<>();
                        for (Event e : events)
                            names.add(e.getName());
                        ListAdapter listAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, names);
                        listView.setAdapter(listAdapter);
                    }
                }
            });
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Intent intent = new Intent(this, EventPrievewActivity.class);
//                    intent.putExtra("Event's ID", result.get(position));
//                    startActivity(intent);
                }
            });
            return rootView;

        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below)
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }


    private static void getGuestsName(List<String> keys) {
        for (String key : keys) {
            tempEvent.getEventByKey(key, new get<Event>() {
                @Override
                public void callBack(Event event) {
                    guestIn_Name.add(event.getName());
                }
            });
        }
    }

    private static void getManagersName(List<String> keys) {
        for (String key : keys) {
            tempEvent.getEventByKey(key, new get<Event>() {
                @Override
                public void callBack(Event event) {
                    MangerOf_Name.add(event.getName());
                }
            });
        }
    }

    private static void getInvitesName(List<String> keys) {
        for (String key : keys) {
            tempEvent.getEventByKey(key, new get<Event>() {
                @Override
                public void callBack(Event event) {
                    InvitedTo_Name.add(event.getName());
                }
            });
        }
    }
}