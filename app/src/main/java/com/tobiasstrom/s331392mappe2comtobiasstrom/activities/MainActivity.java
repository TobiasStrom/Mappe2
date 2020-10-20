package com.tobiasstrom.s331392mappe2comtobiasstrom.activities;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.material.navigation.NavigationView;
import com.tobiasstrom.s331392mappe2comtobiasstrom.R;

import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private AppBarConfiguration mAppBarConfiguration;
    private View background_dimmer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        startMeetingNotifyService(); //starter servicen

        final FloatingActionsMenu fab = findViewById(R.id.fab);
        FloatingActionButton fabNewContact = findViewById(R.id.fabNewContact);
        FloatingActionButton fabNewMeeting = findViewById(R.id.fabNewMeting);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        fabNewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), NewContact.class));
                fab.collapse();
            }
        });
        fabNewMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), NewMeeting.class));
                fab.collapse();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                 R.id.nav_meetings,R.id.nav_contacts, R.id.nav_settings)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void startMeetingNotifyService() {
        //denne skal slå på servicen hver gang applikasjonen startes
       //Intent intent = new Intent(this, CycleService.class);
        // this.startService(intent);
        int myPermissionsRequestSendSms = ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

        if (myPermissionsRequestSendSms != 0) { //sjekker om applikasjonen har permission til å sende sms, hvis ikke så skal det spørs om det
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},0);
        }

        Intent intent = new Intent();
        intent.setAction("test");
        sendBroadcast(intent);

    }

}