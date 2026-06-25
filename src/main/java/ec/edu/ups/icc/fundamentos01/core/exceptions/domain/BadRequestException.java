package ec.edu.ups.icc.fundamentos01.core.exceptions.domain;

import ec.edu.ups.icc.fundamentos01.core.exceptions.base.ApplicationException;
import org.springframework.http.HttpStatus;

/*
 * Excepción usada cuando la solicitud es inválida
 * según reglas de negocio.
 */
public class BadRequestException extends ApplicationException {

    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}