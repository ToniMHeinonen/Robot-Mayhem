package fi.tamk.fi;

import com.badlogic.gdx.Preferences;

public class SaveAndLoad {
    Encryptor E;
    Preferences file;

    /**
     * Retrieves Encryptor and Preferences from creator.
     * @param E Used for encrypting and decrypting
     * @param file File to save and load
     */
    SaveAndLoad(Encryptor E, Preferences file) {
        this.E = E;
        this.file = file;
    }

    /**
     * Searches for key value, if it does not find it, it encrypts given default value.
     * Then it decrypts it and returns it.
     * @param key value to look for
     * @param defValue value which is used at the first playtime
     * @return decrypted value
     */
    public String loadValue(String key, String defValue) {
        return E.decrypt(file.getString(key, E.encrypt(defValue)));
    }

    /**
     * Searches for key value, if it does not find it, it encrypts given default value.
     * Then it decrypts it, changes it's value to Integer and returns it.
     * @param key value to look for
     * @param defValue value which is used at the first playtime
     * @return decrypted value
     */
    public Integer loadValue(String key, Integer defValue) {
        return Integer.valueOf(E.decrypt(file.getString(key, E.encrypt(defValue))));
    }

    /**
     * Searches for key value, if it does not find it, it encrypts given default value.
     * Then it decrypts it, changes it's value to Float and returns it.
     * @param key value to look for
     * @param defValue value which is used at the first playtime
     * @return decrypted value
     */
    public Float loadValue(String key, Float defValue) {
        return Float.valueOf(E.decrypt(file.getString(key, E.encrypt(defValue))));
    }

    /**
     * Searches for key value, if it does not find it, it encrypts given default value.
     * Then it decrypts it, changes it's value to Boolean and returns it.
     * @param key value to look for
     * @param defValue value which is used at the first playtime
     * @return decrypted value
     */
    public Boolean loadValue(String key, Boolean defValue) {
        return Boolean.valueOf(E.decrypt(file.getString(key, E.encrypt(defValue))));
    }

    /**
     * Searches for key to overwrite, encrypts value and writes it.
     * @param key value to overwrite
     * @param value value that will be saved
     */
    public void saveValue(String key, String value) {
        file.putString(key, E.encrypt(value));
    }

    /**
     * Searches for key to overwrite, encrypts value and writes it.
     * @param key value to overwrite
     * @param value value that will be saved
     */
    public void saveValue(String key, Integer value) {
        file.putString(key, E.encrypt(value));
    }

    /**
     * Searches for key to overwrite, encrypts value and writes it.
     * @param key value to overwrite
     * @param value value that will be saved
     */
    public void saveValue(String key, Float value) {
        file.putString(key, E.encrypt(value));
    }

    /**
     * Searches for key to overwrite, encrypts value and writes it.
     * @param key value to overwrite
     * @param value value that will be saved
     */
    public void saveValue(String key, Boolean value) {
        file.putString(key, E.encrypt(value));
    }
}
