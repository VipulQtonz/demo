package com.example.programingdemo.asyncTask

import android.content.Context
import android.os.AsyncTask
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.example.programingdemo.R
import java.lang.ref.WeakReference

class MyAsyncTask(
    context: Context,
    private val imgProfilePicture: ImageView,
    private val tvWait: TextView,
    private val pbMain: ProgressBar,
    private val tvPositionError: TextView,
    private val tvDeveloperNameError: TextView
) : AsyncTask<Void, Void, Long>() {

    private val contextRef = WeakReference(context)

    @Deprecated("Deprecated in Java")
    override fun onPreExecute() {
        super.onPreExecute()
        imgProfilePicture.visibility = View.GONE
        tvWait.visibility = View.VISIBLE
        pbMain.visibility = View.VISIBLE
        tvPositionError.text = contextRef.get()?.getString(R.string.space)
        tvDeveloperNameError.text = contextRef.get()?.getString(R.string.space)
    }

    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: Void?): Long? {
        Thread.sleep(3000)
        return 0
    }

    @Deprecated("Deprecated in Java")
    override fun onPostExecute(result: Long?) {
        super.onPostExecute(result)
        contextRef.get()?.let { context ->
            imgProfilePicture.setImageResource(R.drawable.user_profile)
            tvDeveloperNameError.text = context.getString(R.string.developer_name_)
            tvPositionError.text = context.getString(R.string.developer_position)
            imgProfilePicture.visibility = View.VISIBLE
            tvWait.visibility = View.GONE
            pbMain.visibility = View.GONE
        }
    }
}
