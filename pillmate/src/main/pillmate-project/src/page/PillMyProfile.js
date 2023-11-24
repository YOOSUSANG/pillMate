import React, {useState, useContext} from 'react';
import PillHeader from "../component/PillHeader";
import {PillMateLoginStateContext} from "../App";
import {useNavigate} from "react-router-dom";
import PillMyProfileItem from "../component/PillMyProfileItem";

const PillMyProfile = () => {
    const [id, setId] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();
    const goProfile = () => {
        navigate("/PillProfile")
    }


    return (
        <div >
            <PillHeader onClick={goProfile}/>
            <div className="sampleImageAndText">
                <div className="detail_pillName">내 정보</div>
                <PillMyProfileItem text={"사진"} isTextOrImg={true}></PillMyProfileItem>
                <PillMyProfileItem text={"아이디"} isTextOrImg={false}></PillMyProfileItem>
                <PillMyProfileItem text={"비밀번호"} isTextOrImg={false}></PillMyProfileItem>
                <PillMyProfileItem text={"닉네임"} isTextOrImg={false}></PillMyProfileItem>
            </div>
        </div>
    );
};

export default PillMyProfile;