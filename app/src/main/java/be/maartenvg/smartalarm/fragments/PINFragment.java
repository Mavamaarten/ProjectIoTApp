package be.maartenvg.smartalarm.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import be.maartenvg.smartalarm.R;
import butterknife.Bind;
import butterknife.ButterKnife;

public class PINFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    private PINFragmentListener listener;

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
    @Bind(R.id.edit_pin) EditText editPIN;

    public void setListener(PINFragmentListener listener) {
        this.listener = listener;
    }

    @Override
    public void onStart() {
        super.onStart();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pin, container, false);
        ButterKnife.bind(this, view);

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

        editPIN.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() >= 4 && editable.length() <= 6)
                    listener.onPINOk();
                else
                    listener.onPINNotOk();
            }
        });

        return view;
    }

    public String getPIN(){
        return editPIN.getText().toString();
    }

    public interface PINFragmentListener{
        void onPINOk();
        void onPINNotOk();
    }

}
