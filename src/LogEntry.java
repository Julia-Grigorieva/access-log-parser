import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Pattern pattern = Pattern.compile("\"(https?://[^\"]+)\"");
        Matcher matcher = pattern.matcher(logEntryStr);

        if (matcher.find()) {
            this.referer = matcher.group(1); // Извлекаем реферер
        } else {
            this.referer = null; // Если реферер не найден
        }

        StringBuilder userAgentBuilder = new StringBuilder();
        for (int i = 11; i < parts.length; i++) {
            userAgentBuilder.append(parts[i]).append(" ");
        }
        this.userAgent = new UserAgent(userAgentBuilder.toString().trim());
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