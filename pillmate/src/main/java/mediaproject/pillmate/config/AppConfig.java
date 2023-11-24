package mediaproject.pillmate.config;


import mediaproject.pillmate.repository.HistoryJpaRepository;
import mediaproject.pillmate.repository.UserJpaRepository;
import mediaproject.pillmate.service.HistoryService;
import mediaproject.pillmate.service.HistoryServiceImpl;
import mediaproject.pillmate.service.UserService;
import mediaproject.pillmate.service.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public UserService userService(UserJpaRepository userJpaRepository){
        return new UserServiceImpl(userJpaRepository);
    }
    @Bean
    public HistoryService historyService(HistoryJpaRepository historyJpaRepository){
        return new HistoryServiceImpl(historyJpaRepository);
    }



}
