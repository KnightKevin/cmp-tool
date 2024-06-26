#!/bin/bash

# 进入包含所有仓库的文件夹
cd ./

# 遍历所有文件夹（每个文件夹都是一个 Git 仓库）
for repo in */; do
      # 进入仓库文件夹
      cd "$repo"
      if [ ! -d ".git" ]; then
            echo "Skipping $repo..."
            cd ..
            continue
      fi

      echo "Updating $repo..."

      # 拉取最新代码
      git pull --rebase
      # 返回上级目录
      cd ..
done
