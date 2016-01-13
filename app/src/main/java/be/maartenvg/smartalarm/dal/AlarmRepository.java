package be.maartenvg.smartalarm.dal;

import java.util.List;

import be.maartenvg.smartalarm.dom.LogItem;
import be.maartenvg.smartalarm.dom.Status;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

public interface AlarmRepository {
    @GET("status")
    Call<Status> getStatus();

    @GET("logs")
    Call<List<LogItem>> getLogs();

    @POST("api")
    Call<Void> api(@Query("pin") String pin, @Query("action") String action);
}