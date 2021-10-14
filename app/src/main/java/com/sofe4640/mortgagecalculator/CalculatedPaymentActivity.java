package com.sofe4640.mortgagecalculator;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;

public class CalculatedPaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculated_payment);

        Toolbar mainToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
        ActionBar ab = getSupportActionBar();
        // Set and activate the back-to-home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        // Disable default app title
        ab.setDisplayShowTitleEnabled(false);

        // Get the intent from the main activity
        Intent i = getIntent();
        String paymentAmnt = i.getStringExtra("payment");
        String term = i.getStringExtra("term");

        // Set the calculated payment to the payment text view element in the layout
        TextView paymentView = (TextView) findViewById(R.id.textPayment);
        paymentView.setText("$ " + paymentAmnt);

        // Set the term text view element to the term provided by the user
        TextView termView = (TextView) findViewById(R.id.termLengthText);
        termView.setText("Over the " + term + " year term");
    }
}