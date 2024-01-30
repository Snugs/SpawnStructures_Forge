package net.snuggsy.spawnstructures.functions;

public class NumberFunctions {

    public static boolean isNumeric(String string) {
        if (string == null) {
            return false;
        }
        try {
            Double.parseDouble(string);
        }
        catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }
}
