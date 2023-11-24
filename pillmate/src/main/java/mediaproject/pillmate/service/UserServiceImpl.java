package mediaproject.pillmate.service;

import mediaproject.pillmate.domain.User;
import mediaproject.pillmate.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
public class UserServiceImpl implements UserService{

    private final UserJpaRepository userJpaRepository;


    @Autowired
    public UserServiceImpl(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public User join(User user) {
        user.setUserImg("https://www.lab2050.org/common/img/default_profile.png");
        User saveUser = userJpaRepository.save(user);
        return saveUser;
    }

    @Override
    public Optional<User> findUser(String email, String password) {
        Optional<User> findUser = userJpaRepository.findByUser(email,password);
        return findUser;
    }
    @Override
    public List<User> allUserSearch() {
        List<User> all = userJpaRepository.findAll();
        return all;
    }

    @Override
    public void Remove(Long id) {
        userJpaRepository.deleteById(id);
    }

    @Override
    public void allUserRemove() {
        userJpaRepository.deleteAll();

    }

    @Override
    public User changeImg(String email, String password,String img) {
        Optional<User> findUser = userJpaRepository.findByUser(email,password);
        User user = findUser.get();
        user.setUserImg(img);
        User saveUser = userJpaRepository.save(user);
        return saveUser;
    }

    @Override
    public User changePassword(String email, String password, String newPassword) {
        Optional<User> findUser = userJpaRepository.findByUser(email,password);
        User user = findUser.get();
        user.setPassword(newPassword);
        User saveUser = userJpaRepository.save(user);
        return saveUser;
    }

    @Override
    public User changeNickName(String email, String password,String newNickname) {
        Optional<User> findUser = userJpaRepository.findByUser(email,password);
        User user = findUser.get();
        user.setUserNickname(newNickname);
        User saveUser = userJpaRepository.save(user);
        return saveUser;
    }
}
