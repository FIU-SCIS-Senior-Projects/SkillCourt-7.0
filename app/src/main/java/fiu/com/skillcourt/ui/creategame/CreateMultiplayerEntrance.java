package fiu.com.skillcourt.ui.creategame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import fiu.com.skillcourt.R;
import fiu.com.skillcourt.ui.base.BaseActivity;
import fiu.com.skillcourt.ui.dashboard.MainDashboardActivity;

/**
 * Created by Chandan on 3/20/2017.
 */

public class CreateMultiplayerEntrance extends BaseActivity {
    private Button enterLobby, connectToPlayer, exit;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_choose_connection);
        mAuth = FirebaseAuth.getInstance();

        connectToPlayer = (Button) findViewById(R.id.ChallengePlayerButton);
        enterLobby = (Button) findViewById(R.id.EnterLobbyButton);
        exit = (Button) findViewById(R.id.ExitButton);

        connectToPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Direct message and connect with this player
            }
        });

        enterLobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FirebaseUser user = mAuth.getCurrentUser();
                Intent intent = new Intent(CreateMultiplayerEntrance.this, CreateMultiplayerLobbyActivity.class);
                startActivity(intent);
                finish();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateMultiplayerEntrance.this, MainDashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
