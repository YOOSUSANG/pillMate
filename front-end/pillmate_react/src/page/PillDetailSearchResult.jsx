/*
    약 상세정보 페이지
*/

import React, {useContext, useEffect, useState} from "react";
import MyButton from "../component/MyButton";
import {useNavigate} from "react-router-dom";
import {pillHandlerContext} from "../App"
import {toast, ToastContainer} from "react-toastify";
import PillAllNav from "../component/PillAllNav";
import useAuthorization from "../validaiton/useauthorization";
import BottomSheetModal from "../component/BottomSheetModal";

const springUrl = "http://localhost:8080/"
const PillDetailSearchResult = () => {
    useAuthorization()
    const navigate = useNavigate()
    const [detailState, setDetailState] = useState({})
    const [modalIsOpen, setModalIsOpen] = useState(false);
    const {TakingOnCreate} = useContext(pillHandlerContext)
    const [effectivenessText, setEffectivenessText] = useState('');
    const [methodText, setMethodText] = useState('');
    const [saveText, setSaveText] = useState('');
    const [displayText, setDisplayText] = useState('');
    const [showModal, setShowModal] = useState(false)
    const [count,setCount] = useState(1)
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
    const handleOpenModal = () => {
        setShowModal(true);
    };
    
      const handleCloseModal = () => {
        setShowModal(false);
        fetch(springUrl + "basket/pills/add", {
            method: "POST",
            credentials: 'include',
            body: JSON.stringify(
                {
                    pillId:detailState.id,
                    quantity:count
                }
            ),
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then((res) => res.json())
            .then((data) => {

            })
            .catch((error) => {
      
            })
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
                {detailState.state === "search" && 
                    <div>
                        <MyButton type={'default'} text={"복용 이력에 추가"} onClick={() => {TakingOnCreate(detailState);}}></MyButton>
                        <ToastContainer/>
                    </div>}
                    {detailState.state === 'store'&& <MyButton type={'default'} text={"구매하기"} onClick={handleOpenModal}></MyButton>}
                    {detailState.state === 'basket'&& <MyButton type={'default'} text={"수량변경"} onClick={handleOpenModal}></MyButton>}
                    {detailState.state === 'orderItem'&& <MyButton type={'default'} text={"재구매하기"} onClick={handleOpenModal}></MyButton>}
                <BottomSheetModal show={showModal} onClose={handleCloseModal} count ={count} setCount={setCount}></BottomSheetModal>
            </div>
        </div>)
}
export default PillDetailSearchResult;