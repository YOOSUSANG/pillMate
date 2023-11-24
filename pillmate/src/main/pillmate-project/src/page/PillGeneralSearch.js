import React, {useState, useReducer, useCallback, useContext, useEffect} from "react";
import PillAllNav from "../component/PillAllNav";
import PillSelectFrame from "../component/PillSelectFrame";
import MyButton from "../component/MyButton";
import {pillColorList} from "../utils/pillColor";
import {PillTypeList} from "../utils/PillType";
import {PillShapeList} from "../utils/PillShape";
import {useNavigate} from "react-router-dom";
import {pillHandlerContext} from "../App";
import {pillAllInformation} from "../utils/PillAllInformation"
import styled from 'styled-components'


/*
    약 일반 검색 페이지
*/
// 이부분은 리렌더될 떄 영향을 받지 않는다.
const pillAllList = pillAllInformation();
const PillGeneralSearch = () => {
    const [inputValue, setInputValue] = useState('');
    const [isHaveInputValue, setIsHaveInputValue] = useState(false)
    const [pillAllInformationList, setPillAllInformationList] = useState(pillAllList)
    const [dropDownItemIndex, setDropDownItemIndex] = useState(-1)

    const {recShapeState} = useContext(pillHandlerContext)
    const navigate = useNavigate()
    const handleSubmit = useCallback(() => {

        navigate("/generalSearch/list")

    }, [])
    const showDropDownList = () => {
        if (inputValue === "") {
            recShapeState("not", "입력");
            setIsHaveInputValue(false)
            setPillAllInformationList([])
        } else {
            const choosenNameList = pillAllInformation().filter((pill) =>
                pill.dl_name.includes(inputValue))
            setPillAllInformationList(choosenNameList)
        }

    };

    const changeInputValue = event => {
        setInputValue(event.target.value)
        console.log(event.target.value)
        recShapeState(event.target.value, "입력");
        setIsHaveInputValue(true)
    }
    const clickDropDownItem = clickedItem => {
        setInputValue(clickedItem)
        recShapeState(clickedItem, "입력");
        setIsHaveInputValue(false)
    }
    const handleDropDownKey = event => {
        if (isHaveInputValue) {
            if (event.key === 'ArrowDown' && pillAllInformationList.length - 1 > dropDownItemIndex) {
                setDropDownItemIndex(dropDownItemIndex + 1)
            }
            if (event.key === 'ArrowUp' && dropDownItemIndex >= 0)
                setDropDownItemIndex(dropDownItemIndex - 1)
            if (event.key === 'Enter' && dropDownItemIndex >= 0) {
                clickDropDownItem(pillAllInformationList[dropDownItemIndex]["dl_name"])
                setDropDownItemIndex(-1)
            }
        }
    }
    useEffect(showDropDownList, [inputValue])



    const goProfile = () => {
        navigate("/PillProfile")
    }

    return (

        <div>
            <PillAllNav pillNavState={"pillFind"} pillSubNavState={"generalSearch"} onClick={goProfile}></PillAllNav>
            <WholeBox>
                <InputBox isHaveInputValue={isHaveInputValue}>
                    <Input
                        type='text'
                        value={inputValue}
                        onChange={changeInputValue}
                        onKeyUp={handleDropDownKey}
                        placeholder={"약물의 제품명을 검색해주세요."}
                    />
                    <DeleteButton onClick={() => setInputValue('')}>&times;</DeleteButton>
                </InputBox>
                {isHaveInputValue && (
                    <DropDownBox>
                        {pillAllInformationList.length === 0 && (
                            <DropDownItem>해당하는 단어가 없습니다</DropDownItem>
                        )}
                        {pillAllInformationList.map((dropDownItem, dropDownIndex) => {
                            return (
                                <DropDownItem
                                    key={dropDownIndex}
                                    onClick={() => clickDropDownItem(dropDownItem["dl_name"])}
                                    onMouseOver={() => setDropDownItemIndex(dropDownIndex)}
                                    className={
                                        dropDownItemIndex === dropDownIndex ? 'selected' : ''
                                    }
                                >
                                    {dropDownItem["dl_name"]}
                                </DropDownItem>
                            )
                        })}
                    </DropDownBox>
                )}
            </WholeBox>
            <PillSelectFrame text={"*제형"} list={PillTypeList}></PillSelectFrame>
            <PillSelectFrame text={"*모양"} list={PillShapeList}></PillSelectFrame>
            <PillSelectFrame text={"*색상"} list={pillColorList}></PillSelectFrame>
            <div style={{textAlign: "center"}}>
                <MyButton text={"검색하기"} onClick={handleSubmit}></MyButton>
            </div>
        </div>
    )
}
const activeBorderRadius = '16px 16px 0 0'
const inactiveBorderRadius = '16px 16px 16px 16px'

const WholeBox = styled.div`
  padding-left: 30px;
  padding-right: 30px;
  padding-top: 30px;
`

const InputBox = styled.div`
  display: flex;
  flex-direction: row;
  padding: 16px;
  border: 2px solid rgba(0, 0, 0, 0.3);
  border-radius: ${props =>
          props.isHaveInputValue ? activeBorderRadius : inactiveBorderRadius};
  z-index: 3;

  &:focus-within {
    box-shadow: 0 10px 10px rgb(0, 0, 0, 0.3);
  }
`

const Input = styled.input`
  flex: 1 0 0;
  margin: 0;
  padding: 0;
  background-color: transparent;
  border: none;
  outline: none;
  font-size: 16px;
`

const DeleteButton = styled.div`
  cursor: pointer;
`

const DropDownBox = styled.ul`
  display: block;
  margin: 0 auto;
  padding: 8px 0;
  background-color: white;
  border: 1px solid rgba(0, 0, 0, 0.3);
  border-top: none;
  border-radius: 0 0 16px 16px;
  box-shadow: 0 10px 10px rgb(0, 0, 0, 0.3);
  list-style-type: none;
  z-index: 3;
  cursor: pointer;
`

const DropDownItem = styled.li`
  padding: 0 16px;

  &.selected {
    background-color: lightgray;
  }
`

export default React.memo(PillGeneralSearch);