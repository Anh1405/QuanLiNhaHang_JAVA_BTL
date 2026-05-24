# Bước 1: Build mã nguồn
FROM maven:3.9.6-amazoncorretto-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Bước 2: Chạy ứng dụng
FROM amazoncorretto:17-alpine
WORKDIR /app
# Sử dụng ký tự đại diện để bốc chính xác file jar được sinh ra
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080

# Thêm cấu hình headless=true ngay trong lệnh chạy
ENTRYPOINT ["java", "-Djava.awt.headless=true", "-jar", "app.jar"]
