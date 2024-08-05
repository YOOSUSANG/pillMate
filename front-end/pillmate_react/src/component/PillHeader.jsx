import React from "react";
const PillHeader = ({homeText,onClick}) => {
    return (
        <header className='head_pillMate'>
            <div className='head_left' onClick={onClick}>PillMate</div>
        </header>
    )
}

export default PillHeader;