# Linux常用命令快速入门指南

> 新手开发者必备的Linux命令手册，快速上手日常开发工作

---

## 📚 目录

1. [文件与目录操作](#1-文件与目录操作)
2. [文件查看与编辑](#2-文件查看与编辑)
3. [文件权限管理](#3-文件权限管理)
4. [进程管理](#4-进程管理)
5. [系统信息查看](#5-系统信息查看)
6. [网络相关命令](#6-网络相关命令)
7. [压缩与解压](#7-压缩与解压)
8. [查找与搜索](#8-查找与搜索)
9. [文本处理](#9-文本处理)
10. [实用技巧与组合命令](#10-实用技巧与组合命令)

---

## 1. 文件与目录操作

### 1.1 查看当前目录
```bash
# 显示当前工作目录的完整路径
pwd

# 示例输出：/home/username/projects
```

### 1.2 列出文件
```bash
# 列出当前目录的文件和文件夹
ls

# 详细列表（包括权限、大小、时间等）
ls -l

# 显示所有文件（包括隐藏文件，以.开头的文件）
ls -a

# 组合使用：详细列表 + 显示隐藏文件
ls -la

# 按时间排序（最新的在前）
ls -lt

# 按文件大小排序
ls -lhS

# 递归显示子目录内容
ls -R
```

**常用参数说明**：
- `-l`：详细列表格式
- `-a`：显示所有文件（包括隐藏文件）
- `-h`：人类可读的文件大小（KB、MB、GB）
- `-t`：按修改时间排序
- `-S`：按文件大小排序
- `-R`：递归显示

---

### 1.3 切换目录
```bash
# 进入指定目录
cd /home/username/projects

# 进入用户主目录
cd ~
# 或
cd

# 返回上一级目录
cd ..

# 返回上两级目录
cd ../..

# 返回上次所在的目录
cd -
```

---

### 1.4 创建目录
```bash
# 创建单个目录
mkdir myproject

# 创建多级目录
mkdir -p project/src/main/java

# 创建多个目录
mkdir dir1 dir2 dir3

# 创建目录并设置权限
mkdir -m 755 myproject
```

**常用参数**：
- `-p`：递归创建目录（如果父目录不存在会自动创建）
- `-m`：设置目录权限

---

### 1.5 删除文件/目录
```bash
# 删除文件
rm file.txt

# 删除目录（需要确认）
rm -r myproject

# 强制删除，不询问确认
rm -rf myproject

# 删除前询问确认（推荐使用）
rm -ri myproject

# 删除所有.txt文件
rm *.txt
```

**常用参数**：
- `-r` 或 `-R`：递归删除目录及其内容
- `-f`：强制删除，不询问确认（谨慎使用）
- `-i`：删除前询问确认

⚠️ **警告**：`rm -rf` 命令非常危险，删除后无法恢复，使用前务必确认！

---

### 1.6 复制文件/目录
```bash
# 复制文件
cp file.txt file_backup.txt

# 复制文件到指定目录
cp file.txt /home/username/documents/

# 复制目录（递归复制）
cp -r myproject myproject_backup

# 复制时保留文件属性（权限、时间等）
cp -p file.txt backup.txt

# 复制时显示进度
cp -v file.txt backup.txt

# 复制多个文件到目录
cp file1.txt file2.txt /home/username/documents/
```

**常用参数**：
- `-r` 或 `-R`：递归复制目录
- `-p`：保留文件属性
- `-v`：显示复制过程
- `-i`：覆盖前询问确认

---

### 1.7 移动/重命名文件
```bash
# 移动文件到指定目录
mv file.txt /home/username/documents/

# 重命名文件
mv oldname.txt newname.txt

# 移动并重命名
mv oldname.txt /home/username/documents/newname.txt

# 移动目录
mv olddir newdir

# 移动多个文件
mv file1.txt file2.txt /home/username/documents/
```

**注意**：`mv` 命令既可以移动文件，也可以重命名文件，取决于目标路径是目录还是文件名。

---

### 1.8 创建空文件
```bash
# 创建空文件
touch file.txt

# 创建多个文件
touch file1.txt file2.txt file3.txt

# 更新文件的访问和修改时间（文件存在时）
touch file.txt

# 设置文件的修改时间为指定时间
touch -t 202412221200 file.txt
```

---

## 2. 文件查看与编辑

### 2.1 查看文件内容
```bash
# 查看整个文件内容
cat file.txt

# 查看文件并显示行号
cat -n file.txt

# 查看多个文件
cat file1.txt file2.txt

# 合并文件内容到新文件
cat file1.txt file2.txt > merged.txt

# 追加文件内容
cat file1.txt >> file2.txt
```

---

### 2.2 分页查看文件
```bash
# 分页查看文件（可以上下翻页）
less file.txt

# 在less中的操作：
# 空格键：向下翻页
# b键：向上翻页
# /关键词：搜索
# n键：下一个搜索结果
# q键：退出

# 类似less，但功能更少（早期版本）
more file.txt
```

---

### 2.3 查看文件头部/尾部
```bash
# 查看文件前10行（默认）
head file.txt

# 查看文件前20行
head -n 20 file.txt

# 查看文件后10行（默认）
tail file.txt

# 查看文件后20行
tail -n 20 file.txt

# 实时查看文件更新（常用于查看日志）
tail -f logfile.txt

# 实时查看多个日志文件
tail -f log1.txt log2.txt
```

**实际应用场景**：
```bash
# 查看最新的错误日志
tail -n 100 /var/log/error.log

# 实时监控应用日志
tail -f /var/log/myapp.log
```

---

### 2.4 文件编辑器

#### vi/vim（最常用的编辑器）
```bash
# 打开文件
vim file.txt
# 或
vi file.txt

# vim基本操作：
# 1. 命令模式（默认模式）：
#    i：进入插入模式
#    a：在光标后插入
#    o：在下一行插入
#    :w：保存文件
#    :q：退出
#    :wq：保存并退出
#    :q!：不保存强制退出
#    dd：删除当前行
#    yy：复制当前行
#    p：粘贴
#    /关键词：搜索
#    :set number：显示行号

# 2. 插入模式（按i进入）：
#    可以正常输入文本
#    ESC：退出插入模式，返回命令模式
```

**vim快速入门示例**：
```bash
vim file.txt
# 1. 按 i 进入插入模式
# 2. 输入内容
# 3. 按 ESC 退出插入模式
# 4. 输入 :wq 保存并退出
```

#### nano（简单易用的编辑器）
```bash
# 打开文件
nano file.txt

# nano操作：
# Ctrl+O：保存
# Ctrl+X：退出
# Ctrl+K：剪切行
# Ctrl+U：粘贴
# Ctrl+W：搜索
```

---

## 3. 文件权限管理

### 3.1 查看文件权限
```bash
# 查看文件详细权限
ls -l file.txt

# 输出示例：
# -rw-r--r-- 1 username group 1024 Dec 22 10:00 file.txt
# 
# 权限说明：
# -rw-r--r--
# 第一位：文件类型（-表示文件，d表示目录）
# 接下来3位：所有者权限（rw-：读写）
# 接下来3位：所属组权限（r--：只读）
# 最后3位：其他用户权限（r--：只读）
```

**权限说明**：
- `r`（read）：读权限，数字表示为4
- `w`（write）：写权限，数字表示为2
- `x`（execute）：执行权限，数字表示为1
- 组合：rwx = 4+2+1 = 7，rw- = 4+2 = 6，r-- = 4

---

### 3.2 修改文件权限
```bash
# 使用数字方式修改权限
chmod 755 file.txt
# 7 = 所有者（rwx）
# 5 = 所属组（r-x）
# 5 = 其他用户（r-x）

# 常用权限组合：
chmod 644 file.txt  # 所有者读写，其他只读（文件常用）
chmod 755 file.txt  # 所有者全权限，其他读和执行（目录常用）
chmod 600 file.txt  # 所有者读写，其他无权限（私密文件）

# 使用符号方式修改权限
chmod u+x file.txt      # 给所有者添加执行权限
chmod g+w file.txt      # 给所属组添加写权限
chmod o-r file.txt      # 移除其他用户的读权限
chmod a+x file.txt      # 给所有人添加执行权限

# 递归修改目录权限
chmod -R 755 myproject/
```

**符号说明**：
- `u`：所有者（user）
- `g`：所属组（group）
- `o`：其他用户（others）
- `a`：所有人（all）
- `+`：添加权限
- `-`：移除权限
- `=`：设置权限

---

### 3.3 修改文件所有者
```bash
# 修改文件所有者
chown username file.txt

# 修改文件所有者和所属组
chown username:group file.txt

# 递归修改目录
chown -R username:group myproject/
```

---

### 3.4 修改文件所属组
```bash
# 修改文件所属组
chgrp groupname file.txt

# 递归修改目录
chgrp -R groupname myproject/
```

---

## 4. 进程管理

### 4.1 查看进程
```bash
# 查看当前用户的进程
ps

# 查看所有进程（详细）
ps aux

# 查看进程树
ps auxf

# 查找特定进程（如查找java进程）
ps aux | grep java

# 查看进程详细信息
ps -ef | grep java

# 实时查看进程（类似任务管理器）
top

# 按内存使用排序
top -o %MEM

# 按CPU使用排序
top -o %CPU

# 退出top：按 q 键
```

**ps命令输出说明**：
```
USER       PID %CPU %MEM    VSZ   RSS TTY      STAT START   TIME COMMAND
username  1234  1.0  2.5  10240  5120 ?        S    10:00   0:05 java -jar app.jar
```
- `USER`：进程所有者
- `PID`：进程ID
- `%CPU`：CPU使用率
- `%MEM`：内存使用率
- `COMMAND`：命令

---

### 4.2 结束进程
```bash
# 结束进程（正常结束，进程ID为1234）
kill 1234

# 强制结束进程
kill -9 1234

# 根据进程名结束进程
killall java

# 强制结束所有java进程
killall -9 java

# 查找并结束进程（组合命令）
ps aux | grep java | grep -v grep | awk '{print $2}' | xargs kill
```

**常用信号**：
- `kill -9`：SIGKILL，强制终止，无法被捕获
- `kill -15`：SIGTERM，正常终止（默认）
- `kill -2`：SIGINT，中断（类似Ctrl+C）

---

### 4.3 后台运行进程
```bash
# 后台运行命令
java -jar app.jar &

# 将正在运行的前台进程放到后台
# 1. 按 Ctrl+Z 暂停进程
# 2. 输入 bg 让进程在后台继续运行

# 查看后台任务
jobs

# 将后台任务调到前台
fg %1

# 让进程在后台运行，即使终端关闭也不退出
nohup java -jar app.jar > output.log 2>&1 &
```

---

## 5. 系统信息查看

### 5.1 磁盘空间
```bash
# 查看磁盘使用情况
df -h

# 查看指定目录的磁盘使用
du -h /home/username

# 查看当前目录总大小
du -sh

# 查看目录下各子目录大小（排序）
du -h --max-depth=1 | sort -hr

# 查找大文件（大于100MB）
find /home -type f -size +100M
```

**输出说明**：
```
Filesystem      Size  Used Avail Use% Mounted on
/dev/sda1        50G   20G   28G  42% /
```
- `Size`：总大小
- `Used`：已使用
- `Avail`：可用空间
- `Use%`：使用百分比

---

### 5.2 内存使用
```bash
# 查看内存使用情况
free -h

# 持续监控内存（每2秒刷新）
free -h -s 2

# 输出说明：
#               total        used        free      shared  buff/cache   available
# Mem:           8.0G        2.0G        1.0G        100M        5.0G        4.5G
# Swap:          2.0G          0B        2.0G
```

---

### 5.3 CPU信息
```bash
# 查看CPU信息
cat /proc/cpuinfo

# 查看CPU核心数
nproc

# 查看CPU使用率（需要安装sysstat）
# top 命令也可以查看CPU使用率
```

---

### 5.4 系统信息
```bash
# 查看系统版本
cat /etc/os-release

# 查看内核版本
uname -r

# 查看系统所有信息
uname -a

# 查看系统运行时间
uptime

# 查看当前登录用户
who

# 查看当前用户
whoami
```

---

## 6. 网络相关命令

### 6.1 网络连接测试
```bash
# 测试网络连通性（ping）
ping www.baidu.com

# ping指定次数（4次）
ping -c 4 www.baidu.com

# 停止ping：按 Ctrl+C

# 测试端口是否开放
telnet 192.168.1.1 8080

# 或使用nc（netcat）
nc -zv 192.168.1.1 8080
```

---

### 6.2 下载文件
```bash
# 使用wget下载文件
wget https://example.com/file.zip

# 指定保存文件名
wget -O myfile.zip https://example.com/file.zip

# 断点续传
wget -c https://example.com/file.zip

# 后台下载
wget -b https://example.com/file.zip

# 使用curl下载文件
curl -O https://example.com/file.zip

# 指定保存文件名
curl -o myfile.zip https://example.com/file.zip

# 显示下载进度
curl -# -O https://example.com/file.zip

# 下载并跟随重定向
curl -L -O https://example.com/file.zip
```

---

### 6.3 网络请求
```bash
# GET请求
curl http://api.example.com/users

# POST请求
curl -X POST http://api.example.com/users \
  -H "Content-Type: application/json" \
  -d '{"name":"John","age":30}'

# 带请求头
curl -H "Authorization: Bearer token123" http://api.example.com/users

# 查看响应头
curl -I http://www.example.com

# 保存Cookie
curl -c cookies.txt http://www.example.com

# 使用Cookie
curl -b cookies.txt http://www.example.com
```

---

### 6.4 网络配置
```bash
# 查看IP地址
ip addr
# 或
ifconfig

# 查看路由表
ip route
# 或
route -n

# 查看网络连接状态
netstat -an

# 查看监听的端口
netstat -tuln

# 查看特定端口的连接
netstat -an | grep 8080

# 使用ss命令（更现代的工具）
ss -tuln  # 查看监听端口
ss -an | grep 8080  # 查看8080端口
```

---

## 7. 压缩与解压

### 7.1 tar命令（最常用）
```bash
# 压缩：创建tar.gz压缩包
tar -czf archive.tar.gz directory/

# 解压：解压tar.gz文件
tar -xzf archive.tar.gz

# 查看压缩包内容（不解压）
tar -tzf archive.tar.gz

# 压缩到指定目录
tar -czf /backup/archive.tar.gz /home/username/documents

# 解压到指定目录
tar -xzf archive.tar.gz -C /home/username/documents

# 压缩时显示进度（需要pv工具）
tar -czf - directory/ | pv > archive.tar.gz
```

**常用参数**：
- `-c`：创建压缩包
- `-x`：解压
- `-z`：使用gzip压缩/解压
- `-f`：指定文件名
- `-v`：显示过程（verbose）
- `-t`：查看压缩包内容

**常用组合**：
- `tar -czf`：创建.tar.gz压缩包
- `tar -xzf`：解压.tar.gz文件
- `tar -xzvf`：解压并显示过程

---

### 7.2 zip/unzip
```bash
# 压缩文件/目录
zip -r archive.zip directory/

# 压缩多个文件
zip archive.zip file1.txt file2.txt

# 解压zip文件
unzip archive.zip

# 解压到指定目录
unzip archive.zip -d /home/username/documents

# 查看zip文件内容（不解压）
unzip -l archive.zip

# 解压时不覆盖已存在文件
unzip -n archive.zip
```

---

### 7.3 其他压缩格式
```bash
# .tar.bz2格式
tar -cjf archive.tar.bz2 directory/  # 压缩
tar -xjf archive.tar.bz2             # 解压

# .tar.xz格式
tar -cJf archive.tar.xz directory/   # 压缩
tar -xJf archive.tar.xz              # 解压

# .7z格式（需要安装p7zip）
7z a archive.7z directory/           # 压缩
7z x archive.7z                      # 解压
```

---

## 8. 查找与搜索

### 8.1 find命令（文件查找）
```bash
# 按文件名查找
find /home -name "file.txt"

# 按文件名查找（不区分大小写）
find /home -iname "file.txt"

# 按文件类型查找
find /home -type f  # 查找文件
find /home -type d  # 查找目录

# 按文件大小查找
find /home -size +100M  # 大于100MB
find /home -size -10M   # 小于10MB

# 按修改时间查找
find /home -mtime -7   # 7天内修改的文件
find /home -mtime +30  # 30天前修改的文件

# 按权限查找
find /home -perm 644

# 查找并删除（谨慎使用）
find /home -name "*.tmp" -delete

# 查找并执行命令
find /home -name "*.log" -exec rm {} \;

# 组合条件
find /home -type f -name "*.txt" -size +1M
```

**find常用条件**：
- `-name`：文件名
- `-type`：文件类型（f文件，d目录）
- `-size`：文件大小（+大于，-小于）
- `-mtime`：修改时间（-n表示n天内，+n表示n天前）
- `-perm`：权限
- `-exec`：对找到的文件执行命令

---

### 8.2 grep命令（文本搜索）
```bash
# 在文件中搜索关键词
grep "error" logfile.txt

# 搜索时不区分大小写
grep -i "error" logfile.txt

# 显示行号
grep -n "error" logfile.txt

# 显示匹配的行及其前后几行
grep -A 3 "error" logfile.txt  # 后3行
grep -B 3 "error" logfile.txt  # 前3行
grep -C 3 "error" logfile.txt  # 前后各3行

# 只显示匹配的文件名
grep -l "error" *.txt

# 递归搜索目录
grep -r "error" /home/username/

# 使用正则表达式
grep -E "error|warning" logfile.txt

# 反向匹配（显示不包含关键词的行）
grep -v "error" logfile.txt

# 统计匹配的行数
grep -c "error" logfile.txt
```

**实际应用**：
```bash
# 在Java项目中查找包含"UserService"的文件
grep -r "UserService" src/

# 查看日志中的错误
tail -f logfile.txt | grep "error"

# 统计错误日志数量
grep -c "ERROR" logfile.txt
```

---

### 8.3 locate命令（快速文件查找）
```bash
# 查找文件（需要先更新数据库）
locate file.txt

# 更新locate数据库
sudo updatedb

# 不区分大小写
locate -i file.txt

# 限制结果数量
locate -l 10 file.txt
```

**注意**：locate使用数据库查找，速度快但可能不包含最新文件，需要先运行`updatedb`。

---

## 9. 文本处理

### 9.1 排序和去重
```bash
# 对文件内容排序
sort file.txt

# 反向排序
sort -r file.txt

# 按数字排序
sort -n file.txt

# 去除重复行
sort file.txt | uniq

# 统计重复行出现次数
sort file.txt | uniq -c

# 只显示重复的行
sort file.txt | uniq -d
```

---

### 9.2 文本替换（sed）
```bash
# 替换文件中的文本（输出到屏幕）
sed 's/old/new/g' file.txt

# 替换并保存到新文件
sed 's/old/new/g' file.txt > newfile.txt

# 直接修改原文件（谨慎使用）
sed -i 's/old/new/g' file.txt

# 删除包含特定内容的行
sed '/pattern/d' file.txt

# 只替换第2行
sed '2s/old/new/g' file.txt

# 替换第2到第5行
sed '2,5s/old/new/g' file.txt
```

**sed常用操作**：
- `s/old/new/g`：替换（g表示全局替换）
- `d`：删除行
- `p`：打印行

---

### 9.3 文本分析（awk）
```bash
# 打印文件的第1列（默认以空格分隔）
awk '{print $1}' file.txt

# 打印第1列和第3列
awk '{print $1, $3}' file.txt

# 使用指定分隔符
awk -F ':' '{print $1}' /etc/passwd

# 打印满足条件的行
awk '/pattern/ {print}' file.txt

# 计算列的总和
awk '{sum += $1} END {print sum}' file.txt

# 打印行号
awk '{print NR, $0}' file.txt

# 统计行数
awk 'END {print NR}' file.txt
```

**awk实际应用**：
```bash
# 查看进程的PID和CPU使用率
ps aux | awk '{print $2, $3}'

# 统计日志中的错误数量
grep "ERROR" logfile.txt | awk '{print $1}' | sort | uniq -c
```

---

### 9.4 文本统计
```bash
# 统计文件行数、字数、字符数
wc file.txt

# 输出示例：100  500  3000 file.txt
# 100：行数
# 500：字数
# 3000：字符数

# 只统计行数
wc -l file.txt

# 只统计字数
wc -w file.txt

# 只统计字符数
wc -c file.txt

# 统计多个文件
wc file1.txt file2.txt

# 统计当前目录下所有.txt文件的行数
find . -name "*.txt" | xargs wc -l
```

---

## 10. 实用技巧与组合命令

### 10.1 管道和重定向
```bash
# 管道：将一个命令的输出作为另一个命令的输入
ps aux | grep java

# 重定向：将输出保存到文件（覆盖）
ls -l > filelist.txt

# 追加到文件
echo "new line" >> filelist.txt

# 错误输出重定向
java -jar app.jar 2> error.log

# 标准输出和错误输出都重定向
java -jar app.jar > output.log 2>&1

# 丢弃输出（/dev/null）
java -jar app.jar > /dev/null 2>&1
```

---

### 10.2 命令组合
```bash
# 查找并删除（谨慎使用）
find . -name "*.tmp" -delete

# 查找并压缩
find . -name "*.log" -exec tar -czf logs.tar.gz {} +

# 统计Java文件行数
find . -name "*.java" | xargs wc -l

# 查找包含特定内容的文件
grep -r "UserService" . --include="*.java"

# 查看大文件（按大小排序）
du -h | sort -hr | head -10

# 查看CPU使用率最高的进程
ps aux --sort=-%cpu | head -10

# 查看内存使用率最高的进程
ps aux --sort=-%mem | head -10
```

---

### 10.3 历史命令
```bash
# 查看命令历史
history

# 执行历史命令中的第100条
!100

# 执行上一个命令
!!

# 执行最近以"ls"开头的命令
!ls

# 搜索历史命令（按Ctrl+R）
# 然后输入关键词，按回车执行
```

---

### 10.4 别名（alias）
```bash
# 查看所有别名
alias

# 创建别名
alias ll='ls -lah'
alias grep='grep --color=auto'
alias ..='cd ..'

# 临时使用别名（当前会话有效）
alias ll='ls -lah'

# 永久保存别名（添加到~/.bashrc或~/.bash_profile）
echo "alias ll='ls -lah'" >> ~/.bashrc
source ~/.bashrc  # 重新加载配置

# 取消别名
unalias ll
```

**常用别名推荐**：
```bash
alias ll='ls -lah'
alias la='ls -A'
alias l='ls -CF'
alias ..='cd ..'
alias ...='cd ../..'
alias grep='grep --color=auto'
alias fgrep='fgrep --color=auto'
alias egrep='egrep --color=auto'
```

---

### 10.5 后台任务管理
```bash
# 后台运行命令
command &

# 查看后台任务
jobs

# 将任务调到前台
fg %1

# 让任务在后台继续运行
bg %1

# 断开连接后仍运行（使用nohup）
nohup java -jar app.jar > output.log 2>&1 &

# 查看后台任务的输出
tail -f nohup.out
```

---

### 10.6 快捷键
```bash
# Ctrl+C：终止当前命令
# Ctrl+D：退出当前会话
# Ctrl+L：清屏（等同于clear）
# Ctrl+A：光标移到行首
# Ctrl+E：光标移到行尾
# Ctrl+U：删除光标前的所有内容
# Ctrl+K：删除光标后的所有内容
# Ctrl+W：删除光标前的一个词
# Ctrl+R：搜索历史命令
# Tab：自动补全
# Tab Tab：显示所有可能的补全选项
```

---

### 10.7 常用开发场景

#### 场景1：查看Java应用日志
```bash
# 实时查看日志
tail -f /var/log/myapp.log

# 查看最近100行错误日志
tail -n 100 /var/log/myapp.log | grep ERROR

# 统计错误数量
grep -c ERROR /var/log/myapp.log
```

#### 场景2：查找并替换代码
```bash
# 查找所有Java文件中的旧类名
grep -r "OldClassName" src/ --include="*.java"

# 批量替换（谨慎使用，建议先备份）
find src/ -name "*.java" -exec sed -i 's/OldClassName/NewClassName/g' {} +
```

#### 场景3：清理临时文件
```bash
# 查找并删除.tmp文件
find . -name "*.tmp" -type f -delete

# 查找并删除30天前的日志
find /var/log -name "*.log" -mtime +30 -delete
```

#### 场景4：监控系统资源
```bash
# 监控CPU和内存（每2秒刷新）
watch -n 2 'free -h && echo "" && ps aux --sort=-%cpu | head -5'

# 监控磁盘使用
watch -n 5 'df -h'
```

#### 场景5：打包部署
```bash
# 打包项目
mvn clean package

# 备份当前版本
cp app.jar app.jar.backup.$(date +%Y%m%d_%H%M%S)

# 停止旧进程
ps aux | grep java | grep app.jar | awk '{print $2}' | xargs kill

# 启动新版本
nohup java -jar app.jar > output.log 2>&1 &

# 查看启动日志
tail -f output.log
```

---

## 📝 快速参考表

### 文件操作
| 命令 | 功能 | 示例 |
|------|------|------|
| `ls -la` | 查看文件列表 | `ls -la` |
| `cd` | 切换目录 | `cd /home/user` |
| `pwd` | 显示当前目录 | `pwd` |
| `mkdir -p` | 创建目录 | `mkdir -p project/src` |
| `rm -rf` | 删除目录（谨慎） | `rm -rf olddir` |
| `cp -r` | 复制目录 | `cp -r src backup` |
| `mv` | 移动/重命名 | `mv old new` |

### 文件查看
| 命令 | 功能 | 示例 |
|------|------|------|
| `cat` | 查看文件 | `cat file.txt` |
| `less` | 分页查看 | `less file.txt` |
| `head -n` | 查看前n行 | `head -20 file.txt` |
| `tail -f` | 实时查看 | `tail -f log.txt` |

### 权限管理
| 命令 | 功能 | 示例 |
|------|------|------|
| `chmod 755` | 修改权限 | `chmod 755 file.txt` |
| `chown` | 修改所有者 | `chown user:group file.txt` |

### 进程管理
| 命令 | 功能 | 示例 |
|------|------|------|
| `ps aux` | 查看进程 | `ps aux \| grep java` |
| `top` | 实时查看 | `top` |
| `kill -9` | 强制结束 | `kill -9 1234` |

### 查找搜索
| 命令 | 功能 | 示例 |
|------|------|------|
| `find` | 查找文件 | `find . -name "*.txt"` |
| `grep -r` | 搜索文本 | `grep -r "error" src/` |
| `locate` | 快速查找 | `locate file.txt` |

### 压缩解压
| 命令 | 功能 | 示例 |
|------|------|------|
| `tar -czf` | 压缩 | `tar -czf archive.tar.gz dir/` |
| `tar -xzf` | 解压 | `tar -xzf archive.tar.gz` |
| `zip -r` | zip压缩 | `zip -r archive.zip dir/` |
| `unzip` | zip解压 | `unzip archive.zip` |

---

## 🎯 学习建议

1. **每天练习**：每天使用5-10个命令，加深记忆
2. **结合实际**：在工作中遇到问题时，尝试用命令行解决
3. **使用帮助**：遇到不熟悉的命令，使用`man 命令名`或`命令名 --help`查看帮助
4. **记录常用命令**：把自己常用的命令记录下来，形成个人命令库
5. **安全第一**：使用`rm -rf`等危险命令前，务必确认路径和内容

---

## 📚 进阶学习

### 查看命令帮助
```bash
# 查看命令手册
man ls

# 查看命令简要帮助
ls --help

# 查看命令信息
info ls
```

### 学习资源
- `man`命令：最权威的命令手册
- `--help`参数：快速查看命令用法
- 在线文档：Linux命令大全网站

---

**最后更新**：2025-12-22  
**适用场景**：Linux新手开发者快速入门

