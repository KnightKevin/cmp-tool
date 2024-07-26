#!/bin/bash

DOCKER_CONTAINER="mariadb"

# 定义MySQL的用户名和密码
MYSQL_USER="root"
MYSQL_PASSWORD="password"

# 定义数据库名称数组
DATABASES=(
"zstack_cmp"
)

# 创建数据库函数
create_database() {
    local db_name=$1
    docker compose exec -it "$DOCKER_CONTAINER" mysql -u"$MYSQL_USER" -p"$MYSQL_PASSWORD" -e "CREATE DATABASE IF NOT EXISTS $db_name;"
    echo "数据库 $db_name 创建完成"
}

# 循环遍历数据库名称数组，创建数据库
for db_name in "${DATABASES[@]}"
do
    create_database "$db_name"
done

echo "所有数据库创建完成"
