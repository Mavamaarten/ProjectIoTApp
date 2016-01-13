package be.maartenvg.smartalarm.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import be.maartenvg.smartalarm.R;
import be.maartenvg.smartalarm.activities.PINActivity;
import be.maartenvg.smartalarm.activities.SetupActivity;
import be.maartenvg.smartalarm.adapter.SensorStatusAdapter;
import be.maartenvg.smartalarm.bl.AlarmManager;
import be.maartenvg.smartalarm.dom.AlarmStatus;
import be.maartenvg.smartalarm.dom.Status;
import be.maartenvg.smartalarm.push.PushBroadCastReceiver;
import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class OverviewFragment extends Fragment {
    @Bind(R.id.icon_status)         ImageView imgStatusIcon;
    @Bind(R.id.title_status)        TextView txtTitleStatus;
    @Bind(R.id.subtitle_status)     TextView txtSubtitleStatus;
    @Bind(R.id.btn_911)             Button btn911;
    @Bind(R.id.btn_enable_disable)  Button btnEnableDisable;
    @Bind(R.id.ll_sensor_overview)  LinearLayout llSensorOverview;
    @Bind(R.id.swipeContainer)      SwipeRefreshLayout swipeRefreshLayout;

    public static boolean isForeground = false;

    private AlarmManager alarmManager;
    private BroadcastReceiver receiver;
    private Map<String, Boolean> sensors = new HashMap<>();
    private SensorStatusAdapter sensorStatusAdapter;
    private Status currentStatus;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        ButterKnife.bind(this, view);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onSwipeRefresh();
            }
        });
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onReceivePushMessage(intent.getExtras());
            }
        };

        btnEnableDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PINActivity.class);
                if (currentStatus != null) {
                    switch (currentStatus.getStatus()) {
                        case ARMED:
                        case COUNTDOWN:
                        case SIRENS_ON:
                            intent.putExtra("action", "disarm");
                            break;
                        case DISARMED:
                        case SETTINGS:
                            intent.putExtra("action", "arm");
                            break;
                    }
                }
                if(intent.getStringExtra("action") != null)
                    startActivityForResult(intent, PINActivity.REQUEST_PIN);
                else
                    Snackbar.make(swipeRefreshLayout, "Not connected to your alarm...", Snackbar.LENGTH_LONG).show();
            }
        });

        sensorStatusAdapter = new SensorStatusAdapter(getActivity(), llSensorOverview, sensors);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        if(sharedPreferences.getBoolean("firstRun", true)){
            Intent intent = new Intent(getActivity(), SetupActivity.class);
            startActivityForResult(intent, SetupActivity.SETUP);
        }

        if(sharedPreferences.contains("apiURL")){
            alarmManager = new AlarmManager(sharedPreferences.getString("apiURL", "http://127.0.0.1"));
            onSwipeRefresh();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(PushBroadCastReceiver.BROADCAST_ACTION);
        getActivity().registerReceiver(receiver, filter);
        isForeground = true;
    }

    @Override
    public void onPause(){
        super.onPause();
        getActivity().unregisterReceiver(receiver);
        isForeground = false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK) return;

        switch(requestCode){
            case PINActivity.REQUEST_PIN:
                String pin = data.getStringExtra("PIN");
                String action = data.getStringExtra("action");

                if(action.equals("arm")) alarmManager.arm(pin).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Response<Void> response, Retrofit retrofit) {
                        if(response.code() != 200)
                            Snackbar.make(swipeRefreshLayout, "Error arming alarm: wrong PIN", Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Snackbar.make(swipeRefreshLayout, "Error arming alarm.", Snackbar.LENGTH_LONG).show();
                    }
                });
                else if(action.equals("disarm")) alarmManager.disarm(pin).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Response<Void> response, Retrofit retrofit) {
                        if(response.code() != 200)
                            Snackbar.make(swipeRefreshLayout, "Error disarming alarm: wrong PIN", Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Snackbar.make(swipeRefreshLayout, "Error disarming alarm.", Snackbar.LENGTH_LONG).show();
                    }
                });

                System.out.println();
                break;
            case SetupActivity.SETUP:
                this.alarmManager = new AlarmManager(sharedPreferences.getString("apiURL", "http://127.0.0.1"));
                onSwipeRefresh();
        }
    }

    private void onSwipeRefresh(){
        swipeRefreshLayout.setRefreshing(true);
        alarmManager.getStatus().enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Response<Status> response, Retrofit retrofit) {
                Status status = response.body();
                setStatus(status.getStatus());
                updateSensors(status.getSensorNames(), status.getActiveSensorNames());
                currentStatus = status;
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Throwable t) {
                setStatus(AlarmStatus.NOT_CONNECTED);
                sensors.clear();
                sensors.put("Error communicating with alarm", true);
                sensorStatusAdapter.notifyDataSetChanged();
                Snackbar.make(swipeRefreshLayout, "Error communicating with alarm", Snackbar.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void onReceivePushMessage(Bundle bundle) {
        try {
            String title = bundle.getString("title");
            String alert = bundle.getString("alert");
            Snackbar.make(swipeRefreshLayout, title, Snackbar.LENGTH_LONG).show();
            Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(150);
        } catch (Exception e) {
            e.printStackTrace();
        }
        onSwipeRefresh();
    }

    private void updateSensors(String[] sensorNames, String[] activeSensors){
        Map<String, Boolean> sensors = new HashMap<>();
        if(sensorNames != null)
            for(String sensorName : sensorNames)
                sensors.put(sensorName, false);
        if(activeSensors != null)
            for(String sensorName : activeSensors)
                sensors.put(sensorName, true);

        this.sensors = sensors;
        sensorStatusAdapter.setSensorsValues(sensors);
        sensorStatusAdapter.notifyDataSetChanged();
    }

    private void setStatus(AlarmStatus status){
        switch(status){
            case ARMED:
                imgStatusIcon.setImageResource(R.mipmap.ic_status_armed_ok);
                txtTitleStatus.setText(R.string.armed);
                txtSubtitleStatus.setText(R.string.sensors_ok);
                btn911.setVisibility(View.GONE);
                btnEnableDisable.setText(R.string.disable_alarm);
                btnEnableDisable.setVisibility(View.VISIBLE);
                break;
            case DISARMED:
                imgStatusIcon.setImageResource(R.mipmap.ic_status_disarmed_ok);
                txtTitleStatus.setText(R.string.disarmed);
                txtSubtitleStatus.setText(R.string.sensors_ok);
                btn911.setVisibility(View.GONE);
                btnEnableDisable.setText(R.string.enable_alarm);
                btnEnableDisable.setVisibility(View.VISIBLE);
                break;
            case COUNTDOWN:
                imgStatusIcon.setImageResource(R.mipmap.ic_status_armed_alert);
                txtTitleStatus.setText(R.string.intrusion_alert);
                txtSubtitleStatus.setText(R.string.countdown_initiated);
                btn911.setVisibility(View.VISIBLE);
                btnEnableDisable.setText(R.string.disable_alarm);
                btnEnableDisable.setVisibility(View.VISIBLE);
                break;
            case SIRENS_ON:
                imgStatusIcon.setImageResource(R.mipmap.ic_status_armed_alert);
                txtTitleStatus.setText(R.string.intrusion_alert);
                txtSubtitleStatus.setText(R.string.sirens_active);
                btn911.setVisibility(View.VISIBLE);
                btnEnableDisable.setText(R.string.disable_alarm);
                btnEnableDisable.setVisibility(View.VISIBLE);
                break;
            case SETTINGS:
                imgStatusIcon.setImageResource(R.mipmap.ic_status_armed_ok);
                txtTitleStatus.setText(R.string.menu);
                txtSubtitleStatus.setText(R.string.alarm_being_setup);
                btn911.setVisibility(View.GONE);
                btnEnableDisable.setText(R.string.enable_alarm);
                btnEnableDisable.setVisibility(View.VISIBLE);
                break;
            case NOT_CONNECTED:
                imgStatusIcon.setImageResource(R.mipmap.ic_status_disarmed_alert);
                txtTitleStatus.setText(R.string.status_unknown);
                txtSubtitleStatus.setText(R.string.error_communicating);
                btn911.setVisibility(View.GONE);
                btnEnableDisable.setText(R.string.enable_alarm);
                btnEnableDisable.setVisibility(View.GONE);
                break;
        }
    }
}
