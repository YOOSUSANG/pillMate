import React, {useContext} from "react";
import {pillHandlerContext} from "../App";

const GeneralSearchViewItem = ({ id, text, img, english, onclick, inSelected, title}) => {
    const { recShapeState } = useContext(pillHandlerContext)
    return (
        <div className="frame_item">
            <div onClick={() => {onclick(id);recShapeState(text,title);}}>
                <img src={img}/>
            </div>
            {text !== "기타" && text !=="전체" &&
                <span className={[`item_${english}`, inSelected ? `item_${id}` : "item_off"].join(" ")}>{text}</span>}
            {(text === "기타" || text === "전체")&& inSelected && <span style={{color:"#64C964"}}>_____</span>}


        </div>
    )
}

export default React.memo(GeneralSearchViewItem);