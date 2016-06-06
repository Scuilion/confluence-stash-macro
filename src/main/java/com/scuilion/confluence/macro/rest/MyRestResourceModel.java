package com.scuilion.confluence.macro.rest;

import javax.xml.bind.annotation.*;
@XmlRootElement(name = "message")
@XmlAccessorType(XmlAccessType.FIELD)
public class MyRestResourceModel {

    @XmlAttribute
    private String key;


    @XmlElement(name = "value")
    private String message;

    public MyRestResourceModel() {
    }

    public MyRestResourceModel(String key, String message) {
        this.key = key;
        this.message = message;
    }
 
    public String getKey() {
        return key;
    }
 
    public void setKey(String key) {
        this.key = key;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}