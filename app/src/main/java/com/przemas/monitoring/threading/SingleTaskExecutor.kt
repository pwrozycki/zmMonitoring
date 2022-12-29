package com.przemas.monitoring.threading

import java.util.concurrent.locks.ReentrantLock

class SingleTaskExecutor {
    private var task: MyAsyncTask<*>? = null
    private val taskLock = ReentrantLock()

    fun executeAndCancelPrevious(newTask: MyAsyncTask<*>) {
        synchronized(taskLock) {
            task?.cancel(false)

            task = newTask.also {
                it.execute()
            }
        }
    }

    fun cancel() {
        synchronized(taskLock) {
            task?.let {
                it.cancel(false)
                task = null
            }
        }
    }
}