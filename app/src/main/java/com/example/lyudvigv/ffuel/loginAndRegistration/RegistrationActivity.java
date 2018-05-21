package com.example.lyudvigv.ffuel.loginAndRegistration;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lyudvigv.ffuel.R;

public class RegistrationActivity extends AppCompatActivity {

    private EditText etPhoneNumber;
    private TextView tvGetSMS;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initialize();
    }

    private void initialize(){
        etPhoneNumber = (EditText)findViewById(R.id.etPhoneNumber);
        tvGetSMS = (TextView)findViewById(R.id.tvGetSMS);

        etPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvGetSMS.setBackgroundColor(getResources().getColor(R.color.colorPrimary,null));
                if(etPhoneNumber.getText().toString().equals("")){
                    tvGetSMS.setBackgroundColor(Color.parseColor("#bdc6d3"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvGetSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etPhoneNumber.getText().toString().equals("")){
                    startActivity(new Intent(RegistrationActivity.this,SMSCodeConfirmationActivity.class));
                }
            }
        });
    }

}
