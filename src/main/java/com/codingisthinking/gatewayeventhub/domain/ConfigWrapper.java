package com.codingisthinking.gatewayeventhub.domain;

import java.util.List;

public class ConfigWrapper {
    private Gateways gateways;
    private List<String> realmkeys;

    public Gateways getGateways() {
        return gateways;
    }

    public void setGateways(Gateways gateways) {
        this.gateways = gateways;
    }

    public List<String> getRealmkeys() {
        return realmkeys;
    }

    public void setRealmkeys(List<String> realmkeys) {
        this.realmkeys = realmkeys;
    }
}
