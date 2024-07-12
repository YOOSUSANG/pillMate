import React, {useRef, useState, useEffect, useContext} from "react";
import pandaLogo from "../assets/pandalogo.png"
import {PillMateLoginStateContext} from "../App";

import axios from "axios";

const springUrl = "http://localhost:8080/"
const changeFetch = (baseUrl, subUrl, datas) => {
    fetch(baseUrl + subUrl, {
        method: "POST",
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(datas)
    })
        .then((res) => res.json())
        .then((data) => {
            localStorage.setItem("userLogin", JSON.stringify(data))
        })
        .catch((error) => {
            console.log("POST 요청 중 오류 발생: ", error)
        })
}

const PillMyProfileItem = ({text, isTextOrImg}) => {
    const {loginData, setLoginData} = useContext(PillMateLoginStateContext);

    // //이미지
    // const [profile, setProfile] = useState(
    //     loginData["profile"]
    // )
    //닉네임
    const [inputnickname, setInputnickname] = useState(loginData["nickname"]);
    //이메일
    const [email, setEmail] = useState(loginData["email"])
    //이름
    const [name,setName] = useState(loginData["name"])

    return (
        <div className="pill_profile_frame">
            <div className="pill_profile_text">{text}</div>
            {text === "사진" 
            ?
            (
                <div className="pillProfileImage">
                    <img className="pill_profileImage" src={pandaLogo}/>
                </div>
            )
            :
            (
                <div className="pill_profile_text_frame">
                    <input
                        type="text"
                        className={["profileText", `profileText_${text}`].join(" ")}
                        value={text === "닉네임" ? inputnickname : text === "이메일" ? email : name}
                        readOnly
                    ></input>
                </div>
            )
            }
        </div>
    );
}
export default PillMyProfileItem