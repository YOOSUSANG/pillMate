import PillAllNav from "../component/PillAllNav";
import React, { useContext, useEffect, useState } from "react";
import PillListForm from "../component/PillListForm";
import { ToastContainer } from "react-toastify";
import { useNavigate } from "react-router-dom";
import { pillAllInformation } from "../utils/PillAllInformation";
import useAuthorization from "../validaiton/useauthorization";

/*
    복용이력 페이지
*/

const pillAllList = pillAllInformation();

const PillRecord = () => {
    useAuthorization();
    const [pillRecordList, setPillRecordList] = useState([]);
    const navigate = useNavigate();
    console.log(pillRecordList);

    useEffect(() => {
        const takeData = localStorage.getItem("takingStatus");
        if (takeData) {
            setPillRecordList(JSON.parse(takeData));
        } else {
            setPillRecordList([]);
        }
    }, []);

    const goDetails = () => {
        navigate("/record/detail");
    };

    const goProfile = () => {
        navigate("/mypage");
    };

    return (
        <div>
            <PillAllNav pillNavState={"pillRecord"} onClick={goProfile}></PillAllNav>
            
            <div className="take_pillCount">현재 복용 약품: {pillRecordList.length}개</div>
            <div>
                {pillRecordList.map((it) => (
                    <PillListForm 
                        key={it.id} 
                        {...it} 
                        selectList={it}  
                        buttonType={"cancellation"} 
                        currentState={"pillRecord"} 
                        onClick={goDetails}
                    />
                ))}
            </div>
            <ToastContainer />
        </div>
    );
};

export default PillRecord;
