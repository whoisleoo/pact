package service;

import dao.ProdutoDAO;
import exception.DatabaseException;
import exception.DupeRegisterException;
import exception.RegisterNotFoundException;
import model.repositories.Produto;
import java.sql.SQLException;
import java.util.List;

public class ProdutoService {
    private final ProdutoDAO produtoDAO;

    public ProdutoService(ProdutoDAO produtoDAO) {
        this.produtoDAO = produtoDAO;
    }

    public void registrarProduto(Produto produto) {
        try {
            Produto produtoExists = produtoDAO.findByName(produto.getNome());

            if (produtoExists != null) {
                throw new DupeRegisterException("Produto", produto.getNome());
            }

            produtoDAO.insert(produto);
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao registrar produto", e);
        }
    }

    public List<Produto> listAllProducts() {
        try {
            return produtoDAO.findAll();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar produtos", e);
        }
    }

    public Produto findById(Long id) {
        try {
            Produto produto = produtoDAO.findById(id);

            if (produto == null) {
                throw new RegisterNotFoundException("Produto", id);
            }

            return produto;
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar produto", e);
        }
    }

    public List<Produto> findByCategoria(String categoria) {
        try {
            return produtoDAO.findByCategoria(categoria);
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar produtos por categoria", e);
        }
    }
}
