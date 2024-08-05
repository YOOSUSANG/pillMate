import { useNavigate } from "react-router-dom";
import useAuthorization from "../validaiton/useauthorization";
import PillHeader from "../component/PillHeader";
import PillNav from "../component/PillNav";
import emotion from "../assets/emotion1.png"
import { useEffect, useState } from "react";
import MyButton from "../component/MyButton";
import PillCommonList from "../component/PillCommonList";

const springUrl = "http://localhost:8080/"
const PillOrderBasket = ()=>{
    useAuthorization()
    const navigate = useNavigate()
    const [curBasket,setCurBasket] = useState({})

    useEffect(()=>{
        fetch(springUrl + "basket/pills", {
            method: "GET",
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then((res) => res.json())
            .then((data) => {
                console.log(data.data)
                setCurBasket(data.data);
            })
            .catch((error) => {
      
            })

    },[])

    const goDetails = () => {
        navigate("/pill/detail")
    }

    const goHome = ()=>{
        navigate("/mypage")
    }
    const goStore = ()=>{
        navigate("/pill_store")
    }
    const kakaopay = ()=>{
        fetch(springUrl + "orders/billings", {
            method: "POST",
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then((res) => res.json())
            .then((data) => {
                if (data.next_redirect_pc_url) {
                    window.location.href = data.next_redirect_pc_url;
                } else {
                    console.error("next_redirect_pc_url not found in response data");
                }
            })
            .catch((error) => {
                console.error("Error:", error);
            });
    }
    return (
    <div>
        <PillHeader onClick={goHome}></PillHeader>
        <PillNav centerChild={"장바구니"} state={"common"}></PillNav>
        <div className="common-layout">
            {curBasket.totalPrice == 0 
            ?
            <div className="common-layout">
                <img src={emotion}/>
                <div style={{marginTop:70}}>
                장바구니에 상품이 없습니다. 상품을 추가해보세요
                </div>
                <MyButton text={"상품보러가기"} onClick={goStore}></MyButton>
            </div>
            :
            <div>
                {Array.isArray(curBasket.basketItems) && curBasket.basketItems.map((it) => (
                        <PillCommonList key={it.id} {...it} selectList={it} onClick={goDetails} currentState={"basket"} buttonType={"cancellation"} />
                ))} 
            </div>}
            {curBasket.totalPrice !== 0  && 
                <div className="common-layout">
                     <div style={{marginTop:30}}>
                        총 장바구니 금액 : <span style={{color : "#32CD32"}}>{curBasket.totalPrice}</span>
                    </div >
                    <div style={{marginTop:30}}>
                        <MyButton text={"주문하기"} onClick={kakaopay} ></MyButton>
                    </div>
                  
                </div>
                }
        </div>
    </div>
    )
}
export default PillOrderBasket;