package com.covisint.transform.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.covisint.transform.dao.RouteDefDAO;
import com.covisint.transform.model.RouteDef;
import com.mongodb.WriteResult;

public class RouteDefDAOImpl implements RouteDefDAO {

	 private static final String COLLECTION = "RouteDef";

	 @Autowired
	 MongoTemplate mongoTemplate;
	 
	 

	 public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	public void create(RouteDef routeDef) {
	  if (routeDef != null) {
	   this.mongoTemplate.insert(routeDef, COLLECTION);
	  }
	 }

	 public RouteDef findById(int id) {
	  Query query = new Query(Criteria.where("_id").is(id));
	  return this.mongoTemplate.findOne(query, RouteDef.class, COLLECTION);
	 }

	 public int deleteById(int id) {

	  Query query = new Query(Criteria.where("_id").is(id));
	  WriteResult result = this.mongoTemplate.remove(query, RouteDef.class,
	    COLLECTION);
	  return result.getN();
	 }

	 public void update(RouteDef routeDef) {
	  if (routeDef != null) {
	   this.mongoTemplate.save(routeDef, COLLECTION);
	  }
	 }

	 public List<RouteDef> findAll() {
	  return (List<RouteDef>) mongoTemplate.findAll(RouteDef.class,
	    COLLECTION);
	 }
}
