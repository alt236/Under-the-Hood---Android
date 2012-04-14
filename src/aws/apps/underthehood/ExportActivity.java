package aws.apps.underthehood;

import java.io.File;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import aws.util.underthehood.UsefulBits;

import com.actionbarsherlock.app.SherlockActivity;

public class ExportActivity extends SherlockActivity {
	final String TAG =  this.getClass().getName();
	
	private EditText fldInfo;
	private Button btnShare;
	private Button btnToSd;
	private Button btnClose;
	private String timeDate;
	private UsefulBits uB;
	
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "* Intent started");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.export);
		Bundle extras = getIntent().getExtras();
		uB = new UsefulBits(getApplicationContext());
		
		fldInfo = (EditText) findViewById(R.id.fld_export_text);
		btnShare = (Button) findViewById(R.id.buttonshare);
		btnToSd = (Button) findViewById(R.id.buttontosd);
		btnClose = (Button) findViewById(R.id.buttoncloseexport);
		
		if(extras !=null)
		{
			timeDate = extras.getString("time");
			fldInfo.setText(getString(R.string.text_under_the_hood)  + " @ " + timeDate +"\n\n");
			fldInfo.append(extras.getString("info"));
		}	
		
		btnShare.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				shareResults();
			}
		});
		
		btnToSd.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				try {
					File folder = Environment.getExternalStorageDirectory();
					String filename = "deviceinfo_" + timeDate + ".txt";
					String contents = fldInfo.getText().toString();
					uB.saveToFile(filename, folder, contents);
				} catch (Exception e) {
					Log.e(TAG, "* " + e.getMessage());
				}
			}
		});
		
		btnClose.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private void shareResults(){
		Intent t = new Intent(Intent.ACTION_SEND);
		 
		String text = fldInfo.getText().toString();
		String subject =  getString(R.string.text_under_the_hood)  + " @ " + timeDate;
		
		t.setType("text/plain");
		t.putExtra(Intent.EXTRA_TEXT, text);
		t.putExtra(Intent.EXTRA_SUBJECT, subject);
		t.addCategory(Intent.CATEGORY_DEFAULT);
		Intent share = Intent.createChooser(t, getString(R.string.label_share_dialogue_title));
		startActivity(share);		
	}
	

}
