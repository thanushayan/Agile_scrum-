import { React, useState } from 'react';
import '../css/Slider.css';
import UserProfile from './UserProfile';
import ConfirmationPopup from './DeleteAccount';

const ProfileSlider = ({ SliderisOpen, LogOut, setSliderIsOpen }) => {
    const [isOpenDelConfirm, setIsOpenDelConfirm] = useState(false);
    const closeDelConfirmPopup = () => {
        setIsOpenDelConfirm(false);
    };

    const openDelConfirmPopup = () => {
        setIsOpenDelConfirm(true);
    };

    return (
        <div >
            <div className="slider-container">
                {SliderisOpen && (
                    <div className="slider-content">
                        <div className="profileContain">
                            <button className="logOutBtn" onClick={LogOut}>
                                Logout
                            </button>
                            <UserProfile openDelConfirmPopup={openDelConfirmPopup} />
                        </div>
                    </div>
                )}
            </div>

            {isOpenDelConfirm && (
                <div className='confirmation-popup'>
                    <ConfirmationPopup closePopup={closeDelConfirmPopup} LogOut={LogOut} />
                </div>
            )}

        </div>
    );


};

export default ProfileSlider;
