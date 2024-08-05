import MyButton from "./MyButton";
import React, {useState} from "react";

const PillMyEditPassword = ({password,newPassword,newRePassword,newPasswordSet,setIsCurrentPassword, setIsNewPassword, setNewRePassword}) => {

    const handleCurrentPasswordChange = (e) => {
        const inputCurrentPassword = e.target.value;
        if (password === inputCurrentPassword) {
            setIsCurrentPassword(true)
        }else{
            setIsCurrentPassword(false)
        }
    };
    const handleNewPasswordChange = (e) => {
        const inputRePassword = e.target.value;
        newPasswordSet(e.target.value)

        if (newRePassword === inputRePassword) {
            setIsNewPassword(true)
        }else{
            setIsNewPassword(false)
        }

    }


    const handleNewRePasswordChange = (e) => {
        const inputRePassword = e.target.value;
        setNewRePassword(inputRePassword)
        if (newPassword === inputRePassword) {
            setIsNewPassword(true)
        }else{
            setIsNewPassword(false)
        }
    }

    return (
        <div className="pillMyEditPassword">
            <div className={"pill_password"}>현재 비밀번호</div>
            <input
                type={"password"}
                placeholder={"현재 비밀번호"}
                onChange={handleCurrentPasswordChange}/>
            <div className={"pill_password"}>새 비밀번호</div>
            <input

                type={"password"}

                placeholder={"새 비밀번호"}
                onChange={handleNewPasswordChange}/>
            <div className={"pill_password"}>새 비밀번호 재입력</div>
            <input
                style={{marginBottom: "32px"}}
                type={"password"}

                placeholder={"새 비밀번호"}
                onChange={handleNewRePasswordChange}/>


        </div>)
}
export default PillMyEditPassword