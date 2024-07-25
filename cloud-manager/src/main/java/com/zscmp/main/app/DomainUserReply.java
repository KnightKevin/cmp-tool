package com.zscmp.main.app;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DomainUserReply extends UserReply {
    //user+vdc+role信息
    private DomainUserBinding binding;
}
