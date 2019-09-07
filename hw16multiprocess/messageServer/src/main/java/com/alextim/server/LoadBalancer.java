package com.alextim.server;

import java.util.*;


public class LoadBalancer {

    private Map<IdClient, Integer> frontendClients = new HashMap<>();
    private Map <IdClient, Integer> backendClients = new HashMap<>();

    public LoadBalancer() {
        for(IdClient id: IdClient.values()) {
            if(id.toString().startsWith("FRONTEND")) {
                frontendClients.put(id, 0);
            }
            else if(id.toString().startsWith("BACKEND")) {
                backendClients.put(id, 0);
            }
        }
    }

    public void requestFrontendCountInc(IdClient idClient){
        Integer count = frontendClients.get(idClient);
        if(count != null)
            frontendClients.replace(idClient, count + 1);
    }

    public void requestBackendCountInc(IdClient idClient){
        Integer count = backendClients.get(idClient);
        if(count != null)
            backendClients.replace(idClient, count + 1);
    }

    public IdClient getFrontendId() {
        ArrayList<Map.Entry<IdClient, Integer>> list = new ArrayList<>(frontendClients.entrySet());
        list.sort(Map.Entry.comparingByValue());
        return list.get(0).getKey();
    }

    public IdClient getBackendId() {
        ArrayList<Map.Entry<IdClient, Integer>> list = new ArrayList<>(backendClients.entrySet());
        list.sort(Map.Entry.comparingByValue());
        return list.get(0).getKey();
    }
}
