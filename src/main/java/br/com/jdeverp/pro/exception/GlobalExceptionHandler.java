package br.com.jdeverp.pro.exception;

import java.util.Date;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MsgApiException.class)
	public ResponseEntity<ResponseApi> erroGeralMsgApiException(MsgApiException ex, HttpServletRequest 
			request) {
		
		
		ResponseApi responseApi = new ResponseApi(new Date(), 
				ex.getStatus().value(), 
				ex.getStatus().getReasonPhrase(), 
				ex.getMessage(), 
				request.getRequestURI());
		
		return ResponseEntity.status(ex.getStatus())
				.contentType(MediaType.APPLICATION_JSON)
				.body(responseApi);

	}

}
