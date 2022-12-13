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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
    DatabaseReference db;

    private FirebaseAuth mAuth;

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

    public static ProfileFragment getInstance() {
        return new ProfileFragment();
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

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        showUserData(currentUser);

        // find components by id according to the defined variable
        updateProfile = myFragment.findViewById(R.id.btn_update);

        //set on click listener for update profile button
        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateFullname() && validateUsername()){
                    updateProfileData(currentUser);
                    Toast.makeText(getContext(), "Profile updated!", Toast.LENGTH_SHORT).show();
                    //after review has posted, it will go back to the page before

                }

            }
        });

        // find components by id according to the defined variable
        logOut = myFragment.findViewById(R.id.btn_logout);

        //set on click listener for logout button
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                //make toast for displays a text that has successfully logged out
                Toast.makeText(getContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, Login.class);
                startActivity(i);

            }
        });

        return myFragment;
    }

    private void showUserData(FirebaseUser user){
        String userid = user.getUid();
        db = FirebaseDatabase.getInstance().getReference("table_users").child(userid);
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String emailFromDB = snapshot.child("row_email").getValue(String.class);
                String fullnameFromDB = snapshot.child("row_fullname").getValue(String.class);
                String passwordFromDB = snapshot.child("row_password").getValue(String.class);
                String usernameFromDB = snapshot.child("row_username").getValue(String.class);

                fullname.getEditText().setText(fullnameFromDB);
                username.getEditText().setText(usernameFromDB);
                password.getEditText().setText(passwordFromDB);
                email.getEditText().setText(emailFromDB);
                bigFullname.setText(fullnameFromDB);
                bigUsername.setText(usernameFromDB);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void updateProfileData(FirebaseUser user){
        String userid = user.getUid();
        String fullnameChanged = fullname.getEditText().getText().toString();
        String usernameChanged = username.getEditText().getText().toString();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("table_users");
        ref.child(userid).child("row_fullname").setValue(fullnameChanged);
        ref.child(userid).child("row_username").setValue(usernameChanged);

        DatabaseReference storyref = FirebaseDatabase.getInstance().getReference("table_review");
        Query userData = storyref.orderByChild("row_userid").equalTo(userid);

        userData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){

                    String keyStory = dataSnapshot.getKey();
                    storyref.child(keyStory).child("row_username").setValue(usernameChanged);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private Boolean validateUsername(){
        String val = username.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";
        Boolean boolVal = false;

        if(val.isEmpty()){
            username.setError("Field cannot be empty!");
        }else if(val.length() >= 15){
            username.setError("Username too long!");
        }else if(!val.matches(noWhiteSpace)){
            username.setError("White space are not allowed!");
        }else{
            username.setError(null);
            username.setErrorEnabled(false);
            boolVal = true;
        }

        return boolVal;
    }

    private Boolean validateFullname(){
        String val = fullname.getEditText().getText().toString();
        Boolean boolVal = false;

        if(val.isEmpty()){
            fullname.setError("Field cannot be empty!");
        }else{
            fullname.setError(null);
            fullname.setErrorEnabled(false);
            boolVal = true;
        }

        return boolVal;
    }
}
