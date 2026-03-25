import java.util.ArrayList;

public class JSONHelper {
    /**
     * Extracts the String value associated with a key in a given .json format String
     *
     * @param json String containing the .json information
     * @param key The String key
     * @return The String value associated with the given key
     */
    public static String extractString(String json, String key) {

        // Find "key": then skip to opening quote (handles optional space after colon)
        String search = "\"" + key + "\":";

        int colonEnd = json.indexOf(search) + search.length(); // Position after the colon
        int start = json.indexOf("\"", colonEnd) + 1;          // Skip to char after opening quote
        int end = json.indexOf("\"", start);                    // End at closing quote

        return json.substring(start, end).trim();
    }

    /**
     * Extracts the int value associated with a key in a given .json format String
     *
     * @param json String containing the .json information
     * @param key The String key
     * @return The int value associated with the given key
     */
    public static int extractInt(String json, String key) {
        // Create pattern like: "make":"
        String search = "\"" + key + "\":";

        int start = json.indexOf(search) + search.length(); // Start position

        int end = json.indexOf(",", start); // End at comma separator
        if(end == -1) {
            end = json.indexOf("}", start); // End at the end of json if no more elements
        }

        return Integer.parseInt(json.substring(start, end).trim()); // Return extracted text
    }

    /**
     * Extracts the double value associated with a key in a given .json format String
     *
     * @param json String containing the .json information
     * @param key The String key
     * @return The double value associated with the given key
     */
    public static double extractDouble(String json, String key) {
        // Create pattern like: "make":"
        String search = "\"" + key + "\":";

        int start = json.indexOf(search) + search.length(); // Start position

        int end = json.indexOf(",", start); // End at comma separator
        if(end == -1) {
            end = json.indexOf("}", start); // End at the end of json if no more elements
        }

        return Double.parseDouble(json.substring(start, end).trim()); // Return extracted text
    }

    /**
     * Extracts the elements of a list of Strings associated with a key in a given .json format String
     *
     * @param json String containing the .json information
     * @param key The String key
     * @return An ArrayList containing all Strings in the list
     */
    public static ArrayList<String> extractStringList(String json, String key) {
        String search = "\"" + key + "\":["; // Find the start of the list

        int start = json.indexOf(search) + search.length(); // Start our parsing at the start of the list
        int end = json.indexOf("]", start); // End at end of list
        String strings = json.substring(start, end); // New String just containing the Strings to be parsed

        ArrayList<String> list = new ArrayList<>();

        int next = strings.indexOf(",");
        while(true) {
            if(next > 0) { // If there's still another element after the current one
                list.add(strings.substring(1, next - 1)); // Add the next String, ignoring the quotes
                strings = strings.substring(next + 1); // Remove the first String and comma from the String to be parsed
            }
            else {
                list.add(strings.substring(1, strings.length() - 1)); // Add the last string
                break; // leave the loop
            }
            next = strings.indexOf(","); // Find next comma, if it exists
        }

        return list;
    }
}
