package be.maartenvg.smartalarm.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import be.maartenvg.smartalarm.R;
import be.maartenvg.smartalarm.dom.LogItem;

public class LogsAdapter extends BaseAdapter {
    private List<LogItem> logs;
    private static LayoutInflater inflater = null;

    public LogsAdapter(List<LogItem> logs, Activity activity) {
        this.logs = logs;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return logs.size();
    }

    @Override
    public Object getItem(int i) {
        return logs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) view = inflater.inflate(R.layout.item_log, viewGroup, false);

        LogItem item = logs.get(i);

        LinearLayout logCard = (LinearLayout) view.findViewById(R.id.log_card);
        ImageView logIcon = (ImageView) view.findViewById(R.id.log_icon);
        TextView logTitle = (TextView) view.findViewById(R.id.log_title);
        TextView logTimestamp = (TextView) view.findViewById(R.id.log_timestamp);
        TextView logDescription = (TextView) view.findViewById(R.id.log_description);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                1,
                view.getContext().getResources().getDisplayMetrics()
        );

        if(i == 0){
            layoutParams.setMargins(10 * px, 12 * px, 10 * px, 2 * px);
            logCard.setLayoutParams(layoutParams);
        } else if(i == logs.size() - 1){
            layoutParams.setMargins(10 * px, 5 * px, 10 * px, 8 * px);
            logCard.setLayoutParams(layoutParams);
        } else {
            layoutParams.setMargins(10 * px, 5 * px, 10 * px, 2 * px);
            logCard.setLayoutParams(layoutParams);
        }

        logTitle.setText(toDisplayCase(item.getLogAction()));
        try {
            logTimestamp.setText(formatToYesterdayOrToday(item.getParsedDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(item.getDescription() != null){
            logDescription.setText(item.getDescription());
            logDescription.setVisibility(View.VISIBLE);
        } else {
            logDescription.setVisibility(View.GONE);
        }

        switch(item.getLogAction()){
            case "SENSOR_ACTIVATED":
            case "COUNTDOWN_ACTIVATED":
            case "SIRENS_ACTIVATED":
            case "INCORRECT_PIN":
                logIcon.setImageResource(R.drawable.ic_alert);
                break;

            case "SHUTDOWN":
            case "INFORMATION":
                logIcon.setImageResource(R.drawable.ic_info);
                break;

            case "ARMED":
                logIcon.setImageResource(R.drawable.ic_arm);
                break;

            case "DISARMED":
                logIcon.setImageResource(R.drawable.ic_disarm);
        }

        return view;
    }

    public static String formatToYesterdayOrToday(Date dateTime) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        Calendar today = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        DateFormat timeFormatter = new SimpleDateFormat("HH:mm");
        DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yy, HH:mm");
        if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            return timeFormatter.format(dateTime);
        } else if (calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
            return "Yesterday, " + timeFormatter.format(dateTime);
        } else {
            return dateFormatter.format(dateTime);
        }
    }

    public static String toDisplayCase(String s) {
        s = s.replace('_', ' ').toLowerCase();
        char[] chars = s.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }
}
