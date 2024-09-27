# nro-linux
To start, add your docker-compose.yaml file like:
```
services:
  app:
    build: .
    image: <YOUR-IMAGE-NAME>
    container_name: <YOUR-CONTAINER-NAME>
    ports:
      - "14445:14445"
    volumes:
      - .:/app
    networks:
      - my-network
    depends_on:
      db:
        condition: service_healthy

  db:
    image: mysql:5.7
    container_name: mysql-service
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: <YOUR-PASSWORD>
      MYSQL_DATABASE: <YOUR-DATABASE-NAME>
    volumes:
      - ./init/sqlKamui.sql:/docker-entrypoint-initdb.d/script.sql:ro
    ports:
      - "3306:3306"
    networks:
      - my-network
    healthcheck:
      test: ["CMD-SHELL", "mysqladmin ping -h localhost -p<YOUR-PASSWORD>"]
      interval: 10s
      timeout: 5s
      retries: 3

networks:
  my-network:
    driver: bridge
```

# CHANGE THE CREDENTIALS
- data/config/girlkundb.properties
- data/girlkun/girlkun.properties