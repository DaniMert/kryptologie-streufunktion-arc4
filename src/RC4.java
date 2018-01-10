/**
 * Created by danielmertens on 10.01.18.
 */
public class RC4 {

    private char sbox[] = new char[256];

    RC4(char textregister[]) {
        for (int i = 0; i < 256; i++) {
            sbox[i] = (char) i;
        }
        int j = 0;

        for (int i = 0; i < 256; i++) {
            j = (j + sbox[i] + textregister[i % textregister.length]) % 256;
            vertauscheSboxWerte(i, j);
        }
    }

    public char[] berechneZufallsfolge (char textregister[]) {
        char zufallsfolge[] = new char[textregister.length];
        int i = 0;
        int j = 0;

        for (int n = 0; n < textregister.length; n++) {
            i = (i + 1) % 256;
            j = (j + sbox[i]) % 256;

            vertauscheSboxWerte(i,j);

            char zufallszahl = sbox[(sbox[i] + sbox[j]) % 256];
            zufallsfolge[n] = (char) (zufallszahl^textregister[n]);

        }

        return zufallsfolge;
    }

    private void vertauscheSboxWerte(int i, int j) {
        char zs = sbox[i];
        sbox[i] = sbox[j];
        sbox[j] = zs;
    }
}
