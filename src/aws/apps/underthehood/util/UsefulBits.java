package aws.apps.underthehood.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import aws.apps.underthehood.R;
import aws.apps.underthehood.ui.MyAlertBox;

public class UsefulBits {
	final String TAG =  this.getClass().getName();
	private Context c;

	public UsefulBits(Context cntx) {
		Log.d(TAG, "^ Object created");
		c = cntx;
	}

	public void ShowAlert(String title, String text, String button){
		if (button.equals("")){button = c.getString(R.string.ok);}

		try{
			AlertDialog.Builder ad = new AlertDialog.Builder(c);
			ad.setTitle( title );
			ad.setMessage(text);

			ad.setPositiveButton( button, null );
			ad.show();
		}catch (Exception e){
			Log.e(TAG, "^ ShowAlert()", e);
		}	
	}

	public static int dipToPixels(int dip, Context c) {
		int value = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
				(float) dip, c.getResources().getDisplayMetrics());
		return value;
	}

	public static float dipToPixels(float dip, Context c) {
		float value = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
				dip, c.getResources().getDisplayMetrics());
		return value;
	}

	public boolean isOnline() {
		try{ 
			ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

			if (cm != null) {
				return cm.getActiveNetworkInfo().isConnected();
			} else {
				return false;
			}

		}catch(Exception e){
			return false;
		}
	}

	/**
	 * Gets the software version and version name for this application
	 */
	public enum SOFTWARE_INFO{
		NAME, VERSION, NOTES, CHANGELOG, COPYRIGHT, ACKNOWLEDGEMENTS
	}

	public String getSoftwareInfo(SOFTWARE_INFO info) {
		try {
			PackageInfo pi = c.getPackageManager().getPackageInfo(c.getPackageName(), 0);
			Resources appR = c.getResources();
			CharSequence txt;
			// Store the software version code and version name somewhere..
			switch(info){
			case VERSION:
				return pi.versionName;
			case NAME:
				txt = appR.getText(appR.getIdentifier("app_name", "string", c.getPackageName())); 
				break;
			case NOTES:
				txt = appR.getText(appR.getIdentifier("app_notes", "string", c.getPackageName())); 
				break;
			case CHANGELOG:
				txt = appR.getText(appR.getIdentifier("app_changelog", "string", c.getPackageName())); 
				break;
			case COPYRIGHT:
				txt = appR.getText(appR.getIdentifier("app_copyright", "string", c.getPackageName())); 
				break;
			case ACKNOWLEDGEMENTS:
				txt = appR.getText(appR.getIdentifier("app_acknowledgements", "string", c.getPackageName())); 
				break;
			default:
				return "";
			}
			String res = txt.toString();
			res = res.replaceAll("\\t", "");
			res = res.replaceAll("\\n\\n", "\n");

			return res.trim();
		} catch (Exception e) {
			Log.e(TAG, "^ Error @ getSoftwareInfo(" + info.name() + ") ", e);
			return "";
		}
	} 

	public String formatDateTime(String formatString, Date d){
		Format formatter = new SimpleDateFormat(formatString);
		return formatter.format(d);
	}

	public Calendar convertMillisToDate(long millis){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar;
	}

	public void saveToFile(String fileName, File directory, String contents){

		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)){
			try {

				if (directory.canWrite()){
					File gpxfile = new File(directory, fileName);
					FileWriter gpxwriter = new FileWriter(gpxfile);
					BufferedWriter out = new BufferedWriter(gpxwriter);
					out.write(contents);
					out.close();
					showToast("Saved to SD as '" + directory.getAbsolutePath() + "/" + fileName + "'", 
							Toast.LENGTH_SHORT, Gravity.TOP,0,0);
				}

			} catch (Exception e) {
				showToast("Could not write file:\n+ e.getMessage()", 
						Toast.LENGTH_SHORT, Gravity.TOP,0,0);
				Log.e(TAG, "^ Could not write file " + e.getMessage());
			}

		}else{
			showToast("No SD card is mounted...", Toast.LENGTH_SHORT, Gravity.TOP,0,0);
			Log.e(TAG, "^ No SD card is mounted.");		
		}
	}

	public void showToast(String message, int duration, int location, int x_offset, int y_offset){
		Toast toast = Toast.makeText(c.getApplicationContext(), message, duration);
		toast.setGravity(location,x_offset,y_offset);
		toast.show();
	}

	public static boolean validateIPv6Address(String address) {
		try {
			java.net.Inet6Address.getByName(address);
			return true;
		} catch(Exception e) {
			return false;
		}
	}

	public static boolean validateIPv4Address(String address) {
		try {
			java.net.Inet4Address.getByName(address);
			return true;
		} catch(Exception e) {
			return false;
		}
	}

	public String tableToString(TableLayout t) {
		String res = "";

		for (int i=0; i <= t.getChildCount()-1; i++){
			TableRow row = (TableRow) t.getChildAt(i);

			for (int j=0; j <= row.getChildCount()-1; j++){
				View v = row.getChildAt(j);

				try {
					if(v.getClass() == Class.forName("android.widget.TextView")){
						TextView tmp = (TextView) v;
						res += tmp.getText();

						if(j==0){res += " ";}
					} else if(v.getClass() == Class.forName("android.widget.EditText")){
						EditText tmp = (EditText) v;
						res += tmp.getText().toString();
					} else {
						//do nothing
					}
				} catch (Exception e) {
					res = e.toString();
					Log.e(TAG, "^ tableToString: " + res);
				}
			}
			res +="\n";
		}
		return res;
	}

	public String getAboutDialogueText(){
		String res = getSoftwareInfo(SOFTWARE_INFO.CHANGELOG);
		res += "\n\n";
		res += getSoftwareInfo(SOFTWARE_INFO.NOTES);
		res += "\n\n";
		res += getSoftwareInfo(SOFTWARE_INFO.ACKNOWLEDGEMENTS);
		res += "\n\n";
		res += getSoftwareInfo(SOFTWARE_INFO.COPYRIGHT);

		res = res.replaceAll("\\t", "");
		res = res.replaceAll("\\n\\n", "\n");
		return res;
	}

	public void showAboutDialogue() {
		String text = "";
		String title = "";

		text += this.getSoftwareInfo(SOFTWARE_INFO.CHANGELOG);
		text += "\n\n";
		text += this.getSoftwareInfo(SOFTWARE_INFO.NOTES);
		text += "\n\n";
		text += this.getSoftwareInfo(SOFTWARE_INFO.ACKNOWLEDGEMENTS);
		text += "\n\n";		
		text += this.getSoftwareInfo(SOFTWARE_INFO.COPYRIGHT);
		title = this.getSoftwareInfo(SOFTWARE_INFO.NAME) + " v"
		+ this.getSoftwareInfo(SOFTWARE_INFO.VERSION);

		if ( c != null ){
			//MyAlertBox.create(c, text, title, c.getString(android.R.string.ok)).show();
			MyAlertBox.create(c, text, title, c.getString(android.R.string.ok)).show();
			
		} else {
			Log.d(TAG, "^ context is null...");
		}
	}
	
	public void shareResults(String subject, String text){
		Intent t = new Intent(Intent.ACTION_SEND);
		 
		//String text = fldInfo.getText().toString();
		//String subject =  getString(R.string.text_under_the_hood)  + " @ " + timeDate;
		
		t.setType("text/plain");
		t.putExtra(Intent.EXTRA_TEXT, text);
		t.putExtra(Intent.EXTRA_SUBJECT, subject);
		t.addCategory(Intent.CATEGORY_DEFAULT);
		Intent share = Intent.createChooser(t, c.getString(R.string.label_share_dialogue_title));
		c.startActivity(share);		
	}
}
