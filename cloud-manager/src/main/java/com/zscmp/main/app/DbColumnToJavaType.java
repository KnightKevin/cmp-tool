package com.zscmp.main.app;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.util.List;

public class DbColumnToJavaType implements TemplateMethodModelEx {
    @Override
    public Object exec(List arguments) throws TemplateModelException {

        if (arguments.isEmpty()) {
            throw new TemplateModelException("Missing string argument");
        }
        String dbFiledType = arguments.get(0).toString().toLowerCase();

        switch (dbFiledType) {
            case "varchar":
            case "char":
            case "text":
                return "String";
            case "datetime":
                return "Date";
            case "double":
                return "Double";
            case "int":
            case "tinyint":
            case "int unsigned":
                return "Integer";
            case "bit":
                return "Boolean";
            default:
                return "// todo: "+dbFiledType;
        }

    }
}
