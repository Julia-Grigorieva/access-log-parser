public class UserAgent {
    private final String os;
    private final String browser;
    private final String userAgentStr;

    public UserAgent(String userAgentStr) {
        this.os = extractOS(userAgentStr);
        this.browser = extractBrowser(userAgentStr);
        this.userAgentStr = userAgentStr;
    }

    private String extractOS(String userAgentStr) {
        if (userAgentStr.contains("Windows")) {
            return "Windows";
        } else if (userAgentStr.contains("Macintosh")) {
            return "macOS";
        } else if (userAgentStr.contains("Linux")) {
            return "Linux";
        }
        return "Unknown OS";
    }

    private String extractBrowser(String userAgentStr) {
        if (userAgentStr.contains("Chrome")) {
            return "Chrome";
        } else if (userAgentStr.contains("Firefox")) {
            return "Firefox";
        } else if (userAgentStr.contains("Edge")) {
            return "Edge";
        } else if (userAgentStr.contains("Opera")) {
            return "Opera";
        }
        return "Other";
    }

    public String getOs() {
        return os;
    }

    public String getBrowser() {
        return browser;
    }
    public String getUserAgentStr() {
        return userAgentStr;
    }
}