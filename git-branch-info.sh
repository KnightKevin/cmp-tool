#!/bin/bash

# 遍历目录下的每个文件
for file in *; do

    if [ "$file" == "logs" ]; then
          continue
    fi

    # 检查文件是否为目录
    if [ -d "$file" ]; then
        # 进入目录
        cd "$file"

        # 获取Git分支名称
        branch=$(git rev-parse --abbrev-ref HEAD 2>/dev/null)

        # 输出文件名和分支名称
        echo "分支: $branch"

        # 返回上一级目录
        cd ..
    fi
done
