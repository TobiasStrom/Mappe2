package com.tobiasstrom.s331392mappe2comtobiasstrom.activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tobiasstrom.s331392mappe2comtobiasstrom.services.CycleService;


public class OnBootCompletedReceiver extends BroadcastReceiver {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        //Dersom broadcast receiver kjøres av moblien som slåss på, skal servisen slås på
        Intent intent1 = new Intent(context, CycleService.class);
        context.startService(intent1);
    }
}
