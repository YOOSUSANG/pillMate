import React, {useState, useContext, useEffect} from 'react';
import PillHeader from "../component/PillHeader";
import {useNavigate} from "react-router-dom";
import MyButton from "../component/MyButton";
import pandaLogo from "../assets/pandalogo.png"
import {PillMateLoginStateContext} from "../App"

const springUrl = "http://localhost:8080/"
const PillLogin = () => {
    const {loginData,setLoginData} = useContext(PillMateLoginStateContext);
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const loginFetch = (baseUrl, subUrl, user) => {
        fetch(baseUrl + subUrl, {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(user)
        })
            .then((res) => res.json())
            .then((data) => {
                console.log(data)
                setLoginData(data)
                localStorage.setItem("userLogin", JSON.stringify(data))
            })
            .catch((error) => {
                setLoginData({})
            })
    }

    const goRegistration = () => {
        navigate("/PillRegistration")
    }
    const handleLogin = async () => {
        await loginFetch(springUrl, "login", {email, password})
    };
    useEffect(() => {
        if(Object.keys(loginData).length === 1){
            setError('')
        } else if(Object.keys(loginData).length === 0){
            setError("아이디와 비밀번호를 다시 한번 확인해주세요.")
        }else{
            setError('')
            navigate("/generalsearch",{replace:true})
        }

    }, [loginData]);
    useEffect(()=>{
        setError('')
    },[])
    return (
        <div>
            <PillHeader/>
            <div
                className="registration-form" >
                <div className="logo-container">
                    <img className="loginImage" src={pandaLogo} alt="Panda Logo" />
                </div>
                <div>
                    <input
                        type="text"
                        placeholder="ID"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                    />
                </div>
                <div>
                    <input
                        type="password"
                        placeholder="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </div>
                <div>
                    {error !=='' ?<div className="error-message">{error}</div> : <div style={{height:"16px"}}></div>}
                    <MyButton text={"로그인"} onClick={handleLogin}></MyButton>
                </div>

                <div className="helper-links">
                    <button type="button" className="text-button">비밀번호 찾기</button>
                    <button type="button" className="text-button">아이디 찾기</button>
                    <button type="button" className="text-button" onClick={goRegistration}>회원가입</button>
                </div>
            </div>
        </div>
    );
}
export default PillLogin;