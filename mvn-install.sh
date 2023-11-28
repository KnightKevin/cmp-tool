#!/bin/bash


# 遍历需要执行 mvn install 的文件夹
for dir in base-libs base-server; do
    # 进入文件夹
    cd "$dir"
    
    # 执行 mvn install
    mvn install
    
    # 返回上级目录
    cd ..
done
