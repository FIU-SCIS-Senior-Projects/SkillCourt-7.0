package fiu.com.skillcourt.ui.creategame;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import fiu.com.skillcourt.R;
import fiu.com.skillcourt.entities.Room;
import fiu.com.skillcourt.entities.User;
import fiu.com.skillcourt.ui.base.BaseActivity;

/**
 * Created by Chandan on 3/22/2017.
 */

public class CreateMultiplayerLobbyWaitingActivity extends BaseActivity {
    private ImageView player1Pic, player2Pic;
    private TextView player1Name, player2Name;
    private Button player1ReadyButton, player2ReadyButton, userReadyButton;
    FirebaseUser user;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String roomID;
    String readyText = "I'm Ready!", notReadyText = "Not Ready", otherID;
    User modelUser, otherUser;
    Room room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_waiting_in_room);

        player1Pic = (ImageView) findViewById(R.id.player1LobbyPic);
        player2Pic = (ImageView) findViewById(R.id.player2LobbyPic);
        player1Name = (TextView) findViewById(R.id.player1LobbyName);
        player2Name = (TextView) findViewById(R.id.player2LobbyName);
        player1ReadyButton = (Button) findViewById(R.id.player1LobbyReadyButton);
        player2ReadyButton = (Button) findViewById(R.id.player2LobbyReadyButton);
        userReadyButton = (Button) findViewById(R.id.lobbyReadyUpButton);

        getRoomID();
    }

    private void getRoomID(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mUsers = mRootRef.child("users");
        DatabaseReference mUser = mUsers.child(user.getUid());
        DatabaseReference mUserRoom = mUser.child("room");

        mUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                modelUser = dataSnapshot.getValue(User.class);
                roomID = modelUser.getRoom();
                getRoom();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                roomID="nope";
                //TODO: User has no room. Something messed up. Explode pleasantly somehow.
            }
        });
    }

    public void getRoom(){
        DatabaseReference mRoom = mRootRef.child("rooms").child(roomID);

        mRoom.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                room = dataSnapshot.getValue(Room.class);
                determinePlayer();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Room has no host? Explode.
            }
        });
    }

    public void determinePlayer(){
        Profile p = Profile.getCurrentProfile();
        if(user.getUid().equals(room.getHost())){ //User is host, populate player 1.
            Toast.makeText(CreateMultiplayerLobbyWaitingActivity.this,"You're the host!", Toast.LENGTH_SHORT).show();
            //Populate user fields for player 1
            loadUser(modelUser, player1Pic, player1Name);

            userReadyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    assignButtonToggle(player1ReadyButton);
                }
            });

            //Add listener for player joining
            DatabaseReference mPlayers = mRootRef.child("rooms").child(roomID).child("players");
            mPlayers.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if(!(user.getUid().equals(dataSnapshot.getKey()))){
                    String id = dataSnapshot.getKey();
                    DatabaseReference mOtherUser = mRootRef.child("users").child(id);
                    findOtherUser(mOtherUser);}
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Toast.makeText(CreateMultiplayerLobbyWaitingActivity.this, "Your opponent left.", Toast.LENGTH_SHORT).show();
                    player2Name.setText("Player 2");
                    player2Pic.setImageResource(R.drawable.com_facebook_profile_picture_blank_square);
                    player2ReadyButton.setBackgroundColor(Color.parseColor("#c1c1d7"));
                    player2ReadyButton.setTextColor((Color.BLACK));
                    player2ReadyButton.setText("NOT READY");
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            //Add listener for player 2's status button
        }
        else{//User is player 2
            Toast.makeText(CreateMultiplayerLobbyWaitingActivity.this,"You're NOT the host!", Toast.LENGTH_SHORT).show();
            //Listen for host
            DatabaseReference mHost = mRootRef.child("users").child(room.getHost());
            mHost.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    otherUser = dataSnapshot.getValue(User.class);
                    loadUser(otherUser, player1Pic, player1Name);
                    loadUser(modelUser, player2Pic, player2Name);
                    getOtherPlayer(player1ReadyButton);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            //Populate
            if(modelUser.getPhotoUrl() != null){
                Picasso.with(CreateMultiplayerLobbyWaitingActivity.this.getApplicationContext()).load(modelUser.getPhotoUrl()).into(player2Pic);
            }
            player2Name.setText(p!=null? p.getName() : modelUser.getEmail());
            userReadyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    assignButtonToggle(player2ReadyButton);
                }
            });
        }
    }

    private void findOtherUser(DatabaseReference mOtherUser) {
        User user;
        mOtherUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                otherUser = dataSnapshot.getValue(User.class);
                loadUser(otherUser, player2Pic, player2Name);
                getOtherPlayer(player2ReadyButton);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getOtherPlayer(final Button button) {
        final String userID = this.user.getUid();
        final DatabaseReference mPlayers = mRootRef.child("rooms").child(roomID).child("players");
        if(userID != room.getHost()){
            otherID = room.getHost();
            DatabaseReference mOtherPlayer = mPlayers.child(otherID);
            DatabaseReference mOtherPlayerStatus = mOtherPlayer.child("status");
            listenForOtherPlayer(button, mOtherPlayerStatus);
        }
        else { //The if statement could be removed but the else only really works in a 2-player scenario.
            mPlayers.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for ( DataSnapshot ds : dataSnapshot.getChildren() ) {
                        if(ds.getKey() != userID){
                            otherID = ds.getKey();
                            final DatabaseReference fmPlayers = mPlayers;
                            DatabaseReference mOtherPlayer = fmPlayers.child(otherID);
                            DatabaseReference mOtherPlayerStatus = mOtherPlayer.child("status");
                            listenForOtherPlayer(button, mOtherPlayerStatus);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void listenForOtherPlayer(final Button button, DatabaseReference dr) {
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(String.class).equalsIgnoreCase("Ready")){
                    button.setBackgroundColor(Color.parseColor("#88b732"));
                    button.setTextColor((Color.WHITE));
                    button.setText("READY");
                }
                else{
                    button.setBackgroundColor(Color.parseColor("#c1c1d7"));
                    button.setTextColor((Color.BLACK));
                    button.setText("NOT READY");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void assignButtonToggle(Button playerReadyButton) {
        if(userReadyButton.getText().equals(readyText))
        {
            playerReadyButton.setBackgroundColor(Color.parseColor("#88b732"));
            playerReadyButton.setTextColor((Color.WHITE));
            playerReadyButton.setText("READY");
            mRootRef.child("rooms").child(roomID).child("players").child(user.getUid()).child("status").setValue("ready");
            userReadyButton.setText(notReadyText);
        }
        else{
            playerReadyButton.setBackgroundColor(Color.parseColor("#c1c1d7"));
            playerReadyButton.setTextColor((Color.BLACK));
            playerReadyButton.setText("NOT READY");
            mRootRef.child("rooms").child(roomID).child("players").child(user.getUid()).child("status").setValue("joined");
            userReadyButton.setText(readyText);
        }
        userReadyButton.setBackgroundColor(userReadyButton.getText().equals(readyText) ? Color.parseColor("#88b732") : Color.parseColor("#c1c1d7"));
        userReadyButton.setText(playerReadyButton.getText().equals("READY") ? notReadyText : readyText);
        userReadyButton.setTextColor(playerReadyButton.getText().equals("READY") ? Color.BLACK : Color.WHITE);
    }

    public void loadUser(User user, ImageView playerPic, TextView playerName){
        Profile p = Profile.getCurrentProfile();
        if(user.getPhotoUrl() != null){
            Picasso.with(CreateMultiplayerLobbyWaitingActivity.this.getApplicationContext()).load(user.getPhotoUrl()).into(playerPic);
        }
        playerName.setText(p!=null? p.getName():user.getEmail());
    }
}
