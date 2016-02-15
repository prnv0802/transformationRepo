package com.covisint.transform.model;

/**
 * @author PJ00452307 - Pranav Jha
 */
public enum EndPointType {
    HTTP_REST,
    JSON,
    XML, CSV;


    public String value() {
        return name();
    }

    public static EndPointType fromValue(String v) {
        return valueOf(v);
    }
}
