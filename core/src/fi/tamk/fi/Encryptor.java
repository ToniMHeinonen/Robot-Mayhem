package fi.tamk.fi;

public class Encryptor {

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

    public Integer encrypt(Integer value) {
        String str = String.valueOf(value);
        String encrypted = encrypt(str);

        return Integer.valueOf(encrypted);
    }

    public Float encrypt(Float value) {
        String str = String.valueOf(value);
        String encrypted = encrypt(str);

        return Float.valueOf(encrypted);
    }

    public Boolean encrypt(Boolean value) {
        String str = String.valueOf(value);
        String encrypted = encrypt(str);

        return Boolean.valueOf(encrypted);
    }

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

    public Integer decrypt(Integer value) {
        String str = String.valueOf(value);
        String decrypted = decrypt(str);

        return Integer.valueOf(decrypted);
    }

    public Float decrypt(Float value) {
        System.out.println(value);
        String str = String.valueOf(value);
        System.out.println(str);
        String decrypted = decrypt(str);
        System.out.println(decrypted);

        return Float.valueOf(decrypted);
    }

    public Boolean decrypt(Boolean value) {
        String str = String.valueOf(value);
        String decrypted = decrypt(str);

        return Boolean.valueOf(decrypted);
    }
}
