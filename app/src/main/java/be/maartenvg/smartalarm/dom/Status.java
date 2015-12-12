package be.maartenvg.smartalarm.dom;

public class Status {
    private AlarmStatus status;
    private String[] sensorNames;
    private String[] activeSensorNames;

    public AlarmStatus getStatus() {
        return status;
    }

    public void setStatus(AlarmStatus status) {
        this.status = status;
    }

    public String[] getSensorNames() {
        return sensorNames;
    }

    public void setSensorNames(String[] sensorNames) {
        this.sensorNames = sensorNames;
    }

    public String[] getActiveSensorNames() {
        return activeSensorNames;
    }

    public void setActiveSensorNames(String[] activeSensorNames) {
        this.activeSensorNames = activeSensorNames;
    }
}
