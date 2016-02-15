package com.covisint.transform.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Route Definition
 * @author PJ00452307
 *
 */
@Document
public class RouteDef extends BaseModel{
	
	@Id
	private String id;
	private String payload;
	private String refName;
	private String preProcessorRefName;
	private String parserRefName;
	private String mapperRefName;
	private String packagerRefName;
	private String postProcessorRefName;
	
	private String runtimeArgs;
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getPreProcessorRefName() {
		return preProcessorRefName;
	}

	public void setPreProcessorRefName(String preProcessorRefName) {
		this.preProcessorRefName = preProcessorRefName;
	}

	public String getParserRefName() {
		return parserRefName;
	}

	public void setParserRefName(String parserRefName) {
		this.parserRefName = parserRefName;
	}

	public String getMapperRefName() {
		return mapperRefName;
	}

	public void setMapperRefName(String mapperRefName) {
		this.mapperRefName = mapperRefName;
	}

	public String getPackagerRefName() {
		return packagerRefName;
	}

	public void setPackagerRefName(String packagerRefName) {
		this.packagerRefName = packagerRefName;
	}

	public String getPostProcessorRefName() {
		return postProcessorRefName;
	}

	public void setPostProcessorRefName(String postProcessorRefName) {
		this.postProcessorRefName = postProcessorRefName;
	}

	public String getRuntimeArgs() {
		return runtimeArgs;
	}

	public void setRuntimeArgs(String runtimeArgs) {
		this.runtimeArgs = runtimeArgs;
	}

	public String getRefName() {
		return refName;
	}

	public void setRefName(String refName) {
		this.refName = refName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((mapperRefName == null) ? 0 : mapperRefName.hashCode());
		result = prime * result + ((packagerRefName == null) ? 0 : packagerRefName.hashCode());
		result = prime * result + ((parserRefName == null) ? 0 : parserRefName.hashCode());
		result = prime * result + ((payload == null) ? 0 : payload.hashCode());
		result = prime * result + ((postProcessorRefName == null) ? 0 : postProcessorRefName.hashCode());
		result = prime * result + ((preProcessorRefName == null) ? 0 : preProcessorRefName.hashCode());
		result = prime * result + ((refName == null) ? 0 : refName.hashCode());
		result = prime * result + ((runtimeArgs == null) ? 0 : runtimeArgs.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		RouteDef other = (RouteDef) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (mapperRefName == null) {
			if (other.mapperRefName != null)
				return false;
		} else if (!mapperRefName.equals(other.mapperRefName))
			return false;
		if (packagerRefName == null) {
			if (other.packagerRefName != null)
				return false;
		} else if (!packagerRefName.equals(other.packagerRefName))
			return false;
		if (parserRefName == null) {
			if (other.parserRefName != null)
				return false;
		} else if (!parserRefName.equals(other.parserRefName))
			return false;
		if (payload == null) {
			if (other.payload != null)
				return false;
		} else if (!payload.equals(other.payload))
			return false;
		if (postProcessorRefName == null) {
			if (other.postProcessorRefName != null)
				return false;
		} else if (!postProcessorRefName.equals(other.postProcessorRefName))
			return false;
		if (preProcessorRefName == null) {
			if (other.preProcessorRefName != null)
				return false;
		} else if (!preProcessorRefName.equals(other.preProcessorRefName))
			return false;
		if (refName == null) {
			if (other.refName != null)
				return false;
		} else if (!refName.equals(other.refName))
			return false;
		if (runtimeArgs == null) {
			if (other.runtimeArgs != null)
				return false;
		} else if (!runtimeArgs.equals(other.runtimeArgs))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RouteDef [id=" + id + ", payload=" + payload + ", refName=" + refName + ", preProcessorRefName="
				+ preProcessorRefName + ", parserRefName=" + parserRefName + ", mapperRefName=" + mapperRefName
				+ ", packagerRefName=" + packagerRefName + ", postProcessorRefName=" + postProcessorRefName
				+ ", runtimeArgs=" + runtimeArgs + "]";
	}

	
}
