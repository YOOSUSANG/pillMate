package plaform.pillmate_spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import plaform.pillmate_spring.domain.User;
import plaform.pillmate_spring.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Long join(User user){
        user.setUserImg("초기 이미지 설정");
        User saveUser = userRepository.save(user);
        return saveUser.getId();
    }

    public Optional<User> findUser(String email, String password) {
        Optional<User> findUser = userRepository.findByEmailAndPassword(email,password);
        return findUser;
    }

    public User findUser(Long id) {
        Optional<User> findOptionalUser = userRepository.findById(id);
        return findOptionalUser.get();
    }
    public List<User> findAllUser() {
        List<User> all = userRepository.findAll();
        return all;
    }

    @Transactional
    public void removeUser(Long id) {
        userRepository.deleteById(id);
    }


    @Transactional
    public void removeAllUser() {
        userRepository.deleteAll();

    }

    @Transactional
    public Long changeImg(String email, String password,String img) {
        Optional<User> findUser = userRepository.findByEmailAndPassword(email,password);
        User user = findUser.get();
        user.setUserImg(img);
        return user.getId();
    }

    @Transactional
    public Long changePassword(String email, String password, String newPassword) {
        Optional<User> findUser = userRepository.findByEmailAndPassword(email,password);
        User user = findUser.get();
        user.setPassword(newPassword);
        return user.getId();
    }

    @Transactional
    public Long changeNickName(String email, String password,String newNickname) {
        Optional<User> findUser = userRepository.findByEmailAndPassword(email,password);
        User user = findUser.get();
        user.setUserNickname(newNickname);
        return user.getId();
    }
}
