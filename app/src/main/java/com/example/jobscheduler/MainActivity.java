package com.example.jobscheduler;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;

import static com.example.jobscheduler.SampleJobService.BUNDLE_KEY;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initJobScheduler();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void initJobScheduler()
    {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP)
        {
            ComponentName componentName= new ComponentName(this, SampleJobService.class);
            PersistableBundle bundle= new PersistableBundle();
            bundle.putInt(BUNDLE_KEY, 10);
            JobInfo.Builder builder= new JobInfo.Builder(201, componentName)
                    .setExtras(bundle)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setPersisted(true)
                    .setPeriodic(15*60*1000);

            JobScheduler scheduler= (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            scheduler.schedule(builder.build());
        }
    }
}