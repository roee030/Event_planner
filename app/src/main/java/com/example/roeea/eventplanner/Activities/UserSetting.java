package com.example.roeea.eventplanner.Activities;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.roeea.eventplanner.ObjectClasses.User;
import com.example.roeea.eventplanner.ObjectClasses.get;
import com.example.roeea.eventplanner.PreferenceSetting;
import com.example.roeea.eventplanner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class UserSetting extends AppCompatActivity implements PreferenceSetting.settingLisnner {
    //layout
    private EditTextPreference changeUserName;
    private EditTextPreference changeUserEmail;
    private EditTextPreference changeUserPassword;
    private EditTextPreference deleteUser;
    //Firebase
    private FirebaseAuth FBAuth;
    private FirebaseDatabase FBDatabase;
    private DatabaseReference DBReference;
    SharedPreferences.OnSharedPreferenceChangeListener prefListener;
    private EditTextPreference editTextPreference;
    User user1= new User ();
    @Override
    public void changeUserNameInDB(final String name) {
        FBAuth = FirebaseAuth.getInstance();
        FBDatabase = FirebaseDatabase.getInstance();
        DBReference = FBDatabase.getReference();
        String userid = FBAuth.getCurrentUser().getUid();

        user1 = user1.getUserByUID(userid, new get<User>() {
            @Override
            public void callBack(User user) {
                Toast.makeText(UserSetting.this,user1.toString(),Toast.LENGTH_SHORT).show();
                user1= user;
                String userid = FBAuth.getCurrentUser().getUid();
                FBDatabase.getReference().child("Users").child(userid).child("username").setValue(name);
                user1.setUsername(name);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)   {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        //init the preference setting
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String s = SP.getString("changeUserName",null);
        //Toast.makeText(this,s,Toast.LENGTH_SHORT).show();


        FBAuth = FirebaseAuth.getInstance();
        FBDatabase = FirebaseDatabase.getInstance();
        DBReference = FBDatabase.getReference();

        //add fragment pref_setting from xml Folder
        Fragment fragment = new SettingScreen();
        android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();


        if(savedInstanceState == null)
        {
            fragmentTransaction.add(R.id.userSettingview,fragment,"Settting");
            fragmentTransaction.commit();
        }
        else
        {
            fragment = getFragmentManager().findFragmentByTag("Setting fragment");
        }
        //change name fun

        prefListener =
                new SharedPreferences.OnSharedPreferenceChangeListener() {
                    public void onSharedPreferenceChanged(SharedPreferences prefs,
                                                          String key) {
                        if (key.equals("changeUserName")) {
                          SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                          String s = SP.getString("changeUserName","roee");
                          Toast.makeText(UserSetting.this,s,Toast.LENGTH_SHORT).show();
                        //  changeUserNameInDB(s);
                        }
                    }
                };
        SP.registerOnSharedPreferenceChangeListener(prefListener);
    }

    public static String getPref(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    public static class SettingScreen extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_setting);
        }
    }
}

