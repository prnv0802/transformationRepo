package com.covisint.transform.dao;

import com.covisint.transform.model.Correlation;

public interface CorrelationDAO {

	public Correlation<String, Object> getByRefName(String refName, String realm);
}
