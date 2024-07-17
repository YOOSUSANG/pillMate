package plaform.pillmate_spring;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import plaform.pillmate_spring.domain.dto.PillRequestData;
import plaform.pillmate_spring.domain.service.PillService;

@Profile("local")
@Component
@RequiredArgsConstructor
public class testDataInit {

    private final InitTest initTest;

    @PostConstruct
    public void init() {
        initTest.initData();
    }
    @Component
    @RequiredArgsConstructor
    static class InitTest{
        private final PillService pillService;

        @Transactional
        public void initData() {
            PillRequestData pillRequestData1 = PillRequestData.builder()
                    .name("사리돈에이정 250mg/PTP")
                    .classNo("[01140]해열.진통.소염제")
                    .imgKey("http://connectdi.com/design/img/drug/147800419473500177.jpg")
                    .dlMaterial("아세트아미노펜|이소프로필안티피린|카페인무수물")
                    .dlMaterialEn("Acetaminophen| Caffeine Anhydrous| Isopropylantipyrine")
                    .dlCustomShape("정제")
                    .colorClass1("하양")
                    .drugShape("원형")
                    .build();
            PillRequestData pillRequestData2 = PillRequestData.builder()
                    .name("뮤코펙트정(암브록솔염산염)")
                    .classNo("[02220]진해거담제")
                    .imgKey("https://www.pharm.or.kr/images/sb_photo/big3/200907150540503.jpg")
                    .dlMaterial("암브록솔염산염")
                    .dlMaterialEn("Ambroxol Hydrochloride")
                    .dlCustomShape("정제")
                    .colorClass1("하양")
                    .drugShape("원형")
                    .build();
            PillRequestData pillRequestData3 = PillRequestData.builder()
                    .name("원더칼-디츄어블정")
                    .classNo("[03210]칼슘제")
                    .imgKey("https://www.pharm.or.kr/images/sb_photo/big3/201001150001701.jpg")
                    .dlMaterial("콜레칼시페롤농축분말|구연산칼슘")
                    .dlMaterialEn("Calcium Citrate| Cholecalciferol Concentrated Powder")
                    .dlCustomShape("정제")
                    .colorClass1("하양")
                    .drugShape("원형")
                    .build();
            pillService.join(pillRequestData1);
            pillService.join(pillRequestData2);
            pillService.join(pillRequestData3);
        }


    }

}
