# Użyj obrazu Gradle z JDK 17 do zbudowania projektu
FROM gradle:8.6-jdk17 AS build

# Ustawiamy katalog roboczy
WORKDIR /home/gradle/src

# Kopiujemy konfigurację Gradle oraz kod źródłowy
COPY --chown=gradle:gradle . /home/gradle/src

# Uruchamiamy migracje Flyway. Zmienna środowiskowa SPRING_DATASOURCE_URL powinna być ustawiona
# na adres serwisu `postgres` z pliku docker-compose.yml
RUN gradle flywayMigrate -i