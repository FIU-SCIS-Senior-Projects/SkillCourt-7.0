package fiu.com.skillcourt.ui.creategame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fiu.com.skillcourt.R;
import fiu.com.skillcourt.ui.base.ArduinosStartCommunicationFragment;

/**
 * Created by Chandan on 3/20/2017.
 */

public class CreateMultiplayerFragment extends ArduinosStartCommunicationFragment {

    public static CreateMultiplayerFragment newInstance() { return new CreateMultiplayerFragment();    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //return view;
        return inflater.inflate(R.layout.activity_multiplayer_choose_connection, container, false);
    }
}
