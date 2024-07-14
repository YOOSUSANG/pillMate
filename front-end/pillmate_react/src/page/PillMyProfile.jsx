import React, { useState, useContext, useEffect } from 'react';
import PillHeader from "../component/PillHeader";
import { PillMateLoginStateContext } from "../App";
import { useNavigate } from "react-router-dom";
import PillMyProfileItem from "../component/PillMyProfileItem";
import useAuthorization from '../validaiton/useauthorization';

const PillMyProfile = () => {
    useAuthorization();
    const { loginData } = useContext(PillMateLoginStateContext);
    const [profileImageUrl, setProfileImageUrl] = useState(loginData.profileImageUrl);
    const navigate = useNavigate();

    useEffect(() => {
        // 페이지 로드 시 로컬 스토리지에서 데이터를 가져와 상태에 설정
        const storedLoginData = JSON.parse(localStorage.getItem("userLogin"));
        if (storedLoginData) {
            setProfileImageUrl(storedLoginData.profileImageUrl);
        }
    }, []);

    const goProfile = () => {
        navigate("/PillProfile");
    }

    return (
        <div>
            <PillHeader onClick={goProfile} />
            <div className="sampleImageAndText">
                <div className="detail_pillName">내 정보</div>
                <PillMyProfileItem text={"사진"} isTextOrImg={true} profileImageUrl={profileImageUrl} setProfileImageUrl={setProfileImageUrl} />
                <PillMyProfileItem text={"이메일"} isTextOrImg={false} email={loginData.email} />
                <PillMyProfileItem text={"이름"} isTextOrImg={false} name={loginData.name} />
                <PillMyProfileItem text={"닉네임"} isTextOrImg={false} nickname={loginData.nickname} />
            </div>
        </div>
    );
};

export default PillMyProfile;
