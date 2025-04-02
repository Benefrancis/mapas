# Usar uma imagem base do Maven com JDK para compilar o projeto Spring Boot
FROM maven:3.8.6-openjdk-18-slim AS build

# Definir o mantenedor da imagem
LABEL maintainer="Benefrancis"
LABEL description="Mapas - Georeferenciamento"

# Criar um diretório de trabalho para o backend
WORKDIR /app

# Copiar o arquivo pom.xml e as dependências do Maven
COPY pom.xml .

# Baixar as dependências do Maven, utilizando o cache
RUN --mount=type=cache,target=/root/.m2 mvn dependency:go-offline -B

# Copiar o código-fonte do projeto para o contêiner
COPY src ./src

# Compilar o projeto e gerar o JAR
RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests

# Usar uma imagem base mais enxuta para a execução do JAR
FROM openjdk:18-alpine AS deploy

# Criar um diretório para a aplicação
WORKDIR /app

# Copiar o JAR gerado no estágio anterior para o diretório de trabalho
COPY --from=build /app/target/*.jar /app/mapas.jar

# Copiar recursos estáticos e templates de uma vez
COPY src/main/resources/templates /app/src/main/resources/templates
COPY src/main/resources/static /app/src/main/resources/static

# Expor a porta 80
EXPOSE 80

# Defina o diretório app com permissão de execução
RUN chmod +x /app

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "/app/mapas.jar"]