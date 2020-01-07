package momik.spring.cloud.auth.config;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * description 用户信息验证
 * 创建时间 2018/10/17
 *
 * @author 仇兴洲
 */
@Service("userDetailService")
public class UserDetailServiceImpl implements UserDetailsService {

    @Transactional(noRollbackFor = Exception.class)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //UserVO userVO = macUserMapper.loadUserByUsername(username);
        //return new UserDetailsImpl(userVO);
        return new User(username, new BCryptPasswordEncoder().encode("123456"), AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
    }
}
