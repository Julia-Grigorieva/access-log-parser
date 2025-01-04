import java.net.MalformedURLException;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.*;

public class Statistics {
    private long totalTraffic;
    private OffsetDateTime minTime;
    private OffsetDateTime maxTime;
    private HashSet<String> existingPages;
    private HashSet<String> nonExistentPages;
    private HashMap<String, Integer> osFrequency;
    private HashMap<String, Integer> browserFrequency;
    private int totalOsCount;
    private int totalBrowserCount;
    private int totalUserVisits;
    private int totalErrorRequests;
    private HashSet<String> uniqueUsers;
    private HashMap<Long, Integer> visitsPerSecond;
    private HashMap<String, Integer> userVisits;
    private HashSet<String> referrerDomains;


    public Statistics() {
        totalTraffic = 0;
        minTime = null;
        maxTime = null;
        existingPages = new HashSet<>();
        nonExistentPages = new HashSet<>();
        osFrequency = new HashMap<>();
        browserFrequency = new HashMap<>();
        totalOsCount = 0;
        totalBrowserCount = 0;
        totalUserVisits = 0;
        totalErrorRequests = 0;
        uniqueUsers = new HashSet<>();
        visitsPerSecond = new HashMap<>();
        userVisits = new HashMap<>();
        referrerDomains = new HashSet<>();
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
            if (!entry.getUserAgent().isBot()) {
                totalUserVisits++;
                String ipAddress = entry.getIpAddress();
                uniqueUsers.add(ipAddress);
                userVisits.put(ipAddress, userVisits.getOrDefault(ipAddress, 0) + 1);

                long second = entry.getDateTime().toEpochSecond();
                visitsPerSecond.put(second, visitsPerSecond.getOrDefault(second, 0) + 1);
            }
        } else if (entry.getResponseCode() >= 400 && entry.getResponseCode() < 600) {
            nonExistentPages.add(entry.getPath());
            totalErrorRequests++;
        }

        String os = entry.getUserAgent().getOs();
        osFrequency.put(os, osFrequency.getOrDefault(os, 0) + 1);
        totalOsCount++;

        String browser = entry.getUserAgent().getBrowser();
        browserFrequency.put(browser, browserFrequency.getOrDefault(browser, 0) + 1);
        totalBrowserCount++;

        String referer = entry.getReferer();
        if (referer != null) {
            String domain = extractDomain(referer);
            if (domain != null) {
                referrerDomains.add(domain);
            }
        }
    }
        public double getTrafficRate () {
            if (minTime == null || maxTime == null) {
                return 0;
            }
            double hoursDifference = (maxTime.toEpochSecond() - minTime.toEpochSecond()) / 3600.0;
            return hoursDifference == 0 ? 0 : (double) totalTraffic / hoursDifference;
        }

        public long getTotalTraffic () {

            return totalTraffic;
        }
        public HashSet<String> getExistingPages () {
            return existingPages;
        }

        public HashMap<String, Double> getOsStatistics () {
            HashMap<String, Double> osStats = new HashMap<>();
            for (Map.Entry<String, Integer> entry : osFrequency.entrySet()) {
                double share = (double) entry.getValue() / totalOsCount;
                osStats.put(entry.getKey(), share);
            }
            return osStats;
        }
        public HashSet<String> getNonExistentPages () {
            return nonExistentPages;
        }

        public HashMap<String, Double> getBrowserStatistics () {
            HashMap<String, Double> browserStats = new HashMap<>();
            for (Map.Entry<String, Integer> entry : browserFrequency.entrySet()) {
                double share = (double) entry.getValue() / totalBrowserCount;
                browserStats.put(entry.getKey(), share);
            }
            return browserStats;
        }
        public double getAverageVisitsPerHour () {
            if (minTime == null || maxTime == null || totalUserVisits == 0) {
                return 0;
            }
            double hoursDifference = (maxTime.toEpochSecond() - minTime.toEpochSecond()) / 3600.0;
            return hoursDifference == 0 ? 0 : (double) totalUserVisits / hoursDifference;
        }

        public double getAverageErrorRequestsPerHour () {
            if (minTime == null || maxTime == null || totalErrorRequests == 0) {
                return 0;
            }
            double hoursDifference = (maxTime.toEpochSecond() - minTime.toEpochSecond()) / 3600.0;
            return hoursDifference == 0 ? 0 : (double) totalErrorRequests / hoursDifference;
        }

        public double getAverageVisitsPerUser () {
            if (uniqueUsers.isEmpty()) {
                return 0;
            }
            return (double) totalUserVisits / uniqueUsers.size();
        }
    public int getPeakVisitsPerSecond() {
        return visitsPerSecond.values().stream().max(Integer::compare).orElse(0);
    }



    public int getMaxVisitsPerUser () {
        return userVisits.values().stream().max(Integer::compare).orElse(0);
    }
    private String extractDomain(String referer) {
        try {
            URL url = new URL(referer);
            return url.getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public HashSet<String> getReferrerDomains() {
        return referrerDomains;
    }
}