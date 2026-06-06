## Estrutura do repositório

```
pact/
└── Backend/
    ├── pom.xml
    ├── db/
    │   ├── schema.sql
    │   └── seed.sql
    └── src/main/java/
        ├── model/
        │   ├── domain/
        │   └── repositories/
        ├── dao/
        ├── service/
        ├── controller/
        ├── exception/
        ├── thread/
        └── util/
```
### Backend

| Pacote | Função                                                                                                                                                                                                                               |
|---|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `model.domain` | Value Objects e enums do domínio (`Email`, `Senha`, `Nome`, `Descricao`, `Preco`, `Quantidade`, `Autenticacao`, `DetalhesProduto`, `Categoria`, `StatusPedido`). Classes imutáveis sem setters — objetos nascem válidos pelo construtor.                                                   |
| `model.repositories` | Entidades persistidas (`Usuario`, `Produto`, `Pedido`, `ProdutoPedido`) — `record`s imutáveis que compõem os VOs do `model.domain`. |
| `dao` | Acesso ao banco de dados via JDBC. Cada classe é responsável por um único tipo de entidade (`UsuarioDAO`, `ProdutoDAO`, `PedidoDAO`). Usa exclusivamente `PreparedStatement` e `try-with-resources`. Nenhuma lógica de negócio aqui. |
| `service` | Regras de negócio da aplicação. Orquestra os DAOs, valida dados e coordena a transação de criação de pedidos com verificação de estoque.                                                                                             |
| `controller` | Camada de menus do console. Delega ao `service` correspondente. Não contém lógica de negócio nem acesso direto ao banco (proibido importar `java.sql`).                                                                                                  |
| `exception` | Exceções customizadas do domínio (`EstoqueInsuficienteException`, `ClienteNaoEncontradoException`, `ProdutoNaoEncontradoException`, `EmailInvalidoException`).                                                                       |
| `thread` | `ProcessadorPedidos` — thread de background que busca pedidos com status `FILA`, processa e atualiza para `ENTREGUE`. Gerencia sua própria conexão JDBC por ciclo, isolada da conexão principal.                                   |
| `util` | Classes utilitárias sem vínculo com o domínio. Contém a `ConnectionFactory`, responsável por fornecer a conexão com o banco a partir das variáveis de ambiente do `.env`.                           |

### Backend/db

| Arquivo | Função |
|---|---|
| `schema.sql` | DDL completo — criação do banco, tabelas, relacionamentos e constraints. Execute este arquivo antes de rodar o projeto. |
| `seed.sql` | Dados de teste para popular o banco em ambiente de desenvolvimento. Opcional. |

### Backend/model/domain

| Arquivo | Função |
|---|---|
| `Autenticacao.java` | Agregado de autenticação (`Email` + `Senha`). Expõe `getEmail()` e `autenticar(Senha tentativa)` — a senha nunca é exposta diretamente. |
| `Categoria.java` | Enum com categorias de produto (atualmente: `DEV`, `MARKETING`, `VIDEOS`, `DESIGN`). |
| `Descricao.java` | VO que valida descrição não nula e não vazia. |
| `DetalhesProduto.java` | `record` agregando `Nome`, `Descricao` e `Preco` (informações imutáveis do produto). |
| `Email.java` | VO que valida email no construtor via regex RFC-aproximada (verifica formato, comprimento e domínio). |
| `Nome.java` | VO que valida não nulo/não vazio. |
| `Preco.java` | VO sobre `BigDecimal` que exige valor estritamente positivo (`> 0`). |
| `Quantidade.java` | VO que valida quantidade não-negativa. |
| `Senha.java` | VO que valida senha (mínimo 8 caracteres, ao menos uma letra e um dígito). Implementa `equals`/`hashCode` para autenticação por valor. `toString()` retorna `"***"` para evitar vazamento em logs. |
| `StatusPedido.java` | Enum de estados do pedido: `ABERTO`, `FILA`, `PROCESSAMENTO`, `ENTREGUE`. |

### Backend/model/repositories (entidades persistidas)

| Arquivo | Função |
|---|---|
| `Usuario.java` | `record` representando usuário persistido (`Long id`, `Nome`, `Autenticacao`, `LocalDateTime createdAt`). Construtor auxiliar define `createdAt` para `now()` quando criado em memória. |
| `Produto.java` | `record` com `id`, `DetalhesProduto`, `isAtivo`, `categoria`, `idVendedor`, `createdAt` e `quantidadeEstoque`. Defaults: `isAtivo = true`, `createdAt = now()`. |
| `ProdutoPedido.java` | `record` representando item de pedido (`idProduto`, `idPedido`, `idProdutoPedido`, `precoUnitario`, `quantidade`). |
| `Pedido.java` | `record` com `id`, `idCliente`, `idVendedor`, `List<ProdutoPedido> itens` e `StatusPedido status`. Construtor compacto: status default `ABERTO`, garante que `itens` não seja nulo nem vazio, e usa `List.copyOf` para imutabilidade defensiva. |

Observações gerais

- Os VOs seguem a ideia de objetos imutáveis nascendo válidos no construtor (alinhado ao enunciado e a Object Calisthenics).
- `Senha` nunca expõe o valor cru — autenticação é feita por comparação de VO via `equals`.
- Falta integração com DAOs/`dao/` que façam as operações JDBC com `try-with-resources` e `PreparedStatement`.
- Para operações de pedido, a aplicação deve garantir atualizações condicionais no banco para evitar estoque negativo (por exemplo `UPDATE produto SET estoque = estoque - ? WHERE id = ? AND estoque >= ?`).

Use este arquivo como referência rápida ao implementar DAOs e serviços que consumirão esses modelos.
### Requisitos gerais

O computador precisa ter as seguintes dependencias instaladas e no `PATH`: 

- Java 21
- Maven
- XAMPP (ou outro gerenciador de banco de dados / mysql server / docker)
### Configuração do Banco de Dados (Local - XAMPP)

Para rodar a aplicação utilizando o MySQL do XAMPP (sem Docker):

1. **Iniciar o MySQL**: Abra o *XAMPP Control Panel* e clique em **Start** ao lado do módulo MySQL (e opcionalmente do Apache, caso queira usar o phpMyAdmin).
2. **Criar o Banco de Dados**:
   - Acesse o phpMyAdmin em `http://localhost/phpmyadmin` ou utilize um cliente SQL de sua preferência.
   - Crie um banco de dados chamado `di_foda` com o charset `utf8mb4` (ex: `utf8mb4_general_ci`).
3. **Importar o Schema (DDL)**:
   - Importe ou execute o conteúdo do arquivo [schema.sql](file:///c:/Users/gabri/Desktop/DI/pact/Backend/db/schema.sql) no banco de dados recém-criado.
4. **Credenciais**:
   - Por padrão, a aplicação conecta no MySQL do XAMPP usando a porta `3306`, usuário `root` e senha vazia (sem senha). O arquivo `ConnectionFactory` já está pré-configurado para isso.

---

### Compilação e Execução (Maven)

Certifique-se de estar na pasta do Backend:
```bash
cd Backend
```

#### > Opção Rápida

Execute o arquivo `run_pact.bat` em um terminal aberto na pasta do projeto, ele conectará ao banco de dados, verificará o Maven e as dependencias, baixará todas se não as encontrar e rodará o projeto. 

### Ou

1. **Compilar**:
```bash
mvn compile
```

2. **Rodar a aplicação** (configurando o terminal para UTF-8 para exibir os caracteres ANSI/ASCII corretamente):
- **No PowerShell:**
  ```powershell
  chcp 65001; mvn exec:java
  ```
- **No Command Prompt (CMD):**
  ```cmd
  chcp 65001 && mvn exec:java
  ```
*(O Maven executará a classe principal `cli.App` automaticamente conforme configurado no [pom.xml](file:///c:/Users/gabri/Desktop/DI/pact/Backend/pom.xml))*
