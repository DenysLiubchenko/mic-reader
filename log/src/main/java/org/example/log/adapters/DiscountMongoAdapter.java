package org.example.log.adapters;

import org.example.log.model.DiscountDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountMongoAdapter extends MongoRepository<DiscountDocument, String> {
}
