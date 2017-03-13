package fiu.com.skillcourt.ui.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import fiu.com.skillcourt.R;
import fiu.com.skillcourt.ui.LauncherActivity;
import fiu.com.skillcourt.ui.base.BaseActivity;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {

    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        if(getIntent().hasExtra("logout")){
            LoginManager.getInstance().logOut();
        }
        mAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.facebook_sign_in_button);
        loginButton.setReadPermissions("email", "public_profile");

        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
                Log.d("", "facebook:onSuccess:" + loginResult);
            }

            @Override
            public void onCancel() {
                Log.d("TAG", "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                LoginManager.getInstance().logOut();
                mAuth.signOut();
                Log.d("TAG", "facebook:onError", error);
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Log.d("", "onAuthStateChanged:signed_in:" + user.getUid());

                    //Add to database if not done already
                    final DatabaseReference mUsers = mRootRef.child("users");
                    mUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //User is not in database
                            if(!dataSnapshot.hasChild(user.getUid())){
                                DatabaseReference mNewUser = mUsers.child(user.getUid());
                                DatabaseReference mEmail = mNewUser.child("email");
                                mEmail.setValue(user.getEmail());
                                DatabaseReference mRole = mNewUser.child("role");
                                mRole.setValue("player");
                                String profileId = Profile.getCurrentProfile().getId();
                                String photoUrl = "https://graph.facebook.com/" + profileId + "/picture?width=128&height=128";
                                //String photoUrl = user.getPhotoUrl().toString();
                                DatabaseReference mPhotoUrl = mNewUser.child("photoUrl");
                                mPhotoUrl.setValue(photoUrl);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    /* Use this to test profile picture and other settings*/
                    //Intent intent = new Intent(LoginActivity.this, LoginFacebook.class);


                    Intent intent = new Intent(LoginActivity.this, LauncherActivity.class);
                    startActivity(intent);
                    finish();
                    /*String email = user.getEmail();
                    String name = user.getDisplayName();
                    Uri photoUrl = user.getPhotoUrl();*/
                }
                else{
                    Log.d("TAG", "SIGNED OUT");
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        Log.d("", "handleFacebookAccessToken:" + accessToken);

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("", "signInWithCredential", task.getException());
                        if(!task.isSuccessful()){
                            Log.w("", "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}

