package com.covisint.transform.dao;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.covisint.transform.route.RouteDef;

@EnableMongoRepositories
public interface RouteDefDAO {

	/**
	 * Saves the object state in db. create a new document if it does not exist
	 * @param routeDef
	 * @return routeDef
	 */
	public void save(RouteDef routeDef);
	/**
	 * Delete the document from db.
	 * @param routeDef
	 * @return routeDef of deleted object
	 */
	public void delete(RouteDef routeDef);
	/**
	 * Returns route by Id
	 * @param routeDef
	 * @return
	 */
	public RouteDef getById(String routeId);
	
	/**
	 * Returns the routeDef by refname and realm
	 * @param refName
	 * @param realm
	 * @return
	 */
	public RouteDef getByRefName(String refName, String realm);
	
	
	
}
