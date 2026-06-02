# Escala de Plantões

Projeto desenvolvido como teste técnico para gerenciamento de escalas de plantão de profissionais da saúde.

## Tecnologias

- Java 17
- Spring Boot
- Spring Data JPA
- H2 Database
- Swagger OpenAPI
- Maven
- Lombok

## Funcionalidades

### Profissionais

- Cadastro de profissionais
- Validação de CRM/COREN único
- Busca por categoria

### Plantões

- Cadastro de plantões
- Associação de profissional
- Controle de turnos

### Regras de Negócio

- Impede profissional duplicado no mesmo turno
- Valida carga horária semanal
- Tratamento de exceções com HTTP 400

## Executando o projeto

```bash
mvn spring-boot:run