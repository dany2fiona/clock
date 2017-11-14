package com.dany.circleringview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    CircleRingView crv1;
    CircleRingView crv2;
    CircleRingView crv3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        crv1 = (CircleRingView) findViewById(R.id.crv_1);
        crv2 = (CircleRingView) findViewById(R.id.crv_2);
        crv3 = (CircleRingView) findViewById(R.id.crv_3);
        crv1.setProgress(88);
        crv2.setProgress(12);
        crv3.setProgress(50);
    }
}
