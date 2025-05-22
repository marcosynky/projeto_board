# Projeto Board - Sistema de Gerenciamento de Boards em Java

## 📖 Sobre

Board-management é uma aplicação Java para gerenciamento de boards e seus cards em um sistema tipo Kanban. O projeto utiliza JDBC para comunicação com banco de dados MySQL, implementa controle completo sobre colunas, cards, bloqueios e movimentações, além de aplicar migrações via Liquibase. Desenvolvido para exemplificar boas práticas em Java e manipulação robusta de dados com controle transacional.

## 🚀 Tecnologias

<div>
  <img src="https://img.shields.io/badge/Java-17-blue?style=for-the-badge&logo=java&logoColor=white" />
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white" />
  <img src="https://img.shields.io/badge/Liquibase-007EC6?style=for-the-badge&logo=liquibase&logoColor=white" />
  <img src="https://img.shields.io/badge/JDBC-007396?style=for-the-badge&logo=java&logoColor=white" />
  <img src="https://img.shields.io/badge/Lombok-FC4F00?style=for-the-badge&logo=lombok&logoColor=white" />
  <img src="https://img.shields.io/badge/JUnit5-5.9.2-green?style=for-the-badge&logo=junit&logoColor=white" />
</div>

Este projeto utiliza as seguintes tecnologias:

- **Java 17**: Linguagem principal para desenvolvimento do backend.
- **MySQL**: Banco de dados relacional para persistência das entidades.
- **Liquibase**: Ferramenta para gerenciamento e versionamento das migrações do banco de dados.
- **JDBC**: API Java para comunicação com banco de dados via SQL.
- **Lombok**: Biblioteca para redução de boilerplate, gerando automaticamente getters, setters, construtores, etc.
- **JUnit 5**: Framework de testes unitários para garantir qualidade do código.

## 📊 Estado do Projeto

![Progresso](https://img.shields.io/badge/Progresso-70%25-yellow?style=for-the-badge&labelColor=000000&color=FFD700&logo=github)

> Atualmente, cerca de 70% das funcionalidades principais estão implementadas e testadas.

##  Autor
<h2>Marco Antônio</h2>

<p>Desenvolvedor Full-Stack </p>

<p>
  <a href="https://github.com/marcosynky" target="_blank">
    <img src="https://img.shields.io/badge/GitHub-000000?style=for-the-badge&logo=github&logoColor=white" />
  </a>
<a href="https://www.linkedin.com/in/marco-antônio-developer-fullstack" target="_blank">
    <img src="https://img.shields.io/badge/LinkedIn-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white" />
</a>

</p>


## 📱 Funcionalidades

- Criação, listagem, exclusão e detalhamento de boards.
- Gerenciamento de colunas dentro de cada board, com tipos distintos (Inicial, Pendente, Final, Cancelamento).
- Criação, movimentação, bloqueio e desbloqueio de cards.
- Controle de bloqueios com registro de motivos e histórico.
- Interface de linha de comando (CLI) para interação básica com o sistema.
- Migrações de banco de dados automatizadas via Liquibase.

## 🛠️ Como Rodar o Projeto

### Pré-requisitos

1. **Java 17+**: Certifique-se de ter o Java 17 ou superior instalado.  
   - [Download do Java](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)

2. **MySQL**: Banco de dados MySQL rodando localmente ou remoto.  
   - [Download do MySQL](https://dev.mysql.com/downloads/mysql/)

3. **Liquibase**: Para rodar as migrações ou garantir que o projeto inicializa o banco corretamente.  
   - [Documentação Liquibase](https://www.liquibase.org/)

### Passos para rodar o projeto

1. Clone o repositório para sua máquina local:

```bash
git clone https://github.com/seuusuario/board-management.git
