package com.example.roeea.eventplanner.Server;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.roeea.eventplanner.DatabaseAPI.DatabaseHelper;
import com.example.roeea.eventplanner.ObjectClasses.User;
import com.google.firebase.auth.FirebaseAuth;

public class MiniServer {
    private static final MiniServer miniServerInstance = new MiniServer();
    private DatabaseHelper myDb = null;
    private Context context;
    FirebaseAuth Mauth = FirebaseAuth.getInstance();
    private boolean flag;

    /**
     * Constructor
     */
    private MiniServer() {
    }

    /**
     * @return The instance of the local server.
     */
    public static MiniServer getServerInstance() {
        return MiniServer.miniServerInstance;
    }

    /**
     * Preforme login with email and password
     * @param email
     * @param password
     * @return return Null if login failed.
     */
    public User login(String email, String password, Context context)
    {
        User result = this.myDb.getUserByEmail(email);

        if (result == null)
        {
            Toast.makeText(context,"Email was not founded",Toast.LENGTH_SHORT).show();
            return null;
        }
        if (!result.getPassword().equals(password))
        {
            Toast.makeText(context,"Incorrect password",Toast.LENGTH_SHORT).show();
            return null;
        }
        return result;
    }

    /**
     * Check if user input a unique email to the database.
     * @param email
     * @param context
     * @return true if user can register, false otherwise.
     */
    public boolean preformRegistration(String email, Context context)
    {
        String fireBaseEmail = User.ConvertEmailToFireBaseEmailField(email);
        Log.w("MiniServer", "email = " + email.toString() + "and contverter result: " + fireBaseEmail.toString());
        User result = this.myDb.getUserByEmail(fireBaseEmail);
        if (result != null)
        {
            Toast.makeText(context,"This email is already registered",Toast.LENGTH_SHORT).show();
            return false;
        }
        this.myDb.setNewUser(result);
        return true;
    }
    /**
     * Important for database access. It should be called in
     * any activity that connects to data base.
     * @param context
     */
    public void setContext(Context context) {
        this.myDb = new DatabaseHelper(context);
    }
}
