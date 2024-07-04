import React, {useState} from "react";
import PillHeader from "./PillHeader";
import PillNav from "./PillNav";
import PillSubNav from "./PillSubNav";


const PillAllNav = ({pillNavState, pillSubNavState,onClick}) => {
    return (
        <div>
            <PillHeader onClick={onClick}></PillHeader>
            <PillNav rightChild={"복용 이력"} subLineOne={"|"} centerChild={"약 검색"} subLineTwo={"|"}
                     leftChild={"주변약국"} state={pillNavState}></PillNav>
            {pillNavState === 'pillFind' &&
                <PillSubNav leftName={"일반 검색"} rightName={"이미지 검색"} state={pillSubNavState}></PillSubNav>}

        </div>
    )
}
export default React.memo(PillAllNav);
