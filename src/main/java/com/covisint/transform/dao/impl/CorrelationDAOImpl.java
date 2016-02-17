package com.covisint.transform.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.covisint.transform.dao.CorrelationDAO;
import com.covisint.transform.model.Correlation;
import com.mongodb.WriteResult;

public class CorrelationDAOImpl implements CorrelationDAO {

	private static final String COLLECTION = "Correlation";

	@Autowired
	MongoTemplate mongoTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public Correlation<String, Object> getByRefName(String refName, String realm) {
		Correlation<String, Object> correlation = mongoTemplate
				.findOne(Query.query(Criteria.where("refName").is(refName).and("realm").is(realm)), Correlation.class);
		return correlation;
	}

	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@SuppressWarnings("rawtypes")
	public void create(Correlation correlation) {
		if (correlation != null) {
			this.mongoTemplate.insert(correlation, COLLECTION);
		}
	}

	@SuppressWarnings("unchecked")
	public Correlation<String, Object> findById(int id) {
		Query query = new Query(Criteria.where("_id").is(id));
		return this.mongoTemplate.findOne(query, Correlation.class, COLLECTION);
	}

	public int deleteById(int id) {

		Query query = new Query(Criteria.where("_id").is(id));
		WriteResult result = this.mongoTemplate.remove(query, Correlation.class, COLLECTION);
		return result.getN();
	}

	public void update(Correlation<String, Object> correlation) {
		if (correlation != null) {
			this.mongoTemplate.save(correlation, COLLECTION);
		}
	}

	@SuppressWarnings("rawtypes")
	public List<Correlation> findAll() {
		return (List<Correlation>) mongoTemplate.findAll(Correlation.class, COLLECTION);
	}

}
