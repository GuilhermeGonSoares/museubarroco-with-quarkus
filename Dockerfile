# Etapa de construção
FROM maven:3.9.9-sapmachine-21 as build

# Diretório de trabalho na imagem
WORKDIR /build

# Copiar arquivos do diretório atual para o diretório de trabalho na imagem
COPY . .

# Executar mvn clean package, ignorando os testes
RUN mvn clean package -DskipTests

# Etapa de execução
FROM registry.access.redhat.com/ubi8/openjdk-21:1.19

# Definir o diretório de trabalho
WORKDIR /usr/app

# Copiar tudo da pasta quarkus-app
COPY --from=build /build/target/quarkus-app/ ./

# Copiar os arquivos de chave para o diretório de trabalho
COPY --from=build /build/src/main/resources/privateKey.pem /usr/app/privateKey.pem
COPY --from=build /build/src/main/resources/publicKey.pem /usr/app/publicKey.pem

# Expõe a porta 80
EXPOSE 80

# Comando para executar a aplicação
CMD ["java", "-jar", "quarkus-run.jar"]