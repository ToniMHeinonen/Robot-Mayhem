package fi.tamk.fi;

public class Encryptor {

    /**
     * Encrypts text.
     * @param text given text
     * @return encrypted String
     */
    public String encrypt(String text) {
        String out = "";
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char aux = chars[i];
            aux += (i + 3) * 1;
            out += aux;
        }
        return out;
    }

    /**
     * Changes Integer to String and encrypts it.
     * @param value given value
     * @return encrypted String
     */
    public String encrypt(Integer value) {
        String str = String.valueOf(value);
        return encrypt(str);
    }

    /**
     * Changes Float to String and encrypts it.
     * @param value given value
     * @return encrypted String
     */
    public String encrypt(Float value) {
        String str = String.valueOf(value);
        return encrypt(str);
    }

    /**
     * Changes Boolean to String and encrypts it.
     * @param value given value
     * @return encrypted String
     */
    public String encrypt(Boolean value) {
        String str = String.valueOf(value);
        return encrypt(str);
    }

    /**
     * Decrypts String.
     * @param text encrypted text
     * @return decrypted String
     */
    public String decrypt(String text) {
        String out = "";
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char aux = chars[i];
            aux -= (i + 3) * 1;
            out += aux;
        }
        return out;
    }
}
