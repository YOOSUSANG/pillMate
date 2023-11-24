import {pillAllInformation} from '../utils/PillAllInformation'
import PillListForm from "../component/PillListForm";
import React, {useContext, useEffect, useState} from "react";
import PillAllNav from "../component/PillAllNav";
import {useNavigate} from "react-router-dom";

/*
    약 일반 검색 결과인 약 리스트 페이지
*/

const PillGeneralSearchList = () => {
    const [currentSearch, setCurrentSearch] = useState({})
    const [pillList, setPillList] = useState([])
    const [pillListTextContains, setPillListTextContains] = useState([])

    const navigate = useNavigate()
    useEffect(() => {
        const localData = localStorage.getItem("pillSearchState")
        if (localData) {
            console.log(" PillGeneralSearchList 존재")
            setCurrentSearch(JSON.parse(localData))
        } else {
            console.log(" PillGeneralSearchList 존재x")
        }
    }, []);
    const goDetails = () => {
        navigate("/pill/detail")

    }

    useEffect(() => {
        const allPills = pillAllInformation();
        // const filteredPillList = allPills.filter((it) =>
        //     it.color_class1 === (currentSearch["color"] && currentSearch["color"][0]) &&
        //     it.dl_custom_shape === (currentSearch["custom_shape"] && currentSearch["custom_shape"][0]) &&
        //     it.drug_shape === (currentSearch["drug_shape"] && currentSearch["drug_shape"][0])
        // );

        const filteredPillList = allPills.filter((it) => {
            const colorFilter = currentSearch["color"] && currentSearch["color"][0];
            const customShapeFilter = currentSearch["custom_shape"] && currentSearch["custom_shape"][0];
            const drugShapeFilter = currentSearch["drug_shape"] && currentSearch["drug_shape"][0];

            const colorCondition = it.color_class1 === colorFilter;
            const customShapeCondition =
                (currentSearch["custom_shape"] && customShapeFilter === "전체") ?
                    true : (currentSearch["custom_shape"] && customShapeFilter === it.dl_custom_shape);

            const drugShapeCondition = it.drug_shape === drugShapeFilter;

            return colorCondition && customShapeCondition && drugShapeCondition;
        });


        setPillList(filteredPillList);

        const filteredPillListTextContains = allPills.filter((it) => it.dl_name === (currentSearch["pillSearch"] && currentSearch["pillSearch"][0]))
        ;

        setPillListTextContains(filteredPillListTextContains);

    }, [currentSearch]);
    const goProfile = () => {
        navigate("/PillProfile")
    }
    return (
        <div>
            <PillAllNav pillNavState={"pillFindList"} onClick={goProfile}></PillAllNav>
            <div>
                {/* 검색하지 않은 경우*/}
                {currentSearch["pillSearch"] && currentSearch["pillSearch"][0] === "not" && pillList.map((it) =>
                    <PillListForm key={it.id} {...it} selectList={it} onClick={goDetails}></PillListForm>
                )}
            </div>
            <div>
                {/* 검색한 경우 */}
                {currentSearch["pillSearch"] && currentSearch["pillSearch"][0] !== "not" && pillListTextContains.map((it) =>
                    <PillListForm key={it.id} {...it} selectList={it} onClick={goDetails}></PillListForm>
                )}
            </div>
        </div>
    )
}


export default React.memo(PillGeneralSearchList);