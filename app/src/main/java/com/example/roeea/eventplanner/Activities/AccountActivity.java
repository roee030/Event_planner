package com.example.roeea.eventplanner.Activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.roeea.eventplanner.ObjectClasses.Event;
import com.example.roeea.eventplanner.ObjectClasses.Guest;
import com.example.roeea.eventplanner.ObjectClasses.Invitee;
import com.example.roeea.eventplanner.ObjectClasses.Manager;
import com.example.roeea.eventplanner.ObjectClasses.User;
import com.example.roeea.eventplanner.ObjectClasses.get;
import com.example.roeea.eventplanner.R;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AccountActivity extends AppCompatActivity {

    private static final String TAG = "home";
    private ListView listView;
    private Firebase mRRef;
    private FirebaseUser fUser;
    private FirebaseAuth fAuth;
    private FirebaseDatabase FBdb;
    private DatabaseReference firDatabaseUsers;
    private static List<String> guestIn_Name = new ArrayList<>();
    private static List<String> MangerOf_Name = new ArrayList<>();
    private static List<String> InvitedTo_Name = new ArrayList<>();
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    private User user;
    private ArrayList<List<String>> lists = new ArrayList<List<String>>();
    private String email;
    private static Event tempEvent;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);


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
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(AccountActivity.this,EventCreationActivity.class));
            }
        });


        fAuth = FirebaseAuth.getInstance();
//       fAuth.getInstance().signOut();
        if (fAuth.getCurrentUser() == null) {
            Intent loginIntent = new Intent(this, MainActivity.class);
            startActivity(loginIntent);
            finish();
        }
        FBdb = FirebaseDatabase.getInstance();

        String userUID = fAuth.getInstance().getCurrentUser().getUid();
        firDatabaseUsers = root.child("Users").child(userUID);

        firDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                user = dataSnapshot.getValue(User.class);
                if (user == null)
                    Log.e("Account Activity", "User is null");
                Guest g;
                if ( (g = user.getGuestIn()) != null)
                    AccountActivity.getGuestsName(user.getGuestIn().getEvents());
                Invitee i;
                if ( (i = user.getInvitedTo()) != null)
                    AccountActivity.getInvitesName(user.getInvitedTo().getInviteeEvent());
                Manager m;
                if ( (m = user.getManagerOf()) != null)
                AccountActivity.getManagersName(user.getManagerOf().getEvents());
                lists.set(0,MangerOf_Name);
                lists.set(1,guestIn_Name);
                lists.set(2,InvitedTo_Name);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read value.", error.toException());
            }
        });
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
            return true;
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
        private static List<List<String>> MangerGuestInvitee_Lists;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, List<List<String>> lists) {
            MangerGuestInvitee_Lists = lists;
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_account, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            ListView listView = (ListView) rootView.findViewById(R.id.accountListView);
            int index = getArguments().getInt(ARG_SECTION_NUMBER) - 1;
            final List<String> result = MangerGuestInvitee_Lists.get(index);
            ListAdapter listAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, result);
            listView.setAdapter(listAdapter);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
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
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1, lists);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

    private void getDetailsUser() {
        user.getUserByUID(email, new get<User>() {
            @Override
            public void callBack(User user) {
                AccountActivity.this.user = user;
                Toast.makeText(AccountActivity.this, AccountActivity.this.user.getUsername(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static void getGuestsName(List<String> keys) {
        for (String key : keys) {
            tempEvent.getEventByKey(key, new get<Event>() {
                @Override
                public void callBack(Event user) {
                    guestIn_Name.add(tempEvent.getName());
                }
            });
        }
    }
    private static void getManagersName(List<String> keys) {
        for (String key : keys) {
            tempEvent.getEventByKey(key, new get<Event>() {
                @Override
                public void callBack(Event user) {
                    MangerOf_Name.add(tempEvent.getName());
                }
            });
        }
    }
    private static void getInvitesName(List<String> keys) {
        for (String key : keys) {
            tempEvent.getEventByKey(key, new get<Event>() {
                @Override
                public void callBack(Event user) {
                    InvitedTo_Name.add(tempEvent.getName());
                }
            });
        }
    }
}
