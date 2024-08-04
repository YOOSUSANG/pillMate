import React, {useContext, useState} from "react";
import {pillDetailsContext, pillHandlerContext} from "../App";
import MyButton from "./MyButton";
import BottomSheetModal from "./BottomSheetModal";
const springUrl = "http://localhost:8080/"
const PillCommonList = ({
                          dl_name,
                          dl_class_no,
                          img_key,
                          dl_material,
                          dl_custom_shape,
                          selectList,
                          quantity,
                          price,
                          onClick,
                          buttonType,
                          currentState
                      }) => {

    const defaultImage = "URL_OF_DEFAULT_IMAGE";
    const {pillClickDetails} = useContext(pillDetailsContext)
    // 장바구니 상품 삭제
    const cancelBasketItem = () => {
        fetch(`${springUrl}basket/pills/delete/${selectList.basketItemId}`, {
            method: "POST",
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then((res) => res.json())
            .then((data) => {

            })
            .catch((error) => {
      
            })
        window.location.reload();
    }
    return (<div>
        <div className="pillListItem">
            <div onClick={() => {
                onClick()
                pillClickDetails({...selectList, state:currentState})
            }}>
                <img className="pill_img_wrapper" src={img_key} onError={(e) => e.target.src = defaultImage}/>
                <div className="pill_name">{dl_name}</div>
            </div>
            <div className="info_wrapper" onClick={() => {
                onClick()
                pillClickDetails({...selectList, state:currentState})
            }}>
                {dl_class_no !== undefined && <div className="pill_class_no"><span style={{fontWeight: "bold"}}>분류:</span> {dl_class_no}</div>}
                {dl_material !== undefined && <div className="pill_material"><span style={{fontWeight: "bold"}}>성분:</span> {dl_material}</div>}
                {dl_custom_shape !== undefined && <div className="pill_dl_custom_shape"><span style={{fontWeight: "bold"}}>제형:</span> {dl_custom_shape}</div>}
                <div className="pill_dl_drug_shape"><span style={{fontWeight: "bold"}}>수량:</span> {quantity}</div>
                <div className="pill_dl_drug_shape"><span style={{fontWeight: "bold"}}>가격:</span> <span style={{color:"#64C964"}}>{price}</span></div>
            </div>
            {currentState === "basket" && 
                <MyButton text={"취소"} type={buttonType} onClick={cancelBasketItem} ></MyButton> 
            }
        </div>
    </div>)
}
export default React.memo(PillCommonList)
