package ${entityPackage}.querydsl;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import ${entityPackage}.${className};

import javax.annotation.Generated;

import java.math.BigDecimal;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * Q${className} is a Querydsl query type for ${className}
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class Q${className} extends EntityPathBase<${className}> {

    private static final long serialVersionUID = 2104313057L;

    public static final Q${className} ${tableClassVarName} = new Q${className}("${tableClassVarName}");

<#list list as i>
   ${qFieldBuild(i.type, i.name)}
</#list>


    public Q${className}(String variable) {
        super(${className}.class, forVariable(variable));
    }

    public Q${className}(Path<? extends ${className}> path) {
        super(path.getType(), path.getMetadata());
    }

    public Q${className}(PathMetadata metadata) {
        super(${className}.class, metadata);
    }

}

