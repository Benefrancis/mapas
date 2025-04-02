# UnidadeConservacao - Importação de Dados da API CNUC

Este projeto Spring Boot foi criado para importar dados de Unidades de Conservação (UCs) a partir da API do Cadastro
Nacional de Unidades de Conservação (CNUC) e persistí-los em um banco de dados PostgreSQL.

## Pré-requisitos

Antes de começar, certifique-se de ter instalado:

* **Java Development Kit (JDK)**: Versão 21 ou superior.
* **Maven**: Para gerenciar as dependências e construir o projeto.
* **PostgreSQL**: Um servidor PostgreSQL em execução.
* **Banco de Dados**: Um banco de dados chamado `mapas` com um schema `uni_conservacao` já criado.
* **Lombok Plugin**: Instale o plugin Lombok na sua IDE para que as anotações Lombok funcionem corretamente.

## Configuração

1. **Clone o Repositório:**

   ```bash
   git clone <URL_DO_SEU_REPOSITORIO>
   cd unidadeconservacao
   ```

2. **Configure o Banco de Dados:**

   Edite o arquivo `src/main/resources/application.yaml` e insira as credenciais do seu banco de dados PostgreSQL:

   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/mapas
       username: your_user
       password: your_password
     jpa:
       hibernate:
         ddl-auto: none
       properties:
         hibernate:
           dialect: org.hibernate.dialect.PostgreSQLDialect
           format_sql: true
           show_sql: true
         org:
           hibernate:
             envers:
               audit_table_suffix: _AUDIT
               revision_field_name: REV
               revision_type_field_name: REVTYPE
   ```

    * `spring.datasource.url`: URL de conexão com o banco de dados.
    * `spring.datasource.username`: Nome de usuário do banco de dados.
    * `spring.datasource.password`: Senha do banco de dados.
    * `spring.jpa.hibernate.ddl-auto`: Define como o Hibernate deve lidar com o esquema do banco de dados. `update`
      atualizará o esquema se necessário.
    * `spring.jpa.properties.hibernate.dialect`: Dialeto do Hibernate para PostgreSQL.
    * `spring.jpa.properties.hibernate.format_sql`: Formata as queries SQL para facilitar a leitura nos logs.
    * `spring.jpa.properties.hibernate.show_sql`: Exibe as queries SQL nos logs.
    * `spring.jpa.properties.hibernate.default_schema`: Garante que o hibernate use o schema configurado

3. **Configure o Logback (Opcional):**

   O arquivo `src/main/resources/logback.xml` configura o sistema de logging. Você pode ajustar os níveis de log e os
   appenders conforme necessário.

4. **Execute a Aplicação:**

   ```bash
   mvn spring-boot:run
   ```

   A aplicação estará disponível em `http://localhost`.

## Importação de Dados

Para iniciar a importação de dados, acesse o endpoint `/import` com os parâmetros `startId` e `endId` para definir o
intervalo de IDs das UCs a serem importadas.

```
http://localhost:8080/import?startId=19560&endId=19570
```

* `startId`: ID da UC inicial.
* `endId`: ID da UC final.

A aplicação fará o seguinte:

1. Buscará os dados da API CNUC para cada ID no intervalo especificado.
2. Fará o parsing dos dados JSON e criará objetos Java correspondentes às entidades do banco de dados.
3. Persistirá as entidades no banco de dados PostgreSQL.

O processo de importação é executado de forma assíncrona, utilizando um pool de threads para acelerar o processo.

## Entidades

As entidades principais do projeto são:

* **UnidadeConservacao**: Representa uma Unidade de Conservação.
* **Contatos**: Representa as informações de contato de uma Unidade de Conservação.
* **AtosLegais**: Representa os atos legais relacionados a uma Unidade de Conservação.
* **Bioma**: Representa os biomas presentes na Unidade de Conservação.
* ... (e outras entidades correspondentes às tabelas do banco de dados)

As entidades estão anotadas com Lombok para geração automática de código (getters, setters, construtores, etc.) e com
JPA para mapeamento objeto-relacional.

## Tratamento de Erros

A aplicação implementa um tratamento de erros robusto, com:

* **Exceções Personalizadas**: Para representar erros específicos (ex: `ApiFetchException`, `DataParseException`,
  `DataPersistenceException`).
* **Tratamento Global de Exceções**: Um `@ControllerAdvice` que captura as exceções e retorna respostas HTTP
  apropriadas.
* **Logging**: Logs detalhados para facilitar o rastreamento de problemas.
* **Retentativas (Retry)**: Em caso de falha ao buscar dados da API, a aplicação tentará novamente algumas vezes.

## Multithreading

O processo de importação é executado de forma assíncrona, utilizando um pool de threads para acelerar o processo. O
número de threads pode ser configurado no `UnidadeConservacaoController`.

## Envers (Versionamento)

A aplicação utiliza o Hibernate Envers para versionar as entidades. Sempre que uma entidade é modificada, uma nova
revisão é criada e os dados antigos são armazenados em uma tabela de auditoria.

## Próximos Passos

* Implementar as classes de entidade restantes para todas as tabelas no banco de dados.
* Atualizar a lógica de parsing JSON para preencher corretamente todos os campos das entidades.
* Escrever testes unitários e de integração para verificar se o código está funcionando corretamente.
* Monitorar o desempenho da aplicação e identificar gargalos.
* Otimizar o código para melhorar o desempenho e a escalabilidade.


