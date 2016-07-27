package com.mxk.utils;


import android.app.Fragment;
import android.os.Bundle;


public class MemuFragmentManager
{
    private static final String menuFragmentList[] = new String[]{"SpeechRecognitionFragment","DSLVFragment","DiaryListFragment","TuLingLauncherFragment","DrawerFragment","FloatButtonFragment","SatelliteMenuFragment","PopupFragment","ShareFragment","KeyWordFragment","TextImageFragment","OpenGLFragment","","AboutFragment"};

    @SuppressWarnings("unchecked")
    public static <T extends Fragment> T getFragmentAtPosition(int position)
    {
        if (menuFragmentList.length > position)
        {
            String strApp = menuFragmentList[position];
            if (!"".equals(strApp))
            {
                try
                {
                    T fragment = (T) Class.forName(
                        "com.mxk.fragments." + "" + strApp)
                            .newInstance();
                    Bundle bundle = new Bundle();
                    bundle.putInt(Consts.ARG_SECTION_NUMBER, position);
                    fragment.setArguments(bundle);
                    return fragment;
                }
                catch (InstantiationException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                catch (IllegalAccessException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                catch (ClassNotFoundException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            //            Fragment fragment = new menuFragmentList[position]
        }
        return null;
    }

}
