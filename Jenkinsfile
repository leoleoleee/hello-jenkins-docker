pipeline {
    // 在任意可用的 agent 上跑（我们只有一个 Jenkins 节点）
    agent any

    environment {
        // Docker 镜像的基础名字
        IMAGE_NAME = "hello-jenkins-docker"
    }

    stages {
        stage('Checkout') {
            steps {
                // 从 SCM（此处就是 GitHub 仓库）拉取当前分支代码
                checkout scm
            }
        }

        stage('Build Docker image') {
            steps {
                sh """
                  echo "Building Docker image..."
                  docker build -t ${IMAGE_NAME}:${env.BUILD_NUMBER} .
                """
            }
        }

        stage('Run container for smoke test') {
            steps {
                sh """
                  echo "Start container for smoke test..."

                  # 如果之前有残留容器，先删掉（忽略错误）
                  docker rm -f ${IMAGE_NAME}-test || true

                  # 启动测试容器，把容器 8081 端口映射到宿主机的 8081
                  docker run -d --name ${IMAGE_NAME}-test -p 8081:8081 ${IMAGE_NAME}:${env.BUILD_NUMBER}

                  # 等 5 秒给 Java 程序一点启动时间
                  sleep 5

                  echo "Calling http://localhost:8081/ ..."
                  # -f：状态码不是 2xx/3xx 就返回非 0，让构建失败
                  curl -f http://localhost:8081/

                  echo "Smoke test passed, stop container."
                  docker rm -f ${IMAGE_NAME}-test
                """
            }
        }
    }

    post {
        always {
            sh """
              echo "Current Docker images (top lines):"
              docker images | head
            """
        }
    }
}
