package fiu.com.skillcourt.fcm;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import fiu.com.skillcourt.R;
import fiu.com.skillcourt.ui.creategame.CreateMultiplayerLobbyActivity;
import fiu.com.skillcourt.ui.coach_dashboard.CoachingDashboardActivity;
import fiu.com.skillcourt.ui.coach_dashboard.CoachingFragment;
import fiu.com.skillcourt.ui.creategame.CreateLobbyRoomActivity;
import fiu.com.skillcourt.ui.creategame.CreateMultiplayerLobbyActivity;
import fiu.com.skillcourt.ui.creategame.CreateMultiplayerLobbyWaitingActivity;
import fiu.com.skillcourt.ui.team_details.TeamDetailsActivity;


public class SearchUserNotifFragment extends Fragment {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUserRef = mRootRef.child("users");
    private Menu menu;
    private List<Player> playersResultList = new ArrayList<>();
    ListView listViewPlayersResult;
    String roomID;

    public SearchUserNotifFragment() {
        // Required empty public constructor
    }

    public static SearchUserNotifFragment newInstance() {
        SearchUserNotifFragment fragment = new SearchUserNotifFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_user_notif, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.options_menu, menu);

        this.menu = menu;

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        listViewPlayersResult = (ListView) getView().findViewById(R.id.list_view_players_results);

        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener(){

                    @Override
                    public boolean onQueryTextChange (String newText) {
                        return true;
                    }

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        mUserRef.orderByChild("email").startAt(query)
                                .addValueEventListener(
                                        new ValueEventListener() {
                                            public void onDataChange(DataSnapshot snapshot) {
                                                playersResultList.clear();
                                                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                                    String email = postSnapshot.child("email").getValue(String.class);
                                                    String id = postSnapshot.getKey();
                                                    String fcmId = postSnapshot.child("fcmId").getValue(String.class);
                                                    Player player = new Player(id,email,fcmId);
                                                    playersResultList.add(player);
                                                }

                                                showResultList();
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) { }
                                        });
                        return false;
                    }
                });

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        ComponentName componentName = new ComponentName(
                getActivity().getApplicationContext(), getActivity().getClass());
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(componentName));

    }

    public void showResultList(){
        ArrayAdapter<Player> arrayAdapter = new ArrayAdapter<Player>(getContext(), android.R.layout.simple_list_item_1, playersResultList);

        listViewPlayersResult.setAdapter(arrayAdapter);

        listViewPlayersResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
                final String selectedPlayerID = playersResultList.get(position).getId();
                String selectedPlayerEmail = playersResultList.get(position).getEmail();
                final String selectedPlayerFcmID = playersResultList.get(position).getFcmId();

                final EditText input = new EditText(getContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);

                AlertDialog ad = new AlertDialog.Builder(getContext())
                        .create();
                ad.setCancelable(false);
                ad.setTitle("Want to challenge "+selectedPlayerEmail+"?");
                ad.setButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("THIS ID",selectedPlayerFcmID);
                        new SendNotifByHTTP().sending(selectedPlayerFcmID);
                        dialog.dismiss();

                        createRoom();
                        DatabaseReference mOpponentRoom = mRootRef.child("users").child(selectedPlayerID).child("room");
                        mOpponentRoom.setValue(roomID);
                        Intent intent = new Intent(getActivity(), CreateMultiplayerLobbyWaitingActivity.class);
                        startActivity(intent);

                    }
                });
                ad.show();
            }
        });
    }

    static class Player{
        String id;
        String email;
        String fcmId;

        public Player() {
        }

        public Player(String id, String email, String fcmId) {
            this.id = id;
            this.email = email;
            this.fcmId = fcmId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFcmId() {
            return fcmId;
        }

        public void setFcmId(String fcmId) {
            this.fcmId = fcmId;
        }


        @Override
        public String toString() {
            return email;
        }
    }

    private void createRoom() {
        DatabaseReference mRooms = mRootRef.child("rooms");
        DatabaseReference mRoom = mRooms.push();
        roomID = mRoom.getKey();
        buildJSONTree(mRoom);
    }

    private void buildJSONTree(DatabaseReference location) {
        DatabaseReference mHost = location.child("host");
        mHost.setValue(user.getUid());
        DatabaseReference mName = location.child("name");
        mName.setValue(user.getUid() + "'s room");
        DatabaseReference mStatus = location.child("status");
        mStatus.setValue("created");
        DatabaseReference mSubText = location.child("subtext");
        mSubText.setValue("Closed");

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
        mRoomState.setValue(roomID);
    }
}
