package dsx.bcv.server.views;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserVO {
    private String username;
    private String password;
    private String email;
}
