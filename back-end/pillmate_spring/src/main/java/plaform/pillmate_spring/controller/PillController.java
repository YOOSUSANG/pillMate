package plaform.pillmate_spring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import plaform.pillmate_spring.domain.History;
import plaform.pillmate_spring.domain.User;
import plaform.pillmate_spring.service.HistoryService;
import plaform.pillmate_spring.service.UserService;

import java.util.*;
@Slf4j
@RestController
@RequiredArgsConstructor
public class PillController {


    private final UserService userService;
    private final HistoryService historyService;

    @PostMapping("/join")
    public User saveUser(@RequestBody User user) {
        log.info("회원가입 완료");
        Long joinId = userService.join(user);
        User joinUser = userService.findUser(joinId);
        return joinUser;
    }

    @PostMapping("/login")
    public User loginUser(@RequestBody Map<String, String> userData) {
        String email = userData.get("email");
        String password = userData.get("password");
        Optional<User> user = userService.findUser(email, password);
        return user.orElse(null);
    }

    @PostMapping("/PillMyProfile")
    public User changeUserValue(@RequestBody Map<String, String> changeData) {
        Set<String> keys = changeData.keySet();
        List<String> keyList = new ArrayList<>(keys);
        String key = keyList.get(0);
        String email = keyList.get(1);
        String password = changeData.get(email);
        if (Objects.equals(key, "img")) {
            System.out.println(changeData.get(key));
            Long userId = userService.changeImg(email, password, changeData.get(key));
            User changedImgUser = userService.findUser(userId);
            return changedImgUser;
        }
        if (Objects.equals(key, "newNickname")) {
            Long userId = userService.changeImg(email, password, changeData.get(key));
            User changedNickUser = userService.findUser(userId);
            return changedNickUser;

        }
        if (Objects.equals(key, "newPassword")) {
            Long userId = userService.changeImg(email, password, changeData.get(key));
            User changePasswordUser = userService.findUser(userId);
            return changePasswordUser;
        }
        return null;
    }
    @PostMapping("/pill/detail")
    public History joinHistory(@RequestBody History history) {
        History joinHistory = historyService.join(history);
        return joinHistory;
    }
    @PostMapping("/record")
    public ResponseEntity<String>  removeHistory(@RequestBody Map<String, String> removeData) {
        String userId = removeData.get("userId");
        String pillName = removeData.get("pillName");
        historyService.removeTakePill(Long.valueOf(userId),pillName);
        return ResponseEntity.ok( "{\"message\": \"ok\"}");
    }


    //get은 PathVariable
    @GetMapping("/pill/detail/{id}")
    public List<History> findHistory(@PathVariable Long id) {
        List<History> takePill = historyService.findTakePill(id);
        return takePill;
    }

}
