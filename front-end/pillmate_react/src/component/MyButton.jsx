const MyButton = ({text, type = "default", onClick, onParamClick, removeName, detailType}) => {
    //등록, 취소 버튼 모양이 아니면 이외에 버튼 모양
    const btnType = ['cancellation', 'beforeEdit', 'MyImageChange', 'afterEdit'].includes(type) ? type : 'default'
    return (
        <button className={["MyButton", `MyButton_${btnType}`, `MyButton_${detailType}`].join(" ")} onClick={() => {
            removeName ? onParamClick(removeName) : onClick()
        }}>{text}</button>)
}
export default MyButton;