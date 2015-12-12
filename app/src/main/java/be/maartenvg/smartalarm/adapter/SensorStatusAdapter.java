package be.maartenvg.smartalarm.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Map;

import be.maartenvg.smartalarm.R;

public class SensorStatusAdapter {
    private Map<String, Boolean> sensorsValues;
    private final LinearLayout container;
    private final Context context;

    public SensorStatusAdapter(Context context, LinearLayout container, Map<String, Boolean> sensorsValues) {
        this.container = container;
        this.sensorsValues = sensorsValues;
        this.context = context;
    }

    public Map<String, Boolean> getSensorsValues() {
        return sensorsValues;
    }

    public void setSensorsValues(Map<String, Boolean> sensorsValues) {
        this.sensorsValues = sensorsValues;
    }

    public void notifyDataSetChanged(){
        container.removeAllViewsInLayout();

        for(String sensorName : sensorsValues.keySet()){
            View view = View.inflate(context, R.layout.item_sensor, null);

            TextView txtSensorName = (TextView)view.findViewById(R.id.sensor_name);
            ImageView imgSensorStatus = (ImageView)view.findViewById(R.id.sensor_status);

            txtSensorName.setText(sensorName);
            imgSensorStatus.setImageResource(sensorsValues.get(sensorName) ? R.drawable.ic_alert : R.drawable.ic_check);

            container.addView(view);
        }
    }
}
