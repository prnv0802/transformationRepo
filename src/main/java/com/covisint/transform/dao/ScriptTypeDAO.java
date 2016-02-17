package com.covisint.transform.dao;

import java.util.List;

import com.covisint.transform.model.ScriptType;

/**
 * 
 * @author PJ00452307 - Pranav Jha
 *
 */
public interface ScriptTypeDAO {

	/**
	 * Fetches the ScriptType by refName in a realm
	 * @param refName unique name of ScriptType
	 * @param realm realm for which this ScriptType is valid
	 * @return returns ScriptType
	 */
	ScriptType getByRefName(String refName, String realm);
	
	public void create(ScriptType scriptType);

	 public void update(ScriptType scriptType);

	 public int deleteById(int id);

	 public ScriptType findById(int id);

	 public List<ScriptType> findAll();

}
