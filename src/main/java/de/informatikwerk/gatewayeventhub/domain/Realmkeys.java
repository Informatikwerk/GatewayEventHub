package de.informatikwerk.gatewayeventhub.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Realmkeys.
 */
@Entity
@Table(name = "realmkeys")
public class Realmkeys implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "realmkey", nullable = false)
    private String realmkey;

    @ManyToOne(optional = false)
    @NotNull
    private Gateways gateways;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRealmkey() {
        return realmkey;
    }

    public Realmkeys realmkey(String realmkey) {
        this.realmkey = realmkey;
        return this;
    }

    public void setRealmkey(String realmkey) {
        this.realmkey = realmkey;
    }

    public Gateways getGateways() {
        return gateways;
    }

    public Realmkeys gateways(Gateways gateways) {
        this.gateways = gateways;
        return this;
    }

    public void setGateways(Gateways gateways) {
        this.gateways = gateways;
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
        Realmkeys realmkeys = (Realmkeys) o;
        if (realmkeys.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), realmkeys.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Realmkeys{" +
            "id=" + getId() +
            ", realmkey='" + getRealmkey() + "'" +
            "}";
    }
}
