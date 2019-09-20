package springboot;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.assertTrue;

public class Test {

    @org.junit.Test
    public void testBCryptPasswordEncoderDecoderSpringSecurity() {
        String onePassword = "Plokijuhygtfrdeswaqmnbvcxz0099";
        String twoPassword = "Qawsedrftg99";
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        PasswordEncoder newPasswordEncoder = new BCryptPasswordEncoder();
        String hashedPasswordOne = passwordEncoder.encode(onePassword);
        String hashedPasswordTwo = passwordEncoder.encode(twoPassword);
        assertTrue(newPasswordEncoder.matches(onePassword, hashedPasswordOne));
        assertTrue(newPasswordEncoder.matches(twoPassword, hashedPasswordTwo));
    }

}
