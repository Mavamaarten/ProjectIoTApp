package be.maartenvg.smartalarm.dom;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogItem {
    private String logAction;
    private String timestamp;
    private String description;

    public LogItem(String logAction, String timestamp) {
        this.logAction = logAction;
        this.timestamp = timestamp;
        this.description = null;
    }

    public LogItem(String logAction, String timestamp, String description) {
        this.logAction = logAction;
        this.timestamp = timestamp;
        this.description = description;
    }

    public String getLogAction() {
        return logAction;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setLogAction(String logAction) {
        this.logAction = logAction;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getParsedDate() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        return df.parse(timestamp);
    }

}
