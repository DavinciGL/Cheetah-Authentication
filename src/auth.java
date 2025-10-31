public class auth {
    // Simple 2D array: {username, password}
    public static final String[][] credentials = {
            {"admin", "admin"},
            {"user", "network"},
            {"guest", "guest"}
    };

    public boolean validate(String username, String password) {
            for (String[] pair : credentials) {
                if (pair[0].equals(username) && pair[1].equals(password)) {
                    return true;
                }
            }
            return false;
        }

    }
