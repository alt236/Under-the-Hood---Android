package aws.util.underthehood;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class SavedData {
	final String TAG =  this.getClass().getName();
	
	private List<String>  tIp = new ArrayList<String>();
	private List<String>  tIpConfig = new ArrayList<String>();
	private List<String>  tRoute = new ArrayList<String>();
	private List<String>  tNetlist = new ArrayList<String>();
	private List<String>  tPs = new ArrayList<String>();
	private List<String>  tOther = new ArrayList<String>();
	
	private String dateTime = "";
	private boolean areWeRooted = false;
	private int textSize;
	
	public void setTextSize(int size) {
		textSize = size;
	}
	
	public int getTextSize() {
		return textSize;
	}
	
	public String getDateTime() {
		return dateTime;
	}


	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}


	public List<String> gettIp() {
		return tIp;
	}

	public List<String> gettRoute() {
		return tRoute;
	}

	public List<String> gettIpConfig() {
		return tIpConfig;
	}


	public List<String> gettNetlist() {
		return tNetlist;
	}


	public List<String> gettPs() {
		return tPs;
	}


	public List<String> gettDf() {
		return tOther;
	}

	public boolean getAreWeRooted() {
		return areWeRooted;
	}


	public void setAreWeRooted(boolean areWeRooted) {
		this.areWeRooted = areWeRooted;
	}
	
	
	public void popoulateTIp(TableLayout t){
		tIp = tableToList(t);
	}
	
	public void popoulateTIpConfig(TableLayout t){
		tIpConfig = tableToList(t);
	}
	
	
	public void popoulateRoute(TableLayout t){
		tRoute = tableToList(t);
	}
	
	public void popoulateNetlist(TableLayout t){
		tNetlist = tableToList(t);
	}
	
	public void popoulatePs(TableLayout t){
		tPs = tableToList(t);
	}
	
	public void popoulateOther(TableLayout t){
		tOther = tableToList(t);
	}
	
	
	private List<String> tableToList(TableLayout t){
		List<String>  l = new ArrayList<String>();
		
		l.clear();

		for (int i=0; i <= t.getChildCount()-1; i++){
			TableRow row = (TableRow) t.getChildAt(i);

			for (int j=0; j <= row.getChildCount()-1; j++){
				View v = row.getChildAt(j);

				try {
					if(v.getClass() == Class.forName("android.widget.TextView")){
						TextView tmp = (TextView) v;
						l.add(tmp.getText() + "");
					} else {
					// do nothing
					}
				} catch (Exception e) {
					Log.e(TAG, "^ tableToString: " + e.getMessage());
				}
			}
		}
		return l;
	}
	
}
