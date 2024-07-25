#!/bin/bash

# 检查是否提供了至少一个参数
if [ $# -lt 1 ]; then
  echo "Usage: $0 <operation> [-param1] [-param2] ..."
  exit 1
fi

# 获取第一个参数作为操作
operation=$1
shift # 移除第一个参数，处理剩余参数

# 初始化一个空数组，用于存储以‘-’开头的参数
params=()

# 遍历剩余的参数
while [[ $# -gt 0 ]]; do
  case $1 in
    -*) # 如果参数以‘-’开头，添加到数组
      params+=("$1")
      ;;
    *)
      echo "Warning: Ignoring unknown parameter $1"
      ;;
  esac
  shift # 处理下一个参数
done



# 仓库地址
repository_url="ssh://git@dev.zstack.io:9022/zstackio/cmp-server.git"

# 配置文件路径
CONFIG_FILE="branches.conf"

# 检查配置文件是否存在
if [ ! -f "$CONFIG_FILE" ]; then
    echo "配置文件 $CONFIG_FILE 不存在."
    exit 1
fi

# branches
mvnModules=()
branches=()
dirs=()


# servers
servers=()


# 读取配置文件并执行克隆操作
while IFS=' = ' read -r key value
do
    # 根据配置文件中的键值对执行克隆
    case $key in
        branch)
          branch="$value"
          branches+=($value)
        ;;
        dir)
          mvnModules+=($value)
          dirs+=($value)
        ;;
    esac
done < "$CONFIG_FILE"


clone() {
    echo "Performing clone with parameters: ${params[@]}"
    # 在这里添加 operation1 的逻辑
    num=${#branches[@]}
    for (( i=0; i<$num; i++ )); do
        local dir=${dirs[i]}
        local branch=${branches[i]}
        # 提取仓库名称
        if [ -d "$dir" ]; then
                echo "Skipping branch $branch. Folder already exists: $dir"
            else 
                mkdir -p "$dir" 
                echo "正在克隆到 $dir ..." 
                git clone --branch "$branch" "$repository_url" "$dir"
        fi 
    done

}

clean() {
    # 在这里添加 operation1 的逻辑
    num=${#branches[@]}
    for (( i=0; i<$num; i++ )); do
        local dir=${dirs[i]}
        rm -rf $dir
    done

}

# 执行不同操作
case $operation in
  clone)
    clone
    ;;
  clean)
    clean
    ;;
  *)
    echo "Unknown operation: $operation"
    exit 1
    ;;
esac


