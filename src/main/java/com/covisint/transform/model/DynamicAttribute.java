package com.covisint.transform.model;

public class DynamicAttribute extends BaseModel{

	private String label;
	private DynamicAttributeType type;
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * @return the type
	 */
	public DynamicAttributeType getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(DynamicAttributeType type) {
		this.type = type;
	}
	
}
