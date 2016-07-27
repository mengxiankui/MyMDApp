package com.mxk.fragments;


import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

import com.mxk.myapp.MyBaseFragment;
import com.mxk.myapp.R;
import com.mxk.ui.keyword.KeyWordsView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class KeyWordFragment extends MyBaseFragment
{
    private ArrayList<String> keys;
    private KeyWordsView keyWordsView;
    private Button button;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        keys = new ArrayList<String>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.keywords_fragment, container, false);
        keyWordsView = (KeyWordsView) rootView.findViewById(R.id.keywordsview);
        button = (Button) rootView.findViewById(R.id.btn);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        recreateKeywordView();
        button.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                recreateKeywordView();
            }
        });

    }

    private void recreateKeywordView()
    {
        for (int i = 0; i < 20; i++)
        {
            int rad = new Random().nextInt(15);
            keys.add(RandomStringUtils.random(rad, 65, 90, true, true));
        }
        keyWordsView.addViews(keys);
        for (int i = 0; i < keyWordsView.getChildCount(); i++)
        {
            keyWordsView.getChildAt(i).setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    // TODO Auto-generated method stub
                    Toast.makeText(getActivity(), ((TextView) v).getText().toString(),
                        Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}