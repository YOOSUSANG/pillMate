import React, {useState, useContext} from 'react';
import PillHeader from "../component/PillHeader";
import {PillMateLoginStateContext} from "../App";
import {useNavigate} from "react-router-dom";

import {pillHandlerContext} from "../App"
import {pillAllInformation} from "../utils/PillAllInformation";

const springUrl = "http://localhost:8080/"
const pillAllList = pillAllInformation();
const PillProfile = () => {
    const {loginData, setLoginData} = useContext(PillMateLoginStateContext);
    const {takingDispatch} = useContext(pillHandlerContext);
    const [id, setId] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
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

    const goHome = () => {
        navigate("/")
        localStorage.setItem("userLogin", JSON.stringify({}))
        localStorage.setItem("takingStatus", JSON.stringify([]))
        setLoginData({"check": true})
    }
    const goProfile = () => {
        navigate("/PillProfile")
    }
    const goMyProfile = () => {
        navigate("/PillMyProfile")
    }
    const goPharmacy = () => {
        navigate("/map")
    }
    const goPillSearch = () => {
        navigate("/generalsearch")
    }
    const goTakeRecord = () => {
        const loginStorage = localStorage.getItem("userLogin");
        const loginObject = JSON.parse(loginStorage)
        takeFetchGet(springUrl, "pill/detail/", loginObject["userId"])
        setTimeout(() => {
            navigate("/record")
        }, 50);
    }
    return (
        <div>
            <PillHeader onClick={goProfile}/>
            <div className="profile">
                <img className="profileImage" src={loginData["userImg"]} alt="Panda Logo"/>
                <p style={{fontSize:"18px"}}>{loginData["userNickname"]}</p>

                <div className="menu">
                    <button type="button" className="menu-button" onClick={goMyProfile}>내 정보</button>
                    <button type="button" className="menu-button" style={{color: '#FF5151'}} onClick={goHome}>로그아웃
                    </button>
                </div>
            </div>
            <div className="options">
                <button type="button" className="option-button" onClick={goPharmacy}>주변 약국</button>
                <button type="button" className="option-button" onClick={goPillSearch}>약 검색</button>
                <button type="button" className="option-button" onClick={goTakeRecord}>복용 이력</button>
            </div>
        </div>
    );
};

export default PillProfile;
