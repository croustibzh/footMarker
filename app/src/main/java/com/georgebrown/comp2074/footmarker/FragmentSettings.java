package com.georgebrown.comp2074.footmarker;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentSettings extends Fragment {



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View myView = inflater.inflate(R.layout.fragment_settings,container,false);
        Switch distSwitch = myView.findViewById(R.id.switch3);

        Resources res = getResources();
        boolean status = res.getBoolean(R.bool.distanceUnit);
        distSwitch.setChecked(status);

        return myView;

    }
    


}
