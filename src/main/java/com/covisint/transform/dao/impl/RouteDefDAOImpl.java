package com.covisint.transform.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.covisint.transform.dao.RouteDefDAO;
import com.covisint.transform.route.RouteDef;

public class RouteDefDAOImpl implements RouteDefDAO {

	@Autowired
	protected MongoTemplate mongoTemplate;

	public void save(RouteDef routeDef) {

		mongoTemplate.save(routeDef);
	}

	public void delete(RouteDef routeDef) {
		mongoTemplate.remove(routeDef);
	}

	public RouteDef getById(String routeId) {
		RouteDef routeDef = (RouteDef) mongoTemplate.find(Query.query(Criteria.where("_id").is(routeId)),
				RouteDef.class);
		return routeDef;
	}

	public RouteDef getByRefName(String refName, String realm) {
		RouteDef routeDef = (RouteDef) mongoTemplate
				.find(Query.query(Criteria.where("refName").is(refName).and("realm").is(realm)), RouteDef.class);
		return routeDef;
	}

}
