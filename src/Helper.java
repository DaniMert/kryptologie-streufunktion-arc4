import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Erstellt von Taubert, Pham, Mertens am 10.01.18.
 * Stellt Hilfmethoden bereit.
 */
class Helper {

    //Hilfsmethode für Ausgabe eines Char Arrays als Hexwerte
    static void prettyPrintCharArray(char[] chars) {
        StringBuilder print = new StringBuilder("[");
        for (char character : chars){
            print.append(String.format("%02X", (int) character)).append(", ");
        }
        print = new StringBuilder(print.substring(0, print.length() - 2) + "]");
        System.out.println(print);
    }

    static char[] byteArraytoCharArray(byte[] bytes){
        //Wandle jedes Byte in Char um (ignoriere Vorzeichen)
        char[] chars = new char[bytes.length];
        for(int i = 0; i < bytes.length; i++){
            chars[i] = (char)(bytes[i] & 0xFF);
        }
        return chars;
    }


    static String getPlaintext(String[] path) {
        //Teststring
        String plaintext = "Dies ist ein Test.";

        //Wenn Pfad angegeben, lese Datei aus, sonst nimm Teststring
        if (path != null && path.length > 0) {
            try {
                return new String(Files.readAllBytes(Paths.get(path[0])));

                //Wenn Datei nicht gefunden, verwende Teststring
            } catch (IOException e) {
                System.out.println("Datei nicht gefunden, Teststring wird verwendet.");
                return plaintext;
            }
        } else {
            System.out.println("Keine Datei angegeben, Teststring wird verwendet.");
            return plaintext;
        }
    }

    //Konvertiere int zu Char-Array
    static char[] intToCharArray(int value) {
        return new char[] {
                (char)((byte)(value >>> 24)& 0xFF),
                (char)((byte)(value >>> 16)& 0xFF),
                (char)((byte)(value >>> 8)& 0xFF),
                (char)((byte)value & 0xFF)};
    }
}
