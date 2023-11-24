package mediaproject.pillmate.controller;

import mediaproject.pillmate.config.AppConfig;
import mediaproject.pillmate.domain.History;
import mediaproject.pillmate.domain.User;
import mediaproject.pillmate.repository.HistoryJpaRepository;
import mediaproject.pillmate.repository.UserJpaRepository;
import mediaproject.pillmate.service.HistoryService;
import mediaproject.pillmate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class PillController {


    AppConfig appConfig = new AppConfig();
    UserService userService;
    HistoryService historyService;

    @Autowired
    public PillController(UserJpaRepository userJpaRepository, HistoryJpaRepository historyJpaRepository) {
        this.userService = appConfig.userService(userJpaRepository);
        this.historyService = appConfig.historyService(historyJpaRepository);

    }

    @CrossOrigin(origins = "http://localhost:3000")
    @ResponseBody
    @PostMapping("/join")
    public User saveUser(@RequestBody User user) {
        User joinUser = userService.join(user);
        return joinUser;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @ResponseBody
    @PostMapping("/login")
    public User loginUser(@RequestBody Map<String, String> userData) {
        String email = userData.get("email");
        String password = userData.get("password");
        Optional<User> user = userService.findUser(email, password);
        return user.orElse(null);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @ResponseBody
    @PostMapping("/PillMyProfile")
    public User changeUserValue(@RequestBody Map<String, String> changeData) {
        Set<String> keys = changeData.keySet();
        List<String> keyList = new ArrayList<>(keys);
        String key = keyList.get(0);
        String email = keyList.get(1);
        String password = changeData.get(email);
        if (Objects.equals(key, "img")) {
            System.out.println(changeData.get(key));
            User changedImgUser = userService.changeImg(email, password, changeData.get(key));
            return changedImgUser;
        }
        if (Objects.equals(key, "newNickname")) {
            User changedNickUser = userService.changeNickName(email, password, changeData.get(key));
            return changedNickUser;

        }
        if (Objects.equals(key, "newPassword")) {
            User changePasswordUser = userService.changePassword(email, password, changeData.get(key));
            return changePasswordUser;
        }
        return null;
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @ResponseBody
    @PostMapping("/pill/detail")
    public History joinHistory(@RequestBody History history) {
        History joinHistory = historyService.join(history);
        return joinHistory;
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @ResponseBody
    @PostMapping("/record")
    public ResponseEntity<String>  removeHistory(@RequestBody Map<String, String> removeData) {
        String userId = removeData.get("userId");
        String pillName = removeData.get("pillName");
        historyService.removeTakePill(Long.valueOf(userId),pillName);
        return ResponseEntity.ok( "{\"message\": \"ok\"}");
    }


    //get은 PathVariable
    @CrossOrigin(origins = "http://localhost:3000")
    @ResponseBody
    @GetMapping("/pill/detail/{id}")
    public List<History> findHistory(@PathVariable Long id) {
        List<History> takePill = historyService.findTakePill(id);
        return takePill;
    }

}
