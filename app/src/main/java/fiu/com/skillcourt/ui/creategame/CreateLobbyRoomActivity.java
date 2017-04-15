package fiu.com.skillcourt.ui.creategame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import fiu.com.skillcourt.R;
import fiu.com.skillcourt.ui.base.BaseActivity;

/**
 * Created by Chandan on 3/21/2017.
 */

public class CreateLobbyRoomActivity extends BaseActivity {
    private EditText roomName;
    private NumberPicker numberOfPlayers;
    private Button submitButton, cancelButton;
    FirebaseUser user;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_choose_lobby_options);
        user = FirebaseAuth.getInstance().getCurrentUser();

        roomName = (EditText) findViewById(R.id.roomName);
        numberOfPlayers = (NumberPicker) findViewById(R.id.numberOfPlayersPicker);
        numberOfPlayers.setMinValue(2);
        numberOfPlayers.setMaxValue(2);
        submitButton = (Button) findViewById(R.id.lobSubmit);
        cancelButton = (Button) findViewById(R.id.lobCancel);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(roomName.getText() != null && !roomName.getText().toString().isEmpty()){
                    createRoom();

                    Intent intent = new Intent(CreateLobbyRoomActivity.this, CreateMultiplayerLobbyWaitingActivity.class);
                    startActivity(intent);
                    finish();
                    Toast toast = Toast.makeText(getApplicationContext(), "Room created", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateLobbyRoomActivity.this, CreateMultiplayerLobbyActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void createRoom() {
        DatabaseReference mRooms = mRootRef.child("rooms");
        DatabaseReference mRoom = mRooms.push();
        buildJSONTree(mRoom);
    }

    private void buildJSONTree(DatabaseReference location) {
        DatabaseReference mHost = location.child("host");
        mHost.setValue(user.getUid());
        DatabaseReference mName = location.child("name");
        mName.setValue(roomName.getText().toString());
        DatabaseReference mStatus = location.child("status");
        mStatus.setValue("created");
        DatabaseReference mSubText = location.child("subtext");
        mSubText.setValue("Also temp");

        //Create players sub-tree
        DatabaseReference mPlayers = location.child("players");
        DatabaseReference mPlayer = mPlayers.child(user.getUid());
        DatabaseReference mPlayerStatus = mPlayer.child("status");
        mPlayerStatus.setValue("joined");
        DatabaseReference mPlayerGreenHits = mPlayer.child("greenhits");
        mPlayerGreenHits.setValue(0);
        DatabaseReference mPlayerRedHits = mPlayer.child("redhits");
        mPlayerRedHits.setValue(0);

        //Room created, add it to the user's room state
        DatabaseReference mRoomState = mRootRef.child("users").child(user.getUid()).child("room");
        mRoomState.setValue(location.getKey());

        //go to room
    }
}
