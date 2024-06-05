#!/bin/bash

# 用于忽略本地开发环境中配置文件的修改，效果就是修改了配置文件，执行git status时不会出现在待提交中（注意区别直接忽略）

# 获取当前目录
dirs=$(ls -l | grep '^d' | awk '{print $9}')


# 列出当前目录下的所有文件夹
for folder in $dirs; do
    # 检查文件夹是否是一个Git项目
    cd $folder

    if [ -d ".git" ]; then
        case $folder in
        "basic-libs")
        # skip
        ;;
        "gateway-server")
            git update-index --assume-unchanged src/main/resources/application-local.yml
        ;;
        *)
            git update-index --assume-unchanged starter/src/main/resources/application-local.yml
        ;;
        esac 

    fi
    cd ..
done
