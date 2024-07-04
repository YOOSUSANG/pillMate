import FrameItem from "./GeneralSearchViewItem";
import React, {useCallback, useState} from "react";
import {useNavigate} from "react-router-dom";
import GeneralSearchViewItem from "./GeneralSearchViewItem";

const PillSelectFrame = ({text, list}) => {
    const [pillId, setPillId] = useState(1);

    // mount시에만 이 함수가 생성됨 그 이후에는 재 생성 안함
    const handleClickEmote = useCallback((id) => {
        setPillId(id)
    }, [])

    return (
        <div className="select_frame">
            <div className="frame_name">{text}
            </div>
            <div className={["frame_list_wrapper", text === "*제형" ? "frame_item_wg" : "frame_item_nowg"].join(" ")}>
                {list.map((it) => <GeneralSearchViewItem key={it.id} {...it} onclick={handleClickEmote}
                                             inSelected={pillId === it.id} title={text}></GeneralSearchViewItem>)}
            </div>
        </div>
    )
}
export default React.memo(PillSelectFrame);