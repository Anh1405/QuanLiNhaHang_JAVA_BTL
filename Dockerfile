# Bước 1: Sử dụng môi trường Maven kết hợp Amazon Corretto 17 để build code nguồn
FROM maven:3.9.6-amazoncorretto-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Bước 2: Sử dụng môi trường Java Amazon Corretto 17 gọn nhẹ để chạy ứng dụng
FROM amazoncorretto:17-alpine
WORKDIR /app
COPY --from=build /app/target/QLNH_BTL-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
