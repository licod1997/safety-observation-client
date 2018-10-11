package module;

public class Notification {
    private String NotificationName;
    private String NotificationLocation;
    private String NotificationMessage;

    public Notification(String notificationName, String notificationLocation, String notificationMessage) {
        NotificationName = notificationName;
        NotificationLocation = notificationLocation;
        NotificationMessage = notificationMessage;
    }

    public Notification() {
    }

    public String getNotificationName() {
        return NotificationName;
    }

    public void setNotificationName(String notificationName) {
        NotificationName = notificationName;
    }

    public String getNotificationLocation() {
        return NotificationLocation;
    }

    public void setNotificationLocation(String notificationLocation) {
        NotificationLocation = notificationLocation;
    }

    public String getNotificationMessage() {
        return NotificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        NotificationMessage = notificationMessage;
    }
}
