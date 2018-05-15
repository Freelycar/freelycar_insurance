package com.freelycar.entity;

public class ClientNOrder {

    private Client client;
    private InsuranceOrder order;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public InsuranceOrder getOrder() {
        return order;
    }

    public void setOrder(InsuranceOrder order) {
        this.order = order;
    }
}
