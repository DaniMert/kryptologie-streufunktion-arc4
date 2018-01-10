import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Erstellt von Taubert, Pham, Mertens am 10.01.18.
 * Stellt Hilfmethoden bereit.
 */
class Helper {

    static void prettyPrintCharArray(char[] chars) {
        StringBuilder print = new StringBuilder("[");
        for (char character : chars)
            print.append(character).append(", ");
        print = new StringBuilder(print.substring(0, print.length() - 2) + "]");

        System.out.println(print);
    }

    static char[] byteArraytoCharArray(byte[] bytes) throws UnsupportedEncodingException {

        String text = new String(bytes, "UTF-8");
        return text.toCharArray();
    }


    static String getPlaintext(String[] path) {
        String plaintext = "Dies ist ein Test.";

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
