//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.02.13 at 12:09:41 PM EST 
//


package org.jboss.mapper.camel.blueprint;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for loggingLevel.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="loggingLevel">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="DEBUG"/>
 *     &lt;enumeration value="WARN"/>
 *     &lt;enumeration value="OFF"/>
 *     &lt;enumeration value="INFO"/>
 *     &lt;enumeration value="ERROR"/>
 *     &lt;enumeration value="TRACE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "loggingLevel")
@XmlEnum
public enum LoggingLevel {

    DEBUG,
    WARN,
    OFF,
    INFO,
    ERROR,
    TRACE;

    public String value() {
        return name();
    }

    public static LoggingLevel fromValue(String v) {
        return valueOf(v);
    }

}