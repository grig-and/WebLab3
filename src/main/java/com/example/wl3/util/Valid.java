package com.example.wl3.util;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

import java.util.Map;

@FacesValidator("valid")
public class Valid implements Validator {
    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        Double min;
        Double max;
        Double d;

        Map<String, Object> attributes = uiComponent.getAttributes();
        try {
            d = (Double) o;
            min = Double.parseDouble((String) attributes.get("min"));
            max = Double.parseDouble((String) attributes.get("max"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ValidatorException(new FacesMessage("Invalid number format"));
        }
        if (d == null || d.compareTo(min) < 0 || d.compareTo(max) > 0) {
            throw new ValidatorException(new FacesMessage("Value is out of range: [" + min + ", " + max + "]"));
        }
    }
}