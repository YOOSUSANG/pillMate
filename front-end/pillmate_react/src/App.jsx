import './App.css'
import React, {useCallback, useEffect, useReducer, useState, useContext, createContext} from "react";
import {BrowserRouter, Route, Routes, useNavigate} from 'react-router-dom'
import PillLogin from './page/PillLogin'
import PillGeneralSearch from './page/PillGeneralSearch'
import PillRecord from "./page/PillRecord";
import PillImageSearch from "./page/PillImageSearch";
import Login from "./page/PillLogin"
import PillProfile from "./page/PillProfile"
import PillMyProfile from "./page/PillMyProfile";
import 'react-toastify/dist/ReactToastify.css';
import {toast} from "react-toastify";
import PillGeneralSearchList from "./page/PillGeneralSearchList";
import PillDetailSearchResult from "./page/PillDetailSearchResult";
import PillImageFindSuccess from "./page/PillImageFindSuccess";
import  PillDetailRecordResult from "./page/PillDetailRecordResult"
import PillKakaoMap from './page/PillKakaoMap';
import Notfound from './page/Notfound';
const springUrl = "http://localhost:8080/"
const pillInformationClickReducer = (state, action) => {
    let newState = {}
    switch (action.type) {
        case 'INIT':{
            localStorage.setItem("pillSearchState", JSON.stringify(action.data))
            return action.data
        }
        case 'REC':
        case 'SHAPE':
        case 'COLOR':
        case 'INPUT':{
            newState = {...state, [action.key]: [action.value]}
            break
        }
        default:
            return state
    }
    localStorage.setItem("pillSearchState", JSON.stringify(newState))
    return newState
}
const takingListReducer = (state, action) => {
    let newState = []
    let check = []
    const userLoginDataString = localStorage.getItem("userLogin");
    const userLoginData = JSON.parse(userLoginDataString);
    const userId = userLoginData["userId"]
    switch (action.type) {
        case 'INIT':{
            localStorage.setItem("takingStatus", JSON.stringify(action.data))
            return action.data
        }
        case 'OnCreate':{
            //중복된 약을 추가할 경우 체크하기
            check = state.filter((it) => it.dl_name === action.data.dl_name)
            if (check.length !== 0) {
                toast.success("복용 이력에 존재하는 약품입니다.", {autoClose: 2000})
                newState = [...state]
            } else {
                toast.success("복용 추가 완료", {autoClose: 2000})
                const pillName = action.data.dl_name
                takeFetchPost(springUrl, "pill/take", {"name" : pillName})
                newState = [...state, action.data]
            }
            break
        }
        case 'OnRemove':{
            const pillName = action.data
            newState = state.filter((it) => it.dl_name !== action.data)
            takeFetchPost(springUrl,"pill/delete",{"name" : pillName})
            break
        }
        default:
            return state
    }
    localStorage.setItem("takingStatus", JSON.stringify(newState))
    return newState
}
const takeFetchPost = (baseUrl, subUrl, pill) => {
  fetch(baseUrl + subUrl, {
      method: "POST",
      credentials: 'include',
      headers: {
          'Content-Type': 'application/json',
      },
      body: JSON.stringify(pill)
  })
      .then((res) => res.json())
      .then((data) => {


      })
      .catch((error) => {

      })
}

export const PillMateLoginStateContext = createContext()
export const pillHandlerContext = createContext()
export const pillDetailsContext = createContext()
function App() {
    // 로그인 정보를 객체로 담아 넘긴다.
    const [loginData, setLoginData] = useState({})
    const [DetailState, setDetailState] = useState({})
    const [state, pillClickDispatch] = useReducer(pillInformationClickReducer, {})
    const [takingStatus, takingDispatch] = useReducer(takingListReducer, [])

    //맨 처음 web 화면이 갱신될 떄 처음 실행되는 부분으로 무조건 localStorage안에 pillSearchState라는 이름으로 저장됨
    useEffect(() => {
        pillClickDispatch({
            type: "INIT", data: {
                ["pillSearch"]: ["not"],
                ["custom_shape"]: ["정제"],
                ["drug_shape"]: ["원형"],
                ["color"]: ["빨강"]
            }
        })
    }, [])

    useEffect(() => {
        // localStorage.setItem("userLogin",JSON.stringify({}))
        const loginStorage = localStorage.getItem("userLogin");
        if (loginStorage) {
            console.log("loginStorage 값 존재")
            console.log(JSON.parse(loginStorage))
            setLoginData(JSON.parse(loginStorage))
        }

    }, [])
    useEffect(() => {
        const takingStorage = localStorage.getItem("takingStatus");
        if (takingStorage) {
            console.log("takingStorage 값 존재")
            //get으로 불러오기
            takingDispatch({type: "INIT", data: JSON.parse(takingStorage)})
        }

    }, [])

    // 일반 검색일때 사용되는 함수
    const recShapeState = useCallback((value, title) => {
        if (title === "*제형") {
            pillClickDispatch({type: 'REC', key: "custom_shape", value: value})
        }
        if (title === "*모양") {
            pillClickDispatch({type: 'SHAPE', key: "drug_shape", value: value})

        }
        if (title === "*색상") {
            pillClickDispatch({type: 'COLOR', key: "color", value: value})
        }
        if (title === "입력") {
            pillClickDispatch({type: 'INPUT', key: "pillSearch", value: value})
        }
    }, [])

    // 복용약 섭취시 여기에 저장 -> 이거를 details에 넘겨서 {}로 받아오기!
    // 후에 db로 변경

    const takingOnCreate = useCallback((takeMedicine) => {

        takingDispatch({type: "OnCreate", data: takeMedicine})

    }, [])
    const takingOnRemove = useCallback((takeMedicineName) => {
        // 정말로 삭제할건지 묻기

        toast.success("복용 삭제 완료", {autoClose: 2000});

        takingDispatch({type: "OnRemove", data: takeMedicineName})
        window.location.reload();

    }, [])


    const pillClickDetails = useCallback((detailState) => {
        setDetailState(detailState)
        localStorage.setItem("pillDetailState", JSON.stringify(detailState))
    }, [])
    return (
        <PillMateLoginStateContext.Provider value={{loginData, setLoginData}}>
            <pillHandlerContext.Provider
                value={{recShapeState, TakingOnCreate: takingOnCreate, TakingOnRemove: takingOnRemove, setDetailState , takingDispatch}}>
                <pillDetailsContext.Provider value={{pillClickDetails}}>
                  <div className="App">
                      <Routes>
                          <Route path="/" element={<PillLogin></PillLogin>}></Route>
                          <Route path='/generalsearch' element={<PillGeneralSearch></PillGeneralSearch>}></Route>
                          <Route path='/imagesearch' element={<PillImageSearch></PillImageSearch>}></Route>
                          <Route path='/imagesearch/detail'
                                  element={<PillImageFindSuccess></PillImageFindSuccess>}></Route>
                          <Route path='/record' element={<PillRecord></PillRecord>}></Route>
                          <Route path={"/generalSearch/list"}
                                  element={<PillGeneralSearchList></PillGeneralSearchList>}></Route>
                          <Route path={"/pill/detail"}
                                  element={<PillDetailSearchResult
                                      detailState={DetailState}></PillDetailSearchResult>}></Route>
                          <Route path={"/record/detail"} element={<PillDetailRecordResult></PillDetailRecordResult>}></Route>
                          <Route path={'/Login'} element={<Login></Login>}></Route>
                          <Route path={'/PillProfile'} element={<PillProfile></PillProfile>}></Route>
                          <Route path={'/PillMyProfile'} element={<PillMyProfile></PillMyProfile>}></Route>
                          <Route path={"/map"} element={<PillKakaoMap></PillKakaoMap>}></Route>
                          <Route path="*" element = {<Notfound/>}></Route>
                      </Routes>
                      </div>
                </pillDetailsContext.Provider>
            </pillHandlerContext.Provider>
        </PillMateLoginStateContext.Provider>
    );
}

export default App;
