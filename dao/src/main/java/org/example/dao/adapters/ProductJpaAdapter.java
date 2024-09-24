package org.example.dao.adapters;

import org.example.dao.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductJpaAdapter extends JpaRepository<ProductEntity, Long> {
    @Query("SELECT pi.id.cartId FROM ProductEntity p left join p.productItems pi where p.name ilike '%' || :name || '%'")
    List<Long> findAllCartIdsWithProductByName(String name);
}
