import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        int blockLaenge = 16;
        String klartext = Helper.getPlaintext(args);
        System.out.println("\nVerwendeter Klartext: " + klartext);


        if (klartext.length() % blockLaenge != 0) {
            klartext = klartext + "\u0001";
        }

        char[] chars = klartext.toCharArray();

        LinkedList<char[]> charbloecke = new LinkedList<>(); //LinkedList, um Reihenfolge beizubehalten
        int anfang = 0;
        while (anfang < klartext.length()) {
            int ende = anfang + blockLaenge;

            char[] block = Arrays.copyOfRange(chars, anfang, ende);
            charbloecke.add(block);

            anfang = ende;
        }

        System.out.println("\nKlartextblöcke: ");
        for (char[] block : charbloecke)
            Helper.prettyPrintCharArray(block);

        char textregister[] = Helper.byteArraytoCharArray(MessageDigest.getInstance("SHA-256").digest());

        System.out.println("\nTextregister:");
        for (char[] block : charbloecke) {

            // Letzter Teil des Textregisters 128Bit
            //block XOR Textregister
            //letzter Teil des Textregisters extrahieren


            char[] niederwertigeBytes = Arrays.copyOfRange(textregister, 16, 32);

            //e.
            textregister = exor(niederwertigeBytes, block);

            //Mit Textregister wird ARC4 Initialisiert
            RC4 rc4 = new RC4(textregister);


            // Man lässt die nächsten 256Bytes verfallen
            for (int i = 0; i < 16; i++) {
                rc4.berechneZufallsfolge(textregister);
            }

            char[] ergebnis1 = rc4.berechneZufallsfolge(textregister);
            char[] ergebnis2 = rc4.berechneZufallsfolge(textregister);

            textregister = new char[ergebnis1.length + ergebnis2.length];
            System.arraycopy(ergebnis1, 0, textregister, 0, ergebnis1.length);
            System.arraycopy(ergebnis2, 0, textregister, ergebnis2.length, ergebnis1.length);


            Helper.prettyPrintCharArray(textregister);
        }

        String ergebnis = "";

        for (char f : textregister) {
            ergebnis = ergebnis + Integer.toHexString((int) f);
        }

        System.out.println("\nErmittelter Hashwert:\n" + ergebnis);


    }

    private static char[] exor(char[] char1, char[] char2) {

        char[] charArray = new char[char1.length];

        for (int i = 0; i < char1.length; i++) {

            // convert to ints and xor
            int one = (int) char1[i];
            int two = (int) char2[i];
            int xor = one ^ two;
            // convert back to char
            char b = (char) (0xff & xor);

            charArray[i] = b;
        }

        return charArray;
    }

}