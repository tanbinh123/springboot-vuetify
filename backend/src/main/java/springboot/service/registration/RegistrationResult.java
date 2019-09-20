package springboot.service.registration;

import springboot.service.utils.BaseResult;

public class RegistrationResult extends BaseResult {

    RegistrationResult(String message, boolean isSuccess) {
        super(message, isSuccess);
    }
}
