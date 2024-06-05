#!/bin/bash

# 自动clone下面branches所列的服务分支

# 仓库地址
repository_url="ssh://git@dev.zstack.io:9022/zstackio/cmp-server.git"

# 配置文件路径
CONFIG_FILE="branches.conf"

# 检查配置文件是否存在
if [ ! -f "$CONFIG_FILE" ]; then
    echo "配置文件 $CONFIG_FILE 不存在."
    exit 1
fi

# 分支列表
branches=()


# 读取配置文件并执行克隆操作
while IFS=' = ' read -r key value
do
    # 根据配置文件中的键值对执行克隆
    case $key in
        branch)
          branch="$value" 
          branches+=($branch)
        ;;
        dir) 
            if [ -d "$value" ]; then
                echo "Skipping branch $branch. Folder already exists: $value"
            else 
                mkdir -p "$value" 
                echo "正在克隆到 $value ..." 
                git clone --branch "$branch" "$repository_url" "$value"
            fi 
        ;;
    esac
done < "$CONFIG_FILE"




# 生产pom文件的modules配置
echo ""
for branch in "${branches[@]}"; do
    # 提取仓库名称
    echo "<module>$branch</module>"
done
