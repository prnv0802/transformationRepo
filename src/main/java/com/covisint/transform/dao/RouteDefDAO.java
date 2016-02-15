package com.covisint.transform.dao;

import java.util.List;

import com.covisint.transform.model.RouteDef;

public interface RouteDefDAO {

	public void create(RouteDef routeDef);

	 public void update(RouteDef routeDef);

	 public int deleteById(int id);

	 public RouteDef findById(int id);

	 public List<RouteDef> findAll();

	
}
