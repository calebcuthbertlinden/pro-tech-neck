package com.example.protechneck.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SensorRestartBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(SensorRestartBroadcastReceiver.class.getSimpleName(), "Service Stopped");
        context.startService(new Intent(context, NeckCheckerService.class));
    }
}