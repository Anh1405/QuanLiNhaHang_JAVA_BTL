# Bước 1: Sử dụng môi trường Maven để build code nguồn thành file .jar
FROM maven:3.8.8-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Bước 2: Sử dụng môi trường Java gọn nhẹ để chạy file .jar sau khi build
FROM openjdk:17-jdk-slim
WORKDIR /app
# Thêm biến môi trường ép tắt giao diện đồ họa Swing để tránh sập server
ENV JAVA_OPTS="-Djava.awt.headless=true"

COPY --from=build /app/target/QLNH_BTL-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080

# Chạy ứng dụng kèm theo biến JAVA_OPTS
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]