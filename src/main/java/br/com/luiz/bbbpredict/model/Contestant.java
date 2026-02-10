package br.com.luiz.bbbpredict.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "contestants")
public class Contestant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;
    @Column(nullable = false)
    private String clobTokenId;
    @Column(nullable = false)
    private boolean active = true;

    public Contestant() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClobTokenId() {
        return clobTokenId;
    }

    public void setClobTokenId(String clobTokenId) {
        this.clobTokenId = clobTokenId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Contestant that)) return false;
        return active == that.active && Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(clobTokenId, that.clobTokenId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, clobTokenId, active);
    }
}
