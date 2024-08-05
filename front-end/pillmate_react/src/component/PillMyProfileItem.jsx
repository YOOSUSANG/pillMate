import React, { useState } from "react";
import axios from "axios";

const springUrl = "http://localhost:8080/";

const PillMyProfileItem = ({ text, isTextOrImg, profileImageUrl, setProfileImageUrl, email, name, nickname }) => {

    const handleFileChange = async (event) => {
        const file = event.target.files[0];
        if (file) {
            await handleUpload(file);
        }
    }

    const handleUpload = async (file) => {
        if (!file) return;

        const formData = new FormData();
        formData.append('upload', file);

        try {
            const response = await fetch(springUrl + 'image/upload', {
                method: 'POST',
                credentials: 'include',
                body: formData,
            });

            if (!response.ok) {
                throw new Error('Failed to upload image');
            }

            const data = await response.json();
            const imageUrl = data.url;

            setProfileImageUrl(imageUrl);  // 상위 컴포넌트의 상태 업데이트

            // 로컬 스토리지 업데이트
            const updatedLoginData = {
                ...JSON.parse(localStorage.getItem("userLogin")),
                profileImageUrl: imageUrl
            };
            localStorage.setItem("userLogin", JSON.stringify(updatedLoginData));

        } catch (error) {
            console.error('Error uploading image:', error);
        }
    }

    return (
        <div className="pill_profile_frame">
            <div className="pill_profile_text">{text}</div>
            {text === "사진" ?
                (
                    <div className="pillProfileImage">
                        <img className="pill_profileImage" src={profileImageUrl} alt="프로필 이미지" />
                        <input type="file" onChange={handleFileChange} style={{ display: 'none' }} id="fileInput" />
                        <button 
                        className="MyButton_MyImageChange"
                        onClick={() => document.getElementById('fileInput').click()}>수정</button>
                    </div>
                ) :
                (
                    <div className="pill_profile_text_frame">
                        <input
                            type="text"
                            className={["profileText", `profileText_${text}`].join(" ")}
                            value={text === "닉네임" ? nickname : text === "이메일" ? email : name}
                            readOnly
                        />
                    </div>
                )
            }
        </div>
    );
}

export default PillMyProfileItem;
