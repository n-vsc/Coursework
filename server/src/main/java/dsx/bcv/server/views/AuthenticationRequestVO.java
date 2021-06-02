package dsx.bcv.server.views;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationRequestVO {
    private String username;
    private String password;
}
