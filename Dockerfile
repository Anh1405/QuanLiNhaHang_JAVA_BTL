# --- Giai đoạn 1: Compile và build toàn bộ code ---
FROM maven:3.9.6-amazoncorretto-17 AS build
WORKDIR /app
COPY . .
# Lệnh này vừa tải thư viện vừa biên dịch sạch sẽ toàn bộ project
RUN mvn clean compile dependency:copy-dependencies -DskipTests

# --- Giai đoạn 2: Chạy trực tiếp từ thư mục Class ---
FROM amazoncorretto:17-alpine
WORKDIR /app

# Thiết lập môi trường không đồ họa Swing
ENV JAVA_OPTS="-Djava.awt.headless=true"

# Copy toàn bộ các file class đã biên dịch và thư viện sang
COPY --from=build /app/target/classes /app/classes
COPY --from=build /app/target/dependency /app/dependency

EXPOSE 8080

# Chạy trực tiếp class chính bằng cách chỉ định classpath rõ ràng
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -cp /app/classes:/app/dependency/* org.example.RestaurantApplication"]
