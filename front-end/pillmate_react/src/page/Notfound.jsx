import { useEffect } from "react";
import { ToastContainer } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import useAuthorization from "../validaiton/useauthorization";
import PillHeader from "../component/PillHeader";
import { useNavigate } from "react-router-dom";
const Notfound = () => {
    const nav = useNavigate()
    useAuthorization();
    const goHome = ()=>{
        nav("/")
    }
    return (
        <div>
            <PillHeader onClick={goHome}/>
            <div className="notfound">
                <div>잘못된 페이지입니다. 경로를 다시 확인해주세요</div>
                <ToastContainer />
            </div>
        </div>
    );
};

export default Notfound;
