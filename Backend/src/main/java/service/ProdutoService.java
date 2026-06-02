package service;

import dao.ProdutoDAO;
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


    public void registrarProduto(Produto produto) throws SQLException{
        Produto produtoExists = produtoDAO.findByName(produto.getNome());

        if(produtoExists != null){
            throw new DupeRegisterException("Produto", produto.getNome());
        }

    produtoDAO.insert(produto);

    }


    public List<Produto> listAllProducts() throws SQLException{
        return produtoDAO.findAll();
    }

    public Produto findById(Long id) throws SQLException {
        Produto produto = produtoDAO.findById(id);

        if(produto == null){
            throw new RegisterNotFoundException("Produto", id);
        }

        return produto;
    }


    public List<Produto> findByCategoria(String categoria) throws SQLException {
        return produtoDAO.findByCategoria(categoria);
    }

}
