package com.VersatileDataProcessor.RegexManager.repository;

import com.VersatileDataProcessor.RegexManager.models.TumblrPattern;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface TumblrPatternRepository extends MongoRepository<TumblrPattern, String> {
    @Query("{expression: '?0'}")
    TumblrPattern findPatternByExpression(String expression);

    public long count();
}
