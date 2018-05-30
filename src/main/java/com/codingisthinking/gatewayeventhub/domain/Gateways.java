package com.codingisthinking.gatewayeventhub.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Gateways.
 */
@Entity
@Table(name = "gateways")
public class Gateways implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "external_ip", nullable = false)
    private String externalIp;

    @NotNull
    @Column(name = "internal_ip", nullable = false)
    private String internalIp;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private String userId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalIp() {
        return externalIp;
    }

    public Gateways externalIp(String externalIp) {
        this.externalIp = externalIp;
        return this;
    }

    public void setExternalIp(String externalIp) {
        this.externalIp = externalIp;
    }

    public String getInternalIp() {
        return internalIp;
    }

    public Gateways internalIp(String internalIp) {
        this.internalIp = internalIp;
        return this;
    }

    public void setInternalIp(String internalIp) {
        this.internalIp = internalIp;
    }

    public String getUserId() {
        return userId;
    }

    public Gateways userId(String userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Gateways gateways = (Gateways) o;
        if (gateways.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), gateways.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Gateways{" +
            "id=" + getId() +
            ", externalIp='" + getExternalIp() + "'" +
            ", internalIp='" + getInternalIp() + "'" +
            ", userId='" + getUserId() + "'" +
            "}";
    }
}
