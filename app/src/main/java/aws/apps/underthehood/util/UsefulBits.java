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

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import aws.apps.underthehood.R;
import aws.apps.underthehood.ui.MyAlertBox;

public class UsefulBits {
    final String TAG = this.getClass().getName();
    private final Context c;

    public UsefulBits(Context cntx) {
        Log.d(TAG, "^ Object created");
        c = cntx;
    }

    public String getSoftwareInfo(SOFTWARE_INFO info) {
        try {
            PackageInfo pi = c.getPackageManager().getPackageInfo(c.getPackageName(), 0);
            Resources appR = c.getResources();
            CharSequence txt;
            // Store the software version code and version name somewhere..
            switch (info) {
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

    public String formatDateTime(String formatString, Date d) {
        Format formatter = new SimpleDateFormat(formatString, Locale.US);
        return formatter.format(d);
    }

    public void saveToFile(String fileName, File directory, String contents) {

        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            try {

                if (directory.canWrite()) {
                    File gpxfile = new File(directory, fileName);
                    FileWriter gpxwriter = new FileWriter(gpxfile);
                    BufferedWriter out = new BufferedWriter(gpxwriter);
                    out.write(contents);
                    out.close();
                    showToast("Saved to SD as '" + directory.getAbsolutePath() + "/" + fileName + "'", Toast.LENGTH_SHORT);
                }

            } catch (Exception e) {
                showToast("Could not write file:\n+ e.getMessage()", Toast.LENGTH_SHORT);
                Log.e(TAG, "^ Could not write file " + e.getMessage());
            }

        } else {
            showToast("No SD card is mounted...", Toast.LENGTH_SHORT);
            Log.e(TAG, "^ No SD card is mounted.");
        }
    }

    public void showToast(String message, int duration) {
        Toast toast = Toast.makeText(c.getApplicationContext(), message, duration);
        toast.show();
    }

    public String getAboutDialogueText() {
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

        if (c != null) {
            MyAlertBox.create(c, text, title, c.getString(android.R.string.ok)).show();
        } else {
            Log.d(TAG, "^ context is null...");
        }
    }

    public void shareResults(String subject, String text) {
        Intent t = new Intent(Intent.ACTION_SEND);

        t.setType("text/plain");
        t.putExtra(Intent.EXTRA_TEXT, text);
        t.putExtra(Intent.EXTRA_SUBJECT, subject);
        t.addCategory(Intent.CATEGORY_DEFAULT);
        Intent share = Intent.createChooser(t, c.getString(R.string.label_share_dialogue_title));
        c.startActivity(share);
    }

    /**
     * Gets the software version and version name for this application
     */
    public enum SOFTWARE_INFO {
        NAME, VERSION, NOTES, CHANGELOG, COPYRIGHT, ACKNOWLEDGEMENTS
    }
}
