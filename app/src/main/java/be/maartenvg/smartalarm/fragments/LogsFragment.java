package be.maartenvg.smartalarm.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import be.maartenvg.smartalarm.R;
import be.maartenvg.smartalarm.adapter.LogsAdapter;
import be.maartenvg.smartalarm.bl.AlarmManager;
import be.maartenvg.smartalarm.dom.LogItem;
import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LogsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.swipeContainer) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.list_logs) ListView listLogs;

    private AlarmManager alarmManager;
    private List<LogItem> logs;
    private LogsAdapter logsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logs, container, false);
        ButterKnife.bind(this, view);

        logs = new ArrayList<>();
        logsAdapter = new LogsAdapter(logs, getActivity());
        listLogs.setAdapter(logsAdapter);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        if(sharedPreferences.contains("apiURL")) alarmManager = new AlarmManager(sharedPreferences.getString("apiURL", "http://127.0.0.1"));
        swipeRefreshLayout.setOnRefreshListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        alarmManager.getLogs().enqueue(new Callback<List<LogItem>>() {
            @Override
            public void onResponse(Response<List<LogItem>> response, Retrofit retrofit) {
                logs.clear();
                logs.addAll(response.body());
                logsAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Snackbar.make(swipeRefreshLayout, "Error fetching logs", Snackbar.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }
}
