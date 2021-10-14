package com.sofe4640.mortgagecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.appbar.MaterialToolbar;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up toolbar
        MaterialToolbar mainToolbar = (MaterialToolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // Set up spinner for term
        Spinner termSpinner = (Spinner) findViewById(R.id.term);
        ArrayAdapter<CharSequence> termAdapter = ArrayAdapter.createFromResource(this,
                R.array.term, android.R.layout.simple_spinner_dropdown_item);
        // Specify the layout to use when the list of choices appears
        termAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        termSpinner.setAdapter(termAdapter);

        // Set up spinner for amortization
        Spinner amSpinner = (Spinner) findViewById(R.id.amortization);
        ArrayAdapter<CharSequence> amAdapter = ArrayAdapter.createFromResource(this,
                R.array.amortization, android.R.layout.simple_spinner_dropdown_item);
        // Specify the layout to use when the list of choices appears
        amAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        amSpinner.setAdapter(amAdapter);

        // Set up button to calculate monthly mortgage payments
        Button calcButton = (Button) findViewById(R.id.calcPayment);
        calcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), CalculatedPaymentActivity.class);
                i.putExtra("term", String.valueOf((int) getTerm()));
                i.putExtra("payment", String.valueOf(calculatePayment()));
                // Basic input error check, do not go to next activity if data entered is not valid
                if (calculatePayment() > 0 && getTerm() > 0) {
                    startActivity(i);
                }
            }
        });
    }

    // Helper functions to get the user input from layout elements and calculate the payment
    public double getPrincipalAmount() {
        double amount;
        String rawAmount;
        // Get principal amount value
        EditText principalAmnt = (EditText) findViewById(R.id.principalAmnt);
        rawAmount = principalAmnt.getText().toString();
        // Basic error check for empty or invalid input
        if (rawAmount.equals(" ") || rawAmount.equals("\n") || rawAmount.equals("")) {
            principalAmnt.setError("Please enter the principal amount loaned.");
            amount = 0;
        } else {
            amount = Double.parseDouble(rawAmount);
        }

        return amount;
    }

    public double getTerm() {
        double term;
        // Get term value
        Spinner termChoice = (Spinner) findViewById(R.id.term);
        // Basic error check for unselected option
        if (!(termChoice.getSelectedItem().toString().equals("Select"))) {
            String[] termArr = termChoice.getSelectedItem().toString().split(" ");
            term = Double.parseDouble(termArr[0]);
        }
        else {
            term = 0;
        }
        return term;
    }

    public double getInterestRate() {
        double rate;
        String rawRate;
        // Get interest value
        EditText interestRate = (EditText) findViewById(R.id.interest_rate);
        rawRate = interestRate.getText().toString();
        //Basic error check for empty or invalid input
        if (rawRate.equals(" ") || rawRate.equals("\n") || rawRate.equals("")
        || Double.parseDouble(rawRate) < 0 || Double.parseDouble(rawRate) > 100) {
            interestRate.setError("Please specify the interest rate in percent (0 - 100%).");
            rate = 0;
        } else{
            // Get the rate input and convert from percentage to decimal per month
            rate = Double.parseDouble(rawRate) / (12 * 100);
        }
        return rate;
    }

    public double getAmortization() {
        double amortization;
        // Get amortization period
        Spinner amortizationChoice = (Spinner) findViewById(R.id.amortization);
        // Basic error check for unselected option
        if (!(amortizationChoice.getSelectedItem().equals("Select"))) {
            String[] amArr = amortizationChoice.getSelectedItem().toString().split(" ");
            amortization = Double.parseDouble(amArr[0]);
        } else {
            amortization = 0;
        }

        return amortization;
    }

    public double calculatePayment() {

        double p, a, tm, r, payment;

        p = getPrincipalAmount();
        r = getInterestRate();
        a = getAmortization();

        // Convert term to time in months
        tm = a * 12;

        // Calculate monthly payment using M = P [{r*(1+r)^n}/{(1+r)^n – 1}]
        payment = p * ((r * Math.pow(1 + r, tm)) / (Math.pow(1 + r, tm) - 1));
        BigDecimal bd = new BigDecimal(payment).setScale(2, RoundingMode.HALF_UP);
        double rounded = bd.doubleValue();

        return rounded;
    }
}