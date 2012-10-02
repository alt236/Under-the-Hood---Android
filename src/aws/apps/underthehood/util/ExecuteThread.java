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
package aws.apps.underthehood.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import aws.apps.underthehood.R;

public class ExecuteThread extends Thread {
	public final static String MSG_IPCONFIG = "msg_ipconfig";
	public final static String MSG_IP_ROUTE = "msg_ip_route";
	public final static String MSG_PROC = "msg_proc";
	public final static String MSG_OTHER = "msg_other";
	public final static String MSG_PS = "msg_ps";
	public final static String MSG_NETSTAT = "msg_netstat";
	public final static String MSG_SYSPROP = "msg_sysprop";
	public final static int STATE_DONE = 0;
	public final static int STATE_RUNNING = 1;

	public final static int WORK_COMPLETED = 50;
	public final static int WORK_INTERUPTED = 51;
	
	private final String TAG =  this.getClass().getName();
	private Handler mHandler;
	private Context c;
	private int mState;
	private boolean isRooted;
	private Hashtable<CharSequence, Boolean> actions;

	@SuppressWarnings("unchecked")
	public ExecuteThread(Handler h, Context ctx, Bundle b) {//Hashtable<CharSequence, Boolean> action_list) {
		//super();
		this.mHandler = h;
		this.c = ctx;
		this.actions = (Hashtable<CharSequence, Boolean>) b.get("actions");
		this.isRooted = checkIfSu();
	}

	public void run() {
		mState = STATE_RUNNING;
		Bundle b = new Bundle();
		Message msg = new Message();

		Log.d(TAG, "^ ExecuteThread: Thread Started");

		while (mState == STATE_RUNNING) {
			try {
				Thread.sleep(100);
				b.clear();
				
				b.putStringArrayList(MSG_IPCONFIG, executeIpconfig(actions));
				b.putStringArrayList(MSG_IP_ROUTE, executeIpRoute(actions));
				b.putStringArrayList(MSG_PROC, executeProc(actions));
				b.putStringArrayList(MSG_OTHER, executeOther(actions));
				b.putStringArrayList(MSG_PS, executePs(actions));
				b.putStringArrayList(MSG_NETSTAT, executeNetstat(actions));
				b.putStringArrayList(MSG_SYSPROP, executeGetProp(actions));
				
				msg = new Message();
				msg.what = WORK_COMPLETED;
				msg.setData(b);
				mHandler.sendMessage(msg);

				this.setState(STATE_DONE);
			} catch (InterruptedException e) {
				Log.e(TAG, "^ ExecuteThread: Thread Interrupted");
				b.clear();
				int what = WORK_INTERUPTED;
				msg.what = what;
				msg.setData(b);
				mHandler.sendMessage(msg);
				this.setState(STATE_DONE);
			} catch (Exception e){
				Log.e(TAG, "^ ExecuteThread: exception " + e.getMessage());
				msg = new Message();
				b.clear();
				int what = WORK_INTERUPTED;
				msg.what = what;
				msg.setData(b);
				mHandler.sendMessage(msg);
				this.setState(STATE_DONE);
			}
		}
		Log.d(TAG, "^ ProgressThread: Thread Exited");
	}

	/*
	 * sets the current state for the thread, used to stop the thread
	 */
	public void setState(int state) {
		mState = state;
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////	
	/** It will execute a command without SU privileges */
	private String executeCommandAsUser(String cmd){
		String res = "";
		ExecTerminal et = new ExecTerminal();
		res = et.exec(cmd);

		if (res.trim().equals("")){
			res = "result was blank";
		}
		return res;
	}

	/** It will execute a command as a normal user */
	private String executeCommandAsSu(String cmd){
		String res = "";
		ExecTerminal et = new ExecTerminal();
		res = et.execSu(cmd);

		if (res.trim().equals("")){
			res = "result was blank";
		}
		return res;
	}

	private ArrayList<String> executeIpconfig(Hashtable<CharSequence, Boolean> action_list){
		ArrayList<String> l = new ArrayList<String>();

		l.add(getPromptSymbol(isRooted) +  c.getString(R.string.shell_netcfg));
		l.add(execute(R.string.shell_netcfg, action_list, isRooted));
		
		l.add(getPromptSymbol(isRooted) +  c.getString(R.string.shell_ifconfig_dash_a));
		l.add(execute(R.string.shell_ifconfig_dash_a, action_list, isRooted));

		return l;
	}

	private ArrayList<String> executeIpRoute(Hashtable<CharSequence, Boolean> action_list){
		ArrayList<String> l = new ArrayList<String>();

		l.add(c.getString(R.string.seperator_identifier) + "IPv4");
		l.add(getPromptSymbol(isRooted) +  c.getString(R.string.shell_ip_addr));
		l.add(execute(R.string.shell_ip_addr, action_list, isRooted));
		
		l.add(getPromptSymbol(isRooted) +  c.getString(R.string.shell_ip_route_show));
		l.add(execute(R.string.shell_ip_route_show, action_list, isRooted));
		
		l.add(getPromptSymbol(isRooted) +  c.getString(R.string.shell_route_n));
		l.add(execute(R.string.shell_route_n, action_list, isRooted));
		
		l.add(c.getString(R.string.seperator_identifier) + "IPv6");
		
		l.add(getPromptSymbol(isRooted) +  c.getString(R.string.shell_ip_inet6_addr));
		l.add(execute(R.string.shell_ip_inet6_addr, action_list, isRooted));
		
		l.add(getPromptSymbol(isRooted) +  c.getString(R.string.shell_ip_inet6_route_show));
		l.add(execute(R.string.shell_ip_inet6_route_show, action_list, isRooted));
		
		l.add(getPromptSymbol(isRooted) +  c.getString(R.string.shell_route_inet6));
		l.add(execute(R.string.shell_route_inet6, action_list, isRooted));
		
		return l;

	}

	private ArrayList<String> executeProc(Hashtable<CharSequence, Boolean> action_list){		
		ArrayList<String> l = new ArrayList<String>();
    	
		l.add(c.getString(R.string.seperator_identifier) + "General Info");
		
		l.add(getPromptSymbol(isRooted) +  c.getString(R.string.shell_cat_proc_version));
		l.add(execute(R.string.shell_cat_proc_version, action_list, isRooted));
		
		l.add(getPromptSymbol(isRooted) +  c.getString(R.string.shell_cat_proc_meminfo));
		l.add(execute(R.string.shell_cat_proc_meminfo, action_list, isRooted));
		
		l.add(c.getString(R.string.seperator_identifier) + "Hardware Info");
		
		l.add(getPromptSymbol(isRooted) +  c.getString(R.string.shell_cat_proc_devices));
		l.add(execute(R.string.shell_cat_proc_devices, action_list, isRooted));
		
		l.add(getPromptSymbol(isRooted) +  c.getString(R.string.shell_cat_proc_mounts));
		l.add(execute(R.string.shell_cat_proc_mounts, action_list, isRooted));
		
		l.add(c.getString(R.string.seperator_identifier) + "Network Info");
		
		l.add(getPromptSymbol(isRooted) +  c.getString(R.string.shell_cat_proc_net_arp));
		l.add(execute(R.string.shell_cat_proc_net_arp, action_list, isRooted));
		
		l.add(getPromptSymbol(isRooted) +  c.getString(R.string.shell_cat_proc_net_route));
		l.add(execute(R.string.shell_cat_proc_net_route, action_list, isRooted));

		l.add(getPromptSymbol(isRooted) +  c.getString(R.string.shell_cat_proc_net_wireless));
		l.add(execute(R.string.shell_cat_proc_net_wireless, action_list, isRooted));
		
		l.add(getPromptSymbol(isRooted) +  c.getString(R.string.shell_cat_proc_net_if_inet6));
		l.add(execute(R.string.shell_cat_proc_net_if_inet6, action_list, isRooted));
		
		l.add(getPromptSymbol(isRooted) +  c.getString(R.string.shell_cat_proc_net_ipv6_route));
		l.add(execute(R.string.shell_cat_proc_net_ipv6_route, action_list, isRooted));
	
		return l;
	}

	private  ArrayList<String>  executeGetProp(Hashtable<CharSequence, Boolean> action_list){
		ArrayList<String> l = new ArrayList<String>();

	    File file = new File("/system/build.prop");
	    StringBuilder fileContents = new StringBuilder((int)file.length());
	    Scanner scanner;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			l.add(getPromptSymbol(isRooted) +  c.getString(R.string.shell_getprop_product_version));
			l.add(execute(R.string.shell_getprop_product_version, action_list, false));
			
			l.add(getPromptSymbol(isRooted) +  c.getString(R.string.shell_getprop_build_fingerprint));
			l.add(execute(R.string.shell_getprop_build_fingerprint, action_list, false));
			
			l.add(getPromptSymbol(isRooted) +  c.getString(R.string.shell_getprop_gsm_baseband));
			l.add(execute(R.string.shell_getprop_gsm_baseband, action_list, false));
	
			l.add(getPromptSymbol(isRooted) +  c.getString(R.string.shell_getprop_vm_execution_mode));
			l.add(execute(R.string.shell_getprop_vm_execution_mode, action_list, false));
			
			l.add(getPromptSymbol(isRooted) +  c.getString(R.string.shell_getprop_vm_heapsize));
			l.add(execute(R.string.shell_getprop_vm_heapsize, action_list, false));
			
			l.add(getPromptSymbol(isRooted) +  c.getString(R.string.shell_getprop_lcd_density));
			l.add(execute(R.string.shell_getprop_lcd_density, action_list, false));
			
			return l;
		}
		
	    try {
	    	String line;
	    	String lineArray[];
	    	String tmp;
	        while(scanner.hasNextLine()) {
	        	line = scanner.nextLine();
	        	
	        	if(!line.startsWith("#")){
	        		lineArray = line.split("=");
	        		
	        		if(lineArray.length >= 2){
	        			tmp = "";
	        			l.add("> " +  lineArray[0]);
	        			for(int i = 1; i < lineArray.length; i ++){
	        				if(tmp.length()>0){
	        					tmp += "=" + lineArray[i];
	        				}else{
	        					tmp = lineArray[i];
	        				}
	        				
	        				l.add(tmp.trim());
	        			}
	        		}
	        	}
	        }
	    } finally {
	        scanner.close();
	    }
		  
	    return l;
	}
	
	private  ArrayList<String>  executeOther(Hashtable<CharSequence, Boolean> action_list){
		ArrayList<String> l = new ArrayList<String>();

		l.add(c.getString(R.string.seperator_identifier) + "System info" +
				"");
		
		l.add(getPromptSymbol(isRooted) +  c.getString(R.string.shell_df_ah));
		l.add(execute(R.string.shell_df_ah, action_list, isRooted));
		
		l.add(getPromptSymbol(isRooted) +  c.getString(R.string.shell_uname_a));
		l.add(execute(R.string.shell_uname_a, action_list, isRooted));

		l.add(getPromptSymbol(isRooted) +  c.getString(R.string.shell_lsmod));
		l.add(execute(R.string.shell_lsmod, action_list, isRooted));
		
		//l.add(c.getString(R.string.seperator_identifier) + "build.prop");
				
		return l;
	}

	private ArrayList<String> executePs(Hashtable<CharSequence, Boolean> action_list){
		ArrayList<String> l = new ArrayList<String>();

		l.add(getPromptSymbol(isRooted) +  c.getString(R.string.shell_ps));
		l.add(execute(R.string.shell_ps, action_list, isRooted));
		
		return l;
	}

	private ArrayList<String> executeNetstat(Hashtable<CharSequence, Boolean> action_list){
		ArrayList<String> l = new ArrayList<String>();

		l.add(getPromptSymbol(isRooted) +  c.getString(R.string.shell_netstat_r));
		l.add(execute(R.string.shell_netstat_r, action_list, isRooted));
		
		l.add(getPromptSymbol(isRooted) +  c.getString(R.string.shell_netstat_a));
		l.add(execute(R.string.shell_netstat_a, action_list, isRooted));

		return l;
	}

	private String execute(int commandString, Hashtable<CharSequence, Boolean> action_list, boolean isRooted){
		String result = "";
		String cmd = c.getString(commandString);

		if(action_list.get(cmd)){

			if(isRooted){
				result = executeCommandAsSu(cmd);
			} else {
				result = executeCommandAsUser(cmd);
			}
		} else {
			result = c.getString(R.string.text_execution_skipped_by_user);
		}	

		return result.trim();
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
	private String getPromptSymbol(boolean root){
		if(root){
			return "#";
		}else{
			return "$";
		}
	}
}
