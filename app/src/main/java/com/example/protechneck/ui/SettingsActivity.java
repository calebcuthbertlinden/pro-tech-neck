package com.example.protechneck.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.protechneck.R;
import com.example.protechneck.services.NeckCheckerService;
import com.example.protechneck.util.SensorUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.protechneck.MainActivity.PREF_IS_RETURNING_USER;
import static com.example.protechneck.MainActivity.PREF_IS_SERVICE_RUNNING;
import static com.example.protechneck.MainActivity.PREF_KEY;

public class SettingsActivity extends AppCompatActivity {

    public static final String PREF_APP_STRICTNESS = "PREF_APP_STRICTNESS";

    Intent mServiceIntent;

    private String strictnessChosen;

    @BindView(R.id.strictness) RadioGroup strictness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    protected void onResume() {
        super.onResume();
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_continue)
    public void onContinueClick(View view) {
        // TODO this code will sit here until next ticket implemented
        NeckCheckerService mSensorService = new NeckCheckerService();
        mServiceIntent = new Intent(this, mSensorService.getClass());
        if (!SensorUtil.isMyServiceRunning(mSensorService.getClass(), this)) {
            startService(mServiceIntent);
        }
        setAppServiceRunningPreference();
        // close app and start service
        this.startService(mServiceIntent);
        setIsReturningUserPreference(true);
        setAppStrictnessPreference();
        finish();
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.strict:
                if (checked)
                    strictnessChosen = "STRICT";
                    break;
            case R.id.not_so_strict:
                if (checked)
                    strictnessChosen = "NOT_SO_STRICT";
                    break;
            case R.id.leniant:
                if (checked)
                    strictnessChosen = "LENIANT";
                    break;
        }
    }

    private void setAppStrictnessPreference() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(PREF_KEY, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(PREF_APP_STRICTNESS, strictnessChosen);
        editor.apply();
    }

    private void setAppServiceRunningPreference() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(PREF_KEY, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(PREF_IS_SERVICE_RUNNING, false);
        editor.apply();
    }

    private void setIsReturningUserPreference(boolean value) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(PREF_KEY, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(PREF_IS_RETURNING_USER, value);
        editor.apply();
    }

}
