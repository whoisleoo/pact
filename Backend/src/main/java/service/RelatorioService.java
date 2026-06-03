package service;

import dao.RelatorioDAO;
import java.sql.SQLException;
import java.util.List;
import service.dto.RelatorioCategoria;
import service.dto.RelatorioFaturamento;

public class RelatorioService {

    private final RelatorioDAO relatorioDAO;

    public RelatorioService(RelatorioDAO relatorioDAO) {
        this.relatorioDAO = relatorioDAO;
    }

    public List<RelatorioCategoria> produtosPorCategoria() throws SQLException {
        return relatorioDAO.produtosPorCategoria();
    }

    public List<RelatorioFaturamento> faturamentoPorProduto()
        throws SQLException {
        return relatorioDAO.faturamentoPorProduto();
    }
}
