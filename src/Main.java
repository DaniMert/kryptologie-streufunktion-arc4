import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        int blockLength = 16;
        String plaintext = Helper.getPlaintext(args);
        System.out.println("\nVerwendter Klartext: " + plaintext);


        if (plaintext.length() % blockLength != 0) {
            plaintext = plaintext + "\u0001";
        }

        char[] chars = plaintext.toCharArray();

        LinkedList<char[]> charblocks = new LinkedList<>(); //LinkedList, um Reihenfolge beizubehalten
        int start = 0;
        while (start < plaintext.length()) {
            int end = start + blockLength;

            char[] block = Arrays.copyOfRange(chars, start, end);
            charblocks.add(block);

            start = end;
        }

        System.out.println("\nKlartextblöcke: ");
        for (char[] block : charblocks)
            Helper.prettyPrintCharArray(block);

        char textregister[] = Helper.byteArraytoCharArray(MessageDigest.getInstance("SHA-256").digest());

        System.out.println("\nTextregister:");
        for (char[] block : charblocks) {

            // Letzter Teil des Textregisters 128Bit
            //block XOR Textregister
            //letzter Teil des Textregisters extrahieren


            char[] lastBytes = Arrays.copyOfRange(textregister, 16, 32);

            //e.
            textregister = exor(lastBytes, block);

            //Mit Textregister wird ARC4 Initialisiert
            RC4 rc4 = new RC4(textregister);


            // Man lässt die nächsten 256Bytes verfallen
            for (int i = 0; i < 16; i++) {
                rc4.calculate(textregister);
            }

            char[] result1 = rc4.calculate(textregister);
            char[] result2 = rc4.calculate(textregister);

            textregister = new char[result1.length + result2.length];
            System.arraycopy(result1, 0, textregister, 0, result1.length);
            System.arraycopy(result2, 0, textregister, result2.length, result1.length);


            Helper.prettyPrintCharArray(textregister);
        }

        StringBuilder result = new StringBuilder();

        for (char f : textregister) {
            result.append(Integer.toHexString((int) f));
        }

        System.out.println("\nErmittelter Hashwert:\n" + result);


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