package com.covisint.transform.model;

import java.io.Serializable;
import java.util.LinkedHashMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A model class for Scripts.
 * 
 * @author PJ00452307
 *
 */
@Document
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ScriptType", propOrder = { "id", "inputs", "outputs", "scriptContextObjects" })
public class ScriptType extends BaseModel implements Serializable {

	protected String id;

	protected DynamicAttributeSet inputs;

	protected DynamicAttributeSet outputs;

	protected LinkedHashMap<String, ScriptContextObject> scriptContextObjects;

	/**
	 * Returns unique id.
	 *
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets unique id.
	 *
	 * @return
	 */
	public void setId(String id) {
		this.id = id;
	}

	

	/**
	 * @return the inputs
	 */
	public DynamicAttributeSet getInputs() {
		return inputs;
	}

	/**
	 * @param inputs the inputs to set
	 */
	public void setInputs(DynamicAttributeSet inputs) {
		this.inputs = inputs;
	}

	/**
	 * @return the outputs
	 */
	public DynamicAttributeSet getOutputs() {
		return outputs;
	}

	/**
	 * @param outputs the outputs to set
	 */
	public void setOutputs(DynamicAttributeSet outputs) {
		this.outputs = outputs;
	}

	/**
	 * @param scriptContextObjects the scriptContextObjects to set
	 */
	public void setScriptContextObjects(LinkedHashMap<String, ScriptContextObject> scriptContextObjects) {
		this.scriptContextObjects = scriptContextObjects;
	}

	public LinkedHashMap<String, ScriptContextObject> getScriptContextObjects() {
		if (scriptContextObjects == null) {
			scriptContextObjects = new LinkedHashMap<String, ScriptContextObject>();
		}

		return scriptContextObjects;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((inputs == null) ? 0 : inputs.hashCode());
		result = prime * result + ((outputs == null) ? 0 : outputs.hashCode());
		result = prime * result + ((scriptContextObjects == null) ? 0 : scriptContextObjects.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScriptType other = (ScriptType) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (inputs == null) {
			if (other.inputs != null)
				return false;
		} else if (!inputs.equals(other.inputs))
			return false;
		if (outputs == null) {
			if (other.outputs != null)
				return false;
		} else if (!outputs.equals(other.outputs))
			return false;
		if (scriptContextObjects == null) {
			if (other.scriptContextObjects != null)
				return false;
		} else if (!scriptContextObjects.equals(other.scriptContextObjects))
			return false;
		return true;
	}

}
