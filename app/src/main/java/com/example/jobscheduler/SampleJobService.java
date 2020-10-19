package com.example.jobscheduler;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.RequiresApi;

/**
 *  Job scheduler is modern way to handle the background tasks which was introduced in
 *  API 21(LOLLIPOP) so we need a system with an api = or above 21.
 */


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SampleJobService extends JobService
{
    public static final String BUNDLE_KEY= "Number";
    private DownloadAsyncTask asyncTask;
    public static final String TAG="Tag";
    private JobParameters parameters;
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        /**
         * All the background tasks are done in this function.
         * We are using a persistable bundle in place of bundle.
         * The difference between bundle and persistable bundle is that it bundle can be null.
         * All the remaining operations on bundle liking adding and retrieving data is same and are done
         * from same functions.
         *
         *
         * We still require a AsyncTask as we don't want to block the user from interacting from the
         * user interface
         */
        Log.d(TAG, "onStartJob");
        parameters= jobParameters;
        PersistableBundle bundle= jobParameters.getExtras();
        int number= bundle.getInt(BUNDLE_KEY, -1);
        asyncTask= new DownloadAsyncTask();
        if(-1!= number)
            asyncTask.execute(number);
        return true;
        /**
         * returned value true means:
         *      We want to reschedule our task as it may not have been handled properly or some
         *      other reason.
         *  false means:
         *      Background task has been handled properly and we don't want to reschedule it.
         */
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        /**
         * This function is called when android system kills our background task it may be due
         * to many reasons one of them is that we needed some requirements(Internet) for our background task
         * but right now the requirement is not met so android system kills it.
         *
         * This function is also used to release all the resources that was taken by our
         * background task.
         */

        if(asyncTask!= null)
        {
            if(!asyncTask.isCancelled())
                asyncTask.cancel(true);
        }

        Log.d(TAG, "onStopJob");
        return true;
        /**
         * we are returning true as we want to reschedule our background task as soon as the
         * requirements has been met.
         */
    }



    private class DownloadAsyncTask extends AsyncTask<Integer, Integer, String>
    {
        @Override
        protected String doInBackground(Integer... integers) {
            Log.d(TAG, "On background started for "+integers[0]);
            for(int i=0;i<integers[0];i++)
            {
                publishProgress(i);
                SystemClock.sleep(1000);
            }
            return "Job Finished";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d(TAG, "do in background "+values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "on Post execute "+s);

            jobFinished(parameters, true);
        }
    }
}
