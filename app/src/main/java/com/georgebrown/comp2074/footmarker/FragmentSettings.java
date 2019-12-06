package com.georgebrown.comp2074.footmarker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentSettings extends Fragment {


  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    Switch swSet;

    View myView = inflater.inflate(R.layout.fragment_settings,container,false);
    swSet = myView.findViewById(R.id.switch3);
    swSet.setChecked(Constants.DIST_UNIT);


    swSet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      public void onCheckedChanged(CompoundButton sw, boolean isChecked) {
        boolean stat = sw.isChecked();
        Constants.DIST_UNIT = stat;
      }
    });
    return myView;
  };
}
