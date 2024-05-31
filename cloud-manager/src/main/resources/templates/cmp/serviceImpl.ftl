package ${module}.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zscmp.infrastructure.db.BaseRepository;
import com.zscmp.infrastructure.db.JpaService;
import com.zscmp.infrastructure.utils.Util;
import ${module}.entity.${className};
import ${module}.entity.querydsl.Q${className};
import ${module}.model.req.${className}Qry;
import ${module}.service.I${className}Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class ${className}ServiceImpl extends JpaService implements I${className}Service {

    private static final Q${className} q${className} = Q${className}.${tableClassVarName};
    
    private JPAQueryFactory queryFactory;
    private BaseRepository<${className}, String> repository;
    
    
    @PostConstruct
    private void init() {
        queryFactory = getQueryFactory();
        repository = getRepository(${className}.class);
    }

    @Override
    public QueryResults<${className}> pageList(${className}Qry qry) {
        BooleanBuilder builder = new BooleanBuilder();
        if (!Util.isEmpty(qry.getUuid())) {
            builder.and(q${className}.uuid.eq(qry.getUuid()));
        }


        PathBuilder<${className}> orderByExpression = new PathBuilder<>(${className}.class, "${tableClassVarName}");
        OrderSpecifier<?> sortOrder = new OrderSpecifier("desc".equalsIgnoreCase(qry.getSort()) ? Order.DESC : Order.ASC, orderByExpression.get(qry.getOrder()));

        return queryFactory.selectFrom(q${className})
                .where(builder)
                .offset(qry.getOffset())
                .limit(qry.getLimit())
                .orderBy(sortOrder)
                .fetchResults();
    }

    @Override
    public ${className} getByUuid(String uuid) {
        return queryFactory.selectFrom(q${className})
                .where(q${className}.uuid.eq(uuid))
                .fetchFirst();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ${className} create(${className} entity) {
        if (Util.isNotEmpty(entity.getUuid())) {
            throw new RuntimeException("uuid must be null");
        }

        ${className} result = repository.saveAndFlush(entity);

        log.info("${className} create [{}]", JSONObject.toJSONString(result));

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ${className} update(${className} entity) {

        if (Util.isEmpty(entity.getUuid())) {
            throw new RuntimeException("uuid must not be null");
        }

        ${className} result = repository.saveAndFlush(entity);

        log.info("${className} update [{}]", JSONObject.toJSONString(result));

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long delete(String[] uuids) {
       return queryFactory.delete(q${className})
                   .where(q${className}.uuid.in(uuids))
                   .execute();
    }
}
