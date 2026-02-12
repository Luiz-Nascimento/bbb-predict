package br.com.luiz.bbbpredict.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "probability_history")
public class ProbabilityHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contestant_id", nullable = false)
    private Contestant contestant;

    @Column(nullable = false, precision = 5, scale = 4)
    private BigDecimal probability;
    @Column(nullable = false)
    private Instant timestamp;

    public ProbabilityHistory() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Contestant getContestant() {
        return contestant;
    }

    public void setContestant(Contestant contestant) {
        this.contestant = contestant;
    }

    public BigDecimal getProbability() {
        return probability;
    }

    public void setProbability(BigDecimal probability) {
        this.probability = probability;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ProbabilityHistory that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(contestant, that.contestant) && Objects.equals(probability, that.probability) && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contestant, probability, timestamp);
    }
}
