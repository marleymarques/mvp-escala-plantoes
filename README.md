# Escala de Plantões

MVP fullstack para gestão simples de escala semanal de plantões em contexto hospitalar.

O objetivo do projeto é demonstrar modelagem, API REST, regras de negócio, testes automatizados e uma visualização simples da grade semanal.

## Escopo atendido

- Cadastro de profissionais.
- Listagem de profissionais com filtro por categoria.
- Cadastro de plantões por profissional, data e turno.
- Exclusão de profissional.
- Consulta da escala por semana.
- Grade semanal simples no navegador.
- Destaque visual para profissional que atingiu o limite semanal contratado.
- Validação de duplicidade de plantão no mesmo dia e turno.
- Validação de carga horária semanal contratada.
- Tratamento de erro de regra de negócio com HTTP 400.
- Testes unitários das regras principais.

## Tecnologias

- Java 17
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Bean Validation
- H2 Database em memória
- Lombok
- JUnit 5
- Mockito
- HTML, CSS e JavaScript puro para a tela simples da grade
- Swagger/OpenAPI

## Decisões técnicas

### Backend primeiro

Como o teste avalia principalmente regras de negócio, modelagem, API e clareza de implementação, a lógica principal foi concentrada na camada de service.

Controllers recebem e expõem os dados pela API REST. Repositories ficam responsáveis somente pelo acesso aos dados. As validações de escala ficam em `PlantaoService`.

### Banco H2

Foi utilizado H2 em memória para facilitar a execução local e evitar dependência de instalação de banco externo.

### Frontend simples

A interface foi criada com HTML, CSS e JavaScript puro em `src/main/resources/static/index.html`.

Essa escolha reduz complexidade, evita dependências extras e atende ao requisito de frontend funcional e navegável.

### Grade semanal

A consulta semanal retorna um DTO próprio, em vez de devolver diretamente as entidades JPA. Isso deixa a resposta mais clara para o frontend e evita acoplamento desnecessário.

### Regras de negócio

Foram implementadas duas regras obrigatórias:

1. Um profissional não pode ter dois plantões no mesmo dia e turno.
2. A carga horária alocada na semana não pode ultrapassar a carga contratada.

Para carga horária, foram considerados:

- MANHA = 6h
- TARDE = 6h
- NOITE = 12h

## Como executar

Na raiz do projeto:

```bash
mvn spring-boot:run
```

A aplicação ficará disponível em:

```text
http://localhost:8080
```

A tela da grade semanal pode ser acessada em:

```text
http://localhost:8080/index.html
```

O console do H2 fica disponível em:

```text
http://localhost:8080/h2-console
```

Configuração H2:

```text
JDBC URL: jdbc:h2:mem:escala
User: sa
Password: vazio
```

## Como executar os testes

```bash
mvn test
```

## Endpoints principais

### Health check

```http
GET /hello
```

### Profissionais

Criar profissional:

```http
POST /api/profissionais
Content-Type: application/json
```

```json
{
  "nome": "Dra. Ana Souza",
  "crmCoren": "CRM12345",
  "categoria": "MEDICO",
  "cargaHorariaSemanal": 20
}
```

Listar todos:

```http
GET /api/profissionais
```

Filtrar por categoria:

```http
GET /api/profissionais?categoria=MEDICO
```

Buscar por ID:

```http
GET /api/profissionais/1
```

### Plantões

Criar plantão:

```http
POST /api/plantoes
Content-Type: application/json
```

```json
{
  "data": "2026-06-02",
  "turno": "MANHA",
  "profissional": {
    "id": 1
  }
}
```

Consultar escala semanal:

```http
GET /api/plantoes/semana?dataInicial=2026-06-01
```

Exemplo de resposta resumida:

```json
{
  "dataInicial": "2026-06-01",
  "dataFinal": "2026-06-07",
  "dias": [
    "2026-06-01",
    "2026-06-02",
    "2026-06-03",
    "2026-06-04",
    "2026-06-05",
    "2026-06-06",
    "2026-06-07"
  ],
  "profissionais": [
    {
      "profissionalId": 1,
      "nome": "Dra. Ana Souza",
      "categoria": "MEDICO",
      "cargaHorariaSemanal": 20,
      "horasAlocadas": 18,
      "limiteAtingido": false,
      "dias": {
        "2026-06-01": "MANHA",
        "2026-06-02": "TARDE"
      }
    }
  ]
}
```

## Categorias disponíveis

```text
MEDICO
ENFERMEIRO
TECNICO
```

## Turnos disponíveis

```text
MANHA - 07h às 13h - 6h
TARDE - 13h às 19h - 6h
NOITE - 19h às 07h - 12h
```

## Estrutura principal

```text
src/main/java/br/com/marley/escala
├── controller
├── dto
├── entity
├── enums
├── exception
├── repository
└── service
```

## Pontos preparados para evolução

- Inclusão de endpoint específico para excluir plantão.
- Melhorias de validação com DTOs de entrada.
- Separação entre frontend e backend em aplicações independentes.
- Autenticação, caso o módulo deixe de ser apenas avaliativo.
- Cadastro de unidades, especialidades e regras de cobertura mínima.
- Geração automática de escala.

## Observação

Este projeto é um MVP avaliativo. A prioridade foi entregar uma implementação simples, legível e defensável em revisão técnica, com as regras centrais bem localizadas e testáveis.
