package fiu.com.skillcourt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import fiu.com.skillcourt.ui.base.BaseActivity;
import fiu.com.skillcourt.ui.base.BaseFragment;
import fiu.com.skillcourt.ui.creategame.CreateMultiplayerLobbyActivity;
import fiu.com.skillcourt.ui.creategame.CreateMultiplayerLobbyWaitingActivity;

public class ActivityFromMultiplayerNotif extends AppCompatActivity {

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_from_multiplayer_notif);

        Button acceptButton = (Button) findViewById(R.id.accept_button);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("working?", "working!");
                DatabaseReference mUserRoom = mRootRef.child("users").child(user.getUid()).child("room");
                mUserRoom.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String roomID = dataSnapshot.getValue(String.class);

                        //Add user to room's player list
                        DatabaseReference mPlayers = mRootRef.child("rooms").child(roomID).child("players");
                        DatabaseReference mAddedPlayer = mPlayers.child(user.getUid());
                        DatabaseReference mAddedPlayerStatus = mAddedPlayer.child("status");
                        mAddedPlayerStatus.setValue("joined");
                        DatabaseReference mAddedPlayerGreenHits = mAddedPlayer.child("greenhits");
                        mAddedPlayerGreenHits.setValue(0);
                        DatabaseReference mAddedPlayerRedHits = mAddedPlayer.child("redhits");
                        mAddedPlayerRedHits.setValue(0);

                        //TODO:Take the user to the room and wait.
                        Intent intent = new Intent(ActivityFromMultiplayerNotif.this, CreateMultiplayerLobbyWaitingActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
