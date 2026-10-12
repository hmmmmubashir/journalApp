package com.engdigest.journalApp.journalrepository;

import com.engdigest.journalApp.entity.ConfigEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JournalConfigRepository extends MongoRepository<ConfigEntity, ObjectId> {
}
