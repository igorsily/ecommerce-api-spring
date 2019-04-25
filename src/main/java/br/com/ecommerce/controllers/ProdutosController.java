package br.com.ecommerce.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.ServletContext;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ecommerce.models.Produtos;
import br.com.ecommerce.services.ProdutosService;

@RestController
@RequestMapping("/produtos")
public class ProdutosController {

	@Autowired
	private ProdutosService produtosService;

	@Autowired
	private ServletContext context;

	@GetMapping
	public ResponseEntity<List<Produtos>> index() {

		List<Produtos> produtos = this.produtosService.findAll();

		return new ResponseEntity<List<Produtos>>(produtos, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Void> story(@Valid @ModelAttribute Produtos produto) {

		this.produtosService.save(produto);

		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}

	@PostMapping("/upload")
	public ResponseEntity<Void> image(@RequestParam("file") MultipartFile file, String produtoJson) {

		if (file.isEmpty()) {

			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}

		Produtos produto = this.converterStringEmObjeto(produtoJson);

		Path path = this.uploadImage(file);

		produto.setImagePath(path.toString());

		this.produtosService.save(produto);

		return new ResponseEntity<Void>(HttpStatus.CREATED);

	}

	public Produtos converterStringEmObjeto(String produtoJson) {
		ObjectMapper mapper = new ObjectMapper();

		Produtos produto = null;

		try {
			produto = mapper.readValue(produtoJson, Produtos.class);
			return produto;
		} catch (JsonParseException e) {

			e.printStackTrace();
			return null;
		} catch (JsonMappingException e) {

			e.printStackTrace();
			return null;
		} catch (IOException e) {

			e.printStackTrace();
			return null;
		}

	}

	public Path uploadImage(MultipartFile file) {

		try {

			byte[] bytes = file.getBytes();
			Path path = Paths.get(context.getRealPath("/resources/upload"));

			Files.write(path, bytes);

			return path;
		} catch (IOException e) {

			e.printStackTrace();

			return null;
		}

	}

}
