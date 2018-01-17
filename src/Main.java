import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.LinkedList;

public class Main{

    private static int BLOCKLENGTH = 16;

    public static void main(String[] args) throws NoSuchAlgorithmException {

        //Hole Klartext aus Datei (Teilaufgabe a.)
        String plaintext = Helper.getPlaintext(args);
        System.out.println("\nVerwendter Klartext:\n" + plaintext);

        //Klartext auffüllen und in Blöcke zerlegen (Teilaufgabe a./b.)
        LinkedList<char[]> charblocks = processplaintext(plaintext);

        System.out.println("\nKlartextblöcke: ");
        for (char[] block : charblocks)
            Helper.prettyPrintCharArray(block);

        //Initialisiere 256 Bit (32 Bytes) Textregister mit SHA-256 (Teilaufgabe c.)
        char textregister[] = Helper.byteArraytoCharArray(MessageDigest.getInstance("SHA-256").digest());

        System.out.println("\nTextregister:");
        System.out.print("Nach Initialisierung: ");
        Helper.prettyPrintCharArray(textregister);

        //Durchlaufe jeden Klartextblock (Teilaufgabe d.)
        int blocknumber = 0;
        for (char[] block : charblocks) {
            blocknumber++;

            //verknüpfe niederwertige 128 Bits (16 Bytes) des Textregisters mit Klartextblock (Teilaufgabe e.)
            char[] lastBytes = Arrays.copyOfRange(textregister, 16, 32);
            textregister = exor(lastBytes, block);

            //Initialisiere ARC4 mit Textregister (Teilaufgabe f.)
            RC4 rc4 = new RC4(textregister);


            //ARC4 für 256 bytes durchlaufen lassen (256 Bytes / 16 Bytes (128Bits) pro Durchlauf = 16 Durchläufe) (Teilaufgabe g.)
            for (int i = 0; i < 16; i++) {
                rc4.calculate(textregister);
            }

            //Die nächsten 256 Bit (32 Bytes) liefern neuen Inhalt des Textregisters (Teilaufgabe h.)
            char[] result1 = rc4.calculate(textregister);
            char[] result2 = rc4.calculate(textregister);

            //128 Bit (16 Bytes) pro Durchlauf --> zwei Durchläufe, Ergebnisse zusammenfügen
            textregister = new char[result1.length + result2.length];
            System.arraycopy(result1, 0, textregister, 0, result1.length);
            System.arraycopy(result2, 0, textregister, result2.length, result1.length);

            //Ergebnis: Inhalt des Textregisters (Teilaufgabe i.)
            System.out.print("Block Nummer " + blocknumber + ": ");
            Helper.prettyPrintCharArray(textregister);
        }

        //Ausgabe des Streuwerts
        StringBuilder result = new StringBuilder();
        for (char f : textregister) {
            result.append(String.format("%02x", (int) f));
        }
        System.out.println("\nErmittelter Hashwert:\n" + result);


    }

    private static LinkedList<char[]> processplaintext(String plaintext) {

        //verwende char Datentyp als Byte
        char[] plaintextchars = plaintext.toCharArray();

        //Anzahl der Bytes des Klartexts
        int plaintextcharslength = plaintextchars.length;

        //Erstelle neues Char-Array, Länge = Vielfaches von 512Bits (64 Bytes)
        int newLength = plaintextcharslength + (64 - (plaintextcharslength % 64));
        char[] chars = Arrays.copyOf(plaintextchars, newLength);

        //Setze Bit 1 hinter Klartext
        chars[plaintextchars.length] = 0b10000000;

        //Ermittle Länge des Klartexts in Bit
        char[] plaintextlength = Helper.intToCharArray(plaintextcharslength * 8);

        //Setze Länge des Klartexts als letzte Bytes
        for(int i = 0; i < plaintextlength.length; i++){
            chars[chars.length-1-i] = plaintextlength[plaintextlength.length-1-i];
        }

        //Zerlege Klartext in Blöcke zu je 128 Bits (16 Bytes)
        LinkedList<char[]> charblocks = new LinkedList<>(); //LinkedList, um Reihenfolge beizubehalten
        int start = 0;
        while (start < chars.length) {
            int end = start + BLOCKLENGTH;

            char[] block = Arrays.copyOfRange(chars, start, end);
            charblocks.add(block);

            start = end;
        }
        return charblocks;
    }

    private static char[] exor(char[] char1, char[] char2) {
        char[] charArray = new char[char1.length];
        for (int i = 0; i < char1.length; i++) {

            //Konvertiere zu int
            int one = (int) char1[i];
            int two = (int) char2[i];

            //EXOR-verknüpfe ints
            int xor = one ^ two;

            //Konvertiere zurück zu char
            char b = (char) (0xff & xor);
            charArray[i] = b;
        }

        return charArray;
    }

}