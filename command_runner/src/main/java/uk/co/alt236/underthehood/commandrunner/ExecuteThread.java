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
package uk.co.alt236.underthehood.commandrunner;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.Hashtable;

import uk.co.alt236.underthehood.commandrunner.core.ExecTerminal;
import uk.co.alt236.underthehood.commandrunner.groups.GetPropCommands;
import uk.co.alt236.underthehood.commandrunner.groups.IpConfigCommands;
import uk.co.alt236.underthehood.commandrunner.groups.IpRouteCommands;
import uk.co.alt236.underthehood.commandrunner.groups.NetstatCommands;
import uk.co.alt236.underthehood.commandrunner.groups.OtherCommands;
import uk.co.alt236.underthehood.commandrunner.groups.ProcCommands;
import uk.co.alt236.underthehood.commandrunner.groups.PsCommands;


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

    private final CommandRunner commandRunner = new CommandRunner();
    private final String TAG = this.getClass().getName();
    private final Handler mHandler;
    private final Resources res;
    private final Hashtable<CharSequence, Boolean> actions;
    private final CommandGroup procCommands;
    private final CommandGroup getPropCommands;
    private final CommandGroup otherCommands;
    private final CommandGroup ipRouteCommands;
    private final CommandGroup netstatCommands;
    private final CommandGroup ipConfigCommands;
    private final CommandGroup psCommands;
    private int mState;
    private boolean isRooted;

    @SuppressWarnings("unchecked")
    public ExecuteThread(Handler h, Resources res, Bundle b) {
        //super();
        this.mHandler = h;
        this.res = res;

        actions = (Hashtable<CharSequence, Boolean>) b.get("actions");
        procCommands = new ProcCommands(res);
        getPropCommands = new GetPropCommands(res);
        otherCommands = new OtherCommands(res);
        ipRouteCommands = new IpRouteCommands(res);
        netstatCommands = new NetstatCommands(res);
        ipConfigCommands = new IpConfigCommands(res);
        psCommands = new PsCommands(res);
    }

    public void run() {
        Log.d(TAG, "^ ExecuteThread: Thread Started");
        mState = STATE_RUNNING;
        isRooted = checkIfSu();
        final Bundle b = new Bundle();

        Message msg;
        while (mState == STATE_RUNNING) {
            try {
                Thread.sleep(100);
                b.clear();

                b.putStringArrayList(MSG_IPCONFIG, ipConfigCommands.execute(actions, isRooted));
                b.putStringArrayList(MSG_IP_ROUTE, ipRouteCommands.execute(actions, isRooted));
                b.putStringArrayList(MSG_PROC, procCommands.execute(actions, isRooted));
                b.putStringArrayList(MSG_OTHER, otherCommands.execute(actions, isRooted));
                b.putStringArrayList(MSG_PS, psCommands.execute(actions, isRooted));
                b.putStringArrayList(MSG_NETSTAT, netstatCommands.execute(actions, isRooted));
                b.putStringArrayList(MSG_SYSPROP, getPropCommands.execute(actions, isRooted));

                msg = new Message();
                msg.what = WORK_COMPLETED;
                msg.setData(b);
                mHandler.sendMessage(msg);

                this.setState(STATE_DONE);
            } catch (InterruptedException e) {
                Log.e(TAG, "^ ExecuteThread: Thread Interrupted");
                msg = new Message();
                b.clear();
                msg.what = WORK_INTERUPTED;
                msg.setData(b);
                mHandler.sendMessage(msg);
                this.setState(STATE_DONE);
            } catch (Exception e) {
                Log.e(TAG, "^ ExecuteThread: exception " + e.getMessage());
                msg = new Message();
                b.clear();
                msg.what = WORK_INTERUPTED;
                msg.setData(b);
                mHandler.sendMessage(msg);
                this.setState(STATE_DONE);
            }
        }
        Log.d(TAG, "^ ProgressThread: Thread Exited");
    }

    public void setState(int state) {
        mState = state;
    }

    private boolean checkIfSu() {
        final ExecTerminal et = new ExecTerminal();
        final String res = et.execSu("su && echo 1");

        if (res.trim().equals("1")) {
            Log.d(TAG, "^ Device can do SU");
            return true;
        } else {
            Log.d(TAG, "^ Device can't do SU");
            return false;
        }
    }
}
