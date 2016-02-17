package com.covisint.transform.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 * @author PJ00452307 - Pranav Jha
 *
 */
@XmlType(name = "DynamicAttributeType")
@XmlEnum
public enum DynamicAttributeType {

	String, Text, Object;
	
	 /**
     * Get the corresponding String value
     * @return    The sTring value
     */
    public String value() {
        return name();
    }

    /**
     * Convert string to enum
     * @param v        the value we want converted
     * @return         the string value
     */
    public static DynamicAttributeType fromValue(String v) {
        return valueOf(v);
    }
	
}
