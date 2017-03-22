package fiu.com.skillcourt.ui.creategame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import fiu.com.skillcourt.R;
import fiu.com.skillcourt.entities.Room;
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
    String roomID, host;
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

        DatabaseReference mRoom = getRoom();
        //TODO: Refactor to pass a POJO
        mRoom.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                room = dataSnapshot.getValue(Room.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Room has no host? Explode.
            }
        });
        if(user.getUid().equals(room.getHost())){ //User is host, populate player 1.
            Toast.makeText(CreateMultiplayerLobbyWaitingActivity.this,"You're the host!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(CreateMultiplayerLobbyWaitingActivity.this,"You're NOT the host!", Toast.LENGTH_SHORT).show();
        }
    }

    private DatabaseReference getRoom(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mUserRoom = mRootRef.child("users").child(user.getUid()).child("room");
        mUserRoom.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                roomID = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TODO: User has no room. Something messed up. Explode pleasantly somehow.
            }
        });
        //delete after this
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateMultiplayerLobbyWaitingActivity.this);
// Add the buttons
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
// Set other dialog properties
        builder.setMessage("Room id is "+roomID);

// Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
        DatabaseReference mRoom = mRootRef.child("rooms").child(roomID);
        return mRoom;
    }
}
