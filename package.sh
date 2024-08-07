#!/bin/bash

# 指定java版本
export JAVA_HOME=/usr/local/jdk-17

baseDir="."

# 须要参与docker-compose的模块
modules=("gateway" "connector" "portal" "base" "vm" "rds" "oss" "operation" "ticket" "maintenance")

# 可能指定的模块
module=$1

dockerComposeCmd="docker compose up --remove-orphans --build -d"


## 函数定义
cpCmd() {

  dir="tar"
  # 检查目录是否存在
  if [ ! -d "$dir" ]; then
      echo "目录不存在，正在创建..."
      mkdir -p "$dir"
      echo "目录创建成功！"
  fi

	if [ "$1" = "gateway" ]; then
		cp -f $baseDir/$module-server/target/zscmp-gateway.jar ./tar/zscmp-gateway.jar
	elif [ "$1" = "maintenance" ];then
		cp -f $baseDir/$module-server/starter/target/zscmp-mc.jar ./tar/zscmp-mc.jar
	else
		cp -f $baseDir/$module-server/starter/target/zscmp-$module.jar ./tar/zscmp-$module.jar
	fi
}


# sh ./git-pull-all.sh


echo "====== 打包开始 ======="
mvn clean package -T 4 -Pprod -DskipTests
echo "====== 打包结束 ======="


if [ $# -gt 0 ]; then

	# 执行cp命令
	cpCmd $module

	# 执行docker-compose命令
	echo "执行：$dockerComposeCmd $module"
	$dockerComposeCmd $module

else
    for module in "${modules[@]}"; do
    	cpCmd $module
    done

    # 执行docker-compose命令
	echo "执行：$dockerComposeCmd"
    $dockerComposeCmd

fi







