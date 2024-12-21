import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static java.lang.Integer.parseInt;

public class LogEntry {
    private final String ipAddress;
    private final OffsetDateTime dateTime;
    private final HttpMethod method;
    private final String path;
    private final int responseCode;
    private final int dataSize;
    private final String referer;
    private final UserAgent userAgent;

    public LogEntry(String logEntryStr) {
        String[] parts = logEntryStr.split(" ");

        this.ipAddress = parts[0];
        this.dateTime = parseDateTime(parts[3].substring(1) + " " + parts[4]);
        this.method = HttpMethod.valueOf(parts[5].substring(1).toUpperCase());
        this.path = parts[6];
        this.responseCode = parseInt(parts[8]);
        this.dataSize = parseInt(parts[9]);
        this.referer = parts[10].equals("-") ? null : parts[10];
        this.userAgent = new UserAgent(parts[11]);
    }

    private OffsetDateTime parseDateTime(String dateTimeStr) {
        dateTimeStr = dateTimeStr.replace("]", "");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
        return OffsetDateTime.parse(dateTimeStr, formatter);
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public OffsetDateTime getDateTime() {
        return dateTime;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public int getDataSize() {
        return dataSize;
    }

    public String getReferer() {
        return referer;
    }

    public UserAgent getUserAgent() {
        return userAgent;
    }
}
