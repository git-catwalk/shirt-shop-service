package com.bluntsoftware.shirtshop.repository;

import com.bluntsoftware.shirtshop.model.SequenceId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class SequenceRepo {

    private final MongoTemplate mongoTemplate;
    public SequenceRepo( MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Long getNextSequenceId(String key)  {

        Query query = new Query(Criteria.where("id").is(key));

        Update update = new Update();
        update.inc("seq", 1);

        FindAndModifyOptions options = new FindAndModifyOptions();
        options.returnNew(true);

        SequenceId seqId = mongoTemplate.findAndModify(query, update, options, SequenceId.class);

        if (seqId == null) {
            seqId = mongoTemplate.save(SequenceId.builder().seq(1).id(key).build());
        }
        return seqId.getSeq();
    }
}
