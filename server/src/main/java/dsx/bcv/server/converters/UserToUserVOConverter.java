package dsx.bcv.server.converters;

import dsx.bcv.server.data.models.User;
import dsx.bcv.server.views.UserVO;
import org.springframework.core.convert.converter.Converter;

public class UserToUserVOConverter implements Converter<User, UserVO> {
    @Override
    public UserVO convert(User source) {
        return new UserVO(
                source.getUsername(),
                source.getPassword(),
                source.getEmail()
        );
    }
}
