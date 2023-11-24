import React, {useContext} from "react";
import {pillDetailsContext, pillHandlerContext} from "../App";
import MyButton from "./MyButton";

const PillListForm = ({
                          dl_name,
                          dl_class_no,
                          img_key,
                          dl_material,
                          dl_material_en,
                          drug_shape,
                          dl_custom_shape,
                          selectList,
                          onClick,
                          buttonType,
                          currentState
                      }) => {

    const defaultImage = "URL_OF_DEFAULT_IMAGE";
    const {pillClickDetails} = useContext(pillDetailsContext)
    const {TakingOnRemove} = useContext(pillHandlerContext)
    return (<div>
        <div className="pillListItem">
            <div onClick={() => {
                onClick();
                pillClickDetails(selectList)
            }}>
                <img className="pill_img_wrapper" src={img_key} onError={(e) => e.target.src = defaultImage}/>
                <div className="pill_name">{dl_name}</div>
            </div>
            <div className="info_wrapper" onClick={() => {
                onClick()
                pillClickDetails(selectList)
            }}>
                <div className="pill_class_no"><span style={{fontWeight: "bold"}}>분류:</span> {dl_class_no}</div>
                <div className="pill_material"><span style={{fontWeight: "bold"}}>성분:</span> {dl_material}</div>
                <div className="pill_dl_custom_shape"><span style={{fontWeight: "bold"}}>제형:</span> {dl_custom_shape}</div>
                <div className="pill_dl_drug_shape"><span style={{fontWeight: "bold"}}>모양:</span> {drug_shape}</div>
            </div>
            {/*복용 이력시에만 보이는 버튼*/}
            {currentState === "pillRecord" && <MyButton text={"삭제"} removeName={dl_name} type={buttonType}
                                                        onParamClick={TakingOnRemove} ></MyButton>}
        </div>

    </div>)
}
export default React.memo(PillListForm)
