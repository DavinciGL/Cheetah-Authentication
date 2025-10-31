public class auth {
    // Simple 2D array: {username, password}
    public static final String[][] credentials = {
            {"admin", "cheetah123"},
            {"user", "network456"},
            {"guest", "auth789"}
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