package fiu.com.skillcourt.ui.startgame;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.Profile;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fiu.com.skillcourt.R;
import fiu.com.skillcourt.entities.Game;
import fiu.com.skillcourt.entities.User;
import fiu.com.skillcourt.game.SkillCourtGame;
import fiu.com.skillcourt.game.SkillCourtManager;
import fiu.com.skillcourt.interfaces.Constants;
import fiu.com.skillcourt.ui.base.ArduinosCommunicationFragment;
import fiu.com.skillcourt.ui.base.ArduinosStartCommunicationFragment;
import fiu.com.skillcourt.ui.creategame.CreateGameActivity;
import fiu.com.skillcourt.ui.creategame.CreateMultiplayerGameActivity;
import fiu.com.skillcourt.ui.login.LoginFacebook;

public class StartMultiplayerGameFragment extends ArduinosCommunicationFragment implements StartGameView, View.OnClickListener{

    private User modelUser;
    private User otherUser;
    private String roomID;
    private TextView tvTimer;
    private ProgressBar progressBar;
    private TextView tvHits;
    private TextView tvRedHits;
    private TextView tvGreenHits;
    private TextView p1Name;
    private TextView p2Name;
    private TextView tvOppRedHits;
    private TextView tvOppGreenHits;
    private ImageView p1Pic;
    private ImageView p2Pic;
    private TextView tvScore;
    private TextView tvAccuracy;
    private LinearLayout btnContainer, tvContainer;
    private RelativeLayout timerContainer;
    private FirebaseUser user;
    private String roomState;
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mRoomState = mRootRef.child("rooms").child(roomID).child("status");
    boolean isHost, hostPaused, hostResumed;

    StartGamePresenter startGamePresenter;

    //Firebase instances
    DatabaseReference userDatabaseReference;

    public static StartGameFragment newInstance(ArrayList<String> sequences) {
        StartGameFragment startGameFragment = new StartGameFragment();
        if (!sequences.isEmpty()) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.TAG_SEQUENCE, sequences);
            startGameFragment.setArguments(bundle);
        }
        return startGameFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseReference mUser = mRootRef.child("users").child(user.getUid());
        mUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                modelUser = dataSnapshot.getValue(User.class);
                roomID = modelUser.getRoom();
                helperOnCreate();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        }

    public void helperOnCreate(){
        setupRoomStateListener();
        setIsHost();
        List<String> sequences = new ArrayList<>();
        if (getArguments() != null) {
            if (getArguments().containsKey(Constants.TAG_SEQUENCE))
                sequences = (ArrayList) getArguments().getSerializable(Constants.TAG_SEQUENCE);
        }
        startGamePresenter = new StartGamePresenter(this, sequences);
        userDatabaseReference = mRootRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    private void setIsHost() {
        DatabaseReference mRoomHost = mRootRef.child("rooms").child(roomID).child("status");
        mRoomHost.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String host = dataSnapshot.getValue(String.class);
                isHost = host.equals(user.getUid());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //TODO: Add extra when referring to this fragment
        //TODO: Switch true and false post-testing
        return inflater.inflate(R.layout.fragment_multiplayer_game, container, false);
    }

    private void setupRoomStateListener() {
        mRoomState.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                roomState = dataSnapshot.getValue(String.class);

                switch (roomState)
                {
                    case "play_again":
                        startGamePresenter.playAgain();
                        break;
                    case "new_game":
                        Intent intent = new Intent(getActivity(), CreateMultiplayerGameActivity.class);
                        startActivity(intent);
                        fragmentListener.closeActivity();
                        break;
                    case "save_new_game":
                        startGamePresenter.saveFirebase();
                        Intent intent2 = new Intent(getActivity(), CreateMultiplayerGameActivity.class);
                        startActivity(intent2);
                        fragmentListener.closeActivity();
                        break;
                    case "save_play_again":
                        startGamePresenter.saveFirebase();
                        startGamePresenter.playAgain();
                        break;
                    case "pausing":
                        if(hostPaused)
                        {
                            if(!isHost)
                                onPause();
                        }
                        else{
                            if(isHost)
                                onPause();
                        }
                        break;
                    case "resuming":
                        if(hostResumed){
                            if(!isHost)
                                onResume();
                        }
                        else{
                            if(isHost)
                                onResume();
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnNewGame, btnSaveAndPlay, btnPlayAgain, btnSaveAndNewGame;
        user = FirebaseAuth.getInstance().getCurrentUser();

        p1Name = (TextView)view.findViewById(R.id.player1name);
        p2Name = (TextView)view.findViewById(R.id.player2name);
        //TODO: Replace with actual name
        //TODO: Java ME wifi
        Profile p = Profile.getCurrentProfile();
        p1Name.setText(p == null ? modelUser.getEmail() : p.getName());
        p1Pic = (ImageView)view.findViewById(R.id.player1pic);
        p2Pic = (ImageView)view.findViewById(R.id.player2pic);

        if(p != null){
            Picasso.with(getActivity().getApplicationContext()).load(modelUser.getPhotoUrl()).into(p1Pic);
        }

        //TODO: Look up opposing user by their UID and load their picture.

        tvTimer = (TextView)view.findViewById(R.id.tvTimer);
        //tvAccuracy = (TextView)view.findViewById(R.id.tv_accuracy);
        tvGreenHits = (TextView)view.findViewById(R.id.tv_green_hits_1);
        tvRedHits = (TextView) view.findViewById(R.id.tv_red_hits_1);
        tvOppGreenHits = (TextView)view.findViewById(R.id.tv_green_hits_2);
        tvOppGreenHits.setText("0");
        tvOppRedHits = (TextView) view.findViewById(R.id.tv_red_hits_2);
        tvOppRedHits.setText("0");
        //tvHits = (TextView)view.findViewById(R.id.tv_hits);
        //tvScore = (TextView)view.findViewById(R.id.tv_score);
        btnNewGame = (Button) view.findViewById(R.id.btn_new_game);
        btnPlayAgain = (Button) view.findViewById(R.id.btn_play_again);
        btnSaveAndPlay = (Button) view.findViewById(R.id.btn_save_play_again);
        btnSaveAndNewGame = (Button) view.findViewById(R.id.btn_save_and_new_game);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        btnContainer = (LinearLayout)view.findViewById(R.id.btn_containers);
        tvContainer = (LinearLayout)view.findViewById(R.id.tv_container);
        timerContainer = (RelativeLayout)view.findViewById(R.id.timer_container);
        btnNewGame.setOnClickListener(this);
        btnPlayAgain.setOnClickListener(this);
        btnSaveAndPlay.setOnClickListener(this);
        btnSaveAndNewGame.setOnClickListener(this);

        findOtherUser();
    }

    private void findOtherUser() {
        DatabaseReference mPlayers = mRootRef.child("rooms").child(roomID).child("players");
        mPlayers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    if(!ds.getKey().equals(user.getUid())){
                        populateOtherUser(ds.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("PLAYERS", "Couldn't load players");
            }
        });
    }

    private void populateOtherUser(String key) {
        DatabaseReference mOtherUser = mRootRef.child("users").child(key);
        mOtherUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                otherUser = dataSnapshot.getValue(User.class);
                p2Name.setText(otherUser.getEmail());

                if(otherUser.getPhotoUrl() != null){
                    Picasso.with(getActivity().getApplicationContext()).load(otherUser.getPhotoUrl()).into(p2Pic);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("USERS", "Couldn't find other user. Blowing up");
            }
        });

        addColorListeners(key, "greenhits");
        addColorListeners(key, "redhits");

        startGamePresenter.startGame();
    }

    private void addColorListeners(String key, final String color) {
        DatabaseReference mOtherPlayerColor = mRootRef.child("rooms").child(roomID).child("players").child(key).child(color);
        mOtherPlayerColor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(color.equals("greenhits"))
                {tvOppGreenHits.setText(""+(int)dataSnapshot.getValue());}
                else if(color.equals("redhits"))
                {tvOppRedHits.setText(""+(int)dataSnapshot.getValue());}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("COLOR", "Color not found");
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        hostPaused = isHost ? true : false;
        mRoomState.setValue("pausing");
        startGamePresenter.cancelGame();
    }

    @Override
    public void onResume() {
        super.onResume();
        hostResumed = isHost ? true : false;
        mRoomState.setValue("resuming");
        startGamePresenter.onResume(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        startGamePresenter.cancelGame();
    }

    @Override
    public void setProgressTotal(int seconds) {
        progressBar.setMax(seconds);
    }

    @Override
    public void setTimerText(String time) {
        tvTimer.setText(time);
    }

    @Override
    public void changeProgressView(long seconds) {
        progressBar.setProgress((int)seconds);
    }

    @Override
    public void updateResult(final float totalHits,final float greenHits, final float redHits,final int score,final int accuracy) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DatabaseReference mPlayerInRoom = mRootRef.child("rooms").child(roomID).child("players").child(user.getUid());
                             /*
                tvHits.setText(String.valueOf(totalHits));
                tvScore.setText(String.valueOf(score));
                tvAccuracy.setText(String.valueOf(accuracy) + " %");
            */

                tvRedHits.setText(String.valueOf(redHits));
                mPlayerInRoom.child("redhits").setValue((int)redHits);
                tvGreenHits.setText(String.valueOf(greenHits));
                mPlayerInRoom.child("greenhits").setValue((int)greenHits);
            }
        });
    }

    @Override
    public void setupInitGame() {
        timerContainer.setVisibility(View.VISIBLE);

        if(isHost){
            btnContainer.setVisibility(View.GONE);
            tvContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void setupFinishGame() {
        timerContainer.setVisibility(View.GONE);

        if(isHost){
            btnContainer.setVisibility(View.VISIBLE);
            tvContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_new_game) {
            mRoomState.setValue("new_game");
        } else if (view.getId() == R.id.btn_save_and_new_game) {
            mRoomState.setValue("save_new_game");
        } else if (view.getId() == R.id.btn_save_play_again) {
            mRoomState.setValue("save_play_again");
        } else if (view.getId() == R.id.btn_play_again) {
            mRoomState.setValue("play_again");
        }
    }

    @Override
    public void saveFirebase(Game game) {
        userDatabaseReference.child("games").push().setValue(game);
    }

}
