package com.example.roeea.eventplanner;

public class MiniServer {
    private static final MiniServer miniServerInstance = new MiniServer();
    private DatabaseHelper myDb = null;
    private MiniServer(){}

    /**
     * @return The instance of the local server.
     */

    public static MiniServer getServerInstance()
    {
        return MiniServer.miniServerInstance;

    }


    /**
     * Function takes user credentials and try to perform registration.
     * It will fail if username is already exists.
     * @return true if and only if the registration is o.k.
     */

    public boolean performRegistrationOfUserCall(User user)
    {
        if(this.myDb.getUserByUsernameQuery(user.getUsername()) != null) {

            return false;

        }

        return this.myDb.setNewUser(user);

    }


    /**
     * This function performs login
     * @param username
     * @param password
     * @return return Null if login failed.
     */

    public User performLoginOfUserCall(String username, String password)
    {
        User result = this.myDb.getUserByUsernameQuery(username);


        if( result == null )
        {

            return null;

        }


        if (result.getPassword().equals(password))
        {
            return result;

        }

        return null;

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
