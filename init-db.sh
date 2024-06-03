#!/bin/bash

DOCKER_CONTAINER="mariadb"

# 定义MySQL的用户名和密码
MYSQL_USER="root"
MYSQL_PASSWORD="password"

# 定义数据库名称数组
DATABASES=(
"zscmp_base"
"zscmp_connector"
"zscmp_mc"
"zscmp_operation"
"zscmp_oss"
"zscmp_portal"
"zscmp_rds"
"zscmp_ticket"
"zscmp_vm"
)


initFlag=$(docker exec -it "$DOCKER_CONTAINER" mysql -u"$MYSQL_USER" -p"$MYSQL_PASSWORD" -e "SHOW DATABASES LIKE '${DATABASES[0]}';" | grep "${DATABASES[0]}")
if [ -n "$initFlag" ]; then 
    echo "数据库已经初始化过了，判断条件为${DATABASES[0]}存在"
    exit
fi

# 创建数据库函数
create_database() {
    local db_name=$1
    docker exec -it "$DOCKER_CONTAINER" mysql -u"$MYSQL_USER" -p"$MYSQL_PASSWORD" -e "CREATE DATABASE IF NOT EXISTS $db_name;"
    echo "数据库 $db_name 创建完成"
}

# 循环遍历数据库名称数组，创建数据库
for db_name in "${DATABASES[@]}"
do
    create_database "$db_name"
done

echo "所有数据库创建完成"
