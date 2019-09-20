package springboot.service.passwordupdate;

import springboot.service.utils.BaseResult;

public class PasswordUpdateResult extends BaseResult {

    PasswordUpdateResult(String message, boolean isSuccess) {
        super(message, isSuccess);
    }
}
