package com.mxk.fragments;


import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mxk.myapp.MyBaseFragment;
import com.mxk.myapp.R;
import com.mxk.ui.floatbutton.SatelliteMenu;
import com.mxk.ui.floatbutton.SatelliteMenu.SateliteClickedListener;
import com.mxk.ui.floatbutton.SatelliteMenuItem;


public class SatelliteMenuFragment extends MyBaseFragment
{
    SatelliteMenu menu;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.sat_fragment, null);
        menu = (SatelliteMenu) view.findViewById(R.id.menu);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        List<SatelliteMenuItem> items = new ArrayList<SatelliteMenuItem>();
        items.add(new SatelliteMenuItem(1, R.drawable.u_key_search));
        items.add(new SatelliteMenuItem(2, R.drawable.u_key_remote));
        items.add(new SatelliteMenuItem(3, R.drawable.u_key_search));
        items.add(new SatelliteMenuItem(4, R.drawable.u_key_search));
        items.add(new SatelliteMenuItem(5, R.drawable.u_key_search));
//        items.add(new SatelliteMenuItem(5, R.drawable.sat_item));
        menu.addItems(items);        
        
        menu.setOnItemClickedListener(new SateliteClickedListener() {
            
            public void eventOccured(int id) {
                Log.i("sat", "Clicked on " + id);
            }
        });
    }
    
}