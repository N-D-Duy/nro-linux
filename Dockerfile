# Stage 1: Build the project with Ant using JDK 8
FROM eclipse-temurin:8-jdk AS build
WORKDIR /app

# Install Ant
RUN apt-get update && apt-get install -y ant

# Copy the project files to the container
COPY . .

# Set the classpath to include the libraries in the lib directory
ENV CLASSPATH=/app/lib/*

# Run the Ant build and ensure the JAR file is created
RUN ant -f build.xml -v

# Stage 2: Create a minimal runtime environment with Java 8
FROM eclipse-temurin:8-jre
WORKDIR /app

# Entry point to run the application with classpath including the libraries
ENTRYPOINT ["java", "-cp", "/app/dist/Kamui.jar:/app/lib/*", "ServerData.Server.ServerManager"]
