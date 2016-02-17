package com.covisint.transform.model;

public class DynamicAttribute extends BaseModel{

	private String label;
	private DynamicAttributeType type;
	private boolean required=false;
	private Object value;
	
	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	/**
	 * @return the required
	 */
	public boolean isRequired() {
		return required;
	}
	/**
	 * @param required the required to set
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}
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
