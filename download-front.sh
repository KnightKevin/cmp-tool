#!/bin/bash

# 自动获取前端文件然后解压到目标目录

# 目标文件夹（自己配）
target_folder="./base-server/starter/src/main/resources/static"


# 下载 .tar 文件
wget -O download_file.tar "http://172.20.15.213/frontendSource/mas/dist.tar"

# 判断目标文件夹下的 portal 文件夹是否存在
if [ -d "$target_folder" ]; then
    # 如果存在，删除
    rm -rf "$target_folder"
fi

# 创建 portal 文件夹
mkdir -p "$target_folder"

# 解压 .tar 文件到目标文件夹
tar -xf download_file.tar -C "./"

mv ./dist/* "$target_folder"

rm -rf ./dist

# 删除下载的 .tar 文件
rm download_file.tar
