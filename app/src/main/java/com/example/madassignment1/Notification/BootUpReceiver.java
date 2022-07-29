package com.example.madassignment1.Notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.madassignment1.PageSelection;

public class BootUpReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, PageSelection.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);


    }
}
