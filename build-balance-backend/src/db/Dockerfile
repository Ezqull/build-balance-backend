FROM gradle:8.6-jdk17 as builder

# Skopiuj projekt do obrazu
COPY --chown=gradle:gradle . /home/gradle/db

# Ustaw katalog roboczy
WORKDIR /home/gradle/db

# Uruchom migracje Flyway
CMD ["gradle", "build", "--no-daemon"]

# Jeśli chcesz zbudować kod źródłowy lub uruchomić testy, możesz to zrobić tutaj
# RUN gradle build -x test --no-daemon