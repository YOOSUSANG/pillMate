import MyButton from "./MyButton";
import React, {useRef, useState, useEffect, useContext} from "react";
import PillMyEditPassword from "./PillMyEditPassword";
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
    const fileInputRef = useRef(null)
    const [file, setFile] = useState(null)
    const [textSrc, setTextSrc] = useState(
        ""
    )
    const [imageSrc, setImageSrc] = useState(
        loginData["userImg"]
    )
    const [isEditing, setIsEditing] = useState(false);
    //닉네임
    const [inputnickname, setInputnickname] = useState(loginData["userNickname"]);
    //아이디
    const [inputId, setInputId] = useState(loginData["email"])
    //현재 비밀번호
    const [currentPassword, setCurrentPassword] = useState(loginData["password"])
    //새 비밀번호
    const [newPassword, setNewPassword] = useState("")
    //새 비밀번호 재 입력
    const [newRePassword, setNewRePassword] = useState("")

    // 현재 비밀번호 확인
    const [isCurrentPassword, setIsCurrentPassword] = useState(false)
    //새 비밀번호 확인
    const [isNewPassword, setIsNewPassword] = useState(false)


    const [placeholder, setPlaceholder] = useState("닉네임을 수정해주세요...");
    const handleProfileImageChange = async (e) => {
        const selectedFile = e.target.files[0];

        // FormData를 사용하여 이미지를 서버에 전송
        const formData = new FormData();
        formData.append('image', selectedFile);

        try {
            // 서버의 업로드 엔드포인트로 POST 요청 보내기
            await axios.post('http://localhost:3001/upload', formData);
            await changeFetch(springUrl,"PillMyProfile",{"img":"http://localhost:3001/userImg/" + selectedFile.name, [inputId]: currentPassword})
            setTimeout(() => {
                window.location.reload();
            }, 300);

            // 업로드가 성공하면 추가적인 작업 수행 가능
            console.log('이미지 업로드 성공');
        } catch (error) {
            console.error('이미지 업로드 실패', error);
        }
    };
    useEffect(() => {
        setImageSrc(loginData["userImg"])
        setInputId(loginData["email"])
        setCurrentPassword(loginData["password"])
        setInputnickname(loginData["userNickname"])

    }, [loginData]);
    useEffect(() => {
        if (isEditing) {
            setPlaceholder("닉네임을 수정해주세요...");
        } else {
            setPlaceholder("");
        }
    }, [isEditing]);
    const handleButtonClick = () => {
        // 클릭 시 파일 업로드 창 열기
        fileInputRef.current.click()
    };
    const handleTextEdit = () => {
        setInputnickname("");
        setIsEditing(true);

    }
    const handlePasswordEdit = () => {
        setIsEditing(true);
    }
    const handleInputChange = (e) => {
        setInputnickname(e.target.value);
    };

    const handlePasswordEditComplete = async () => {
        if (!isCurrentPassword && !isNewPassword) {
            // 현재 비밀번호와 새 비밀번호가 입력되지 않았을 때
            alert("현재 비밀번호와 새 비밀번호를 알맞게 입력해주세요");
        } else if (!isCurrentPassword) {
            // 현재 비밀번호가 입력되지 않았을 때
            alert("현재 비밀번호를 알맞게 입력해주세요.");
        } else if (!isNewPassword) {
            // 새 비밀번호가 입력되지 않았을 때
            alert("새 비밀번호와 재입력 비밀번호를 알맞게 입력해주세요.");
        } else {
            alert("비밀번호 수정 완료!")
            await changeFetch(springUrl, "PillMyProfile", {"newPassword":newPassword, [inputId]: currentPassword})
            setCurrentPassword(newPassword)
            setTimeout(() => {
                window.location.reload();
            }, 300);
            // 모든 조건이 충족되었을 때 setIsEditing을 실행
            setIsEditing(false);
            // Perform any additional logic with the updated value
        }
    };
    const handleNickNameEditComplete = async () => {
        await changeFetch(springUrl, "PillMyProfile", {"newNickname":inputnickname, [inputId]: currentPassword})
        alert("닉네임 수정 완료!")
        setTimeout(() => {
            window.location.reload();
        }, 300);
        setIsEditing(false);

    }

    return (
        <div className="pill_profile_frame">
            <div className="pill_profile_text">{text}</div>
            {isTextOrImg === true ? (
                <div className="pillProfileImage">

                    <img className="pill_profileImage" src={imageSrc}/>
                    <MyButton
                        text={"사진 변경"}
                        type={"MyImageChange"}
                        onClick={handleButtonClick}
                    ></MyButton>
                    <input
                        type="file"
                        accept="image/*"
                        style={{display: "none"}}
                        ref={fileInputRef}
                        onChange={handleProfileImageChange}
                    />
                </div>
            ) : (
                <div className="pill_profile_text_frame">
                    {/*isEditing은 수정 버튼을 누르면 true로 변경되므로 ?b:c 에서 b가 실행된다.*/}
                    {isEditing ? (
                        <>
                            {text === "비밀번호" ? <PillMyEditPassword password={currentPassword}
                                                                   newPasswordSet={setNewPassword}
                                                                   newPassword={newPassword}
                                                                   setIsCurrentPassword={setIsCurrentPassword}
                                                                   setIsNewPassword={setIsNewPassword}
                                                                   setNewRePassword={setNewRePassword}
                                                                   newRePassword={newRePassword}
                            ></PillMyEditPassword> : <input
                                type={text === "비밀번호" ? "password" : "text"}
                                style={{
                                    marginLeft: "300px",
                                    marginTop: "20px",
                                    marginBottom: "20px",
                                    textAlign: "left"
                                }}
                                value={inputnickname}
                                placeholder={placeholder}
                                onChange={handleInputChange}
                            ></input>}

                            <MyButton
                                text={"완료"}
                                type={"afterEdit"}
                                onClick={text === "비밀번호" ? handlePasswordEditComplete : handleNickNameEditComplete}
                            ></MyButton>
                        </>
                    ) : (
                        // 수정버튼이 보이는 기본 상태
                        <>
                            <input
                                type={text === "비밀번호" ? "password" : "text"}
                                className={["profileText", `profileText_${text}`].join(" ")}
                                value={text === "닉네임" ? inputnickname : text === "아이디" ? inputId : currentPassword}
                                readOnly
                                onClick={handleTextEdit}
                            ></input>
                            {text !== "아이디" && (
                                <MyButton
                                    text={"수정"}
                                    type={"beforeEdit"}
                                    onClick={text === "닉네임" ? handleTextEdit : handlePasswordEdit}
                                ></MyButton>
                            )}
                        </>
                    )}
                </div>
            )}
        </div>
    );
}
export default PillMyProfileItem