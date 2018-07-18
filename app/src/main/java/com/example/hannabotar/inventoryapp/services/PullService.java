package com.example.hannabotar.inventoryapp.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;

import com.example.hannabotar.inventoryapp.InventoryActivity;
import com.example.hannabotar.inventoryapp.util.AppUtil;

public class PullService extends JobService {
    private static final String TAG = "PullService";

    @Override
    public boolean onStartJob(JobParameters params) {

        System.out.println("Start job");

        startService(new Intent(this, GetItemsService.class));

        AppUtil.scheduleJob(getApplicationContext()); // reschedule the job

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}
