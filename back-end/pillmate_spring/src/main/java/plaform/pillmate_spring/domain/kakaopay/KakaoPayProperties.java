package plaform.pillmate_spring.domain.kakaopay;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class KakaoPayProperties {

    public final String secretKey;
    public final String cid;
    public final String readyUrl;
    public final String approveUrl;
    public final String cancelUrl;
    public final String cname;

    @Autowired
    public KakaoPayProperties(@Value("${kakaopay.secret-key}") String secretKey,
                              @Value("${kakaopay.cid}") String cid,
                              @Value("${kakaopay.ready-url}") String readyUrl,
                              @Value("${kakaopay.approve-url}") String approveUrl,
                              @Value("${kakaopay.cancel-url}") String cancelUrl,
                              @Value("${kakaopay.cname}") String cname){
        this.secretKey = secretKey;
        this.cid = cid;
        this.readyUrl = readyUrl;
        this.approveUrl = approveUrl;
        this.cancelUrl = cancelUrl;
        this.cname = cname;

    }

}
