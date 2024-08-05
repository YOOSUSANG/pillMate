import { useNavigate } from "react-router-dom"
import PillAllNav from "../component/PillAllNav"
import { useEffect, useState } from "react"
import basketImg from "../assets/basketImg.png"
import useAuthorization from "../validaiton/useauthorization"
import PillCommonList from "../component/PillCommonList"
import BottomSheetModal from "../component/BottomSheetModal"



const springUrl = "http://localhost:8080/"
const PillPurchase = ()=>{
    useAuthorization()
    const navigate = useNavigate()
    const [pillStoreData, setPillStoreData] = useState([])
    const goProfile = () => {
        navigate("/mypage")
    }
    const goDetails = () => {
        navigate("/pill/detail")
    }
    const goBasket = ()=>{
        navigate("/orders/basket")
    }
    useEffect(()=>{
        fetch(springUrl + "pill_register", {
            method: "GET",
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then((res) => res.json())
            .then((data) => {
                console.log(data.data)
                setPillStoreData(data.data);
            })
            .catch((error) => {
      
            })
    },[])
    return (
    <div>
         <PillAllNav pillNavState={"pillStore"} onClick={goProfile}></PillAllNav>
         <img src={basketImg} style={{marginLeft:"530px", marginTop:"20px",cursor:"pointer"}} onClick={goBasket}/>
         <div>
            {pillStoreData.map((it) =>
                    <PillCommonList key={it.id} {...it} selectList={it} onClick={goDetails} currentState={"store"}/>
                )}
         </div>

    </div>)
}
export default PillPurchase