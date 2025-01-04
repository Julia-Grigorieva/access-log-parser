import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Statistics {
    private long totalTraffic;
    private OffsetDateTime minTime;
    private OffsetDateTime maxTime;
    private HashSet<String> existingPages;
    private HashMap<String, Integer> osFrequency;
    private int totalOsCount;


    public Statistics() {
        totalTraffic = 0;
        minTime = null;
        maxTime = null;
        existingPages = new HashSet<>();
        osFrequency = new HashMap<>();
        totalOsCount = 0;
    }

    public void addEntry(LogEntry entry) {
        totalTraffic += entry.getDataSize();

        if (minTime == null || entry.getDateTime().isBefore(minTime)) {
            minTime = entry.getDateTime();
        }
        if (maxTime == null || entry.getDateTime().isAfter(maxTime)) {
            maxTime = entry.getDateTime();
        }
        if (entry.getResponseCode() == 200) {
            existingPages.add(entry.getPath());
        }

        String os = entry.getUserAgent().getOs();
        osFrequency.put(os, osFrequency.getOrDefault(os, 0) + 1);
        totalOsCount++;
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
    public HashSet<String> getExistingPages() {
        return existingPages;
    }

    public HashMap<String, Double> getOsStatistics() {
        HashMap<String, Double> osStats = new HashMap<>();
        for (Map.Entry<String, Integer> entry : osFrequency.entrySet()) {
            double share = (double) entry.getValue() / totalOsCount;
            osStats.put(entry.getKey(), share);
        }
        return osStats;
    }
}