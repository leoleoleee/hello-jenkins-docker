# 使用 Eclipse Temurin 官方 JDK 21（Alpine 版）作为基础镜像
FROM eclipse-temurin:21-jdk-alpine

# 在容器中创建工作目录
WORKDIR /app

# 把 Java 源码复制到容器
COPY src/Main.java /app/Main.java

# 在容器中编译 Java 源文件
RUN javac Main.java

# 暴露 HTTP 端口（与 Java 程序中保持一致）
EXPOSE 8081

# 容器启动时运行 Java 程序
CMD ["java", "Main"]
