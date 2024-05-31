package com.zscmp.resource.model.req;

import com.zscmp.common.model.Pagination;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ${className}Qry extends Pagination {
    private String uuid;
    private String name;

    public ${className}Qry() {
        super();
    }

    public ${className}Qry(Long offset, Long limit, String order, String sort) {
        super(offset, limit, order, sort);
    }
}
