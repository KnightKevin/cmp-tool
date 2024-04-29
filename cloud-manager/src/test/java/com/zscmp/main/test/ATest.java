package com.zscmp.main.test;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ATest {

    @Test
    public void aTest() {
        log.info(JSON.toJSONString(new InputParams(), SerializerFeature.WriteMapNullValue));
    }

    
   
    @Setter
    @Getter
    public static class InputParams {
        private String userId;
        private String accountId;
        private String resourceType = "VM";
        private List<WorkOrderItem> workOrderItems = Arrays.asList(new WorkOrderItem());

        // @Getter
        // @Setter
        // public static class Nics {
        //     String vpcId;
        //     String subnetId;
        //     String fixedIp;
        // }

        // @Getter
        // @Setter
        // public static class Disk {
        //     String volumeType;
        //     Long size;
        // }

     
    }

    @Data
    public static class WorkOrderItem {
        private WorkOrderItemConfig workOrderItemConfig = new WorkOrderItemConfig();
    }

    @Data
    public static class WorkOrderItemConfig {
        // private String platformId;
        private String vmName;
        private String originInstanceUuid;
        private List<Network> networks = Arrays.asList(new Network());
        private String rootPassword;
        private int cycleCnt;
        private int cycleType;
        private String acctItemClass;
    }

    @Data
    private static class Network {
        List<CtFixedIp> ct_fixed_ips = Arrays.asList(new CtFixedIp());
        private String uuid;
    }

    @Data
    private static class CtFixedIp {
        private String subnetId;
    }

}