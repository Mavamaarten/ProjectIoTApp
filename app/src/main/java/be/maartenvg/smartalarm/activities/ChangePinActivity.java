package be.maartenvg.smartalarm.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro2;

import be.maartenvg.smartalarm.R;
import be.maartenvg.smartalarm.bl.AlarmManager;
import be.maartenvg.smartalarm.fragments.PINFragment;

public class ChangePinActivity extends AppIntro2 implements PINFragment.PINFragmentListener {
    private PINFragment oldPinFragment = new PINFragment(),
            newPinFragment = new PINFragment(),
            confirmPinFragment = new PINFragment();

    private SharedPreferences sharedPreferences;
    private AlarmManager alarmManager;

    @Override
    public void init(Bundle savedInstanceState) {
        oldPinFragment.setListener(this);
        oldPinFragment.setFragmentTitle(getResources().getString(R.string.oldpin_title));
        oldPinFragment.setFragmentSubtitle(getResources().getString(R.string.enter_old_pin));

        newPinFragment.setListener(this);
        newPinFragment.setFragmentTitle(getResources().getString(R.string.newpin_title));
        newPinFragment.setFragmentSubtitle(getResources().getString(R.string.enter_new_pin));

        confirmPinFragment.setListener(this);
        confirmPinFragment.setFragmentTitle(getResources().getString(R.string.confirm_title));
        confirmPinFragment.setFragmentSubtitle(getResources().getString(R.string.confirm_new_pin));

        addSlide(oldPinFragment);
        addSlide(newPinFragment);
        addSlide(confirmPinFragment);
    }

    @Override
    public void onDonePressed() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(sharedPreferences.contains("apiURL")){
            alarmManager = new AlarmManager(sharedPreferences.getString("apiURL", "http://127.0.0.1"));
            alarmManager.setPIN(confirmPinFragment.getPIN());
            Toast.makeText(this, "PIN successfully changed", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "PIN not changed: no connection", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onNextPressed() {

    }

    @Override
    public void onSlideChanged() {
        setNextPageSwipeLock(true);
        showDoneButton(false);
    }

    @Override
    public void onPINOk() {
        if(getPager().getCurrentItem() == 2){
            if(newPinFragment.getPIN().equals(confirmPinFragment.getPIN())){
                setNextPageSwipeLock(false);
                showDoneButton(true);
            } else {
                setNextPageSwipeLock(true);
                showDoneButton(false);
            }
        } else
        {
            setNextPageSwipeLock(false);
            showDoneButton(true);
        }
    }

    @Override
    public void onPINNotOk() {
        setNextPageSwipeLock(true);
        showDoneButton(false);
    }
}
