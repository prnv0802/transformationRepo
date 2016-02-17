package com.covisint.transform.dao;

import java.util.List;

import com.covisint.transform.model.Script;

public interface ScriptDAO {

	public void create(Script script);

	 public void update(Script script);

	 public int deleteById(int id);

	 public Script findById(int id);

	 public List<Script> findAll();

	public Script getByRefName(String scriptName, String realm);
}
