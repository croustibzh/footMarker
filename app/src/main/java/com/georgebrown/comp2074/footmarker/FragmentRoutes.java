package com.georgebrown.comp2074.footmarker;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentRoutes extends Fragment {
    RoutesListAdapter adapter;
    ArrayList<RouteDetails> rl = new ArrayList<>();
    DataBaseHelper dbHelper;
    int id;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_routes,container,false);
        dbHelper = new DataBaseHelper(getActivity());

        final ListView lstRoutes = v.findViewById(R.id.lstRoutes);
        final SearchView searchView = v.findViewById(R.id.searchView);

        Cursor data = dbHelper.getAllRoutes();
        final ArrayList<RouteDetails> routesList = new ArrayList<>();
        while (data.moveToNext()){
            RouteDetails routeDetails = new RouteDetails(data.getInt(0),data.getString(1),data.getDouble(2),data.getLong(3),data.getFloat(4),data.getBlob(5),data.getString(6));
            routesList.add(routeDetails);
        }

        adapter = new RoutesListAdapter(getActivity(),R.layout.custom_list,routesList);
        lstRoutes.setAdapter(adapter);

        lstRoutes.setClickable(true);
        lstRoutes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                  id = adapter.getItem(i).getId();
//                float rating = adapter.getItem(i).getRating();

                Bundle bundle = new Bundle();
                bundle.putInt("id", id);
//                bundle.putFloat("rating", rating);

                Fragment saveModal = new SaveModal();
                saveModal.setArguments(bundle);

                FragmentRouteDetails fragmentRouteDetails = new FragmentRouteDetails();
                fragmentRouteDetails.setArguments(bundle);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.fragment_container, fragmentRouteDetails);
                fragmentTransaction.commit();
            }
        });

        lstRoutes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                id = adapter.getItem(i).getId();
                dbHelper.deleteItem(id);
                adapter.setNotifyOnChange(routesList.remove(adapter.getItem(i)));
                lstRoutes.setAdapter(adapter);
                return true;
            }
        });

        //SearchView entire clickable area
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });

        //Search Routes
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                rl.clear();
                for(RouteDetails name: routesList){
                    if (name.getName().toLowerCase().contains(s.toLowerCase())){
                        rl.add(name);
                    }
                }
                for(RouteDetails hashTag: routesList){
                    if (hashTag.getHashTag().toLowerCase().contains(s.toLowerCase())){
                        rl.add(hashTag);
                    }
                }

                adapter = new RoutesListAdapter(getActivity(),R.layout.custom_list, rl);
                lstRoutes.setAdapter(adapter);
                getView().clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return v;
    }
}
