import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

const springUrl = "http://localhost:8080/";

function useRefresh(){
    const navigate = useNavigate();
    const reissueToken = () => {
        fetch(springUrl + "reissue", {
            method: "POST",
            credentials: 'include'
        })
        .then((res) => {
            //refresh 만료시
            if (res.status === 400) {
                navigate("/");
                localStorage.setItem("userLogin", JSON.stringify({}))
                localStorage.setItem("takingStatus", JSON.stringify([]))
                // toast.success("로그인이 필요합니다.", { position: "top-center", hideProgressBar: true });
                // setTimeout(() => {
                //     navigate("/");
                // }, 0.00001);
            }
            window.location.reload();
        })
        .catch((error) => {
            console.log(error);
        });
    };
    return reissueToken;
}
export default useRefresh;
