# OrderSystem – Sistema de Pedidos com Microserviços

Este repositório contém o sistema **OrderSystem**, uma aplicação baseada em micro-serviços construída com **Spring Boot**, **RabbitMQ**, **PostgreSQL** e **Docker**, utilizando arquitetura orientada a eventos. O objetivo é simular um sistema de pedidos no qual diferentes serviços se comunicam entre si de forma desacoplada e assíncrona.

---

## 📦 Estrutura do Projeto

O sistema está organizado em três micro-serviços:

### 1. `customer-service`
Responsável pela gestão de clientes. Permite criar, buscar, atualizar e deletar informações dos clientes.
> Exemplo de dados: nome, CPF, telefone, e-mail.

### 2. `order-management`
Centraliza o fluxo dos pedidos. É o serviço orquestrador que recebe requisições de novos pedidos, valida se o cliente e o restaurante existem e publica eventos de criação ou cancelamento de pedidos via RabbitMQ.

### 3. `restaurant-service`
Gerencia os restaurantes e os itens do cardápio. Também é responsável por validar se um restaurante e seus produtos existem e estão disponíveis quando um pedido é criado.

---

## 🧩 Tecnologias Utilizadas

- Java com Spring Boot
- PostgreSQL (banco separado por serviço)
- RabbitMQ (mensageria entre serviços)
- Docker e Docker Compose
- Swagger (documentação das APIs)

---

## 🛠️ Como Rodar o Projeto

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/order-system.git
   cd order-system
   
2. Suba os containers com Docker:
    ````bash
   docker-compose up
3. Execute cada micro-serviço individualmente (via a sua IDE ou terminal):
- customer-service
- order-management
- restaurant-service

## 📚 Em Desenvolvimento
- Validação de pedidos com itens inválidos

- Confirmação de recebimento de mensagens RabbitMQ

- Documentação de endpoints no Swagger

## 🧑‍💻 Autor
Desenvolvido por Matheus Scola\
[linkedin](https://www.linkedin.com/in/matheus-scola/)