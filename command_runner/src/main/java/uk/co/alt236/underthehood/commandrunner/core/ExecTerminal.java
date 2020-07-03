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
package uk.co.alt236.underthehood.commandrunner.core;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExecTerminal {
    final String TAG = this.getClass().getSimpleName();

    public String exec(String cmd) {
        Log.d(TAG, "Executing '" + cmd + "'");

        try {
            Process process = Runtime.getRuntime().exec("sh");
            DataInputStream is = new DataInputStream(process.getInputStream());
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            os.close();

            final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            final String result = readFromReader(reader);

            process.waitFor();
            reader.close();

            return result;
        } catch (IOException | InterruptedException e) {
            Log.e(TAG, "exec, Exception: " + e);
            e.printStackTrace();
        }

        return "";
    }

    public String execSu(String cmd) {
        Log.d(TAG, "Executing as SU '" + cmd + "'");
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataInputStream is = new DataInputStream(process.getInputStream());
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            os.close();

            final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            final String result = readFromReader(reader);

            process.waitFor();
            reader.close();

            return result;
        } catch (IOException | InterruptedException e) {
            Log.e(TAG, "execSU, Exception: " + e);
            e.printStackTrace();
        }

        return "";
    }


    private String readFromReader(BufferedReader reader) {
        try {
            StringBuilder fullOutput = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                fullOutput.append(line).append('\n');
            }
            return fullOutput.toString();
        } catch (IOException e) {
            Log.e(TAG, "Failed to read from reader: " + e.getMessage());
            e.printStackTrace();
        }

        return "";
    }
}

