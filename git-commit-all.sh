#!/bin/bash

# 默认提交消息
commit_message="Auto commit"

commitedDirs=()

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
        if [ ! -d ".git" ]; then
          continue
        fi

        # 检查是否存在提交
        if [[ ! $(git status --porcelain) ]]; then
          continue
        fi

        basename=$(basename "$folder")
        echo "Processing $(basename "$folder")..."


        git add .
        # 运行提交脚本，传递参数m
        git commit -m "$commit_message"

        # 获取到commit id
        commit_id=$(git rev-parse HEAD)

        git pull --rebase

        git push


        commitedDirs+=("$basename $commit_id")

        echo "Done."
    fi
done



# final message:
# above git project commit:
echo "############### result #################"
echo "The following directories execute 'git commit'"

echo "后端"
echo "commit:"
for ((i=0; i<${#commitedDirs[@]}; i++)); do
    echo -e "\e[0;36m${commitedDirs[$i]}\e[0m"
done