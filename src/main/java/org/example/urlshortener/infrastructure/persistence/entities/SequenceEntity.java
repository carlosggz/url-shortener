package org.example.urlshortener.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity(name = "sequences")
@EntityListeners(AuditingEntityListener.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SequenceEntity {
    @Id
    @Column(name = "zone", nullable = false, length = 3)
    private String zone;

    @Column(name = "sequence_value", nullable = false)
    private long sequenceValue;
}
