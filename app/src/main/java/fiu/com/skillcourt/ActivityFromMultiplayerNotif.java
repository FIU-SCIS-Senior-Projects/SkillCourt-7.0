package fiu.com.skillcourt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import fiu.com.skillcourt.ui.base.BaseActivity;
import fiu.com.skillcourt.ui.base.BaseFragment;

public class ActivityFromMultiplayerNotif extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_from_multiplayer_notif);

        Button acceptButton = (Button) findViewById(R.id.accept_button);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("working?", "working!");
            }
        });
    }
}
