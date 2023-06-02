/**
 * @author Ruslan
 */

package jpa.experiment.experimentjpa.exception;

import org.springframework.http.ResponseEntity;

import java.util.Objects;

public class CommonException extends RuntimeException {
    private ResponseEntity<?> responseEntity;

    public CommonException(String message) {
        super(message);
    }

    public CommonException(ResponseEntity<?> responseEntity) {
        super(Objects.requireNonNull((ResponseData) responseEntity.getBody()).getErrorMessage());
        this.responseEntity = responseEntity;
    }

    public ResponseEntity<?> getResponseEntity() {
        return responseEntity;
    }

    public void setResponseEntity(ResponseEntity<ResponseData<ResponseMessage>> responseEntity) {
        this.responseEntity = responseEntity;
    }
}
