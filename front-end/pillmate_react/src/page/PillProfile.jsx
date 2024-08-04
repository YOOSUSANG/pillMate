import React, {useState, useContext, useEffect} from 'react';
import PillHeader from "../component/PillHeader";
import {PillMateLoginStateContext} from "../App";
import {useNavigate} from "react-router-dom";
import pandaLogo from "../assets/pandalogo.png"
import {pillHandlerContext} from "../App"
import {pillAllInformation} from "../utils/PillAllInformation";
import useAuthorization from '../validaiton/useauthorization';

const springUrl = "http://localhost:8080/"
const pillAllList = pillAllInformation();
const PillProfile = () => {
    useAuthorization()
    const {loginData, setLoginData} = useContext(PillMateLoginStateContext);
    const {takingDispatch} = useContext(pillHandlerContext);
     const [profile,setProfile] = useState(loginData["profileImageUrl"])
    const navigate = useNavigate();

    const takeFetchGet = (baseUrl, subUrl, userId) => {
        if (userId !== undefined) {
            fetch(baseUrl + subUrl + userId).then((res) => {
                return res.json()
            }).then((data) => {
                //data 필터링
                console.log(data)

                let takeFilterResult = pillAllList.filter((pit) => {
                    return data.some((cit) => pit.dl_name === cit.pillName);
                });
                console.log(takeFilterResult)

                takingDispatch({type: 'INIT', data: takeFilterResult})
            })

        }
    }
    //jwt 로그아웃시 실행
    const jwtLogout = (baseUrl, subUrl) => {
        fetch(baseUrl + subUrl, {
            method: "POST",
            credentials: 'include'
        })
        .then((res) => {
            // 상태 코드 확인
            if (res.status === 200) { // 200 OK
              
                localStorage.setItem("userLogin", JSON.stringify({}))
                localStorage.setItem("takingStatus", JSON.stringify([]))
                setLoginData({"check": true})
                navigate("/")
            } else {
                throw new Error('Logout failed with status: ' + res.status);
            }
        })
        .catch((error) => {
            alert(error);
        });
    }
    const goProfile = () => {
        navigate("/mypage")
    }
    const goMyProfile = () => {
        navigate("/mypage/detail")
    }
    const goPharmacy = () => {
        navigate("/map")
    }
    const goPillSearch = () => {
        navigate("/generalsearch")
    }
    const goPillPurchase = () => {
        navigate("/pill_store")
    }
    const goOrdersuccessList = () => {
        navigate("/mypage/order_list")
    }
    const goOrderRefundList = () => {
        navigate("/mypage/refund_list")
    }
    const goTakeRecord = () => {
        const loginStorage = localStorage.getItem("userLogin");
        const loginObject = JSON.parse(loginStorage)
        takeFetchGet(springUrl, "pill/detail/", loginObject["userId"])
        setTimeout(() => {
            navigate("/record")
        }, 50);
    }

    useEffect(() => {
        // 페이지 로드 시 로컬 스토리지에서 데이터를 가져와 상태에 설정
        const storedLoginData = JSON.parse(localStorage.getItem("userLogin"));
        if (storedLoginData) {
            setProfile(storedLoginData.profileImageUrl);
        }
    }, []);
    return (
        <div>
            <PillHeader onClick={goProfile}/>
            <div className="profile">
                <img className="profileImage" src={profile} alt="Panda Logo"/>
                <p style={{fontSize:"18px"}}>{loginData["nickname"]}</p>

                <div className="menu">
                    <button type="button" className="menu-button" onClick={goMyProfile}>내 정보</button>
                    <button type="button" className="menu-button" style={{color: '#FF5151'}} onClick={() => jwtLogout(springUrl,"logout")}>로그아웃
                    </button>
                </div>
            </div>
            <div className="options">
                <button type="button" className="option-button" onClick={goPharmacy}>주변 약국</button>
                <button type="button" className="option-button" onClick={goPillPurchase}>약품 구매</button>
                <button type="button" className="option-button" onClick={goPillSearch}>약품 찾기</button>
                <button type="button" className="option-button" onClick={goTakeRecord}>복용 이력</button>
                <button type="button" className="option-button" onClick={goOrdersuccessList}>주문 목록</button>
                <button type="button" className="option-button" onClick={goOrderRefundList}>환불 목록</button>
            </div>
        </div>
    );
};

export default PillProfile;
