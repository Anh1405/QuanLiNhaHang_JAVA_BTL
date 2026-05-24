# Bước 1: Sử dụng môi trường Maven kết hợp Amazon Corretto để build code
FROM maven:3.9.6-amazoncorretto-17 AS build
WORKDIR /app

# Copy toàn bộ code hiện tại vào trong container
COPY . .

# 🔥 ĐOẠN ĐƯỜNG DẪN CHUẨN: Tạo thư mục static và copy toàn bộ ruột bên trong restoran-1.0.0 sang
RUN mkdir -p src/main/resources/static
RUN cp -r frontend/restoran-1.0.0/* src/main/resources/static/

# Tiến hành build đóng gói file .jar
RUN mvn clean package -DskipTests

# Bước 2: Sử dụng môi trường chạy Java Corretto siêu gọn nhẹ
FROM amazoncorretto:17-alpine
WORKDIR /app
ENV JAVA_OPTS="-Djava.awt.headless=true"

# Bốc file jar ra để chạy
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
