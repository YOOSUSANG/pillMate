/*
    복용 이력 페이지
*/

import React, {useContext, useEffect, useState} from "react";
import PillHeader from "../component/PillHeader";
import {useNavigate} from "react-router-dom";


const PillDetailTakeResult = () => {

    const [detailState, setDetailState] = useState({})
    const navigate  =useNavigate()

    useEffect(() => {
        const localData = localStorage.getItem("pillDetailState")
        if (localData) {
            console.log("pillDetailState local 존재")
            setDetailState(JSON.parse(localData))
        }

    }, []);
    const goProfile = () => {
        navigate("/PillProfile")
    }
    return (
        <div>
            <PillHeader onClick={goProfile}></PillHeader>
            <div className="sampleImageAndText">
                <div className="detail_pillName">{detailState["dl_name"]}</div>
                <img className="detail_pillImg" src={detailState["img_key"]}></img>
                <div className="pill_explanation_box">
                    <div className="pill_explanation">약품 설명</div>
                </div>
                <div className="pill_explanation_content">
                    {detailState["dl_content1"]}
                </div>
                <div className="pill_explanation_content">
                    {detailState["dl_content2"]}
                </div>
                <div className="pill_explanation_content">
                    {detailState["dl_content3"]}
                </div>
            </div>
        </div>)
}
export default PillDetailTakeResult;