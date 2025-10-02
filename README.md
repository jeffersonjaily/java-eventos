# ☕ Sistema de Gerenciamento de Eventos

![Java](https://img.shields.io/badge/Java-JDK%2017-blue)
![GUI](https://img.shields.io/badge/GUI-Java%20Swing-orange)
![Status](https://img.shields.io/badge/Status-Concluído-brightgreen)

## 📖 Descrição do Projeto

Este é um projeto de um sistema de desktop para gerenciamento de eventos, desenvolvido em Java com a biblioteca Swing para a interface gráfica. A aplicação permite que usuários se cadastrem, façam login e interajam com eventos urbanos, inscrevendo-se ou cancelando suas participações.

O projeto foi estruturado com uma separação clara de responsabilidades (Modelo, Serviço e Visão), e os dados são persistidos localmente em arquivos de texto (`users.data` e `events.data`).

## ✨ Funcionalidades

-   **Cadastro de Usuários:** Permite que novos usuários se cadastrem no sistema fornecendo nome e e-mail.
-   **Login por ID:** Autenticação simples de usuários através de um ID único gerado no cadastro.
-   **Cadastro de Eventos:** Formulário para criar novos eventos com nome, endereço, descrição, categoria e data/hora.
-   **Visualização de Eventos:**
    -   Lista de todos os próximos eventos, ordenados por data.
    -   Lista de eventos em que o usuário logado está inscrito.
-   **Inscrição e Cancelamento:** Permite que o usuário logado se inscreva ou cancele a participação em qualquer evento futuro.
-   **Persistência de Dados:** Todas as informações de usuários e eventos são salvas localmente, garantindo que os dados não se percam ao fechar a aplicação.

## 💻 Tecnologias Utilizadas

-   **Java 17 (JDK)**
-   **Java Swing** para a Interface Gráfica (GUI)
-   **Git & GitHub** para versionamento e hospedagem do código.

## 🚀 Como Executar o Projeto

Siga os passos abaixo para compilar e executar o projeto localmente.

### Pré-requisitos

Antes de começar, você vai precisar ter instalado em sua máquina:
-   [JDK (Java Development Kit)](https://www.oracle.com/java/technologies/downloads/) - Versão 17 ou superior.
-   [Git](https://git-scm.com/downloads)


## 📖 Como Usar

1.  Ao iniciar a aplicação, a tela de login será exibida.
2.  Como não há usuários, clique no botão **"Cadastrar Novo Usuário"**.
3.  Preencha seu nome e e-mail nas caixas de diálogo. Uma mensagem de sucesso mostrará seu **ID único**. Anote este número.
4.  Na tela de login, insira o ID que você anotou e clique em **"Entrar"**.
5.  A tela principal será aberta, e você poderá cadastrar e interagir com os eventos.

<<<<<<< HEAD
---
=======
---
>>>>>>> ffce1247d790f732728220d948c34e67f7f973e9
