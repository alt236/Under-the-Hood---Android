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

import androidx.annotation.NonNull;

public class ExecTerminal {
    final String TAG = this.getClass().getSimpleName();

    @NonNull
    public TerminalOutput exec(@NonNull String cmd) {
        return execute(false, cmd);
    }

    @NonNull
    public TerminalOutput execSu(@NonNull String cmd) {
        return execute(true, cmd);
    }


    private TerminalOutput execute(boolean asRoot,
                                   @NonNull String command) {
        Log.d(TAG, "Executing (root=" + asRoot + ") '" + command + "'");

        try {
            final Process process = createProcess(asRoot);

            final DataInputStream stdOutStream = new DataInputStream(process.getInputStream());
            final DataInputStream stdErrStream = new DataInputStream(process.getErrorStream());
            final BufferedReader stdOutReader = new BufferedReader(new InputStreamReader(stdOutStream));
            final BufferedReader stdErrReader = new BufferedReader(new InputStreamReader(stdErrStream));

            final DataOutputStream outputStream = new DataOutputStream(process.getOutputStream());
            outputStream.writeBytes(command + "\n");
            outputStream.writeBytes("exit\n");
            outputStream.flush();
            outputStream.close();

            final String stdOut = readFromReader(stdOutReader);
            final String stdErr = readFromReader(stdErrReader);
            process.waitFor();
            stdOutReader.close();

            return new TerminalOutput(
                    asRoot,
                    command,
                    process.exitValue(),
                    stdOut,
                    stdErr);
        } catch (IOException | InterruptedException e) {
            Log.e(TAG, "Executing (root=" + asRoot + ") Exception: " + e);
            e.printStackTrace();

            return new TerminalOutput(asRoot, command, -1, "", e.getMessage());
        }
    }


    private Process createProcess(boolean asRoot) throws IOException {
        if (asRoot) {
            return Runtime.getRuntime().exec("su");
        } else {
            return Runtime.getRuntime().exec("sh");
        }
    }

    private String readFromReader(BufferedReader reader) {
        StringBuilder fullOutput = new StringBuilder();
        try {
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

