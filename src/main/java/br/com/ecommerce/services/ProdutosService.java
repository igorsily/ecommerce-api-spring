package br.com.ecommerce.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ecommerce.models.Produtos;
import br.com.ecommerce.repositories.ProdutosRepository;

@Service
public class ProdutosService {

	@Autowired
	private ProdutosRepository produtosRepository;

	public List<Produtos> findAll() {

		return this.produtosRepository.findAll();
	}

	public void save(Produtos produto) {

		this.produtosRepository.save(produto);

	}
}
