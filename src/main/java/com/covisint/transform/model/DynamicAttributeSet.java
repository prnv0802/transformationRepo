package com.covisint.transform.model;

import java.util.HashMap;
import java.util.Map;

public class DynamicAttributeSet {

	private Map<String, DynamicAttribute> attributes;

	/**
	 * @return the attributes
	 */
	public Map<String, DynamicAttribute> getAttributes() {
		if(attributes == null)
		{
			attributes = new HashMap<String, DynamicAttribute>();
		}
		return attributes;
	}

	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(Map<String, DynamicAttribute> attributes) {
		this.attributes = attributes;
	}
	
	

	
	
}
