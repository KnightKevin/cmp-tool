#!/bin/bash

# 进入包含所有仓库的文件夹
cd ./

# 遍历所有文件夹（每个文件夹都是一个 Git 仓库）
for repo in */; do
      if [ "$repo" == "logs/" ] || [ "$repo" == "cloud-manager/" ]; then
            echo "Skipping $repo..."
            continue
      fi
    echo "Updating $repo..."
    # 进入仓库文件夹
    cd "$repo"
    # 拉取最新代码
    git pull --rebase
    # 返回上级目录
    cd ..
done
