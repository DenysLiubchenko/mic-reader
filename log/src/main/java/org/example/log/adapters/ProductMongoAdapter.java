package org.example.log.adapters;

import org.example.log.model.ProductDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductMongoAdapter extends MongoRepository<ProductDocument, String> {
    Optional<ProductDocument> findByProductId(Long id);
}
