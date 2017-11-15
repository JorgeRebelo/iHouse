package org.academiadecodigo.enuminatti.ihouse.client.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by codecadet on 15/11/17.
 */
public class ServiceRegistry {

    private static ServiceRegistry registry;
    private Map<String, Service> registryMap = new HashMap<>();

    private ServiceRegistry() {
    }

    public static ServiceRegistry getServiceRegistry() {

        if (registry == null) {
            registry = new ServiceRegistry();
        }

        return registry;

    }

    public Service getService(String sname) {
        return registryMap.get(sname);
    }

    public void registerService(String sname, Service service) {
        registryMap.put(sname, service);
    }

}
