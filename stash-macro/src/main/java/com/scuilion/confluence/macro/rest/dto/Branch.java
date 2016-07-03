package com.scuilion.confluence.macro.rest.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class Branch {

    String id;
    String displayId;

}
