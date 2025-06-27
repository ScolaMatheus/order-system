# 🧾 Order System - Microserviços com Spring Boot, RabbitMQ e PostgreSQL

Este projeto é um sistema gerenciador de pedidos distribuído, baseado em **microserviços** utilizando **Spring Boot**, comunicação via **RabbitMQ**, persistência em **PostgreSQL** e documentação com **Swagger**.

---
### 🧭 Fluxo de Eventosta

- Cliente → `customer-service`: `POST /api/pedidos`
- `customer-service` → Exchange: `pedido.status.criado`
- Consumidores:
   - `order-management`: salva pedido
   - `restaurant-service`: valida pedido
      - Se válido → `pedido.status.preparando`
      - Se inválido → `pedido.status.cancelado`
- Todos escutam os eventos `preparando`/`cancelado`
- Quando o preparo do pedido for concluído:
   - `restaurant-service` chama endpoint `POST /pedidos/{id}/em-rota`
   - Publica `pedido.status.em-rota`
   - Aplicações envolvidas atualizam status conforme evento
- Quando o pedido for entregue:
   - `customer-service` chama endpoint `PATCH /clientes/pedidos/{id}/entrega`
   - Publica `pedido.status.entregue`
   - Aplicações envolvidas atualizam status conforme evento
---

## 📦 Microserviços

### 1. customer-service
- CRUD de clientes
- Envia pedidos via evento
- Consome eventos de atualização de status
- Informa pedido entregue

### 2. order-management
- Gerencia e persiste pedidos
- Atualiza status com base nos eventos recebidos

### 3. restaurant-service
- Gerencia restaurantes e itens
- Valida pedidos e responde via evento
- Informa quando pedido estiver em rota de entrega

---

## 🧪 Tecnologias utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- RabbitMQ
- PostgreSQL
- Swagger/OpenAPI
- Docker e Docker Compose
- Lombok

---

## 📦 Execução do projeto

Requisitos:
- Java 17+
- Maven
- Docker

### 1. Subir os containers com Docker Compose
```bash
docker-compose up -d
```

### 2. ‘Build’ e execução de cada serviço
```bash
cd customer-service
./mvnw clean install
java -jar target/customer-service.jar
```

Repita para os outros serviços (`order-management`, `restaurant-service`).

---

## 📜 Documentação Swagger

- customer-service: `http://localhost:8081/swagger-ui.html`
- order-management: `http://localhost:8082/swagger-ui.html`
- restaurant-service: `http://localhost:8083/swagger-ui.html`

---

## 🔁 Comunicação via eventos

| Evento                        | Emissor             | Consumidor(es)                           |
|------------------------------|---------------------|------------------------------------------|
| `pedido.status.criado`       | customer-service    | order-management, restaurant-service     |
| `pedido.status.preparando`   | restaurant-service  | customer-service, order-management       |
| `pedido.status.cancelado`    | restaurant-service  | customer-service, order-management       |
| `pedido.status.em-rota`      | restaurant-service  | customer-service, order-management       |
| `pedido.status.entregue`     | customer-service    | restaurant-service, order-management       |

---

## 🛡️ Boas práticas aplicadas

- Separação de responsabilidades por microsserviço
- Comunicação assíncrona orientada a eventos
- Tratamento de exceções centralizado
- Testes unitários por serviço
- Swagger para documentação de APIs
---

## 🧑‍💻 Autor

Projeto desenvolvido por Matheus Scola com fins educacionais e de demonstração de arquitetura de microserviços com Spring Boot.
