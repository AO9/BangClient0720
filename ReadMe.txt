
## 服务器相关信息
ssh -q -l root -p 22 140.143.121.65
fxv4jc9ZLHLEse

mysql -uroot -ptul75mfzuq4Y

# 解决gradle running慢的方法 以下路径下创建以下文件，并且在as里配置gradle为 offline work 模式
# 路径：/Users/shenjialong/.gradle

# 文件名：gradle.properties 内容如下
# org.gradle.daemon=true
# org.gradle.jvmargs=-Xmx2048m -XX:MaxPermSize=512m -XX:+HeapDumpOnOutOfMemoryError -Dfile.encoding=UTF-8
# org.gradle.parallel=true
# org.gradle.configureondemand=true