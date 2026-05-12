## Estrutura do repositório

```
pact/
├── Backend/
│   ├── db/
│   │   ├── schema.sql
│   │   └── seed.sql
│   └── src/main/java/
│       ├── model/
│       ├── dao/
│       ├── service/
│       ├── controller/
│       ├── exception/
│       ├── thread/
│       └── util/
└── Front/
    ├── pages/
    ├── scripts/
    └── styles/
```

### Backend

| Pacote | Função                                                                                                                                                                                                                               |
|---|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `model` | Entidades do domínio (`Cliente`, `Produto`, `Pedido`, `ItemPedido`) e enums (`Categoria`, `StatusPedido`). Classes imutáveis sem setters — objetos nascem válidos pelo construtor.                                                   |
| `dao` | Acesso ao banco de dados via JDBC. Cada classe é responsável por um único tipo de entidade (`ClienteDAO`, `ProdutoDAO`, `PedidoDAO`). Usa exclusivamente `PreparedStatement` e `try-with-resources`. Nenhuma lógica de negócio aqui. |
| `service` | Regras de negócio da aplicação. Orquestra os DAOs, valida dados e coordena a transação de criação de pedidos com verificação de estoque.                                                                                             |
| `controller` | Recebe as requisições HTTP do frontend e delega ao service correspondente. Não contém lógica de negócio nem acesso direto ao banco.                                                                                                  |
| `exception` | Exceções customizadas do domínio (`EstoqueInsuficienteException`, `ClienteNaoEncontradoException`, `ProdutoNaoEncontradoException`, `EmailInvalidoException`).                                                                       |
| `thread` | `ProcessadorPedidos` — thread de background que busca pedidos com status `FILA`, processa e atualiza para `FINALIZADO`. Gerencia sua própria conexão JDBC por ciclo, isolada da conexão principal.                                   |
| `util` | Classes utilitárias sem vínculo com o domínio. Contém a `ConnectionFactory`, responsável por fornecer a conexão com o banco a partir das variáveis de ambiente do `.env` (teoricamente vai ser fornecida).                           |

### Backend/db

| Arquivo | Função |
|---|---|
| `schema.sql` | DDL completo — criação do banco, tabelas, relacionamentos e constraints. Execute este arquivo antes de rodar o projeto. |
| `seed.sql` | Dados de teste para popular o banco em ambiente de desenvolvimento. Opcional. |

### Frontend

| Pasta | Função |
|---|---|
| `pages` | Páginas HTML da aplicação. |
| `scripts` | Arquivos JavaScript. `main.js` contém a lógica de chamadas à API. `tailwind-config.js` configuração do Tailwind CSS. |
| `styles` | Folhas de estilo CSS globais da aplicação. |
 
---