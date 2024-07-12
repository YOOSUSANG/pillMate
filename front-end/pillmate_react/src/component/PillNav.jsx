import React,{useContext, useState} from "react";
import {useNavigate} from "react-router-dom";
import {pillAllInformation} from "../utils/PillAllInformation";
import {pillHandlerContext} from "../App"

const springUrl = "http://localhost:8080/"
const pillAllList = pillAllInformation();
const PillNav = ({leftChild, subLineOne, centerChild, subLineTwo, rightChild, state}) => {
    const {takingDispatch} = useContext(pillHandlerContext);
    const navigate = useNavigate()
    const takeFetchGet = (baseUrl, subUrl, userId) => {
        if (userId !== undefined) {
            fetch(baseUrl + subUrl + userId).then((res) => {
                return res.json()
            }).then((data) => {
                //data 필터링
                console.log(data)

                let takeFilterResult = pillAllList.filter((pit) => {
                    return data.some((cit) => pit.dl_name === cit.pillName);
                });
                console.log(takeFilterResult)

                takingDispatch({type: 'INIT', data: takeFilterResult})
            })

        }

    }
    // 카카오톡 지도 api로 변경
    const pillBoardTextClick = () => {
        navigate("/map")


    }
    const pillFindTextClick = () => {
        navigate("/generalSearch")
        window.location.reload()
    }
    const pillRecordTextClick = () => {
        const loginStorage = localStorage.getItem("userLogin");
        const loginObject = JSON.parse(loginStorage)
        takeFetchGet(springUrl, "pill/detail/", loginObject["userId"])
        setTimeout(() => {
            navigate("/record")
        }, 50);
    }
    return (
        <nav className="pillNav">
            <div className={["nav_leftChild", `nav_leftChild_${state}`].join(" ")}
                 onClick={pillBoardTextClick}> {leftChild} </div>
            <div className="nav_subLineOne"> {subLineOne} </div>
            <div className={["nav_centerChild", `nav_centerChild_${state}`].join(" ")}
                 onClick={pillFindTextClick}> {centerChild} </div>
            <div className="nav_subLineTwo"> {subLineTwo} </div>
            <div className={["nav_rightChild", `nav_rightChild_${state}`].join(" ")}
                 onClick={pillRecordTextClick}> {rightChild} </div>
        </nav>
    )
}
export default PillNav;