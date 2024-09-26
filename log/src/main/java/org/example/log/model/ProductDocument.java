package org.example.log.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Document("product-history")
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDocument {
    @Id
    private String id;
    @Indexed
    private Long productId;
    private String reason;
    private String name;
    private BigDecimal cost;
    @Indexed(direction = IndexDirection.DESCENDING, expireAfter = "#{@documentDurationTimeout}")
    @CreatedDate
    private Instant createdAt;
}
