package com.przemas.monitoring.threading

import android.os.AsyncTask

class MyAsyncTask<Result>(
    private val action: () -> Result,
    private val postAction: (Result) -> Unit
) : AsyncTask<Unit, Unit, Result>() {
    public override fun doInBackground(vararg params: Unit): Result {
        return action.invoke()
    }

    public override fun onPostExecute(result: Result) {
        postAction.invoke(result)
    }
}