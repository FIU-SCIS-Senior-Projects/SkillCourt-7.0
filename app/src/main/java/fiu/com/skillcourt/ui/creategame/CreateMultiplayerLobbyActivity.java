package fiu.com.skillcourt.ui.creategame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
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
 * Created by Chandan on 3/20/2017.
 */

public class CreateMultiplayerLobbyActivity extends BaseActivity {
    private ListView listView;
    private Button createGameButton;
    FirebaseUser user;
    String roomID;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    FirebaseListAdapter<Room> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_lobby);

        listView = (ListView) findViewById(R.id.room_list_view);
        createGameButton = (Button) findViewById(R.id.CreateNewMultiplayerRoomButton);
        user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mRooms = mRootRef.child("rooms");
        createGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRoom();
            }
        });

        DatabaseReference mUser = mRootRef.child("users").child(user.getUid());
        mUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild("room")){ //User not currently subscribed to a room. Let's make one!
                    createGameButton.setVisibility(View.VISIBLE);
                }
                else{
                    createGameButton.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listAdapter = new FirebaseListAdapter<Room>(this, Room.class, android.R.layout.simple_list_item_2, mRooms) {
            @Override
            protected void populateView(View v, Room model, int position) {
                TextView title = (TextView) v.findViewById(android.R.id.text1);
                TextView subtext = (TextView) v.findViewById(android.R.id.text2);

                title.setText(model.getName());
                subtext.setText(model.getSubtext());
            }
        };

        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                roomID = listAdapter.getRef(position).getKey();
                AlertDialog dialog = createModalDialog("Confirmation",
                        "Are you sure you want to join room '" + listAdapter.getItem(position).getName() + "'?",
                        "Join", "Cancel");
                dialog.show();
            }
        });
    }

    private AlertDialog createModalDialog(String title, String message, String confirmation, String cancel){
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateMultiplayerLobbyActivity.this);
// Add the buttons
        builder.setPositiveButton(confirmation, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                //Subscribe to room.
                DatabaseReference mUser = mRootRef.child("users").child(user.getUid());
                DatabaseReference mUserRoom = mUser.child("room");
                mUserRoom.setValue(roomID);

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
                Intent intent = new Intent(CreateMultiplayerLobbyActivity.this, CreateMultiplayerLobbyWaitingActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        builder.setTitle(title).setMessage(message);
// Set other dialog properties

// Create the AlertDialog
        AlertDialog dialog = builder.create();
        return dialog;
    }

    public void goToRoom(){
        Intent intent = new Intent(CreateMultiplayerLobbyActivity.this, CreateLobbyRoomActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listAdapter.cleanup();
    }
}
