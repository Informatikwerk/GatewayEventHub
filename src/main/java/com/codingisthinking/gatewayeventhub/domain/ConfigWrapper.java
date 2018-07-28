package com.codingisthinking.gatewayeventhub.domain;

import java.util.List;

public class ConfigWrapper {
    private Gateways gateways;
    private List<Realmkeys> realmkeys;
    private String websocketid;

    public Gateways getGateways() {
        return gateways;
    }

    public void setGateways(Gateways gateways) {
        this.gateways = gateways;
    }

    public List<Realmkeys> getRealmkeys() {
        return realmkeys;
    }

    public void setRealmkeys(List<Realmkeys> realmkeys) {
        this.realmkeys = realmkeys;
    }

    public String getWebsocketid() {
        return websocketid;
    }

    public void setWebsocketid(String websocketid) {
        this.websocketid = websocketid;
    }
}
