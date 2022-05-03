package advisor.Model;

import java.util.HashMap;

public class DataBase {
    private final HashMap<String, String> categoryMap = new HashMap<>();
    private final static DataBase DATA_BASE = new DataBase();

    private DataBase() {
    }

    public static DataBase geInstance() {
        return DATA_BASE;
    }

    public HashMap<String, String> getCategoryMap() {
        return categoryMap;
    }

    public void addCategory(String key, String value) {
        categoryMap.put(key, value);
    }

    public String getCategoryID(String nameCategory) {
        return categoryMap.get(nameCategory);
    }
}
