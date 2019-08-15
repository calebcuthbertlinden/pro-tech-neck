package com.example.protechneck.Util;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.example.protechneck.Models.PostureAnalyticsEvent;
import com.example.protechneck.Models.PostureEventType;
import com.example.protechneck.R;

public class TechNeckActivity extends AppCompatActivity {

    private TextView postureType;
    private ConstraintLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tech_neck);

        postureType = findViewById(R.id.posture_type);
        container = findViewById(R.id.container);


        PostureEventType viewType = PostureEventType.valueOf(getIntent().getStringExtra("VIEW_TYPE_EXTRA"));

        switch (viewType) {
            case FLAT_PHONE:
                // Bad posture
                AnalyticsUtil.sendAnalyticsEvent(PostureAnalyticsEvent.BAD_POSTURE, null, null);
                Log.e("Posture", "BAD_POSTURE - FLAT PHONE");
                postureType.setText("BAD POSTURE");
                container.setBackgroundColor(ContextCompat.getColor(this, R.color.badBackground));
                break;
            case LOW_ANGLED:
                // Bad posture
                AnalyticsUtil.sendAnalyticsEvent(PostureAnalyticsEvent.BAD_POSTURE, null, null);
                Log.e("Posture", "BAD_POSTURE - LOW ANGLED");
                postureType.setText("OKAY POSTURE");
                container.setBackgroundColor(ContextCompat.getColor(this, R.color.okayBackground));
                break;
            case HIGH_ANGLED:
                // Okay posture
                AnalyticsUtil.sendAnalyticsEvent(PostureAnalyticsEvent.OKAY_POSTURE, null, null);
                Log.e("Posture", "OKAY_POSTURE - HIGH ANGLE");
                postureType.setText("BETTER POSTURE");
                container.setBackgroundColor(ContextCompat.getColor(this, R.color.badBackground));
            case PERFECT_POSTURE:
                // Perfect posture
                AnalyticsUtil.sendAnalyticsEvent(PostureAnalyticsEvent.PERFECT_POSTURE, null, null);
                Log.e("Posture", "BEST_POSTURE - PERFECT POSTURE");
                postureType.setText("PERFECT POSTURE");
                container.setBackgroundColor(ContextCompat.getColor(this, R.color.goodBackground));
                break;
            default:
                break;
        }

    }

    protected void onResume() {
        super.onResume();
    }

}
