# 🧾 Order System - Microserviços com Spring Boot, RabbitMQ e PostgreSQL

Este projeto é um sistema distribuído de **gestão de pedidos**, desenvolvido com foco em arquitetura de **microserviços**, utilizando:

- **Spring Boot**
- **RabbitMQ** para comunicação assíncrona
- **PostgreSQL** para persistência
- **Swagger** para documentação das APIs
- **Docker Compose** para orquestração

---
### 🧭 Fluxo de Eventosta

1. O cliente realiza um pedido via `customer-service`:
    - `POST /api/pedidos`
2. O serviço publica o evento `pedido.status.criado`
3. Esse evento é consumido por:
    - `order-management`: persiste o pedido
    - `restaurant-service`: valida os itens e responde com:
        - `pedido.status.preparando` (sucesso)
        - `pedido.status.cancelado` (falha)
4. Todos os serviços consomem os eventos e atualizam seus estados
5. Quando o restaurante finaliza a preparação:
    - Chama `POST /pedidos/{id}/em-rota`
    - Publica o evento `pedido.status.em-rota`
6. Quando o pedido é entregue:
    - `customer-service` chama `PATCH /clientes/pedidos/{id}/entrega`
    - Evento `pedido.status.entregue` é disparado

---

## 📦 Microserviços

### 1. customer-service
- CRUD de clientes
- Criação de pedidos
- Consome eventos de status
- Publica evento de entrega

### 2. order-management
- Persistência e gestão de pedidos
- Atualização de status via eventos

### 3. restaurant-service
- CRUD de restaurantes e itens do cardápio
- Validação de pedidos
- Geração de eventos `preparando`, `cancelado` e `em-rota`

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

## 🚀 Como executar o projeto

### ✅ Requisitos

- Docker + Docker Compose

### ▶️ Subindo tudo com Docker Compose

```bash
docker-compose up -d
```

Os serviços serão inicializados automaticamente com as imagens públicas do Docker Hub.

---

## 📜 Documentação Swagger

- customer-service: `http://localhost:8081/swagger-ui.html`
- order-management: `http://localhost:8082/swagger-ui.html`
- restaurant-service: `http://localhost:8083/swagger-ui.html`

> Obs: as portas podem ser ajustadas no `docker-compose.yml`.

---

## 🔁 Comunicação via eventos

| Evento                        | Emissor             | Consumidores                         |
|------------------------------|---------------------|--------------------------------------|
| `pedido.status.criado`       | customer-service    | order-management, restaurant-service |
| `pedido.status.preparando`   | restaurant-service  | customer-service, order-management   |
| `pedido.status.cancelado`    | restaurant-service  | customer-service, order-management   |
| `pedido.status.em-rota`      | restaurant-service  | customer-service, order-management   |
| `pedido.status.entregue`     | customer-service    | restaurant-service, order-management |

---

## 🛡️ Boas práticas aplicadas

- Arquitetura baseada em eventos (event-driven)
- Separação clara de domínios por microsserviço
- Tolerância a falhas com `depends_on + healthcheck`
- Dockerização com imagens públicas e plug-and-play
- Swagger para documentação de cada API
- Testes unitários por contexto de serviço
---

## 🧑‍💻 Autor

Projeto desenvolvido por Matheus Scola com fins educacionais e de demonstração de arquitetura de microserviços com Spring Boot.
