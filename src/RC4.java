/**
 * Erstellt von Taubert, Pham, Mertens am 10.01.18.
 * Stellt RC4 Algorithmen zur Verfügung.
 */
class RC4 {

    private char sbox[] = new char[256];

    RC4(char textregister[]) {
        for (int i = 0; i < 256; i++) {
            sbox[i] = (char) i;
        }
        int j = 0;

        for (int i = 0; i < 256; i++) {
            j = (j + sbox[i] + textregister[i % textregister.length]) % 256;
            swapSboxValues(i, j);
        }
    }

    char[] calculate(char textregister[]) {
        char randomResult[] = new char[textregister.length];
        int i = 0;
        int j = 0;

        for (int n = 0; n < textregister.length; n++) {
            i = (i + 1) % 256;
            j = (j + sbox[i]) % 256;

            swapSboxValues(i, j);

            char randomValue = sbox[(sbox[i] + sbox[j]) % 256];
            randomResult[n] = (char) (randomValue ^ textregister[n]);

        }

        return randomResult;
    }

    private void swapSboxValues(int i, int j) {
        char zs = sbox[i];
        sbox[i] = sbox[j];
        sbox[j] = zs;
    }
}
