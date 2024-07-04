import React, {useContext, useEffect} from "react"
import {useNavigate} from "react-router-dom";
import {pillDetailsContext} from "../App"

const PillDetails = ({dl_name, img_key, successList}) => {
    console.log({dl_name, img_key})
    const navigate = useNavigate()
    const {pillClickDetails} = useContext(pillDetailsContext)
    const reImageSearch = () => {
        navigate("/imagesearch", {replace: true})

    }
    const goDetails = () => {
        navigate("/pill/detail")
    }

    return (
        <div className="sampleImageAndText">
            <img className="PillDetailImages" src={img_key}/>
            <div className="PillDetailName">{dl_name}</div>
            <div className="PillButtonSet">
                <div className="PillDetailsButton" onClick={() => {
                    pillClickDetails(successList)
                    goDetails()
                }}>상세정보 확인
                </div>
                <span style={{fontSize: "30px"}}>  | </span>
                <div className="PillReButton" onClick={reImageSearch}> 약 다시 인식하기</div>
            </div>
        </div>
    )
}
export default React.memo(PillDetails)