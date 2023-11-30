#!/bin/bash

# 自动clone下面branches所列的服务分支

# 仓库地址
repository_url="ssh://git@dev.zstack.io:9022/zstackio/cmp-server.git"

# 分支列表
branches=(
    "basic-libs"
    "base-server"
    "gateway-server" 
    "connector-server"
    "maintenance-server"
    "operation-server"
    "vm-server"
    "rds-server"
    "oss-server"
    "ticket-server"
    "portal-server"
    )

# 目标文件夹
base_folder="./"

# 创建目标文件夹
mkdir -p "$base_folder"

# 遍历分支列表并克隆指定分支到对应的文件夹
for branch in "${branches[@]}"; do
    # 提取仓库名称
    repo_name=$(basename -s .git "$repository_url")

    # 目标文件夹路径
    target_folder="$base_folder/$branch"

    # 检查目标文件夹是否存在，如果存在则跳过
    if [ -d "$target_folder" ]; then
        echo "Skipping branch $branch. Folder already exists: $target_folder"
    else
        # 克隆指定分支
        git clone -b "$branch" "$repository_url" "$target_folder"
        echo "Cloned branch $branch to $target_folder"
    fi
done

# 生产pom文件的modules配置
echo ""
for branch in "${branches[@]}"; do
    # 提取仓库名称
    echo "<module>$branch</module>"
done
