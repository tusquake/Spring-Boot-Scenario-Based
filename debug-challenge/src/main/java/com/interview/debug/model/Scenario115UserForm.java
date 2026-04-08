package com.interview.debug.model;

import lombok.Data;
import java.io.Serializable;

/**
 * Scenario 115: Form Data Object
 * This is used to demonstrate how Spring MVC binds form parameters
 * to a Java object using @ModelAttribute.
 */
@Data
public class Scenario115UserForm implements Serializable {
    private String username;
    private String email;
    private String favoriteTopic;
}
