package com.kafka.sandbox.dto;
import lombok.Data;

@Data
public class ClusterHealth {

    private int brokerCount;
    private int underReplicatedPartitions;
    private boolean controllerAvailable;

    public ClusterHealth(int brokerCount, int underReplicatedPartitions, boolean controllerAvailable) {
        this.brokerCount = brokerCount;
        this.underReplicatedPartitions = underReplicatedPartitions;
        this.controllerAvailable = controllerAvailable;
    }

    // getters + setters
}
