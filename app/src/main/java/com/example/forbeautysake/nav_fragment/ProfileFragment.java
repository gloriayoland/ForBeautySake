package com.example.forbeautysake.nav_fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.forbeautysake.Login;
import com.example.forbeautysake.R;
import com.example.forbeautysake.Register;
import com.example.forbeautysake.utils.DBHelper;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment{

    // define variables
    TextInputLayout fullname, username, password, email;
    TextView bigFullname, bigUsername;
    Button updateProfile, logOut;
    SwipeRefreshLayout swipeRefresh;

    Context context;
    View myFragment;
    DBHelper db;

    SharedPreferences sp;

    // define the name of shared preferences and key
    String SP_NAME = "mypref";
    String KEY_UNAME = "username";

    //make array list for user data
    ArrayList<String> userData = new ArrayList();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        swipeRefresh = myFragment.findViewById(R.id.swipeRefresh);
        //swipe refresh on click listener
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFragmentManager().beginTransaction().replace(R.id.container_fragment, new ProfileFragment()).commit();
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragment = inflater.inflate(R.layout.fragment_profile, container, false);

        // find components by id according to the defined variable
        fullname = myFragment.findViewById(R.id.fullname);
        username = myFragment.findViewById(R.id.username);
        email = myFragment.findViewById(R.id.email);
        password = myFragment.findViewById(R.id.password);
        bigFullname = myFragment.findViewById(R.id.bigFullname);
        bigUsername = myFragment.findViewById(R.id.bigUsername);


        //get shared preferences
        sp = getContext().getSharedPreferences(SP_NAME, MODE_PRIVATE);

        //check availability of sp
        final String name = sp.getString(KEY_UNAME, null);

        displayProfile(name);

        // find components by id according to the defined variable
        updateProfile = myFragment.findViewById(R.id.btn_update);

        //set on click listener for update profile button
        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(name, fullname.getEditText().getText().toString(),
                        email.getEditText().getText().toString(),
                        password.getEditText().getText().toString());

            }
        });

        // find components by id according to the defined variable
        logOut = myFragment.findViewById(R.id.btn_logout);

        //set on click listener for logout button
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();

                //make toast for displays a text that has successfully logged out
                Toast.makeText(getContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, Login.class);
                startActivity(i);

            }
        });

        return myFragment;
    }

    public void displayProfile(String Username){
        //get data from database
        db = new DBHelper(context);
        Cursor data = db.fetchAllProfileData(Username);
        while(data.moveToNext()){
            //put data to arraylist
            userData.add(data.getString(0));
            userData.add(data.getString(1));
            userData.add(data.getString(2));
            userData.add(data.getString(3));
            userData.add(data.getString(4));
        }

        //display data from arraylist
        fullname.getEditText().setText(userData.get(1));
        email.getEditText().setText(userData.get(2));
        username.getEditText().setText(userData.get(3));
        password.getEditText().setText(userData.get(4));
        bigFullname.setText(userData.get(1));
        bigUsername.setText(userData.get(3));
    }

    public void updateProfile(String usernameSP, String fullname, String email, String password){
        //update data to database
        db = new DBHelper(context);
        //get id user
        int idUser = db.getIdUser(usernameSP);

        //update data by id user
        boolean res = db.updateProfileData(fullname, email, password, String.valueOf(idUser));

        if (res){

            // make toast for displays a text that has successfully updated profile
            Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
        }
    }

}
