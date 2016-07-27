package com.mxk.fragments;


import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mxk.myapp.MyBaseFragment;
import com.mxk.myapp.R;
import com.mxk.ui.floatbutton.SatelliteMenu;
import com.mxk.ui.floatbutton.SatelliteMenuItem;


public class FloatButtonFragment extends MyBaseFragment
{
    private SatelliteMenu satelliteMenu;

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
        View rootView = inflater
                .inflate(R.layout.float_button_fragment, container, false);
        satelliteMenu = (SatelliteMenu) rootView.findViewById(R.id.suspend_button_container);
        
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        List<SatelliteMenuItem> items = new ArrayList<SatelliteMenuItem>();
        items.add(new SatelliteMenuItem(1, R.drawable.u_key_search));
        items.add(new SatelliteMenuItem(2, R.drawable.u_key_remote));
        satelliteMenu.addItems(items);
    }

}