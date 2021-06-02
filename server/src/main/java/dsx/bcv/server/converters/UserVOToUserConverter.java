package dsx.bcv.server.converters;

import dsx.bcv.server.data.models.User;
import dsx.bcv.server.views.UserVO;
import org.springframework.core.convert.converter.Converter;

public class UserVOToUserConverter implements Converter<UserVO, User> {
    @Override
    public User convert(UserVO source) {
        return new User(
                source.getUsername(),
                source.getPassword(),
                source.getEmail()
        );
    }
}
