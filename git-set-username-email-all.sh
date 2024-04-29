#!/bin/bash

user=""
email=""

# 默认提交消息
commit_message="Auto commit"

# 获取当前目录
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# 解析命令行参数
while getopts ":u:e:" opt; do
  case $opt in
    u)
      user="$OPTARG"
      ;;
    e)
      email="$OPTARG"
      ;;
    \?)
      echo "Invalid option: -$OPTARG" >&2
      exit 1
      ;;
    :)
      echo "Option -$OPTARG requires an argument." >&2
      exit 1
      ;;
  esac
done


# 检查参数是否存在
if [ -z "$user" ] || [ -z "$email" ]; then
    echo "Usage: $0 -u <user> -e <email>"
    exit 1
fi

# 列出当前目录下的所有文件夹
for folder in "$DIR"/*; do
    if [ -d "$folder" ]; then
        cd "$folder" || exit

        # 检查文件夹是否是一个Git项目
        if [ -d ".git" ]; then
            echo "Processing $(basename "$folder")..."

            git config user.name "$user"
            git config user.email "$email"

        fi
    fi
done
