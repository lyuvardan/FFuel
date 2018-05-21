package com.example.lyudvigv.ffuel.loginAndRegistration;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lyudvigv.ffuel.R;

public class SMSCodeConfirmationActivity extends AppCompatActivity {

    private EditText etCode1;
    private EditText etCode2;
    private EditText etCode3;
    private EditText etCode4;

    private ImageView ivResponseIcon;
    private ProgressBar pbLoader;


    TextView tvSendCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smscodeconfirmation);
        initialize();
    }

    private void initialize(){
        etCode1 = (EditText)findViewById(R.id.etCode1);
        etCode2 = (EditText)findViewById(R.id.etCode2);
        etCode3 = (EditText)findViewById(R.id.etCode3);
        etCode4 = (EditText)findViewById(R.id.etCode4);

        ivResponseIcon = (ImageView)findViewById(R.id.ivResponseIcon);
        pbLoader = (ProgressBar) findViewById(R.id.pbLoader);

        etCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                etCode2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                etCode3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                etCode4.requestFocus();
                tvSendCode.setBackgroundColor(getResources().getColor(R.color.colorPrimary,null));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvSendCode = (TextView)findViewById(R.id.tvSendCode);
        tvSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvSendCode.setBackgroundColor(Color.parseColor("#bdc6d3"));
                pbLoader.setVisibility(View.VISIBLE);
            }
        });

    }
}
