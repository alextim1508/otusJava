package com.alextim.server;

import java.util.Arrays;
import java.util.List;

public enum IdClient {

    FRONTEND_1, BACKEND_1,
    FRONTEND_2, BACKEND_2;

    private static List<IdClient> idClients = Arrays.asList(IdClient.values());

    public static boolean isFrontendId(String id) {
        return idClients.contains(IdClient.valueOf(id)) && id.startsWith("FRONTEND");
    }

    public static boolean isBackendId(String id) {
        return idClients.contains(IdClient.valueOf(id)) && id.startsWith("BACKEND");
    }
}
