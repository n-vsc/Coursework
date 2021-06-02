package dsx.bcv.server.views;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationResponseVO {
    private String username;
    private String token;
}
