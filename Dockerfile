# Используем официальный образ OpenJDK как базовый
FROM openjdk:17-jdk-slim

# Устанавливаем рабочий каталог
WORKDIR /app

# Копируем JAR файл приложения
COPY target/ReactIqManagerC.jar app.jar

# Открываем порт, на котором приложение будет слушать
EXPOSE 8081

CMD ["java", "-jar", "app.jar", "--spring.profiles.active=dev"]
