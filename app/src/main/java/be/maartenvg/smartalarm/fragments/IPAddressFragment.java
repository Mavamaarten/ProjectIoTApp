package be.maartenvg.smartalarm.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import be.maartenvg.smartalarm.R;
import be.maartenvg.smartalarm.bl.AlarmManager;
import be.maartenvg.smartalarm.dom.Status;
import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class IPAddressFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    private IPAddressFragmentListener listener;

    @Bind(R.id.btn_connect)             Button btnConnect;
    @Bind(R.id.edit_ip)                 EditText editIP;
    @Bind(R.id.img_connection_success)  ImageView img_connection_success;

    public void setListener(IPAddressFragmentListener listener) {
        this.listener = listener;
    }

    @Override
    public void onStart() {
        super.onStart();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ipaddress, container, false);
        ButterKnife.bind(this, view);

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    final AlarmManager alarmManager = new AlarmManager("http://" + editIP.getText().toString());
                    alarmManager.getStatus().enqueue(new Callback<Status>() {
                        @Override
                        public void onResponse(Response<Status> response, Retrofit retrofit) {
                            listener.onIPAddressConfirmed();

                            img_connection_success.setImageResource(R.mipmap.ic_status_armed_ok);
                            img_connection_success.setVisibility(View.VISIBLE);

                            SharedPreferences.Editor prefs = sharedPreferences.edit();
                            prefs.putString("apiURL", alarmManager.getApiURL());
                            prefs.apply();
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            showIPError();
                        }
                    });
                } catch(Exception ex){
                    showIPError();
                }
            }
        });

        return view;
    }

    public String getApiURL(){
        return "http://" + editIP.getText().toString();
    }

    private void showIPError(){
        img_connection_success.setImageResource(R.mipmap.ic_status_armed_alert);
        img_connection_success.setVisibility(View.VISIBLE);
        Snackbar.make(getActivity().findViewById(R.id.ip_fragment), "Failed to connect", Snackbar.LENGTH_LONG).show();
    }

    public interface IPAddressFragmentListener{
        void onIPAddressConfirmed();
    }

}
