package br.com.ciandt.handson.dto;

import br.com.ciandt.handson.dto.BaseDto;

public abstract class BaseResponseDto {
	private String message;
	private BaseDto dto;
	
	public BaseResponseDto() {		
	}
	
	public BaseResponseDto(String message, BaseDto dto) {
		this.message = message;
		this.dto = dto;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public BaseDto getDto() {
		return dto;
	}
	public void setDto(BaseDto dto) {
		this.dto = dto;
	}
	
	
}
