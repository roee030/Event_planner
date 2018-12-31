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
import com.firebase.client.Firebase;
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
    private Firebase FB;
    private FirebaseDatabase FBDatabase;
    private DatabaseReference DBReference;
    SharedPreferences.OnSharedPreferenceChangeListener prefListener;
    private EditTextPreference editTextPreference;
    User user1 = new User ();
    private String nameToReplace;
    private String uid;



    @Override
    public void changeUserNameInDB(final String name, final String uid) {

        user1 = user1.getUserByUID(uid, new get<User>() {
            @Override
            public void callBack(User user) {
                FirebaseDatabase FBdb = FirebaseDatabase.getInstance();
                user1= user;
                String nameforchecking = name;
                DBReference.child("Users").child(uid).child("username").setValue(nameforchecking);
                Toast.makeText(UserSetting.this,"Your name changed successfully to: "+ name, Toast.LENGTH_SHORT).show();
                //user1.setUsername(name);
                return;
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
                          nameToReplace = SP.getString("changeUserName","roee");
                          uid = FBAuth.getUid();
                          changeUserNameInDB(nameToReplace,uid);

                        }
                        if (key.equals("changeUserPassword"))
                        {
                            //add fun to change password via FBAUTH
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

