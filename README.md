
<br id="topo"> <h1 align="center"> Projeto Paz 🕊️ </h1> <p 
align="center"> <a href="#descgeral">Descrição Geral</a> | <a 
href="#equipe">Equipe</a> | <a 
href="#fundamentacao">Fundamentação Teórica</a> | <a href="#publicoalvo">Público-Alvo</a> | <a href="#metodologia">Metodologia</a> | <a href="#tecnologias">Tecnologias</a> | <a href="#instalacao">Instalação e Execução</a> 

----------

<span id="descgeral">

## 📑 Descrição Geral

O **Projeto Paz** é uma iniciativa voltada ao desenvolvimento de uma **plataforma multiplataforma (mobile, web e desktop)**, criada em **Kotlin Multiplatform**, com o objetivo de **otimizar a contabilidade e gestão de vendas informais**.

A aplicação automatiza o **registro de vendas, controle de estoque, visualização de pedidos em tempo real, autenticação de usuários e geração de relatórios**, promovendo **profissionalização e confiabilidade** para vendedores informais, voluntários e trabalhadores comissionados.

→ [Voltar ao topo](#topo)

----------
<span id="equipe">

## 👥 Equipe

- Arthur Juan Farias Lima --- [GitHub](https://github.com/arthurxzl)
- Guilherme Cabral de Lima --- [GitHub](https://github.com/Guilherme-Cabral-de-Lima)
- Gustavo Costa Oliveira --- [GitHub](https://github.com/Guh1254)
- João Victor Alexandre da Silva --- [GitHub](https://github.com/JoaoV-A01)
- Lucas José Gomes da Silva --- [GitHub](https://github.com/LukasJSilva)
- Murilo Vieira Nascimento --- [GitHub](https://github.com/nasc2005)


→ [Voltar ao topo](#topo)

<span id="fundamentacao">
----------

## 📚 Fundamentação Teórica

A motivação do Projeto Paz surgiu da observação de eventos religiosos, nos quais vendedores informais enfrentam dificuldades em **controlar vendas, registrar dados e acompanhar resultados financeiros**.

Entrevistas com membros de organizações religiosas evidenciaram a **ausência de ferramentas digitais eficientes**, o que reforçou a necessidade de uma solução multiplataforma acessível e automatizada.

### ⚖️ Aspectos Jurídicos

-   **Decreto-Lei nº 2.041/1940** — Reconhece o comércio ambulante.
    
-   **Lei nº 4.886/1965** — Regula a atividade do representante comercial autônomo.
    
-   **Lei nº 8.420/1992** — Ajustes na legislação de representação comercial.
    

Esses dispositivos fornecem **base legal indireta** para o público-alvo do projeto.

→ [Voltar ao topo](#topo)

----------

<span id="publicoalvo">

## 🎯 Público-Alvo

Vendedores informais, voluntários e trabalhadores comissionados em **eventos religiosos, comunitários e sociais** que enfrentam dificuldades de organização, gestão financeira e registro de vendas.

### 💡 Proposta de Valor

-   Agilidade nas vendas e no controle de estoque
    
-   Redução de erros com automação
    
-   Relatórios para decisões estratégicas
    
-   Interface acessível em diferentes plataformas
    

→ [Voltar ao topo](#topo)

----------

<span id="metodologia">

## 📕 Metodologia

O projeto segue o framework **Scrum**, com sprints iterativas e reuniões de acompanhamento:

-   **Planning:** definição das tarefas da sprint
    
-   **Daily Scrum:** alinhamento rápido e resolução de bloqueios
    
-   **Sprint Review:** demonstração das entregas concluídas
    
-   **Retrospective:** identificação de melhorias contínuas
    

Também é utilizado **Kanban** para visualização e controle de tarefas no GitHub Projects.

→ [Voltar ao topo](#topo)

----------

<span id="tecnologias">

## 🛠️ Tecnologias Utilizadas

### 🚀 Arquitetura Geral

Este projeto é uma modernização da aplicação "Projeto Paz", implementado com uma arquitetura de Backend em Spring Boot com Kotlin e um Frontend compartilhado para Desktop, Web e Mobile usando Kotlin Multiplatform (KMP) com Compose Multiplatform.

----------

### 🔧 Backend

-   **Kotlin**

-   **Spring Boot**
    
-   **Spring Data JPA**
    
-   **MySQL**
    
-   **Swagger/OpenAPI** (documentação da API)
    

----------

### 💻 Frontend

-   **Kotlin Multiplatform + Compose Multiplatform**
   ----------
   
   ### 💻 Comunicação
        
-   **API REST com Ktor Client** 
    
-   **Material Design Compose**
----------
   ### 💻 Documentação da API
   
   -   **Swagger/OpenAPI** 


→ [Voltar ao topo](#topo)

----------

<span id="instalacao">

## ⚙️ Instalação e Execução

### 📋 Pré-requisitos

Certifique-se de ter instalado:

-   ☕ **Java JDK 17** — Necessário para o backend
    
-   💡 **IntelliJ IDEA** — IDE recomendada
    
-   🤖 **Android Studio** — Para o Android SDK e emuladores
    
-   🐬 **MySQL** — Banco de dados relacional
    
-   🌐 **Node.js (LTS)** — Para build e execução Web
    

----------

### 🧰 Configuração do Ambiente

#### 1️⃣ Clonando o Repositório

`git clone https://github.com/SpringDevsFatec/ProjetoPaz_KotlinMultiplatforma.git` 

Abra a pasta `ProjetoPaz_KotlinMultiplatforma` no IntelliJ IDEA.  
O IntelliJ reconhecerá automaticamente os módulos **backend** e **frontend-paz**.

----------

#### 2️⃣ Configurando o Backend 

1.  **Crie o banco de dados** no MySQL:
    
    `CREATE DATABASE projetopaz;` 
    
2.  **Configure o arquivo** `backend/src/main/resources/application.properties`:
    
    `spring.datasource.url=jdbc:mysql://localhost:3306/projetopaz?createDatabaseIfNotExist=true
    spring.datasource.username=root
    spring.datasource.password=SUA_SENHA_AQUI
    server.port=8081` 
    
----------

#### 3️⃣ Configurando o Frontend

Mesmo que você rode apenas o Desktop, é necessário configurar o SDK Android.

Crie o arquivo `frontend-paz/local.properties` e adicione:

`sdk.dir=C:/Users/SEU_USUARIO/AppData/Local/Android/Sdk` 

Depois, sincronize o projeto com **"Reload All Gradle Projects"**.

----------

### ▶️ Executando o Projeto

> **Importante:** o backend deve estar rodando antes do frontend.

#### ⚙️ 1. Rodando o Backend

-   No IntelliJ, abra `ProjetoPazKotlinApplication.kt`
    
-   Clique ▶️ ao lado da função `main`
    
-   Aguarde a mensagem:
    
    `Tomcat started on port(s): 8081` 
    
-   Verifique no navegador: [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
    

----------

#### 💻 2. Rodando o Frontend

##### 🖥️ Desktop (JVM)

-   Selecione a configuração `composeApp [jvm]`
    
-   Clique ▶️ **Play** para abrir o app desktop
    

##### 🌐 Web (JS)

-   No painel Gradle → `frontend-paz → composeApp → Tasks → kotlin browser`
    
-   Execute `jsBrowserDevelopmentRun`
    
-   Acesse: [http://localhost:8080](http://localhost:8080)
    

##### 📱 Mobile (Android)

1.  Ative a virtualização (Intel VT-x / AMD-V)
    
2.  Crie um **emulador (AVD)** no Android Studio
    
3.  No IntelliJ, selecione `composeApp [android]`
    
4.  Escolha o emulador e clique ▶️ **Play**
    

> 🔹 O `ApiClient.kt` já usa automaticamente o IP `10.0.2.2` para acessar o backend no emulador.

----------

### 🧩 Observações Importantes

-   O backend deve sempre ser iniciado antes do frontend.
    
-   No emulador Android, use `10.0.2.2:8081`.
    
-   Em dispositivos físicos, substitua pelo IP local da máquina.
    
-   As dependências já estão configuradas via **Gradle**.
    

→ [Voltar ao topo](#topo)

----------

