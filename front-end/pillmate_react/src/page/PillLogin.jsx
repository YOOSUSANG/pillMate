import React, {useState, useContext, useEffect, useRef} from 'react';
import PillHeader from "../component/PillHeader";
import {useNavigate} from "react-router-dom";
import pandaLogo from "../assets/pandalogo.png"
import {PillMateLoginStateContext} from "../App"
import { GoogleLoginButton } from 'react-social-login-buttons';

const springUrl = "http://localhost:8080/"

const onNaverLogin = ()=>{
    window.location.href = "http://localhost:8080/oauth2/authorization/naver"
}
const onGooleLogin = ()=>{
    window.location.href = "http://localhost:8080/oauth2/authorization/google"
}
const onKakaoLogin = ()=>{
    window.location.href = "http://localhost:8080/oauth2/authorization/kakao"
}
const PillLogin = () => {
    const {loginData,setLoginData} = useContext(PillMateLoginStateContext);
    const [name, setName] = useState(loginData["name"])
    const nav = useNavigate()
    console.log(name)
   

    
    return (
        <div>
            <PillHeader/>
            {name === undefined
            ?
            (
                <div className="registration-form" >
                    <div className="logo-container">
                        <img className="loginImage" src={pandaLogo} alt="Panda Logo" />
                    </div>
                    <div>
                        <div style={{ color: 'gray', marginTop: 30, fontSize: 10}}>-간편 로그인-</div>
                        <div>
                            <GoogleLoginButton
                                style={{width:"200px",marginBottom:"10px",height:"30px",fontSize:"13px",marginLeft:"50px",marginTop:"20px",padding:"20px"}}
                                text='구글로 시작하기'
                                onClick={onGooleLogin}
                            />
                        </div>
                        <div style={{fontSize:"10px"}}>
                            <span style={{cursor:'pointer'}} onClick={onKakaoLogin}>카카오로 시작</span>
                            <span> | </span>
                            <span style={{cursor:'pointer'}} onClick={onNaverLogin}>네이버로 시작</span>
                        </div>
                    </div>
                </div>
            )
            :
            (
                <div className="registration-form" >
                    <div className="logo-container">
                        <img className="loginImage" onClick={()=>nav("/generalsearch")}src={pandaLogo} alt="Panda Logo" style={{cursor:'pointer'}}  />
                    </div>
                    <div>
                        <div style={{ color: 'gray', marginTop: 30, fontSize: 10}}>안녕하세요.  {name}님 판다를 클릭해서 약품을 찾아보세요!!</div>
                    </div>
                </div>
            )
            }
           
        </div>
    );
}
export default PillLogin;