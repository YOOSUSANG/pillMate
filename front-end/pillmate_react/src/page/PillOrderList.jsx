import { useNavigate } from "react-router-dom"
import PillHeader from "../component/PillHeader"
import PillNav from "../component/PillNav"
import { useEffect, useState } from "react"
import emotion from "../assets/emotion1.png"
import useAuthorization from "../validaiton/useauthorization"
import MyButton from "../component/MyButton"
import PillCommonList from "../component/PillCommonList"

const springUrl = "http://localhost:8080/"
const PillOrderList = ()=>{
    useAuthorization()
    const navigate = useNavigate()
    const [orders, setOrders] = useState({})
    const isEmpty = Object.keys(orders).length === 0
    const goHome = ()=>{
        navigate("/mypage")
    }

    const goStore = ()=>{
        navigate("/pill_store")
    }
    const goDetails = () => {
        navigate("/pill/detail")
    }

    useEffect(()=>{
        fetch(springUrl + "members/orders", {
            method: "GET",
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then((res) => res.json())
            .then((data) => {
                console.log(data.data)
                setOrders(data.data)
            })
            .catch((error) => {
      
            })
    },[])
    const orderListDelete = (id)=>{
        fetch(`${springUrl}orders/${id}/delete`, {
            method: "POST",
            credentials: 'include',
            headers: {
              'Content-Type': 'application/json',
            },
          })
            .then((res) => {
                window.location.reload();
            })
            .catch((error) => {
              console.error("Error:", error);
            });
          
    }
    const orderListCancel = (id)=>{
        fetch(`${springUrl}orders/${id}/RefundBillings`, {
            method: "POST",
            credentials: 'include',
            headers: {
              'Content-Type': 'application/json',
            },
          })
            .then((res) => {
                window.location.reload();
            })
            .catch((error) => {
              console.error("Error:", error);
            });
          
    }
    return (<div>
        <PillHeader onClick={goHome} />
        <PillNav centerChild={"주문 목록"} state={"common"}/>
        <div>
            {
            isEmpty 
            ?
            <div className="common-layout">
                <img src={emotion}/>
                <div style={{marginTop:70}}>
                주문하신 상품이 없습니다. 상품을 구매해볼까요??
                </div>
                <MyButton text={"상품보러가기"} onClick={goStore}></MyButton>
            </div>
            :
            <div className="orders-layout">
                {orders.map((it) => (
                    <div key={it.id}>
                        <div style={{marginRight:40, marginTop:30}}>{new Date(it.orderTime).toISOString().split('T')[0]}<span>  {it.deliveryStatusKr}</span>
                        <span style={{marginLeft:180}}>
                            <MyButton text={"주문취소"} type={"orderDelete"} removeName={it.id}onParamClick={orderListCancel} ></MyButton> 
                            <MyButton text={"주문내역삭제"} type={"orderDelete"} removeName={it.id}onParamClick={orderListDelete} ></MyButton> 
                            </span></div>
                        {Array.isArray(it.orderItems) && it.orderItems.map((item) => (
                        <PillCommonList key={item.id} {...item} selectList={item} onClick={goDetails} currentState={"orderItem"}/>
                ))} 
                    </div>
                ))}

            </div>
            }
        </div>
    </div>)
}
export default PillOrderList