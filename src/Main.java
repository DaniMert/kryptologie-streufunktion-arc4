import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        String klartext = "Dies ist ein Test.";
        int blockLaenge = 16;

        if (klartext.length() % blockLaenge != 0) {
            klartext = klartext + "\u0001";
        }

        byte[] bytes = klartext.getBytes("UTF-8");

        LinkedList<byte[]> bytebloecke = new LinkedList<>(); //LinkedList, um Reihenfolge beizubehalten
        int anfang = 0;
        while (anfang < klartext.length()) {
            int ende = anfang + blockLaenge;

            byte[] block = Arrays.copyOfRange(bytes, anfang, ende);
            bytebloecke.add(block);

            anfang = ende;
        }

        byte textregister[] = MessageDigest.getInstance("SHA-256").digest();


        for (byte[] block : bytebloecke) {

            // Letzter Teil des Textregisters 128Bit
            //block XOR Textregister
            //letzter Teil des Textregisters extrahieren

            byte[] niederwertigeBytes = Arrays.copyOfRange(textregister, 16, 32);

            //e.
            textregister = exor(niederwertigeBytes, block);

            //Mit Textregister wird ARC4 Initialisiert


            ARC4 arc4 = new ARC4();





        }

        prettyPrintBytes(bytebloecke);
        for (byte f : textregister)
            System.out.print(f + ", ");

    }

    private static byte[] exor(byte[] byte1, byte[] byte2) {

        byte[] byteArray = new byte[byte1.length];

        for (int i = 0; i < byte1.length; i++) {

            // convert to ints and xor
            int one = (int) byte1[i];
            int two = (int) byte2[i];
            int xor = one ^ two;
            // convert back to byte
            byte b = (byte) (0xff & xor);

            byteArray[i] = b;
        }

        return byteArray;
    }

    private static void prettyPrintBytes(LinkedList<byte[]> bytebloecke) {
        for (byte[] block : bytebloecke) {
            String print = "[";
            for (byte character : block)
                print = print + character + ", ";
            print = print.substring(0, print.length() - 2) + "]";
            System.out.println(print);
        }
    }

}


