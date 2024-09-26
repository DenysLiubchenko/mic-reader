package org.example.log.adapters;

import org.example.log.model.CartDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartMongoAdapter extends MongoRepository<CartDocument, String> {
}
