package com.zscmp.main.app;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.util.List;

public class ToCamelCaseMethod implements TemplateMethodModelEx {
    @Override
    public Object exec(List arguments) throws TemplateModelException {

        if (arguments.isEmpty()) {
            throw new TemplateModelException("Missing string argument");
        }
        String str = arguments.get(0).toString();
        StringBuilder result = new StringBuilder();
        boolean nextUpper = false;

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '_') {
                nextUpper = true;
            } else {
                if (nextUpper) {
                    result.append(Character.toUpperCase(c));
                    nextUpper = false;
                } else {
                    result.append(i == 0 ? Character.toLowerCase(c) : c);
                }
            }
        }
        return result.toString();

    }
}
