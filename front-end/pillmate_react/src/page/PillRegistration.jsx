import React, {useState, useContext} from 'react';
import PillHeader from "../component/PillHeader";
import {PillMateLoginStateContext} from "../App";
import {useNavigate} from "react-router-dom";
import MyButton from "../component/MyButton";

const springUrl = "http://localhost:8080/"
const joinFetch = (baseUrl, subUrl, user) => {
    fetch(baseUrl + subUrl, {
        method: "POST",
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(user)
    })
        .then((res) => res.json())
        .then((data) => {
        })
        .catch((error) => {
            console.log("POST 요청 중 오류 발생: ", error)
        })
}

const PillRegistration = () => {
    const loginData = useContext(PillMateLoginStateContext);

    const [email, setEmail] = useState('')
    const [userNickname, setNickName] = useState('')
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [passwordError, setPasswordError] = useState('');
    const [confirmPasswordError, setConfirmPasswordError] = useState('');
    const navigate = useNavigate()


    const handleEmailChange = (e) => {
        setEmail(e.target.value)
    }
    const handleNickNameChange = (e) => {
        setNickName(e.target.value)
    }
    const handlePasswordChange = (e) => {
        let valid = true;
        setPassword(e.target.value);
        if(e.target.value.length < 8 ){
            setPasswordError('비밀번호는 8자리 이상 입력해주세요.');
        }else{
            setPasswordError('');
        }
        // // If the confirm password field is not empty, check if passwords match
        // if (confirmPassword && e.target.value !== confirmPassword) {
        //     setConfirmPasswordError('비밀번호가 일치하지 않습니다.');
        // } else {
        //     setConfirmPasswordError('');
        // }
    };

    const handleConfirmPasswordChange = (e) => {
        setConfirmPassword(e.target.value);
        // Only display mismatch error if the password field is not empty
        if (e.target.value && e.target.value !== password) {
            setConfirmPasswordError('비밀번호가 일치하지 않습니다.');
        } else {
            setConfirmPasswordError('');
        }
    };


    const handleSubmit = () => {
        joinFetch(springUrl,"join",{userNickname,email,password})
        navigate("/",{replace:true})
    }
    return (
        <div>
            <PillHeader />
            <div className="registration-form" >
                <label htmlFor="userid">아이디</label>
                <input type="text" id="userid" name="userid" placeholder="아이디" onChange={handleEmailChange}/>

                <label htmlFor="password">비밀번호</label>
                <input type="password" id="password" name="password" placeholder="비밀번호" value={password}
                       onChange={handlePasswordChange}/>
                {passwordError ? <div style={{color: 'red',fontSize:"12px",paddingBottom:"10px"}}>{passwordError}</div>:<div style={{height:"26px"}}> </div>}

                <label htmlFor="password-confirm">비밀번호 재확인</label>
                <input type="password" id="password-confirm" name="password-confirm" placeholder="비밀번호 재확인"
                       value={confirmPassword} onChange={handleConfirmPasswordChange}/>
                {confirmPasswordError ?<div style={{color: 'red',fontSize:"12px",paddingBottom:"10px"}}>{confirmPasswordError}</div>  : <div style={{height:"26px"}}></div>}

                <label htmlFor="nickname">닉네임</label>
                <input type="text" id="nickname" name="nickname" placeholder="닉네임" onChange={handleNickNameChange}/>
                <MyButton text={"가입하기"} onClick={handleSubmit}></MyButton>
            </div>
        </div>
    );
};

export default PillRegistration;
