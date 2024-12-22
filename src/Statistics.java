import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class Statistics {
    private long totalTraffic;
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
        double hoursDifference = (maxTime.toEpochSecond() - minTime.toEpochSecond()) / 3600.0;
        return hoursDifference == 0 ? 0 : (double) totalTraffic / hoursDifference;
    }

    public long getTotalTraffic() {

        return totalTraffic;
    }
}
