package mediaproject.pillmate.service;

import mediaproject.pillmate.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    User join(User user);

    Optional<User> findUser(String email,String password);

    List<User> allUserSearch();

    void Remove(Long id);

    User changeImg(String email,String password,String img);

    User changePassword(String email,String password,String newPassword);
    User changeNickName(String email,String password,String newNickname);

    void allUserRemove();
}
