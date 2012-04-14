package aws.apps.underthehood;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import aws.util.underthehood.ExecTerminal;
import aws.util.underthehood.ExecuteThread;
import aws.util.underthehood.GuiCreation;
import aws.util.underthehood.SavedData;
import aws.util.underthehood.UsefulBits;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;


public class Main extends SherlockActivity {
	private static final int DIALOG_EXECUTING = 1;
	final String TAG =  this.getClass().getName();

	private TabHost mTabHost;
	private String TimeDate="";
	private UsefulBits uB;
	private TableLayout tableIpconfig;
	private TableLayout tableIpRoute;
	private TableLayout tablePs;
	private TableLayout tableOther;
	private TableLayout tableNetstat;
	private TableLayout tableProc;
	private TableLayout tableDeviceInfo;
	private TextView lblRootStatus;
	private TextView lblTimeDate;
	private TextView lblDevice;	
	private boolean device_rooted = false;
	private int DataTextSize = 0;
	private GuiCreation gui;
	private ExecuteThread executeThread;
	private ProgressDialog executeDialog;
	private Bundle threadBundle;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "^ Intent Started");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		uB = new UsefulBits(this);

		//setup GUI
		gui = new GuiCreation(this);
		tableIpconfig = (TableLayout) findViewById(R.id.main_table_ipconfig_info);
		tableIpRoute= (TableLayout) findViewById(R.id.main_table_ip_route_info);
		tablePs= (TableLayout) findViewById(R.id.main_table_ps_info);
		tableOther= (TableLayout) findViewById(R.id.main_table_other_info);
		tableNetstat= (TableLayout) findViewById(R.id.main_table_netstat_info);
		tableProc= (TableLayout) findViewById(R.id.main_table_proc_info);
		tableDeviceInfo= (TableLayout) findViewById(R.id.main_table_device_info);
		lblRootStatus= (TextView) findViewById(R.id.tvRootStatus);
		lblTimeDate = (TextView) findViewById(R.id.tvTime);
		//lblROM = (TextView) findViewById(R.id.tvROM);
		lblDevice = (TextView) findViewById(R.id.tvDevice);

		setupTabs();

		populateInfo();
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		Log.d(TAG, "^ onRetainNonConfigurationInstance()");

		final SavedData saved = new SavedData();

		saved.popoulateOther(tableOther);
		saved.popoulateNetlist(tableNetstat);
		saved.popoulatePs(tablePs);
		saved.popoulateRoute(tableProc);
		saved.popoulateTIp(tableIpRoute);
		saved.popoulateTIpConfig(tableIpconfig);
		saved.setAreWeRooted(device_rooted);
		saved.setDateTime(TimeDate);

		return saved;
	}

	/** Creates the menu items */
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);

		return true;
	}

	/** Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();

		if(R.id.menu_about == id){
			uB.showAboutDialogue();
			return true;
		}
		else if(R.id.menu_export == id ){
			Intent myIntent = new Intent();
			String export_text = "";

			export_text += uB.tableToString(tableDeviceInfo);
			export_text += getString(R.string.label_ipconfig_info) + "\n";
			export_text += uB.tableToString(tableIpconfig);
			export_text += getString(R.string.label_ip_route_info) + "\n";
			export_text += uB.tableToString(tableIpRoute);
			export_text += getString(R.string.label_proc_info) + "\n";
			export_text += uB.tableToString(tableProc);
			export_text += getString(R.string.label_netlist_info) + "\n";
			export_text += uB.tableToString(tableNetstat);
			export_text += getString(R.string.label_ps_info) + "\n";
			export_text += uB.tableToString(tablePs);
			export_text += getString(R.string.label_other_info) + "\n";
			export_text += uB.tableToString(tableOther);			
			myIntent.putExtra("info", export_text);
			myIntent.putExtra("time", TimeDate);
			myIntent.setClassName(getPackageName(),getPackageName() + ".ExportActivity");
			startActivity(myIntent);
			return true;
		}
		else if(R.id.menu_increase_font_size == id ){
			increaseFontSize();
			return true;
		}
		else if(R.id.menu_decrease_font_size == id ){
			decreaseFontSize();
			return true;
		}
		else if(R.id.menu_refresh == id ){
			refreshInfo();
			return true;
		}

		return false;
	}

	//	/** Creates the menu items */
	//	public boolean onCreateOptionsMenu(Menu menu) {
	//		menu.add(0, MENU_BUTTONS.REFRESH.ordinal(), 0,
	//				getString(R.string.label_menu_refresh)).setIcon(android.R.drawable.ic_menu_rotate);
	//		menu.add(0, MENU_BUTTONS.EXPORT.ordinal(), 0,
	//				getString(R.string.label_menu_export)).setIcon(android.R.drawable.ic_menu_upload);
	//		menu.add(0, MENU_BUTTONS.ABOUT.ordinal(), 0,
	//				getString(R.string.label_menu_about)).setIcon(android.R.drawable.ic_menu_info_details);
	//		menu.add(0, MENU_BUTTONS.QUIT.ordinal(), 0,
	//				getString(R.string.label_menu_quit)).setIcon(android.R.drawable.ic_menu_close_clear_cancel);
	//		menu.add(0, MENU_BUTTONS.FONT_MINUS.ordinal(), 0,
	//				getString(R.string.label_menu_font_size_minus)).setIcon(getResources().getDrawable(R.drawable.minus));
	//		menu.add(0, MENU_BUTTONS.FONT_PLUS.ordinal(), 0,
	//				getString(R.string.label_menu_font_size_plus)).setIcon(getResources().getDrawable(R.drawable.plus));		
	//		return true;
	//	}
	//
	//	/** Handles item selections */
	//	public boolean onOptionsItemSelected(MenuItem item) {
	//		switch (MENU_BUTTONS.lookUpByOrdinal(item.getItemId())) {
	//		case ABOUT:
	//			String text = "";
	//			String title = "";
	//
	//			text = uB.getAboutDialogueText();
	//			title = uB.getSoftwareInfo(SOFTWARE_INFO.NAME) + " v"
	//			+ uB.getSoftwareInfo(SOFTWARE_INFO.VERSION);
	//
	//			MyAlertBox.create(this, text, title, getString(R.string.ok)).show();
	//			return true;
	//		case REFRESH:
	//			refreshInfo();
	//			return true;
	//		case QUIT:
	//			this.finish();
	//			return true;
	//		case EXPORT:
	//			Intent myIntent = new Intent();
	//			String export_text = "";
	//
	//			export_text += uB.tableToString(tableDeviceInfo);
	//			export_text += getString(R.string.label_ipconfig_info) + "\n";
	//			export_text += uB.tableToString(tableIpconfig);
	//			export_text += getString(R.string.label_ip_route_info) + "\n";
	//			export_text += uB.tableToString(tableIpRoute);
	//			export_text += getString(R.string.label_proc_info) + "\n";
	//			export_text += uB.tableToString(tableProc);
	//			export_text += getString(R.string.label_netlist_info) + "\n";
	//			export_text += uB.tableToString(tableNetstat);
	//			export_text += getString(R.string.label_ps_info) + "\n";
	//			export_text += uB.tableToString(tablePs);
	//			export_text += getString(R.string.label_other_info) + "\n";
	//			export_text += uB.tableToString(tableOther);			
	//			myIntent.putExtra("info", export_text);
	//			myIntent.putExtra("time", TimeDate);
	//			myIntent.setClassName(getPackageName(),getPackageName() + ".ExportActivity");
	//			startActivity(myIntent);
	//			return true;
	//		case FONT_MINUS:
	//			decreaseFontSize();
	//			return true;
	//		case FONT_PLUS:
	//			increaseFontSize();
	//			return true;
	//		}
	//		return false;
	//	}

	public void increaseFontSize(){
		DataTextSize = DataTextSize + 2;
		if (DataTextSize > getResources().getInteger(R.integer.max_font_size)){
			DataTextSize = getResources().getInteger(R.integer.max_font_size);
		}
		changeFontSize(tableIpconfig, DataTextSize);
		changeFontSize(tableIpRoute, DataTextSize);
		changeFontSize(tableNetstat, DataTextSize);
		changeFontSize(tableProc, DataTextSize);
		changeFontSize(tableOther, DataTextSize);
		changeFontSize(tablePs, DataTextSize);
	}

	public void decreaseFontSize(){
		DataTextSize = DataTextSize - 2;
		if (DataTextSize < getResources().getInteger(R.integer.min_font_size)){
			DataTextSize = getResources().getInteger(R.integer.min_font_size);
		}
		changeFontSize(tableIpconfig, DataTextSize);
		changeFontSize(tableIpRoute, DataTextSize);
		changeFontSize(tableNetstat, DataTextSize);
		changeFontSize(tableProc, DataTextSize);
		changeFontSize(tableOther, DataTextSize);
		changeFontSize(tablePs, DataTextSize);
	}

	/** Convenience function combining clearInfo and getInfo */
	public void refreshInfo() {
		clearInfo();
		showSelectionDialogue();
	}

	/** Clears the table and field contents */
	public void clearInfo() {
		tableIpconfig.removeAllViews();
		tableIpRoute.removeAllViews();
		tableNetstat.removeAllViews();
		tableProc.removeAllViews();
		tableOther.removeAllViews();
		tablePs.removeAllViews();

		lblRootStatus.setText("");
		lblTimeDate.setText("");
		gui.resetOffset();
	}

	/** Retrieves and displays info */
	private void populateInfo(){
		//HashMap<Integer, Boolean> actionMap) {
		final Object data = getLastNonConfigurationInstance();
		LayoutParams lp = new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

		if (data == null) { // We need to do everything from scratch!
			TimeDate = uB.formatDateTime("yyyy-MM-dd-HHmmssZ", new Date());
			lblTimeDate.setText(TimeDate);

			device_rooted = checkIfSu();
			DataTextSize = getResources().getInteger(R.integer.terminal_data_font);

			showSelectionDialogue();
		} else {
			final SavedData saved = (SavedData) data;
			TimeDate = saved.getDateTime();
			device_rooted = saved.getAreWeRooted();

			lblTimeDate.setText(TimeDate);
			DataTextSize = saved.getTextSize();

			listToTable(saved.gettIpConfig(), tableIpconfig, lp);
			listToTable(saved.gettIp(),tableIpRoute, lp);
			listToTable(saved.gettRoute(),tableProc, lp);
			listToTable(saved.gettDf(),tableOther, lp);
			listToTable(saved.gettPs(),tablePs, lp);
			listToTable(saved.gettNetlist(),tableNetstat, lp);
		}

		if(device_rooted){
			device_rooted = true;
			lblRootStatus.setText(getString(R.string.root_status_ok));
		} else {
			lblRootStatus.setText(getString(R.string.root_status_not_ok));
		}
		lblDevice.setText(Build.PRODUCT + " " + Build.DEVICE);

		//String res = Build.HOST+ " " + Build.ID+ " " + Build.MODEL+ " " + Build.PRODUCT+ " " + Build.TAGS+ " " + Build.TIME+ " " + Build.TYPE+ " " + Build.USER;

	}

	/////////////////////////////////////////////
	private void setupTabs(){

		mTabHost = (TabHost)findViewById(android.R.id.tabhost);
		mTabHost.setup();

		TabWidget tw = mTabHost.getTabWidget();

		mTabHost.addTab(mTabHost.newTabSpec("tab_ipconfig").setIndicator("netcfg / ipconfig", getResources().getDrawable(R.drawable.ipconfig)).setContent(R.id.main_scrollview_ipconfig_info));
		mTabHost.addTab(mTabHost.newTabSpec("tab_ip_route").setIndicator("ip", getResources().getDrawable(R.drawable.ip)).setContent(R.id.main_scrollview_ip_route_info));
		mTabHost.addTab(mTabHost.newTabSpec("tab_proc").setIndicator("proc", getResources().getDrawable(R.drawable.route)).setContent(R.id.main_scrollview_proc_info));
		mTabHost.addTab(mTabHost.newTabSpec("tab_netstat").setIndicator("netstat", getResources().getDrawable(R.drawable.netlist)).setContent(R.id.main_scrollview_netstat_info));
		mTabHost.addTab(mTabHost.newTabSpec("tab_ps").setIndicator("ps", getResources().getDrawable(R.drawable.ps)).setContent(R.id.main_scrollview_ps_info));
		mTabHost.addTab(mTabHost.newTabSpec("tab_other").setIndicator("other", getResources().getDrawable(R.drawable.other)).setContent(R.id.main_scrollview_other_info));
		mTabHost.setCurrentTab(0);

		for (int i = 0; i < tw.getChildCount(); i++) {
			tw.getChildAt(i).getLayoutParams().height = UsefulBits.dipToPixels(40, this);
			final TextView tv = (TextView) tw.getChildAt(i).findViewById(android.R.id.title);        
			//tv.setTextColor(this.getResources().getColorStateList(R.drawable.text_tab_indicator));
			tv.setTextSize(10f);
		} 
	}

	private void listToTable(List<String> l, TableLayout t, LayoutParams lp){
		String chr;
		String seperator = getString(R.string.seperator_identifier);

		try{
			if (l.size() == 0){
				return;
			}

			for (int i=0; i < l.size();i++){
				chr = l.get(i).substring(0, 1);

				if ( chr.equals("#") || chr.equals("$") ) {
					t.addView(gui.createTitleRow(l.get(i)),lp);
				} else if (chr.equals(seperator)){
					t.addView(gui.createSeperatorRow(l.get(i)),lp);
				}else {
					t.addView(gui.createDataRow(l.get(i), DataTextSize),lp);
				}
			}
		} catch (Exception e){
			Log.e(TAG, "^ listToTable() Exception: " + e.getMessage());
		}
	}

	private void changeFontSize(TableLayout t, int FontSize){
		for (int i=0; i <= t.getChildCount()-1; i++){
			TableRow row = (TableRow) t.getChildAt(i);

			for (int j=0; j <= row.getChildCount()-1; j++){
				View v = row.getChildAt(j);

				try {
					if(v.getClass() == Class.forName("android.widget.TextView")){
						TextView tv = (TextView) v;
						if ( i % 2 == 0 ) {
						}else {
							tv.setTextSize(FontSize);
						}
					}
				} catch (Exception e) {
					Log.e(TAG, "^ changeFontSize: ");
				}
			}
		}
	}

	private boolean checkIfSu(){
		String res = "";
		ExecTerminal et = new ExecTerminal();
		res = et.execSu("su && echo 1");

		if (res.trim().equals("1")){
			Log.d(TAG, "^ Device can do SU");
			return true;
		}

		Log.d(TAG, "^ Device can't do SU");
		return false;
	}

	// Sets screen rotation as fixed to current rotation setting
	private void mLockScreenRotation(){
		// Stop the screen orientation changing during an event
		switch (this.getResources().getConfiguration().orientation)
		{
		case Configuration.ORIENTATION_PORTRAIT:
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			break;
		case Configuration.ORIENTATION_LANDSCAPE:
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			break;    	
		}
	}

	private void showSelectionDialogue(){
		final CharSequence[] cb_items = getResources().getTextArray(R.array.shell_commands);
		final Hashtable<CharSequence, Boolean> action_list = new Hashtable<CharSequence, Boolean>();

		boolean[] cb_item_state = new boolean[cb_items.length];

		Arrays.sort(cb_items, 0, cb_items.length);
		for (int i=0;i < cb_items.length; i ++ ){
			cb_item_state[i] = true;
			action_list.put(cb_items[i], cb_item_state[i]);
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.dialogue_title_select_commands));
		builder.setMultiChoiceItems(cb_items, cb_item_state, new DialogInterface.OnMultiChoiceClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				/* Used has clicked a check box */

				action_list.put(cb_items[which], isChecked);

			}}).setPositiveButton(getString(R.string.ok), new
					DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int	whichButton) { /* User clicked Yes so do some stuff */
					threadBundle = new Bundle();
					threadBundle.putSerializable("actions", action_list);
					mLockScreenRotation();
					showDialog(DIALOG_EXECUTING);				
				}
			})
			.setNegativeButton(getString(R.string.cancel), new
					DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int	whichButton) { /* User clicked No so do some stuff */
				}
			}) ;
		AlertDialog alert = builder.create();
		alert.show();
	}


	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_EXECUTING:
			executeDialog = new ProgressDialog(this);
			executeDialog.setMessage(getString(R.string.dialogue_text_please_wait));

			executeThread = new ExecuteThread(handler, this, threadBundle);
			executeThread.start();
			return executeDialog;
		default:
			return null;
		}
	}

	final Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			final LayoutParams lp = new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			ArrayList<String> l = new ArrayList<String>();
			switch(msg.what){

			case ExecuteThread.WORK_COMPLETED:
				Log.d(TAG, "^ Worker Thread: WORK_COMPLETED");

				l = (ArrayList<String>) msg.getData().getSerializable("other");
				listToTable(l, tableOther, lp);

				l = (ArrayList<String>) msg.getData().getSerializable("ip_route");
				listToTable(l, tableIpRoute, lp);

				l = (ArrayList<String>) msg.getData().getSerializable("ipconfig");
				listToTable(l, tableIpconfig, lp);

				l = (ArrayList<String>) msg.getData().getSerializable("netstat");
				listToTable(l, tableNetstat, lp);

				l = (ArrayList<String>) msg.getData().getSerializable("ps");
				listToTable(l, tablePs, lp);

				l = (ArrayList<String>) msg.getData().getSerializable("proc");
				listToTable(l, tableProc, lp);

				executeThread.setState(ExecuteThread.STATE_DONE);
				removeDialog(DIALOG_EXECUTING);
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

			case ExecuteThread.WORK_INTERUPTED:
				executeThread.setState(ExecuteThread.STATE_DONE);
				removeDialog(DIALOG_EXECUTING);
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
				break;
			}

		}
	};
}