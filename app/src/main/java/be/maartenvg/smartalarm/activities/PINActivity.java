package be.maartenvg.smartalarm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import be.maartenvg.smartalarm.R;
import butterknife.Bind;
import butterknife.ButterKnife;

public class PINActivity extends AppCompatActivity {
    @Bind(R.id.btn0) TextView btn0;
    @Bind(R.id.btn1) TextView btn1;
    @Bind(R.id.btn2) TextView btn2;
    @Bind(R.id.btn3) TextView btn3;
    @Bind(R.id.btn4) TextView btn4;
    @Bind(R.id.btn5) TextView btn5;
    @Bind(R.id.btn6) TextView btn6;
    @Bind(R.id.btn7) TextView btn7;
    @Bind(R.id.btn8) TextView btn8;
    @Bind(R.id.btn9) TextView btn9;
    @Bind(R.id.btnBackspace) ImageView btnBackspace;
    @Bind(R.id.btnPinOK) ImageView btnOk;
    @Bind(R.id.edit_pin) EditText editPIN;
    @Bind(R.id.PinScreenTitle) TextView txtPinScreenTitle;

    public static final int REQUEST_PIN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        String action = getIntent().getStringExtra("action");

        switch(action){
            case "arm":
                txtPinScreenTitle.setText(R.string.enable_alarm);
                setTitle(R.string.enable_alarm);
                break;

            case "disarm":
                txtPinScreenTitle.setText(R.string.disable_alarm);
                setTitle(R.string.disable_alarm);
                break;
        }

        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPIN.append("0");
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPIN.append("1");
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPIN.append("2");
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPIN.append("3");
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPIN.append("4");
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPIN.append("5");
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPIN.append("6");
            }
        });
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPIN.append("7");
            }
        });
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPIN.append("8");
            }
        });
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPIN.append("9");
            }
        });

        btnBackspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pin = editPIN.getText().toString();
                if(pin.length() > 0) pin = pin.substring(0, pin.length() - 1);
                editPIN.setText(pin);
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                data.putExtra("PIN", editPIN.getText().toString());
                data.putExtra("action", getIntent().getStringExtra("action"));
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

}
