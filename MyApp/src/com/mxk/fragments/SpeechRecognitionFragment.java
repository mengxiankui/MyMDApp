package com.mxk.fragments;

import java.util.ArrayList;
import java.util.List;




import com.mxk.myapp.MyBaseFragment;
import com.mxk.myapp.R;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class SpeechRecognitionFragment extends MyBaseFragment
{
    /** Standard activity result: operation canceled. */
    public static final int RESULT_CANCELED    = 0;
    /** Standard activity result: operation succeeded. */
    public static final int RESULT_OK           = -1;
    /** Start of user-defined activity results. */
    public static final int RESULT_FIRST_USER   = 1;
    private final int SPEECHTOTEXT = 1;
    
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
        View rootView = inflater.inflate(R.layout.recognizer_fragment, container, false);
        // Getting an instance of btn_speak
        Button btnSpeak = (Button) rootView.findViewById(R.id.btn_speak);

        // Defining button click listener
        OnClickListener onClickListener = new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                // Getting an instance of PackageManager
                PackageManager pm = getActivity().getPackageManager();

                // Querying Package Manager
                List<ResolveInfo> activities = pm.queryIntentActivities(intent, 0);

                if (activities.size() <= 0)
                {
                    Toast.makeText(
                        getActivity().getBaseContext(),
                        "No Activity found to handle the action ACTION_RECOGNIZE_SPEECH",
                        Toast.LENGTH_SHORT).show();
                    return;
                }

                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                    "Voice recognition Demo...");
                startActivityForResult(intent, SPEECHTOTEXT);
            }
        };

        //Setting a click event handler for the button
        btnSpeak.setOnClickListener(onClickListener);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        ListView lvText = (ListView) getView().findViewById(R.id.lv_text);

        switch (requestCode)
        {
            case SPEECHTOTEXT :
                if (resultCode == RESULT_OK && null != data)
                {
                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        getActivity().getBaseContext(),
                        android.R.layout.simple_list_item_1, text);

                    lvText.setAdapter(adapter);
                }
                break;
        }
    }
    
}