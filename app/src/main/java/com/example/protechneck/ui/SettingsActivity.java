package com.example.protechneck.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.protechneck.R;
import com.example.protechneck.services.NeckCheckerService;
import com.example.protechneck.util.PreferencesHelper;
import com.example.protechneck.util.SensorUtil;
import com.example.protechneck.models.Strictness;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {

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
        if (!SensorUtil.getInstance(this).isMyServiceRunning(mSensorService.getClass())) {
            startService(mServiceIntent);
        }
        PreferencesHelper.getInstance(getApplicationContext()).setAppServiceRunningPreference(false);
        // close app and start service
        this.startService(mServiceIntent);
        PreferencesHelper.getInstance(getApplicationContext()).setIsReturningUserPreference();
        PreferencesHelper.getInstance(getApplicationContext()).setAppStrictnessPreference(strictnessChosen);
        finish();
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.strict:
                if (checked)
                    strictnessChosen = Strictness.STRICT.name();
                    break;
            case R.id.not_so_strict:
                if (checked)
                    strictnessChosen = Strictness.NOT_SO_STRICT.name();
                    break;
            case R.id.leniant:
                if (checked)
                    strictnessChosen = Strictness.LENIANT.name();
                    break;
        }
    }
}
