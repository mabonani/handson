package br.com.ciandt.handson.competicao.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.ciandt.handson.competicao.dto.CompeticaoDto;
import br.com.ciandt.handson.competicao.dto.CompeticaoResponseDto;
import br.com.ciandt.handson.competicao.service.CompeticaoService;

@RestController
@RequestMapping("/handson/competicao")
public class CompeticaoController{

	private CompeticaoService competicaoService;	

	@Autowired
	public CompeticaoController(CompeticaoService competicaoService) {
		this.competicaoService = competicaoService;
	}

	@GetMapping
	ResponseEntity<List<CompeticaoDto>> getCompeticao(@RequestParam(required=false)String modalidade){
		List<CompeticaoDto> competicaoList = competicaoService.getByModalidade(modalidade).stream()
				.map(CompeticaoDto::new).collect(Collectors.toList());		
		return ResponseEntity.ok(competicaoList);
	}

	@PostMapping
	ResponseEntity<CompeticaoResponseDto> saveComposicao(@RequestBody CompeticaoDto competicaoDto){		
		try {
			return ResponseEntity.ok(new CompeticaoResponseDto("SALVO COM SUCESSO", 
					new CompeticaoDto(competicaoService.save(competicaoDto))));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CompeticaoResponseDto(e.getMessage(), 
					competicaoDto));
		}		
	}
	
	@PutMapping
	ResponseEntity<CompeticaoResponseDto> updateComposicao(@RequestBody CompeticaoDto competicaoDto){		
		try {
			return ResponseEntity.ok(new CompeticaoResponseDto("SALVO COM SUCESSO", 
					new CompeticaoDto(competicaoService.update(competicaoDto))));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CompeticaoResponseDto(e.getMessage(), 
					competicaoDto));
		}		
	}
	
	@DeleteMapping("/{id}")
	ResponseEntity<String> deleteComposicao(@PathVariable Long id){		
		competicaoService.delete(id);
		return ResponseEntity.ok("DELETADO COM SUCESSO");		
	}
	

}
