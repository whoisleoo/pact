package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import service.dto.RelatorioCategoria;
import service.dto.RelatorioFaturamento;

/** Consultas agregadas (GROUP BY, COUNT, SUM, AVG, JOIN) dos relatórios. */
public class RelatorioDAO {

    private final Connection connection;

    public RelatorioDAO(Connection connection) {
        this.connection = connection;
    }

    public List<RelatorioCategoria> produtosPorCategoria() throws SQLException {
        String sql = "SELECT categoria, COUNT(*) AS qtd_produtos, SUM(estoque) AS total_estoque, AVG(preco) AS preco_medio FROM produto GROUP BY categoria ORDER BY qtd_produtos DESC";

        List<RelatorioCategoria> linhas = new ArrayList<>();
        try (
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                linhas.add(
                    new RelatorioCategoria(
                        rs.getString("categoria"),
                        rs.getLong("qtd_produtos"),
                        rs.getLong("total_estoque"),
                        rs.getBigDecimal("preco_medio")
                    )
                );
            }
        }
        return linhas;
    }

    public List<RelatorioFaturamento> faturamentoPorProduto()
        throws SQLException {
        String sql = "SELECT p.nome AS nome, SUM(pp.quantidade) AS total_vendido, SUM(pp.quantidade * pp.preco_unitario) AS faturamento FROM produto_pedido pp JOIN produto p ON p.id_produto = pp.id_produto GROUP BY p.id_produto, p.nome ORDER BY faturamento DESC";

        List<RelatorioFaturamento> linhas = new ArrayList<>();
        try (
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                linhas.add(
                    new RelatorioFaturamento(
                        rs.getString("nome"),
                        rs.getLong("total_vendido"),
                        rs.getBigDecimal("faturamento")
                    )
                );
            }
        }
        return linhas;
    }
}
