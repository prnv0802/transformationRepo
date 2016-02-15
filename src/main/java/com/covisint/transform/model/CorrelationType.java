package com.covisint.transform.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * @author PJ00452307 - Pranav Jha
 */
@XmlType(name = "CorrelationType")
@XmlEnum
public enum CorrelationType {
    /** A One to Many Relationship **/
    OneToMany,
    /** A Many to One Relationship **/
    ManyToOne,
    /** A One to One Relationship **/
    OneToOne,
    /** A Many to Many Relationship **/
    ManyToMany;

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
    public static CorrelationType fromValue(String v) {
        return valueOf(v);
    }

}
