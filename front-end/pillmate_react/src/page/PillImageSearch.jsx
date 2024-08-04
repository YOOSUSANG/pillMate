import PillAllNav from "../component/PillAllNav";
import React, {useRef, useState} from "react";
import MyButton from "../component/MyButton";
import {useNavigate} from "react-router-dom";
import axios from 'axios'
import imageSearchSampleImage from './../assets/imageSearchSampleImage.png'
import {toast, ToastContainer} from "react-toastify";
import useAuthorization from "../validaiton/useauthorization";
/*
    약 이미지 검색 페이지
*/

const PillImageSearch = () => {
    useAuthorization()
    const fileInputRef = useRef(null);
    const navigate = useNavigate()
    const [file, setFile] = useState(null);
    const handleButtonClick = () => {
        // 클릭 시 파일 업로드 창 열기
        fileInputRef.current.click();
    };
    const handleFileChange = (e) => {
        const selectedFile = e.target.files[0];
        setFile(selectedFile);

        const formData = new FormData();
        formData.append('file', selectedFile);

        fetch('http://localhost:5000/upload', {
            method: 'POST',
            body: formData,
        })
            .then(response => response.json())
            .then(data => {
                console.log('Server response:', data);
            })
            .catch(error => {
                console.error('Error uploading file:', error);
            });
        toast.success("현재 인식중입니다...", {autoClose: 20000,position : "top-center"});
        setTimeout(() => {
            navigate("/imagesearch/detail");
        }, 18000); // 18초
    };

    const goProfile = () => {
        navigate("/mypage")
    }
    return (
        <div>
            <PillAllNav pillNavState={"pillFind"} pillSubNavState={"imageSearch"} onClick={goProfile}></PillAllNav>
            <div className="sampleImageAndText">
                <img src={imageSearchSampleImage}></img>
                <div className="sampleName">⦁ 예시사례</div>
                <MyButton type={'default'} text={"약 인식하기"} onClick={handleButtonClick}></MyButton>
                <input
                    type="file"
                    accept="image/*" // 이미지 파일만 선택할 수 있도록 함
                    style={{display: 'none'}}
                    ref={fileInputRef}
                    onChange={handleFileChange}
                />
                <div>
                    <div className="caution">주의사항</div>
                    <div style={{marginBottom: 10}}>● 알약을 초점에 맞춰서 선명하게 촬영해주세요.</div>
                    <div style={{marginBottom: 10}}>● 최대한 화면에 알약이 꽉 차게 촬영해주세요.</div>
                    <div style={{marginBottom: 10}}>● 알약을 위에서 아래 수직으로 촬영해주세요.</div>
                    <div style={{marginBottom: 10}}>● 1:1 비율로 촬영해주세요.</div>
                </div>
                <ToastContainer></ToastContainer>
            </div>
        </div>
    )
}
export default PillImageSearch