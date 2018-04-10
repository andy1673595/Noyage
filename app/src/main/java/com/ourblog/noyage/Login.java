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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


import java.util.ArrayList;

public class Login extends AppCompatActivity implements View.OnClickListener {
    Button loginButton;
    Button createButton;
    Button logoutButton;
    Button testButton;
    Button inviteButton;
    Button addTagButton;
    EditText invitefriendemail;
    EditText contentInput;
    EditText titleInput;
    EditText tagEdit;
    EditText userEmailEdit;
    EditText userPasswordEdit;
    String UID ="";
    ArrayList<String> taglist = new ArrayList<>();
    String userEmail="";

    private FirebaseAuth mAuth;
    FirebaseUser user;
    Firebase mRef;
    Firebase myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Firebase.setAndroidContext(this);
        loginButton = (Button)findViewById(R.id.loginButton);
        createButton = (Button)findViewById(R.id.createButton);
        logoutButton = (Button)findViewById(R.id.LogoutBtton);
        testButton = (Button)findViewById(R.id.testButton);
        inviteButton = (Button)findViewById(R.id.buttonInvite);
        invitefriendemail = (EditText)findViewById(R.id.invitefriendemail) ;
        contentInput =(EditText)findViewById(R.id.content) ;
        titleInput= (EditText)findViewById(R.id.title) ;
        addTagButton = (Button)findViewById(R.id.addTagButton);
        tagEdit = (EditText)findViewById(R.id.tagEdit);
        userEmailEdit = (EditText) findViewById(R.id.useremail);
        userPasswordEdit= (EditText) findViewById(R.id.userpassword);

        mRef = new Firebase("https://ourblog-1887d.firebaseio.com/");
        myRef = new Firebase("https://ourblog-1887d.firebaseio.com/data/");

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        loginButton.setOnClickListener(this);
        createButton.setOnClickListener(this);
        testButton.setOnClickListener(this);
        inviteButton.setOnClickListener(this);
        addTagButton.setOnClickListener(this);
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
            case R.id.buttonInvite:
                String eMail = String.valueOf(invitefriendemail.getText());
                inviteFriend( eMail );
                break;
            case R.id.addTagButton:
                String tag = String.valueOf(tagEdit.getText());
                taglist.add(tag);
                break;
        }



    }

    public void createID(){
        userEmail = String.valueOf(userEmailEdit.getText());
        mAuth.createUserWithEmailAndPassword(userEmail,String.valueOf(userPasswordEdit.getText()))
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "success", Toast.LENGTH_SHORT).show();
                            addAccountsTodatabase();
                        } else {
                            Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void Login() {
        mAuth.signInWithEmailAndPassword(String.valueOf(userEmailEdit.getText()),String.valueOf(userPasswordEdit.getText()))
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Login.this, "success", Toast.LENGTH_SHORT).show();
                    userEmail = String.valueOf(userEmailEdit.getText());
                } else {
                    Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void Logout(){
        FirebaseAuth.getInstance().signOut();
    }

    public void write() {
        if(!userEmail.equals("")) {
            Firebase newRef = mRef.child("data");

            mAuth = FirebaseAuth.getInstance();
            user = mAuth.getCurrentUser();
            String myUID = user.getUid();

            Article article = new Article(String.valueOf(titleInput.getText()),
                    String.valueOf(contentInput.getText()),
                    taglist);
            newRef.child(myUID).child("articles").push().setValue(article);
            taglist.clear();
        }

        else {
            Toast.makeText(Login.this, "請登入", Toast.LENGTH_SHORT).show();
        }

    }

    void inviteFriend(final String email) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("data").orderByChild("email").equalTo(email);


        query.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (com.google.firebase.database.DataSnapshot datafind : dataSnapshot.getChildren()) {
                        UID =datafind.getKey();

                        if(UID.equals("")) {
                            Toast.makeText(Login.this, "QQ沒這個人", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            mAuth = FirebaseAuth.getInstance();
                            user = mAuth.getCurrentUser();
                            String myUID = user.getUid();
                            myRef.child(UID).child("friendrequest").setValue(userEmail);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
     /*   newRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot objSnapshot: snapshot.getChildren()) {
                    String myString =objSnapshot.child("email").getValue().toString();
                    if(myString.equals(email)) {
                        UID =objSnapshot.getKey();
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("Read failed", firebaseError.getMessage());
            }

        });*/
    }

    void addAccountsTodatabase() {
        Firebase newRef = mRef.child("data");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        String myUID = user.getUid();
        newRef.child(myUID).child("email").setValue(userEmail);

    }
}
