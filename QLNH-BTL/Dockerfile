# Bước 1: Sử dụng môi trường Maven kết hợp Amazon Corretto để build code
FROM maven:3.9.6-amazoncorretto-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Bước 2: Sử dụng môi trường chạy Java Corretto siêu gọn nhẹ
FROM amazoncorretto:17-alpine
WORKDIR /app

# Ép tắt giao diện đồ họa Java Swing để tránh sập máy chủ Render
ENV JAVA_OPTS="-Djava.awt.headless=true"

# Bốc chính xác file jar từ thư mục target (sử dụng dấu * để tự động bắt tên file)
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080

# Kích hoạt chạy ứng dụng đi kèm cấu hình ngầm JAVA_OPTS
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
