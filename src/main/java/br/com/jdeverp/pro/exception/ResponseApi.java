package br.com.jdeverp.pro.exception;

import java.util.Date;

public class ResponseApi {
	// é uma padronização para mostrar a data e o horário do disparo da exceção
	private Date localDateTime;
	private int status;
	private String error;
	private String message;
	private String path;

	// Construtor

	public ResponseApi(Date localDateTime, int status, String error, String message, String path) {
		super();
		this.localDateTime = localDateTime;
		this.status = status;
		this.error = error;
		this.message = message;
		this.path = path;
	}

	public Date getLocalDateTime() {
		return localDateTime;
	}

	public int getStatus() {
		return status;
	}

	public String getError() {
		return error;
	}

	public String getMessage() {
		return message;
	}

	public String getPath() {
		return path;
	}

	// Só retorna o get porque serve para front e back para obter essa informaçõe de
	// dentro do objeto e passar as informações por meio do construtor

}
