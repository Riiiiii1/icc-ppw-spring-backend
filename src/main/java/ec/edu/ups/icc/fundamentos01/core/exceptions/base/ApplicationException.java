package ec.edu.ups.icc.fundamentos01.core.exceptions.base;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class ApplicationException extends RuntimeException {
    private final HttpStatus status;

    protected ApplicationException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

}
