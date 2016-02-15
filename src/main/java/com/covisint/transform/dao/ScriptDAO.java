package com.covisint.transform.dao;

import java.util.List;

import com.covisint.transform.model.RouteDef;
import com.covisint.transform.model.Script;

public interface ScriptDAO {

	public void create(RouteDef routeDef);

	 public void update(RouteDef routeDef);

	 public int deleteById(int id);

	 public RouteDef findById(int id);

	 public List<RouteDef> findAll();

	public Script getByRefName(String scriptName, String realm);
}
