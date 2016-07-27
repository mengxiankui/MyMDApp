package com.mxk.myapp;


import com.mxk.utils.Consts;

import android.app.Activity;
import android.app.Fragment;


public class MyBaseFragment extends Fragment
{
    
    @Override
    public void onAttach(Activity activity)
    {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getArguments().getInt(
            Consts.ARG_SECTION_NUMBER));
    }
    

}