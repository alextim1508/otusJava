package com.alextim.server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LoadBalancerTest {

    @Test
    public void test() {
        LoadBalancer balancer = new LoadBalancer();

        for(int i =0; i<1; i++)
            balancer.requestFrontendCountInc(IdClient.FRONTEND_1);
        for(int i =0; i<2; i++)
            balancer.requestFrontendCountInc(IdClient.FRONTEND_2);
        for(int i =0; i<3; i++)
            balancer.requestBackendCountInc(IdClient.BACKEND_1);
        for(int i =0; i<4; i++)
            balancer.requestBackendCountInc(IdClient.BACKEND_2);

        Assertions.assertEquals(IdClient.FRONTEND_1, balancer.getFrontendId());
        Assertions.assertEquals(IdClient.BACKEND_1, balancer.getBackendId());
    }
}
