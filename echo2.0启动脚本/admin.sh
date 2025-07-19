#!/bin/sh
# 该脚本为Linux下启动java程序的脚本
# 特别注意：
# 该脚本使用系统kill命令来强制终止指定的java程序进程。
# 所以在杀死进程前，可能会造成数据丢失或数据不完整。如果必须要考虑到这类情况，则需要改写此脚本，
# 根据实际情况来修改以下配置信息 ##################################
# JAVA应用程序的名称
APP_NAME=$2'-admin'
# jar包存放路径
JAR_PATH='/opt/echo2/'$2'/admin'
#更改jar 名称
mv $JAR_PATH/"echo2-admin.jar" $JAR_PATH/$2"-admin.jar"
# jar包名称
JAR_NAME=$2"-admin.jar"
# PID 代表是PID文件
JAR_PID=$JAR_NAME\.pid
# 日志输出文件
LOG_FILE=


# java虚拟机启动参数
#-Xms：初始Heap(堆)大小，使用的最小内存,cpu性能高时此值应设的大一些
XMS="-Xms512m"
#-Xmx: java heap(堆)最大值，使用的最大内存
XMX="-Xmx1024m"
#-XX:MetaspaceSize：元空间Metaspace扩容时触发FullGC(垃圾回收)的初始化阈值
METASPACE_SIZE="-XX:MetaspaceSize=512m"
#-XX:MaxMetaspaceSize: 元空间Metaspace扩容时触发FullGC(垃圾回收)的最大阈值;建议MetaspaceSize和MaxMetaspaceSize设置一样大
MAX_METASPACE_SIZE="-XX:MaxMetaspaceSize=512m"
#-XX:+PrintGCDateStamps: GC日志打印时间戳信息,你可以通过-XX:+PrintGCDateStamps开启，或者-XX:-PrintGCDateStamps关闭 取值boolean
PRINTGCDATESTAMPS="-XX:+PrintGCDateStamps"
#-XX:+PrintGCDetails:GC日志打印详细信息,你可以通过-XX:+PrintGCDetails开启，或者-XX:-PrintGCDetails关闭 取值boolean
PRINTGCDETAILS="-XX:+PrintGCDetails"
#-XX:ParallelGCThreads: CMS/G1通用线程数设置
PARALLELGCTHREADS="-XX:ParallelGCThreads=10"
#-XX:+HeapDumpOnOutOfMemoryError:当JVM发生OOM时，自动生成DUMP文件,
HEAPDUMPONOUTOFMEMORYERROR="-XX:+HeapDumpOnOutOfMemoryError"
#-XX:HeapDumpPath:当JVM发生OOM时，自动生成DUMP文件的保存路径，缺省情况未指定目录时，JVM会创建一个名称为java_pidPID.hprof的堆dump文件在JVM的工作目录下
HeapDumpPath="-XX:HeapDumpPath=$JAR_PATH/$LOG_FILE/gc/dump/$APP_NAME.hprof"
#-Dfile.encoding: 文件编码
FILE_ENCODING="-Dfile.encoding=utf-8"
#拼接参数
JAVA_OPTS="$XMS $XMX $METASPACE_SIZE $MAX_METASPACE_SIZE $PRINTGCDATESTAMPS $HEAPDUMPONOUTOFMEMORYERROR $HeapDumpPath $FILE_ENCODING -Xloggc:$LOG_FILE/gc/gclog.log"
# 根据实际情况来修改以上配置信息 ##################################
is_exist() {
  # 查询出应用服务的进程id，grep -v 是反向查询的意思，查找除了grep操作的run.jar的进程之外的所有进程
  pid=`ps -ef|grep $JAR_NAME|grep -v grep|awk '{print $2}' `
  # [ ]表示条件测试。注意这里的空格很重要。要注意在'['后面和']'前面都必须要有空格
  # [ -z STRING ] 如果STRING的长度为零则返回为真，即空是真
  # 如果不存在返回0，存在返回1
  if [ -z "${pid}" ]; then
   return 0
  else
    return 1
  fi
}

# ######### Shell脚本中$0、$?、$!、$$、$*、$#、$@等的说明 #########
# $$ Shell本身的PID（ProcessID，即脚本运行的当前 进程ID号）
# $! Shell最后运行的后台Process的PID(后台运行的最后一个进程的 进程ID号)
# $? 最后运行的命令的结束代码（返回值）即执行上一个指令的返回值 (显示最后命令的退出状态。0表示没有错误，其他任何值表明有错误)
# $- 显示shell使用的当前选项，与set命令功能相同
# $* 所有参数列表。如"$*"用「"」括起来的情况、以"$1 $2 … $n"的形式输出所有参数，此选项参数可超过9个。
# $@ 所有参数列表。如"$@"用「"」括起来的情况、以"$1" "$2" … "$n" 的形式输出所有参数。
# $# 添加到Shell的参数个数
# $0 Shell本身的文件名
# $1～$n 添加到Shell的各参数值。$1是第1参数、$2是第2参数…。

# 服务启动方法
start() {
  is_exist
  if [ $? -eq "1" ]; then
    echo "$APP_NAME 已经在运行pid是 ${pid}"
  else
    # jar服务启动脚本
    nohup java $JAVA_OPTS -jar $JAR_PATH/$JAR_NAME  --spring.profiles.active=$2 >$JAR_PATH/admin.log 2>&1 &
    echo $! > $JAR_PID
    echo "启动 $APP_NAME 成功 pid是 $! "
    sleep 15
    # 睡眠一会等启动完成后输出启动日志
    cat $JAR_PATH/admin.log
  fi
}

# 服务停止方法
stop() {
  # is_exist
  pidf=$(cat $JAR_PID)
  # echo "$pidf"
  echo "pid = $pidf begin kill $pidf"
  kill $pidf
  rm -rf $JAR_PID
  sleep 2
  # 判断服务进程是否存在
  is_exist
  if [ $? -eq "1" ]; then
    echo "pid = $pid begin kill -9 $pid"
    kill -9 $pid
    sleep 2
    echo "$APP_NAME 已停止！"
  else
    echo "$APP_NAME 没有运行！"
  fi
}

# 服务运行状态查看方法
status() {
  is_exist
  if [ $? -eq "1" ]; then
    echo "$APP_NAME 正在运行，pid 是 ${pid}"
  else
    echo "$APP_NAME 没有运行！"
  fi
}

# 重启服务方法
restart() {
  # 调用服务停止命令
  stop
  # 调用服务启动命令
  start $1 $2
}

# 帮助说明，用于提示输入参数信息
usage() {
    echo "Usage: sh $APP_NAME.sh [ start | stop | restart | status ]"
    exit 1
}

###################################
# 读取脚本的第一个参数($1)，进行判断
# 参数取值范围：{ start | stop | restart | status }
# 如参数不在指定范围之内，则打印帮助信息
###################################
#根据输入参数，选择执行对应方法，不输入则执行使用说明
case "$1" in
  'start')
    start $1 $2
    ;;
  'stop')
    stop
    ;;
  'restart')
    restart $1 $2
    ;;
  'status')
    status
    ;;
  *)
    usage
    ;;
esac
exit 0
