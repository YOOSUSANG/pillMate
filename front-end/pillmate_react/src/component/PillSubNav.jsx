import {useNavigate} from "react-router-dom";
import {useState} from "react";

const PillSubNav = ({leftName, rightName, state}) => {
    const navigate = useNavigate()
    const [current, setCurrent] = useState()
    const subLeftClick = ()=>{
        if(state === 'imageSearch'){
            navigate('/generalSearch')
        }

        if(state === 'qsBoard'){
            navigate('/freeBoard')
        }
    }
    const subRightClick = ()=>{
        if(state === 'generalSearch'){
            navigate('/imageSearch')
        }
        if(state === 'freeBoard'){
            navigate('/qsBoard')
        }

    }


    return (
        <nav className='pillSubNav'>
            <div className={['nav_pillSubLeftChild', `nav_pillSubLeft_${state}`].join(" ")} onClick={subLeftClick}>{leftName}</div>
            <div className={['nav_pillSubRightChild', `nav_pillSubRight_${state}`].join(" ")} onClick={subRightClick}>{rightName}</div>
        </nav>)
}
export default PillSubNav;