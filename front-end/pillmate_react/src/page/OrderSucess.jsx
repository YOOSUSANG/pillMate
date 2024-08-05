import { useNavigate } from "react-router-dom"
import PillHeader from "../component/PillHeader"
import MyButton from "../component/MyButton"
import useAuthorization from "../validaiton/useauthorization"
import { useEffect, useState } from "react"

const springUrl = "http://localhost:8080/"
const OrderSucess = () =>{
    useAuthorization()
    const navigate = useNavigate()
    const [orderState,setOrderState] = useState({})
    const goProfile = ()=>{
        // 약 구매 목록으로 이동
        navigate("/mypage")
    }
    useEffect(()=>{

        fetch(springUrl + "order_info", {
            method: "POST",
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then((res) => res.json())
            .then((data) => {
                setOrderState(data)
            })
            .catch((error) => {
                console.error("Error:", error);
            });


    },[])
    return (
    <div>
        <PillHeader onClick={goProfile} />
        <div className="common-layout">
            <div className="successComplete">주문완료</div>
            <div className="order-layout">
                <div className="order-content">
                    <div style={{color:"#A3A3A3"}}>주문번호</div>
                    <div style={{marginTop:20}}>{orderState.tid}</div>
                </div>
                <div className="order-content">
                    <div style={{color:"#A3A3A3"}}>주문상품</div>
                    <div style={{marginTop:20}}>{orderState.itemName}</div>
                    <div style={{marginTop:10, color:"#4CAF50"}}>{orderState.amount}원</div>
                </div>
                <div className="order-content">
                    <div style={{color:"#A3A3A3"}}>배송상태</div>
                    <div style={{marginTop:20}}>{orderState.deliveryStatusKr}</div>
                </div>
                <MyButton text={"확인"} detailType={"order"} onClick={goProfile}></MyButton>
            </div>
        </div>
    </div>) 
}
export default OrderSucess