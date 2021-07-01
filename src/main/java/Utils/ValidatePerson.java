package Utils;

public class ValidatePerson {
    public static boolean validateEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    public static boolean validatePhone(String s) {
        String regex = "\\d{10}";
        return s.matches(regex);
    }
}
