## cmp-cloud
基于3.3.3开发的，3.3.3最后一个提交commit号为：697d841014cf2a50b6010b9889ec3f27ca69e52a


## 准备工作
windows一定要在git bash环境下使用，因为所提供的脚本工具都是shell脚本


### git bash wget配置
1、下载wget二进制安装包，地址：https://eternallybored.org/misc/wget/

2、解压安装包，将wget.exe 拷贝到{git安装路径}\mingw64\bin\ 下面；（或者解压之后将解压文件中wget.exe的路径添加到环境变量中）

至此，完成安装；

在git bash中执行 wget验证即可，done；

## 如何使用
将所提供的压缩文件解压到某个路径，并进入这个文件夹，比如`C:\cmp-cloud`,
```shell
# 这回
chmod +x git-clone-all.sh
./git-clone-all.sh
```

然后用idea打开cmp-cloud这个工程就好了


## 其他工具脚本
自己打开看注释就好了