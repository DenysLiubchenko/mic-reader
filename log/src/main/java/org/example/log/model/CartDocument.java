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

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Document("cart-history")
@Getter
@Setter
@EqualsAndHashCode(exclude = {"products", "discounts"})
@ToString(exclude = {"products", "discounts"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDocument {
    @Id
    private String id;
    @Indexed
    private Long cartId;
    private String reason;
    private Set<ProductItem> products = new HashSet<>();
    private Set<String> discounts = new HashSet<>();
    @Indexed(direction = IndexDirection.DESCENDING, expireAfter = "#{@documentDurationTimeout}")
    @CreatedDate
    private Instant createdAt;
}
