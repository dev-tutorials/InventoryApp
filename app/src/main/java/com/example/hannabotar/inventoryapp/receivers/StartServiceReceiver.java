package com.example.hannabotar.inventoryapp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.hannabotar.inventoryapp.util.AppUtil;

public class StartServiceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AppUtil.scheduleJob(context);
    }
}
