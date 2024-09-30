package org.example.log.adapters;

import org.example.log.model.CartDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartMongoAdapter extends MongoRepository<CartDocument, String> {
    Optional<CartDocument> findByCartId(Long id);
}
