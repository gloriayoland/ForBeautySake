package com.example.forbeautysake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.forbeautysake.utils.DBHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;

public class Login extends AppCompatActivity {

    // define variables
    Button register, login;
    TextInputLayout email, pass;
    //DBHelper dbHelper;
    SharedPreferences sp;
    private FirebaseAuth mAuth;

    // define the name of shared preferences and key
    //String SP_NAME = "mypref";
    //String KEY_UNAME = "username";


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();

            Toast.makeText(Login.this, "Hello "+ email, Toast.LENGTH_SHORT) .show();
            startActivity(new Intent(Login.this, Dashboard.class));
        }else{
            Toast.makeText(Login.this, "No Active user", Toast.LENGTH_SHORT) .show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        // find components by id according to the defined variable
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        register = findViewById(R.id.btn_regis);
        login = findViewById(R.id.btn_login);

        //initiate DBHelper class
        //dbHelper = new DBHelper(this);

        //get shared preferences
        //sp = getSharedPreferences(SP_NAME, MODE_PRIVATE);

        //check availability of sp
        //String uname = sp.getString(KEY_UNAME, null);

        //set onclick register button which will display on register page
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Register.class);
                startActivity(i);
            }
        });


        //set onclick login button which will display on dashboard page
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail =  email.getEditText().getText().toString().trim();
                String password =  pass.getEditText().getText().toString().trim();
                String regex = "^(.+)@adminfbs(.+)$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(mail);

                //setting email admin



                if(validateEmail() & validatePassword()){
                    mAuth.signInWithEmailAndPassword(mail, password)
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if (matcher.matches()){
                                            Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT) .show();
                                            startActivity(new Intent(Login.this, AdminDashboard.class));
                                        }
                                        else{
                                            Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT) .show();
                                            startActivity(new Intent(Login.this, Dashboard.class));

                                        }
                                        //updateUI(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(Login.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }








            }
        });

    }

    private Boolean validateEmail(){
        String val = email.getEditText().getText().toString();
        Boolean boolVal = false;

        if(val.isEmpty()){
            email.setError("Field cannot be empty!");
        }else{
            email.setError(null);
            email.setErrorEnabled(false);
            boolVal = true;
        }

        return boolVal;
    }

    private Boolean validatePassword(){
        String val = pass.getEditText().getText().toString();
        Boolean boolVal = false;
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if(val.isEmpty()){
            pass.setError("Field cannot be empty!");
        }else if(val.length() >= 15){
            pass.setError("Password cannot be more than 15 characters!");
        }else if(val.length() <= 8){
            pass.setError("Username cannot be less than 8 characters!");
        }else if(!val.matches(noWhiteSpace)){
            pass.setError("White space are not allowed!");
        }else{
            pass.setError(null);
            pass.setErrorEnabled(false);
            boolVal = true;
        }

        return boolVal;
    }

    @Override
    public void onBackPressed() {

        //set alert dialog for user who click back button on the phone
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                int pid = android.os.Process.myPid();
//                android.os.Process.killProcess(pid);
//                finish();

                Intent i = new Intent(Intent.ACTION_MAIN);
                i.addCategory(Intent.CATEGORY_HOME);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}