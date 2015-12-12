package be.maartenvg.smartalarm.bl;

import be.maartenvg.smartalarm.dal.AlarmRepository;
import be.maartenvg.smartalarm.dom.Status;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class AlarmManager {
    private AlarmRepository alarmRepository;

    public AlarmManager(String apiURL) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        alarmRepository = retrofit.create(AlarmRepository.class);
    }

    public Call<Status> getStatus(){
        return alarmRepository.getStatus();
    }

    public Call<Void> arm(String pin){ return alarmRepository.api(pin, "arm"); }

    public Call<Void> disarm(String pin){ return alarmRepository.api(pin, "disarm"); }
}
