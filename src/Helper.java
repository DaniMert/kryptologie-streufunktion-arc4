import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

/**
 * Created by danielmertens on 10.01.18.
 */
public class Helper {

    static String plaintext = "Dies ist ein Test.";

    public static void prettyPrintCharArray(char[] chars) {
        String print = "[";
        for (char character : chars)
            print = print + character + ", ";
        print = print.substring(0, print.length() - 2) + "]";

        System.out.println(print);
    }

    public static char[] byteArraytoCharArray(byte[] bytes) throws UnsupportedEncodingException {

        String text = new String(bytes, "UTF-8");
        return text.toCharArray();
    }

    public static String getPlaintext(String[] path) {
        if (path != null && path.length > 0) {
            try {
                return new String(Files.readAllBytes(Paths.get(path[0])));

            } catch (IOException e) {
                System.out.println("Datei nicht gefunden, Teststring wird verwendet.");
                return plaintext;
            }
        } else {
            System.out.println("Keine Datei angegeben, Teststring wird verwendet.");
            return plaintext;
        }
    }
}
