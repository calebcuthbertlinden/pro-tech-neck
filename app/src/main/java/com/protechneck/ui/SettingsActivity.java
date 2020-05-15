package com.protechneck.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import protechneck.R;
import com.protechneck.services.NeckCheckerService;
import com.protechneck.util.PreferencesHelper;
import com.protechneck.models.Strictness;
import com.google.android.material.button.MaterialButtonToggleGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {

    Intent mServiceIntent;

    private String strictnessChosen;

    @BindView(R.id.strictness) MaterialButtonToggleGroup strictness;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings2);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        strictness.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            switch(checkedId) {
                case R.id.strict:
                    if (isChecked)
                        strictnessChosen = Strictness.STRICT.name();
                    break;
                case R.id.not_so_strict:
                    if (isChecked)
                        strictnessChosen = Strictness.NOT_SO_STRICT.name();
                    break;
                case R.id.leniant:
                    if (isChecked)
                        strictnessChosen = Strictness.LENIANT.name();
                    break;
            }
        });
    }

    protected void onResume() {
        super.onResume();
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_continue)
    public void onContinueClick(View view) {
        // TODO this code will sit here until next ticket implemented

        PreferencesHelper.getInstance(getApplicationContext()).setAppServiceRunningPreference(false);
        PreferencesHelper.getInstance(getApplicationContext()).setIsReturningUserPreference();
        PreferencesHelper.getInstance(getApplicationContext()).setAppStrictnessPreference(strictnessChosen);
        // close app and start service
        NeckCheckerService mSensorService = new NeckCheckerService();
        mServiceIntent = new Intent(this, mSensorService.getClass());
        this.startService(mServiceIntent);
        finish();
    }
}
