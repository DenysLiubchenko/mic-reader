package org.example.log.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductItem implements Serializable {
    private Long cartId;
    private Long productId;
    private Integer quantity;
}
