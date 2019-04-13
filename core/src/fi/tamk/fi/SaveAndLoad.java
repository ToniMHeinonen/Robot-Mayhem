package fi.tamk.fi;

import com.badlogic.gdx.Preferences;

public class SaveAndLoad {
    Encryptor E;
    Preferences file;

    SaveAndLoad(Encryptor E, Preferences file) {
        this.E = E;
        this.file = file;
    }

    public String loadValue(String key, String defValue) {
        return E.decrypt(file.getString(key, E.encrypt(defValue)));
    }

    public Integer loadValue(String key, Integer defValue) {
        return E.decrypt(file.getInteger(key, E.encrypt(defValue)));
    }

    public Float loadValue(String key, Float defValue) {
        return E.decrypt(file.getFloat(key, E.encrypt(defValue)));
    }

    public Boolean loadValue(String key, Boolean defValue) {
        return E.decrypt(file.getBoolean(key, E.encrypt(defValue)));
    }

    public void saveValue(String key, String value) {
        file.putString(key, E.encrypt(value));
    }

    public void saveValue(String key, Integer value) {
        file.putInteger(key, E.encrypt(value));
    }

    public void saveValue(String key, Float value) {
        file.putFloat(key, E.encrypt(value));
    }

    public void saveValue(String key, Boolean value) {
        file.putBoolean(key, E.encrypt(value));
    }
}
