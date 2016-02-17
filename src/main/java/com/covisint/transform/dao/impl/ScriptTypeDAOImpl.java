package com.covisint.transform.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.covisint.transform.dao.ScriptTypeDAO;
import com.covisint.transform.model.ScriptType;
import com.mongodb.WriteResult;

public class ScriptTypeDAOImpl implements ScriptTypeDAO {

	private static final String COLLECTION = "ScriptType";

	@Autowired
	MongoTemplate mongoTemplate;

	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public ScriptType getByRefName(String refName, String realm) {
		ScriptType scriptType = mongoTemplate
				.findOne(Query.query(Criteria.where("refName").is(refName).and("realm").is(realm)), ScriptType.class);
		return scriptType;
	}

	public void create(ScriptType scriptType) {
		if (scriptType != null) {
			this.mongoTemplate.insert(scriptType, COLLECTION);
		}
	}

	public ScriptType findById(int id) {
		Query query = new Query(Criteria.where("_id").is(id));
		return this.mongoTemplate.findOne(query, ScriptType.class, COLLECTION);
	}

	public int deleteById(int id) {

		Query query = new Query(Criteria.where("_id").is(id));
		WriteResult result = this.mongoTemplate.remove(query, ScriptType.class, COLLECTION);
		return result.getN();
	}

	public void update(ScriptType scriptType) {
		if (scriptType != null) {
			this.mongoTemplate.save(scriptType, COLLECTION);
		}
	}

	public List<ScriptType> findAll() {
		return (List<ScriptType>) mongoTemplate.findAll(ScriptType.class, COLLECTION);
	}

}
