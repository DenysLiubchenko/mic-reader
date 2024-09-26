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

@Document("discount-history")
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiscountDocument {
    @Id
    private String id;
    @Indexed
    private String code;
    private String reason;
    private Instant due;
    @Indexed(direction = IndexDirection.DESCENDING, expireAfter = "#{@documentDurationTimeout}")
    @CreatedDate
    private Instant createdAt;
}
