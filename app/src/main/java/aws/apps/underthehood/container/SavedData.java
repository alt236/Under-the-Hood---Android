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
package aws.apps.underthehood.container;

import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SavedData {
    final String TAG = this.getClass().getName();

    private List<String> tIp = new ArrayList<>();
    private List<String> tIpConfig = new ArrayList<>();
    private List<String> tRoute = new ArrayList<>();
    private List<String> tNetlist = new ArrayList<>();
    private List<String> tPs = new ArrayList<>();
    private List<String> tOther = new ArrayList<>();
    private List<String> tSysProp = new ArrayList<>();

    private String dateTime = "";
    private boolean areWeRooted = false;
    private int textSize;

    public boolean getAreWeRooted() {
        return areWeRooted;
    }

    public void setAreWeRooted(boolean areWeRooted) {
        this.areWeRooted = areWeRooted;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public List<String> gettDf() {
        return tOther;
    }

    public List<String> gettSysProp() {
        return tSysProp;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int size) {
        textSize = size;
    }

    public List<String> gettIp() {
        return tIp;
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

    public List<String> gettRoute() {
        return tRoute;
    }

    public void populateIp(TableLayout t) {
        tIp = tableToList(t);
    }

    public void populateIpConfig(TableLayout t) {
        tIpConfig = tableToList(t);
    }

    public void populateNetlist(TableLayout t) {
        tNetlist = tableToList(t);
    }

    public void populateOther(TableLayout t) {
        tOther = tableToList(t);
    }

    public void populatePs(TableLayout t) {
        tPs = tableToList(t);
    }

    public void populateRoute(TableLayout t) {
        tRoute = tableToList(t);
    }

    public void populateSysProp(TableLayout t) {
        tSysProp = tableToList(t);
    }

    private List<String> tableToList(TableLayout t) {
        List<String> l = new ArrayList<>();

        l.clear();

        for (int i = 0; i <= t.getChildCount() - 1; i++) {
            TableRow row = (TableRow) t.getChildAt(i);

            for (int j = 0; j <= row.getChildCount() - 1; j++) {
                View v = row.getChildAt(j);

                try {
                    if (v.getClass() == Class.forName("android.widget.TextView")) {
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
