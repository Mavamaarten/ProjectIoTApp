package be.maartenvg.smartalarm.activities;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

import be.maartenvg.smartalarm.R;
import be.maartenvg.smartalarm.bl.AlarmManager;
import be.maartenvg.smartalarm.fragments.IPAddressFragment;
import be.maartenvg.smartalarm.fragments.PINFragment;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SetupActivity extends AppIntro2 implements IPAddressFragment.IPAddressFragmentListener, PINFragment.PINFragmentListener {
    private final IPAddressFragment ipFragment = new IPAddressFragment();
    private final PINFragment pinFragment = new PINFragment();
    private SharedPreferences sharedPreferences;

    public final static int SETUP = 2;

    @Override
    public void init(Bundle savedInstanceState) {
        ipFragment.setListener(this);
        pinFragment.setListener(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        addSlide(AppIntroFragment.newInstance(
                        "Getting started",
                        "Thank you for purchasing a SmartAlarm! We will quickly help you set up and use SmartAlarm.",
                        R.drawable.ic_network,
                        Color.parseColor("#3F51B5"))
        );
        addSlide(AppIntroFragment.newInstance(
                        "Start up your SmartAlarm",
                        "Connect your SmartAlarm to your home network and turn it on. Make sure it is fully booted up before continuing to the next step.",
                        R.drawable.ic_power,
                        Color.parseColor("#2196F3"))
        );
        addSlide(ipFragment);
        addSlide(pinFragment);
    }

    @Override
    public void onDonePressed() {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putBoolean("firstRun", false);
        prefsEditor.apply();

        String pin = pinFragment.getPIN();
        AlarmManager manager = new AlarmManager(ipFragment.getApiURL());
        manager.setPIN(pin).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Response<Void> response, Retrofit retrofit) {
                if(response.code() == 200){
                    setResult(RESULT_OK);
                    finish();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(SetupActivity.this, "Error setting PIN", Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public void onNextPressed() {
    }

    @Override
    public void onSlideChanged() {
        if(getPager().getCurrentItem() == 2) setNextPageSwipeLock(true);
        if(getPager().getCurrentItem() == 3) showDoneButton(false);
    }

    @Override
    public void onIPAddressConfirmed() {
        setNextPageSwipeLock(false);
    }

    @Override
    public void onPINOk() {
        showDoneButton(true);
        System.out.println("OK");
    }

    @Override
    public void onPINNotOk() {
        showDoneButton(false);
        System.out.println("NOT OK");
    }
}
