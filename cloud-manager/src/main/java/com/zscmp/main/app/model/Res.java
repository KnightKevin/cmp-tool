package com.zscmp.main.app.model;

import com.zscmp.main.app.Sensitive;

import lombok.Data;

@Data
public class Res {
    private String name;
    
    @Sensitive
    private String password;
    
}
