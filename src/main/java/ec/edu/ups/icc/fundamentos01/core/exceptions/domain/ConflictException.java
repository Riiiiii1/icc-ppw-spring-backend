package ec.edu.ups.icc.fundamentos01.core.exceptions.domain;

import ec.edu.ups.icc.fundamentos01.core.exceptions.base.ApplicationException;
import org.springframework.http.HttpStatus;

public class ConflictException extends ApplicationException {
    public ConflictException(final String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
