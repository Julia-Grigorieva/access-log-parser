import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class Statistics {
    private int totalTraffic;
    private OffsetDateTime minTime;
    private OffsetDateTime maxTime;

    public Statistics() {
        totalTraffic = 0;
        minTime = null;
        maxTime = null;
    }

    public void addEntry(LogEntry entry) {
        totalTraffic += entry.getDataSize();

        if (minTime == null || entry.getDateTime().isBefore(minTime)) {
            minTime = entry.getDateTime();
        }
        if (maxTime == null || entry.getDateTime().isAfter(maxTime)) {
            maxTime = entry.getDateTime();
        }
    }

    public double getTrafficRate() {
        if (minTime == null || maxTime == null) {
            return 0;
        }
        long hoursDifference = maxTime.toEpochSecond() / 3600 - minTime.toEpochSecond() / 3600;
        return hoursDifference == 0 ? totalTraffic : (double) totalTraffic / hoursDifference;
    }

    public double getTotalTraffic() {

        return totalTraffic;
    }
}
