/*******************************************************************************
 * Copyright 2010 Alexandros Schillings
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.co.alt236.underthehood.commandrunner

import android.content.res.Resources
import android.os.Build
import android.util.Log
import uk.co.alt236.underthehood.commandrunner.groups.*
import uk.co.alt236.underthehood.commandrunner.model.Result
import java.util.concurrent.Callable

internal class ExecuteCallable(res: Resources) : Callable<Result> {
    private val commandRunner = Cli()
    private val ipConfigCommands = IpConfigCommands(res)
    private val ipRouteCommands = IpRouteCommands(res)
    private val netstatCommands = NetstatCommands(res)
    private val otherCommands = OtherCommands(res)
    private val procCommands = ProcCommands(res)
    private val psCommands = PsCommands(res)
    private val sysPropCommands = SysPropCommands(res)

    private var isRooted = false

    override fun call(): Result {
        Log.d(TAG, "^ ExecuteThread: Thread Started")
        isRooted = commandRunner.checkIfSu()

        return try {
            return collectResult(isRooted)
        } catch (e: Exception) {
            Log.e(TAG, "^ ExecuteThread: exception " + e.message)
            Result.ERROR
        }
    }

    private fun collectResult(isRooted: Boolean): Result {
        val result = Result()

        result.isRooted = isRooted
        result.timestamp = System.currentTimeMillis()
        result.deviceinfo = Build.PRODUCT + " " + Build.DEVICE

        result.ipConfigData = (ipConfigCommands.execute(isRooted))
        result.ipRouteData = (ipRouteCommands.execute(isRooted))
        result.netstatData = (netstatCommands.execute(isRooted))
        result.otherData = (otherCommands.execute(isRooted))
        result.procData = (procCommands.execute(isRooted))
        result.psData = (psCommands.execute(isRooted))
        result.sysPropData = (sysPropCommands.execute(isRooted))

        Log.d(TAG, "^ ExecuteThread: Returning result")

        return result
    }

    companion object {
        private val TAG = this::class.java.simpleName
    }
}