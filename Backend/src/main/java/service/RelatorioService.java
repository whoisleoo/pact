package service;

import dao.RelatorioDAO;
import exception.DatabaseException;
import java.sql.SQLException;
import java.util.List;
import service.dto.RelatorioCategoria;
import service.dto.RelatorioFaturamento;

public class RelatorioService {

    private final RelatorioDAO relatorioDAO;

    public RelatorioService(RelatorioDAO relatorioDAO) {
        this.relatorioDAO = relatorioDAO;
    }

    public List<RelatorioCategoria> produtosPorCategoria() {
        try {
            return relatorioDAO.produtosPorCategoria();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao gerar relatório por categoria", e);
        }
    }

    public List<RelatorioFaturamento> faturamentoPorProduto() {
        try {
            return relatorioDAO.faturamentoPorProduto();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao gerar faturamento por produto", e);
        }
    }
}
