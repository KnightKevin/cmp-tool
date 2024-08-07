#!/bin/bash

DOCKER_CONTAINER="mariadb"

# 定义MySQL的用户名和密码
MYSQL_USER="root"
MYSQL_PASSWORD="password"

# 删除本机的license信息
rm -rf /usr/local/zstack-cmp/licenseTemp.lic
rm -rf /usr/local/zstack-cmp/publicCerts.keystore

# 删除数据库中license信息
docker compose exec -it "$DOCKER_CONTAINER" mysql -u"$MYSQL_USER" -p"$MYSQL_PASSWORD" -e "use zscmp_base; delete from sys_config where category='REG';"

