package com.ourblog.noyage;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Login extends AppCompatActivity implements View.OnClickListener {
    Button loginButton;
    Button createButton;
    Button logoutButton;
    Button testButton;
    Button loadButtton;
    Button inviteButton;
    EditText invitefriendemail;

    String account = "andy001@gmail.com";
    String password = "1fasyfsayflf";
    String UID ="";

    private FirebaseAuth mAuth;
    FirebaseUser user;
    Firebase mRef;
    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Firebase.setAndroidContext(this);
        loginButton = (Button)findViewById(R.id.loginButton);
        createButton = (Button)findViewById(R.id.createButton);
        logoutButton = (Button)findViewById(R.id.LogoutBtton);
        testButton = (Button)findViewById(R.id.testButton);
        loadButtton = (Button)findViewById(R.id.buttonLoad);
        inviteButton = (Button)findViewById(R.id.buttonInvite);
        invitefriendemail = (EditText)findViewById(R.id.invitefriendemail) ;

        mRef = new Firebase("https://ourblog-1887d.firebaseio.com/");

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        loginButton.setOnClickListener(this);
        createButton.setOnClickListener(this);
        testButton.setOnClickListener(this);
        loadButtton.setOnClickListener(this);
        inviteButton.setOnClickListener(this);



    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.createButton:
                createID();
                break;
            case R.id.loginButton:
                Login();
                break;
            case R.id.LogoutBtton:
                Logout();
                break;
            case R.id.testButton:
                write();
                break;
            case R.id.buttonLoad:
                load();
                break;
            case R.id.buttonInvite:
                String eMail = String.valueOf(invitefriendemail.getText());
                inviteFriend( eMail );
                break;
        }



    }

    public void createID(){
        mAuth.createUserWithEmailAndPassword(account, password)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void Login() {

        mAuth.signInWithEmailAndPassword(account,password).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Login.this, "success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();


    }
    public void Logout(){
        FirebaseAuth.getInstance().signOut();
    }

    public void write() {
        List<User> userLists= new ArrayList<User>();
        User user1 = new User("1111");
        User user2 = new User("2222");
        Firebase newRef = mRef.child("data");

       newRef.push().setValue(user1);

    }

    public void load(){
        Firebase newRef = mRef.child("data");
        newRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot objSnapshot: snapshot.getChildren()) {
                   String myString =objSnapshot.child("email").getValue().toString();
                   if(myString.equals("kvliao.tw@gmail.com")) {
                      UID =objSnapshot.getKey();
                    break;
                   }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("Read failed", firebaseError.getMessage());
            }

        });
    }

    void inviteFriend(String email) {
        Firebase newRef = mRef.child("data");
        newRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot objSnapshot: snapshot.getChildren()) {
                    String myString =objSnapshot.child("email").getValue().toString();
                    if(myString.equals("kvliao.tw@gmail.com")) {
                        UID =objSnapshot.getKey();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("Read failed", firebaseError.getMessage());
            }

        });
    }
}
