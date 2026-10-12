package com.engdigest.journalApp.journalrepository;

import com.engdigest.journalApp.entity.JournalEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface JournalEntryRepository extends MongoRepository<JournalEntry, ObjectId> {

}
