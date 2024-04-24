#!/bin/bash

# 默认提交消息
commit_message="Auto commit"

# 获取当前目录
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# 解析命令行参数
while getopts ":m:" opt; do
  case $opt in
    m)
      commit_message="$OPTARG"
      ;;
    \?)
      echo "Invalid option: -$OPTARG" >&2
      ;;
  esac
done

# 列出当前目录下的所有文件夹
for folder in "$DIR"/*; do
    if [ -d "$folder" ]; then
        cd "$folder" || exit

        # 检查文件夹是否是一个Git项目
        if [ -d ".git" ]; then
            echo "Processing $(basename "$folder")..."

            git add .
            # 运行提交脚本，传递参数m
            git commit -m "$commit_message"

            git pull --rebase

            git push

            echo "Done."
        fi
    fi
done
