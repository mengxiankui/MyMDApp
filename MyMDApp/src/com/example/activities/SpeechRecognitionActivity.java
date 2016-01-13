package com.example.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.example.mymdapp.R;
import com.mxk.baseapplication.LBaseActivity;

public class SpeechRecognitionActivity extends LBaseActivity {

	/** Standard activity result: operation canceled. */
    public static final int RESULT_CANCELED    = 0;
    /** Standard activity result: operation succeeded. */
    public static final int RESULT_OK           = -1;
    /** Start of user-defined activity results. */
    public static final int RESULT_FIRST_USER   = 1;
    private final int SPEECHTOTEXT = 1;
    
    @Bind(R.id.btn_speak)
    Button btnSpeak;
	
	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.activity_recognizer;
	}

	@Override
	public void setToolBar(Toolbar mToolbar) {
		// TODO Auto-generated method stub
		mToolbar.setTitle("SpeechRecognition");
	}

	@Override
	public void onAfterOnCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		ButterKnife.bind(this);
		setupToolBar();
		init();
	}
	
	private void init() {
		// TODO Auto-generated method stub
		btnSpeak.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                // Getting an instance of PackageManager
                PackageManager pm = SpeechRecognitionActivity.this.getPackageManager();

                // Querying Package Manager
                List<ResolveInfo> activities = pm.queryIntentActivities(intent, 0);

                if (activities.size() <= 0)
                {
                    Toast.makeText(
                        SpeechRecognitionActivity.this,
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
        });
	    
	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        ListView lvText = (ListView)findViewById(R.id.lv_text);

        switch (requestCode)
        {
            case SPEECHTOTEXT :
                if (resultCode == RESULT_OK && null != data)
                {
                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1, text);

                    lvText.setAdapter(adapter);
                }
                break;
        }
    }

	private void setupToolBar() {
		// TODO Auto-generated method stub
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getToolBar().setNavigationOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	}

}