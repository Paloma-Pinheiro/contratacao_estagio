# Contratação de Estágio

Participantes: Breno Guimarães Gatti, Guilherme Machado da Silva, Gustavo Marques Pereira e Paloma de Queiroz Pinheiro.

## Como executar
1. Crie o banco `contratacao_estagio` no MariaDB (`CREATE DATABASE contratacao_estagio;`).
2. Execute o script `sql/procedures.sql` para criar a stored procedure.
3. Ajuste usuário e senha em `src/main/resources/application.properties`.
4. Rode `./mvnw spring-boot:run` e acesse `http://localhost:8080`.

## Itens de avaliação
| Item | Onde está |
|---|---|
| Conexão com BD | `application.properties` (JDBC MariaDB via Spring Data JPA) |
| Inserção / Alteração / Exclusão / Consulta | `ContratoService`, `AlunoService`, `EmpresaService` |
| Relatório | `GET /contrato/relatorio` e dashboard (`/`) |
| Stored Procedure | `sql/procedures.sql` + `POST /contrato/encerrar-vencidos` |
