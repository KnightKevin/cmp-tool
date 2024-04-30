#!/bin/bash

ignoreDir=("logs/" "cloud-manager/" "tar")



# 遍历所有文件夹（每个文件夹都是一个 Git 仓库）
for repo in */; do
    # 进入仓库文件夹
    cd "$repo"
    if [ ! -d ".git" ]; then
        echo "Skipping $repo..."
        cd ..
        continue
    fi

    echo "push $repo..."

    # 拉取最新代码
    git pull --rebase
    git push
    # 返回上级目录
    cd ..
done
