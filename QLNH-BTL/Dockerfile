# Bước 1: Sử dụng môi trường Maven kết hợp Amazon Corretto để build code
FROM maven:3.9.6-amazoncorretto-17 AS build
WORKDIR /app

# Copy toàn bộ code Backend (đang ở trong QLNH-BTL) vào container
COPY . .

# 🔥 ĐOẠN THẦN THÁNH: Tự tạo thư mục static và bốc frontend từ ngoài vào trong src
RUN mkdir -p src/main/resources/static
COPY ../frontend/ src/main/resources/static/

# Tiến hành build code nguồn thành file .jar
RUN mvn clean package -DskipTests

# Bước 2: Sử dụng môi trường chạy Java Corretto siêu gọn nhẹ
FROM amazoncorretto:17-alpine
WORKDIR /app
ENV JAVA_OPTS="-Djava.awt.headless=true"

# Bốc file jar ra để chạy
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
