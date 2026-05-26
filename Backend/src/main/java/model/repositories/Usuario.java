package model.repositories;

import model.domain.Autenticacao;
import model.domain.Nome;

public record Usuario(Nome nome, Autenticacao autenticacao) {
}