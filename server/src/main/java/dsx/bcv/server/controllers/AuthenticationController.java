package dsx.bcv.server.controllers;

import dsx.bcv.server.data.models.User;
import dsx.bcv.server.security.JwtTokenProvider;
import dsx.bcv.server.services.data_services.UserService;
import dsx.bcv.server.views.AuthenticationRequestVO;
import dsx.bcv.server.views.AuthenticationResponseVO;
import dsx.bcv.server.views.UserVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Предоставляет api для регистрации и авторизации пользователей
 */
@RestController
@RequestMapping("auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final ConversionService conversionService;

    public AuthenticationController(
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider,
            UserService userService,
            ConversionService conversionService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.conversionService = conversionService;
    }

    @ApiOperation("Authenticate user. Returns token.")
    @PostMapping("login")
    public AuthenticationResponseVO login(@RequestBody AuthenticationRequestVO authenticationRequestVO) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequestVO.getUsername(),
                    authenticationRequestVO.getPassword()
            ));
            var user = userService.findByUsername(authenticationRequestVO.getUsername());
            var token = jwtTokenProvider.createToken(user);
            return new AuthenticationResponseVO(
                    user.getUsername(),
                    token
            );
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password", e);
        }
    }

    @ApiOperation("Register user. Returns token.")
    @PostMapping("register")
    public AuthenticationResponseVO register(@RequestBody UserVO userVO) {
        var user = conversionService.convert(userVO, User.class);
        assert user != null;
        userService.register(user);
        return login(new AuthenticationRequestVO(userVO.getUsername(), userVO.getPassword()));
    }
}
