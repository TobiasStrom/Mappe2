package com.tobiasstrom.s331392mappe2comtobiasstrom.ui.settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.tobiasstrom.s331392mappe2comtobiasstrom.R;
import com.tobiasstrom.s331392mappe2comtobiasstrom.activities.NewContact;

import static android.content.ContentValues.TAG;

public class SettingsFragment extends Fragment {
    EditText customSmsMessage;
    SwitchCompat smsServiceSwitch;
    Button setSmsTime;

    boolean smsServiceIsOn;
    String smsMessage;
    int smsSendTimeHour;
    int smsSendTimeMinutes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_settings, container, false);
        customSmsMessage = root.findViewById(R.id.customSmsMessage);
        smsServiceSwitch = root.findViewById(R.id.smsService);
        setSmsTime = root.findViewById(R.id.SmsTimeButton);

        //hente verdier fra sharedpreferences, dersom de fantes ikke, skal deault verdier plasseres
        smsServiceIsOn = this.getActivity().getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE).getBoolean("smsServiceIsOn", true);
        smsMessage = this.getActivity().getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE).getString("smsMessage", "Du er invitert ti et møte");
        smsSendTimeHour = this.getActivity().getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE).getInt("smsSendTimeHour", 8);
        smsSendTimeMinutes = this.getActivity().getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE).getInt("smsSendTimeMinutes", 0);

        //justere elementer på grensesnittet ut av verdier hentet fra sharedpreferences
        customSmsMessage.setText(smsMessage);
        smsServiceSwitch.setChecked(smsServiceIsOn);
        setSmsTime.setEnabled(smsServiceIsOn); //elementet justeres ut av parent setting
        customSmsMessage.setEnabled(smsServiceIsOn); //elementet justeres ut av parent setting

        smsServiceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                smsServiceIsOn = b; //oppdatere boolean som skal bli lagret til shared preferences
                customSmsMessage.setEnabled(b); //elementet justeres ut av parent setting
                setSmsTime.setEnabled(b); //elementet justeres ut av parent setting
            }
        });

        setSmsTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener timePicker = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        smsSendTimeHour = i; //oppdatere ints som skal bli lagret til share preferences
                        smsSendTimeMinutes = i1;
                    }
                };
                new TimePickerDialog(getActivity(), timePicker, smsSendTimeHour, smsSendTimeMinutes,true).show();
            }
        });
        return root;
    }

    @Nullable
    private ActionBar getSupportActionBar() {
        ActionBar actionBar = null;
        if (getActivity() instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            actionBar = activity.getSupportActionBar();
        }
        return actionBar;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        //lagre verdier til sharedpreferences
        this.getActivity().getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE).edit().putBoolean("smsServiceIsOn", smsServiceIsOn).apply();
        this.getActivity().getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE).edit().putString("smsMessage", customSmsMessage.getText().toString()).apply();
        this.getActivity().getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE).edit().putInt("smsSendTimeHour", smsSendTimeHour).apply();
        this.getActivity().getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE).edit().putInt("smsSendTimeMinutes", smsSendTimeMinutes).apply();
    }



}

