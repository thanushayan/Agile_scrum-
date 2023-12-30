import React from 'react';


const ConfirmationPopupContent = ({ handleConfirm, closePopup, text }) => {
    return (
        <div className="popup-content">
            <h2 className="header">Confirmation</h2>
            <p className="Que">{text}</p>
            <button className="yesBtn" onClick={handleConfirm}>
                Yes
            </button>
            <button className="noBtn" onClick={closePopup}>
                No
            </button>
        </div>
    );
};

export default ConfirmationPopupContent;
