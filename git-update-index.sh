#!/bin/bash

# 用于忽略本地开发环境中配置文件的修改，效果就是修改了配置文件，执行git status时不会出现在待提交中（注意区别直接忽略）

# 获取当前目录
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"


# 列出当前目录下的所有文件夹
for folder in "$DIR"/*; do
    if [ -d "$folder" ]; then
        cd "$folder" || exit

        # 检查文件夹是否是一个Git项目
        if [ -d ".git" ]; then
            git update-index --assume-unchanged starter/src/main/resources/application-local.yml
        fi
    fi
done
