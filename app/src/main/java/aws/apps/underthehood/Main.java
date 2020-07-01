/*******************************************************************************
 * Copyright 2010 Alexandros Schillings
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package aws.apps.underthehood;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import aws.apps.underthehood.ui.GuiCreation;
import aws.apps.underthehood.util.UsefulBits;
import uk.co.alt236.underthehood.commandrunner.ExecuteThread;
import uk.co.alt236.underthehood.commandrunner.core.ExecTerminal;

public class Main extends AppCompatActivity {
    private static final int DIALOG_EXECUTING = 1;
    private final String TAG = this.getClass().getName();

    private NameViewModel model;

    private ViewAbstraction view;
    private String mTimeDate = "";
    private UsefulBits uB;
    final Handler handler = new Handler() {
        @SuppressWarnings({"unchecked",})
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case ExecuteThread.WORK_COMPLETED:
                    Log.d(TAG, "^ Worker Thread: WORK_COMPLETED");

                    setData(msg.getData());

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
    private ExecuteThread executeThread;

    @SuppressWarnings("unchecked")
    private void setData(Bundle bundle) {
        ArrayList<String> l;

        l = (ArrayList<String>) bundle.getSerializable(ExecuteThread.MSG_OTHER);
        view.setTableOtherData(l);

        l = (ArrayList<String>) bundle.getSerializable(ExecuteThread.MSG_IP_ROUTE);
        view.setTableIpRouteData(l);

        l = (ArrayList<String>) bundle.getSerializable(ExecuteThread.MSG_IPCONFIG);
        view.setTableIpConfigData(l);

        l = (ArrayList<String>) bundle.getSerializable(ExecuteThread.MSG_NETSTAT);
        view.setTableNetstatData(l);

        l = (ArrayList<String>) bundle.getSerializable(ExecuteThread.MSG_PS);
        view.setTablePsData(l);

        l = (ArrayList<String>) bundle.getSerializable(ExecuteThread.MSG_PROC);
        view.setTableProcData(l);

        l = (ArrayList<String>) bundle.getSerializable(ExecuteThread.MSG_SYSPROP);
        view.setTableSysPropData(l);
    }

    private Bundle threadBundle;

    private boolean checkIfSu() {
        String res = "";
        ExecTerminal et = new ExecTerminal();
        res = et.execSu("su && echo 1");

        if (res.trim().equals("1")) {
            Log.d(TAG, "^ Device can do SU");
            return true;
        }

        Log.d(TAG, "^ Device can't do SU");
        return false;
    }

    // Sets screen rotation as fixed to current rotation setting
    private void lockScreenRotation() {
        // Stop the screen orientation changing during an event
        switch (this.getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
        }
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "^ Intent Started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        model = new ViewModelProvider(this).get(NameViewModel.class);


        uB = new UsefulBits(this);
        view = new ViewAbstraction(this);

        //setup GUI
        final GuiCreation gui = new GuiCreation(this);

        final Observer<String> nameObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newName) {
                // Update the UI, in this case, a TextView.
                uB.showToast("New name: " + newName, Toast.LENGTH_SHORT);
            }
        };

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        model.getCurrentName().observe(this, nameObserver);

        refreshInfo();
    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_EXECUTING:
                final ProgressDialog executeDialog = new ProgressDialog(this);
                executeDialog.setMessage(getString(R.string.dialogue_text_please_wait));

                executeThread = new ExecuteThread(handler, getResources(), threadBundle);
                executeThread.start();
                return executeDialog;
            default:
                return null;
        }
    }

    /**
     * Creates the menu items
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }


    private String collectDataForExport() {
        StringBuilder sb = new StringBuilder();

        sb.append(getString(R.string.text_under_the_hood) + " @ " + mTimeDate + "\n\n");

        sb.append("---------------------------------\n\n");

//        sb.append(tableConverter.tableToString(tableDeviceInfo));
//        sb.append(getString(R.string.label_ipconfig_info) + "\n");
//        sb.append(tableConverter.tableToString(tableIpconfig));
//        sb.append(getString(R.string.label_ip_route_info) + "\n");
//        sb.append(tableConverter.tableToString(tableIpRoute));
//        sb.append(getString(R.string.label_proc_info) + "\n");
//        sb.append(tableConverter.tableToString(tableProc));
//        sb.append(getString(R.string.label_netlist_info) + "\n");
//        sb.append(tableConverter.tableToString(tableNetstat));
//        sb.append(getString(R.string.label_ps_info) + "\n");
//        sb.append(tableConverter.tableToString(tablePs));
//        sb.append(getString(R.string.label_sys_prop) + "\n");
//        sb.append(tableConverter.tableToString(tableSysProp));
//        sb.append(getString(R.string.label_other_info) + "\n");
//        sb.append(tableConverter.tableToString(tableOther));
        sb.append("\n\n---------------------------------");
        return sb.toString().trim();
    }

    /**
     * Handles item selections
     */
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (R.id.menu_about == id) {
            uB.showAboutDialogue();
            return true;
        } else if (R.id.menu_share == id) {
            uB.shareResults(
                    getString(R.string.text_under_the_hood) + " @ " + mTimeDate,
                    collectDataForExport());
            return true;
        } else if (R.id.menu_to_sd == id) {
            try {
                File folder = Environment.getExternalStorageDirectory();
                String filename = "deviceinfo_" + mTimeDate + ".txt";
                String contents = collectDataForExport();
                uB.saveToFile(filename, folder, contents);
            } catch (Exception e) {
                Log.e(TAG, "^ " + e.getMessage());
            }
        }
//		else if(R.id.menu_increase_font_size == id ){
//			fontSizeIncrease();
//			return true;
//		}
//		else if(R.id.menu_decrease_font_size == id ){
//			fontSizeDecrease();
//			return true;
//		}
        else if (R.id.menu_refresh == id) {
            refreshInfo();
            return true;
        }

        return false;
    }

//    @Override
//    public Object onRetainCustomNonConfigurationInstance() {
//        Log.d(TAG, "^ onRetainNonConfigurationInstance()");
//
//        final SavedData saved = new SavedData();
//
//        saved.populateOther(tableOther);
//        saved.populateNetlist(tableNetstat);
//        saved.populatePs(tablePs);
//        saved.populateRoute(tableProc);
//        saved.populateIp(tableIpRoute);
//        saved.populateIpConfig(tableIpconfig);
//        saved.populateSysProp(tableSysProp);
//        saved.setAreWeRooted(device_rooted);
//        saved.setDateTime(mTimeDate);
//
//        return saved;
//    }

    private void refreshInfo() {
        view.setDeviceInfo(Build.PRODUCT + " " + Build.DEVICE);

        mTimeDate = uB.formatDateTime("yyyy-MM-dd-HHmmssZ", new Date());
        final boolean device_rooted = checkIfSu();

        view.setTimestamp(mTimeDate);
        view.setTextSize(getResources().getDimension(R.dimen.terminal_data_font));

        if (device_rooted) {
            view.setRootStatus(getString(R.string.root_status_ok));
        } else {
            view.setRootStatus(getString(R.string.root_status_not_ok));
        }

        executeCommands();
    }

    private void executeCommands() {
        final CharSequence[] cb_items = getResources().getTextArray(R.array.shell_commands);
        final Hashtable<CharSequence, Boolean> action_table = new Hashtable<>();

        boolean[] cb_item_state = new boolean[cb_items.length];

        Arrays.sort(cb_items, 0, cb_items.length);
        for (int i = 0; i < cb_items.length; i++) {
            cb_item_state[i] = true;
            action_table.put(cb_items[i], true);
        }

        threadBundle = new Bundle();
        threadBundle.putSerializable("actions", action_table);
        lockScreenRotation();
        showDialog(DIALOG_EXECUTING);
    }

//	private void showSelectionDialogue(){
//		final CharSequence[] cb_items = getResources().getTextArray(R.array.shell_commands);
//		final Hashtable<CharSequence, Boolean> action_table = new Hashtable<CharSequence, Boolean>();
//
//		boolean[] cb_item_state = new boolean[cb_items.length];
//
//		Arrays.sort(cb_items, 0, cb_items.length);
//		for (int i=0;i < cb_items.length; i ++ ){
//			cb_item_state[i] = true;
//			action_table.put(cb_items[i], cb_item_state[i]);
//		}
//
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//		builder.setTitle(getString(R.string.dialogue_title_select_commands));
//		builder.setMultiChoiceItems(cb_items, cb_item_state, new DialogInterface.OnMultiChoiceClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
//				/* Used has clicked a check box */
//
//				action_table.put(cb_items[which], isChecked);
//
//			}}).setPositiveButton(getString(R.string.ok), new
//					DialogInterface.OnClickListener() {
//				@SuppressWarnings("deprecation")
//				public void onClick(DialogInterface dialog, int	whichButton) { /* User clicked Yes so do some stuff */
//					threadBundle = new Bundle();
//					threadBundle.putSerializable("actions", action_table);
//					mLockScreenRotation();
//					showDialog(DIALOG_EXECUTING);				
//				}
//			})
//			.setNegativeButton(getString(R.string.cancel), new
//					DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int	whichButton) { /* User clicked No so do some stuff */
//				}
//			}) ;
//		AlertDialog alert = builder.create();
//		alert.show();
//	}
}
