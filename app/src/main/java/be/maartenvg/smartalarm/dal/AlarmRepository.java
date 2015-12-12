package be.maartenvg.smartalarm.dal;

import be.maartenvg.smartalarm.dom.Status;
import retrofit.Call;
import retrofit.Response;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface AlarmRepository {
    @GET("status")
    Call<Status> getStatus();

    @POST("api")
    Call<Void> api(@Query("pin") String pin, @Query("action") String action);
}