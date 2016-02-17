package com.covisint.transform.dao;

import java.util.List;

import com.covisint.transform.model.ScriptSecurityPolicy;

public interface ScriptSecurityPolicyDAO {

	/**
	 * Fetches the ScriptSecurityPolicy by refName in a realm
	 * @param refName unique name of ScriptType
	 * @param realm realm for which this ScriptType is valid
	 * @return returns ScriptType
	 */
	ScriptSecurityPolicy getByRefName(String refName, String realm);
	
	public void create(ScriptSecurityPolicy scriptSecurityPolicy);

	 public void update(ScriptSecurityPolicy scriptSecurityPolicy);

	 public int deleteById(int id);

	 public ScriptSecurityPolicy findById(String string);

	 public List<ScriptSecurityPolicy> findAll();
}
