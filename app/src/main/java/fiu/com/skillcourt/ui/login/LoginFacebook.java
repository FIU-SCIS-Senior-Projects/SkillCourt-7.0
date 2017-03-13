package fiu.com.skillcourt.ui.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import fiu.com.skillcourt.R;
import fiu.com.skillcourt.ui.base.BaseActivity;

public class LoginFacebook extends BaseActivity {
    LoginButton btnOut;
    private TextView txtuserID, txtemail;
    private ImageView profilePic;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_facebook);

        mAuth = FirebaseAuth.getInstance();
        btnOut = (LoginButton) findViewById(R.id.btnOut);
        txtuserID = (TextView) findViewById(R.id.textUserID);
        txtemail = (TextView) findViewById(R.id.textEmail);
        profilePic = (ImageView) findViewById(R.id.userProfilePic);

        btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                LoginManager.getInstance().logOut();
            }
        });
            mAuthListener = new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    for(UserInfo userInfo : user.getProviderData()){
                        Log.d("TAG", user.getProviderId());
                    }

                    txtuserID.setText(user.getDisplayName());
                    txtemail.setText(user.getEmail());
                    String profileId = Profile.getCurrentProfile().getId();
                    String photoUrl = "https://graph.facebook.com/" + profileId + "/picture?width=128&height=128";
                    Picasso.with(LoginFacebook.this).load(photoUrl).into(profilePic);
                }
                else{
                    Intent intent = new Intent(LoginFacebook.this, LoginActivity.class);
                    intent.putExtra("logout", true);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

}
