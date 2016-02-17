package com.covisint.transform.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.covisint.transform.dao.ScriptDAO;
import com.covisint.transform.dao.ScriptSecurityPolicyDAO;
import com.covisint.transform.model.Script;
import com.mongodb.WriteResult;

public class ScriptDAOImpl implements ScriptDAO {

	private static final String COLLECTION = "Script";

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	ScriptSecurityPolicyDAO scriptSecurityPolicyDAO;

	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public Script getByRefName(String refName, String realm) {
		Query query = Query.query(Criteria.where("refName").is(refName).and("realm").is(realm));
		System.out.println(query.toString());
		Script script = mongoTemplate.findOne(query, Script.class);
		/*if (script != null) {
			ScriptSecurityPolicy policy = scriptSecurityPolicyDAO.findById(script.getScriptSecurityPolicyId());
			if (policy != null) {
				// hack to get the security policy, security policy can be made
				// DBRef to pull it out
				script.setScriptSecurityPolicy(policy);
			}
		}*/
		return script;
	}

	public void create(Script script) {
		if (script != null) {
			this.mongoTemplate.insert(script, COLLECTION);
		}
	}

	public Script findById(int id) {
		Query query = new Query(Criteria.where("_id").is(id));
		return this.mongoTemplate.findOne(query, Script.class, COLLECTION);
	}

	public int deleteById(int id) {

		Query query = new Query(Criteria.where("_id").is(id));
		WriteResult result = this.mongoTemplate.remove(query, Script.class, COLLECTION);
		return result.getN();
	}

	public void update(Script script) {
		if (script != null) {
			this.mongoTemplate.save(script, COLLECTION);
		}
	}

	public List<Script> findAll() {
		return (List<Script>) mongoTemplate.findAll(Script.class, COLLECTION);
	}

}
