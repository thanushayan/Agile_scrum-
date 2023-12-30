import React from 'react';
import axios from 'axios';
import '../css/DeleteProject.css';
import '../css/Mode.css';
import config from '../config.json';
import { getItemFromLocalStorage } from './AuthStorageUtil';
import ConfirmationPopupContent from './ConfimationPopup';

const deleteAccount = ({ closePopup, LogOut }) => {
    const handleConfirm = () => {
        // Perform the action to confirm
        deleteUserAccount();
    };
    const token = getItemFromLocalStorage('token');
    const ServerURL = config.APIURL;
    const username = getItemFromLocalStorage('username')

    const deleteUserAccount = () => {

        axios.delete(`${ServerURL}api/userprofiles/${username}`,
            {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            })

            .then(response => {
                // Handle successful deletion
                console.log('Account deleted successfully');
                LogOut();
            })
            .catch(error => {
                // Handle error
                console.error('Error deleting account:', error);
            });
    };

    return (
        <div>
            <ConfirmationPopupContent
                handleConfirm={handleConfirm}
                closePopup={closePopup}
                text="Do you want to delete your account permenantly? Please remember it cannot be recover in future!"
            />
        </div>
    );
};

export default deleteAccount;
