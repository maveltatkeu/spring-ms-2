package com.thecodealchemist.main.db;

import java.util.Map;

/**
 * Represents a store where the keys will be stored e.g. DB table or any other solution.
 */
public class KeyStore {
    private static final Map<String, String> DB = Map.of(
            "john", "$2a$10$O.xWv.Z2f2YlhDlTTmGgve8syjzGKFhnSEUHwIIpkOehuqIZkEroy"
    );

    public static String getKeyByOwner(String owner) {
        return DB.get(owner);
    }
}
