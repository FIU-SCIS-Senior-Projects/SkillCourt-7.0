package fiu.com.skillcourt.ui.creategame;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;


import fiu.com.skillcourt.R;
import fiu.com.skillcourt.fcm.MyFirebaseInstanceIDService;
import fiu.com.skillcourt.fcm.SearchUserNotifActivity;
import fiu.com.skillcourt.fcm.SendNotifByHTTP;
import fiu.com.skillcourt.ui.base.BaseActivity;
import fiu.com.skillcourt.ui.dashboard.MainDashboardActivity;

/**
 * Created by Chandan on 3/20/2017.
 */

public class CreateMultiplayerEntrance extends BaseActivity {
    private Button enterLobby, connectToPlayer, exit;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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
                Intent intent = new Intent(CreateMultiplayerEntrance.this, SearchUserNotifActivity.class);
                startActivity(intent);
                finish();
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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("CreateMultiplayerEntrance Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
