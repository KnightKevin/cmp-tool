package com.zscmp.main.app;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.util.List;

import com.zscmp.main.app.model.DbField;

import cn.hutool.core.util.StrUtil;

public class QFieldBuild implements TemplateMethodModelEx {
    @Override
    public Object exec(List arguments) throws TemplateModelException {

        if (arguments.isEmpty()) {
            throw new TemplateModelException("Missing string argument");
        }
        String type = arguments.get(0).toString();
        String name = arguments.get(1).toString();


        String fieldName = StrUtil.toCamelCase(name);
        switch (type.toLowerCase()) {
            case "varchar":
            case "char":
            case "text":
            case "mediumtext":
                return buildFieldStr(fieldName);
            case "datetime":
                return buildFieldDateTime(fieldName);
            case "double":
                return buildFieldDouble(fieldName);
            case "int":
            case "tinyint":
            case "int unsigned":
                return buildFieldInt(fieldName);
            case "bigint":
                return buildFieldLong(fieldName);
            case "bit":
                return buildFieldBoolean(fieldName);
            case "decimal":
                return buildFieldDecimal(fieldName);
            default:
                return "// todo: "+type;
        }

    }

    private String buildFieldStr(String fieldName) {
        String t = """
            public final StringPath %s = createString("%s");
                """;;


        return String.format(t, fieldName, fieldName);
    }

    private String buildFieldInt(String fieldName) {
        String t = """
            public final NumberPath<Integer> %s = createNumber("%s", Integer.class);
                """;;


        return String.format(t, fieldName, fieldName);
    }

    private String buildFieldLong(String fieldName) {
        String t = """
            public final NumberPath<Long> %s = createNumber("%s", Long.class);
                """;;


        return String.format(t, fieldName, fieldName);
    }

    private String buildFieldDouble(String fieldName) {
        String t = """
            public final NumberPath<Double> %s = createNumber("%s", Double.class);
                """;;


        return String.format(t, fieldName, fieldName);
    }

    private String buildFieldDecimal(String fieldName) {
        String t = """
            public final NumberPath<BigDecimal> %s = createNumber("%s", BigDecimal.class);
                """;;


        return String.format(t, fieldName, fieldName);
    }

    private String buildFieldDateTime(String fieldName) {
        String t = """
            public final DateTimePath<java.util.Date> %s = createDateTime("%s", java.util.Date.class);
                """;;


        return String.format(t, fieldName, fieldName);
    }

    private String buildFieldBoolean(String fieldName) {
        String t = """
            public final BooleanPath %s = createBoolean("%s");
                """;;


        return String.format(t, fieldName, fieldName);
    }
}
