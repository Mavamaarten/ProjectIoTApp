package be.maartenvg.smartalarm.bl;

import java.util.List;

import be.maartenvg.smartalarm.dal.AlarmRepository;
import be.maartenvg.smartalarm.dom.LogItem;
import be.maartenvg.smartalarm.dom.Status;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class AlarmManager {
    private AlarmRepository alarmRepository;
    private String apiURL;

    public AlarmManager(String apiURL) {
        this.apiURL = apiURL;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        alarmRepository = retrofit.create(AlarmRepository.class);
    }

    public String getApiURL() {
        return apiURL;
    }

    public Call<Status> getStatus(){
        return alarmRepository.getStatus();
    }

    public Call<Void> arm(String pin){ return alarmRepository.api(pin, "arm"); }

    public Call<Void> disarm(String pin){ return alarmRepository.api(pin, "disarm"); }

    public Call<Void> setPIN(String pin) { return alarmRepository.api(pin, "setpin"); }

    public Call<List<LogItem>> getLogs() { return alarmRepository.getLogs(); }
}
