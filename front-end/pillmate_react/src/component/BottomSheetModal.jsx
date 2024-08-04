// BottomSheetModal.js
import React, { useEffect, useState } from 'react';
const BottomSheetModal = ({ show, onClose, count, setCount}) => {
    const [detailState, setDetailState] = useState({})
    useEffect(() => {
        const localData = localStorage.getItem("pillDetailState")
        if (localData) {
            setDetailState(JSON.parse(localData))
        }


    }, []);
    const quantityPlus = ()=>{
            setCount(count+1)
    }
    const quantityMinus = ()=>{
        if(count!=1){
            setCount(count-1)
        }
    }
  return (
    <div className={`bottom-sheet ${show ? 'show' : ''}`}>
      <div className="bottom-sheet-content">
        <div className="option-select">
        </div>
        <div className="actions">
            <div style={{ display: 'flex', alignItems: 'center' }}>
            <label>수량</label>
            <button
                style={{
                background: 'none',
                border: 'none',
                color: 'black',
                cursor: 'pointer',
                margin: '0 10px',
                fontSize: '16px',
                }}
                onClick={quantityMinus}
            >
                -
            </button>
            <span>{count}</span>
            <button
                style={{
                background: 'none',
                border: 'none',
                color: 'black',
                cursor: 'pointer',
                margin: '0 10px',
                fontSize: '16px',
                }}
                onClick={quantityPlus}
            >
                +
            </button>
            <span style={{ marginLeft: '10px', color: '#32CD32' }}>
                {count * detailState.price}</span><span>&nbsp;원</span>
            </div>
            <button onClick={onClose} className="btn btn-buy">장바구니 담기</button>
        </div>
      </div>
    </div>
  );
};

export default BottomSheetModal;
