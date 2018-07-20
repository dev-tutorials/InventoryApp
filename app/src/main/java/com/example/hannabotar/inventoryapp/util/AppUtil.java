package com.example.hannabotar.inventoryapp.util;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

import com.example.hannabotar.inventoryapp.services.PullService;

public class AppUtil {
    private AppUtil() {
        throw new ExceptionInInitializerError("Clasa utilitara");
    }

    public static void  scheduleJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, PullService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(1 * 3000); // wait at least
        builder.setOverrideDeadline(5 * 1000); // maximum delay
        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.schedule(builder.build());
    }
}
