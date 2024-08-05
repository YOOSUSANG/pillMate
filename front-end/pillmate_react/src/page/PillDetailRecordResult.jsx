import React, {useContext, useEffect, useState} from "react";
import MyButton from "../component/MyButton";
import {useNavigate} from "react-router-dom";
import {pillHandlerContext} from "../App"
import {toast, ToastContainer} from "react-toastify";
import PillAllNav from "../component/PillAllNav";
import useAuthorization from "../validaiton/useauthorization";

const PillDetailRecordResult = ()=>{
    useAuthorization()
    const navigate = useNavigate()
    const [detailState, setDetailState] = useState({})
    const [displayText, setDisplayText] = useState('');


    useEffect(() => {
        const localData = localStorage.getItem("pillDetailState")
        if (localData) {
            console.log("pillDetailState local 존재")
            setDetailState(JSON.parse(localData))
        }


    }, []);

    const goProfile = () => {
        navigate("/mypage")
    }

    const handleEffectivenessClick = () => {
        setDisplayText(detailState["dl_effect"]);
    };
    const handleMethodClick = () => {
        setDisplayText(detailState["dl_method"]);
    };
    const handleSaveClick = () => {
        setDisplayText(detailState["dl_save"]);
    };

    return (
        <div>
            <PillAllNav onClick={goProfile}></PillAllNav>
            <div className="sampleImageAndText">
                <img className="detail_pillImg" src={detailState["img_key"]}></img>
                <div className="detail_pillName">{detailState["dl_name"]}</div>
                <div className="detail_pillClass">분류: {detailState["dl_class_no"]}</div>
                <div className="detail_pillMaterial">성분: {detailState["dl_material"]}</div>
                <div className="detail_pillMaterial_en">영문: {detailState["dl_material_en"]}</div>
                {/*여기서부터 추가*/}
                <section className="detail_button">
                    <button className="detail_effect" onClick={handleEffectivenessClick}>효능/효과</button>
                    <button className="detail_effect" onClick={handleMethodClick}>복용법</button>
                    <button className="detail_effect" onClick={handleSaveClick}>보관법</button>
                </section>
                {displayText && <div className="display_text" style={{ whiteSpace: 'pre-line' }}>{displayText}</div>}
                <ToastContainer/>
            </div>
        </div>)
}
export default PillDetailRecordResult