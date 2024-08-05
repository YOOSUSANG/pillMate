import { useEffect } from "react";
import useRefresh from "./userefresh";

const springUrl = "http://localhost:8080/";
function useAuthorization(){
    const reissueToken = useRefresh()
    
    useEffect(()=>{
        fetch(springUrl+"validation/jwt", {
            method: "POST",
            credentials: 'include'
        })
        .then((res) => {
            //authorization 없거나 만료됐다.
            console.log(res.status)
            if (res.status === 401) {
                reissueToken();
            }
        })
        .catch((error) => {
            console.log(error);
        });
    },[])
}
export default useAuthorization;
