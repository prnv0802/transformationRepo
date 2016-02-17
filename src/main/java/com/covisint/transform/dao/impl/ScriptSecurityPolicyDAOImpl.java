package com.covisint.transform.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.covisint.transform.dao.ScriptSecurityPolicyDAO;
import com.covisint.transform.model.ScriptSecurityPolicy;
import com.mongodb.WriteResult;

public class ScriptSecurityPolicyDAOImpl implements ScriptSecurityPolicyDAO {

	private static final String COLLECTION = "ScriptSecurityPolicy";

	@Autowired
	MongoTemplate mongoTemplate;

	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public ScriptSecurityPolicy getByRefName(String refName, String realm) {
		ScriptSecurityPolicy scriptSecurityPolicy = mongoTemplate.findOne(
				Query.query(Criteria.where("refName").is(refName).and("realm").is(realm)), ScriptSecurityPolicy.class);
		return scriptSecurityPolicy;
	}

	public void create(ScriptSecurityPolicy scriptSecurityPolicy) {
		if (scriptSecurityPolicy != null) {
			this.mongoTemplate.insert(scriptSecurityPolicy, COLLECTION);
		}
	}

	public ScriptSecurityPolicy findById(String id) {
		Query query = new Query(Criteria.where("_id").is(id));
		return this.mongoTemplate.findOne(query, ScriptSecurityPolicy.class, COLLECTION);
	}

	public int deleteById(int id) {

		Query query = new Query(Criteria.where("_id").is(id));
		WriteResult result = this.mongoTemplate.remove(query, ScriptSecurityPolicy.class, COLLECTION);
		return result.getN();
	}

	public void update(ScriptSecurityPolicy scriptSecurityPolicy) {
		if (scriptSecurityPolicy != null) {
			this.mongoTemplate.save(scriptSecurityPolicy, COLLECTION);
		}
	}

	public List<ScriptSecurityPolicy> findAll() {
		return (List<ScriptSecurityPolicy>) mongoTemplate.findAll(ScriptSecurityPolicy.class, COLLECTION);
	}

}
