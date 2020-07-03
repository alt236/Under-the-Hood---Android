package uk.co.alt236.underthehood.commandrunner

import android.content.res.Resources
import uk.co.alt236.underthehood.commandrunner.model.Result
import java.util.concurrent.Callable
import java.util.concurrent.Executors

class CommandRunner(private val resources: Resources) {
    private val executor = Executors.newSingleThreadExecutor()

    fun runCommands(callback: Callback<Result>) {
        val callable: Callable<Result> = ExecuteCallable(resources)
        executor.submit(ListenableCallable(callable, callback))
    }

    interface Callback<T> {
        fun onCommandsCompleted(result: T)
    }

    private class ListenableCallable<T>(private val callable: Callable<T>,
                                        private val callback: Callback<T>) : Callable<T> {
        override fun call(): T {
            val result = callable.call()
            callback.onCommandsCompleted(result)

            return result
        }

    }
}