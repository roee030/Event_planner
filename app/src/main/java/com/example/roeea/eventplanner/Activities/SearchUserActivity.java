package com.example.roeea.eventplanner.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roeea.eventplanner.ObjectClasses.User;
import com.example.roeea.eventplanner.R;
import com.example.roeea.eventplanner.ViewModels.UserViewModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class SearchUserActivity extends AppCompatActivity {
    private EditText mSearchField;
    private ImageButton mSearchBtn;
    private RecyclerView mResultList;
    private DatabaseReference mUserDatabase;
    private FirebaseRecyclerAdapter<User, UsersViewHolder> adapter;
    private String eventID;
    private String userID = null;
    private ArrayList<String> invited;
    private UserViewModel userViewModel;
    private static DatabaseReference fireDatabaseT;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.check)
        {
            Intent intent = new Intent(getBaseContext(), AccountActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_user);
        Intent intent = getIntent();
        eventID = intent.getStringExtra("eventID");
        invited = intent.getStringArrayListExtra("invited");
        mUserDatabase = FirebaseDatabase.getInstance().getReference("Users");
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.loadUseer();

        fireDatabaseT = FirebaseDatabase.getInstance().getReference();

        mSearchField = (EditText) findViewById(R.id.search_field);
        mSearchBtn = (ImageButton) findViewById(R.id.search_btn);

        mResultList = (RecyclerView) findViewById(R.id.result_list);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(this));

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String searchText = mSearchField.getText().toString();
                firebaseUserSearch(searchText);
                adapter.startListening();
            }
        });

    }

    private void firebaseUserSearch(String searchText) {

        Toast.makeText(getBaseContext(), "Started Search - Invite user by click", Toast.LENGTH_LONG).show();

        Query firebaseSearchQuery = mUserDatabase.orderByChild("username").startAt(searchText).endAt(searchText + "\uf8ff");


        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(firebaseSearchQuery, User.class)
                        .build();


        adapter = new FirebaseRecyclerAdapter<User, UsersViewHolder>(options) {
            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.list_layout, viewGroup, false);
                return new UsersViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull User model) {
                holder.setDetails(getApplicationContext(), model);
            }


            @NonNull
            @Override
            public DatabaseReference getRef(int position) {
                DatabaseReference databaseReference = super.getRef(position);
                userID = databaseReference.getKey();
                Log.i("SearchUserActivity", userID);
                return databaseReference;
            }
        };
        mResultList.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter != null)
            adapter.stopListening();
    }
    // View Holder Class

    public class UsersViewHolder extends RecyclerView.ViewHolder {

        View mView;
        private User user;

        public UsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(user != null) {
                        user.getInvitedTo().getInviteeEvent().add(eventID);
                        adapter.getRef(getAdapterPosition()).setValue(user);
                        if (userID != null) {
                            invited.add(eventID);
                            fireDatabaseT.child("Events").child(eventID).child("invited").setValue(invited);
                            Toast.makeText(SearchUserActivity.this, "User added", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("SearchUserActivity", "userId is null");
                        }
                    }
                    else
                    {
                        Log.e("SearchUserActivity", "user is null");
                    }
                }
            });

        }

        public void setDetails(Context ctx, User user) {
            this.user = user;
            TextView user_name = (TextView) mView.findViewById(R.id.name_text);
            TextView user_email = (TextView) mView.findViewById(R.id.email_text);
            user_name.setText(user.getUsername());
            user_email.setText(user.getEmail());
        }
    }
}
