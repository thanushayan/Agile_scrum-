import React from 'react';
import axios from 'axios';
import '../css/DeleteProject.css';
import '../css/Mode.css';
import config from '../config.json';
import { getItemFromLocalStorage } from './AuthStorageUtil';
import ConfirmationPopupContent from './ConfimationPopup';
import { ToastContainer, toast } from 'react-toastify';

const ActiateSprint = ({ projectId, closePopup, sprint, setShowSprintExpand }) => {
    const handleConfirm = () => {
        // Perform the action to confirm
        handleActivateSprint(sprint);
    };
    const TOKEN = getItemFromLocalStorage('token');
    const ServerURL = config.APIURL;
    const userId = getItemFromLocalStorage('username');

    const handleActivateSprint = (sprint) => {
        sprint.createdDate = null;
        sprint.lastUpdatedDate = null;
        fetch(`${ServerURL}projects/activeSprint/${projectId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': TOKEN,
            },
            body: JSON.stringify(sprint)
        })
            .then(response => {
                console.log(JSON.stringify(sprint));
                if (response.ok) {
                    window.location.reload();
                    closePopup();
                    toast.success('Sprint activated successfully');
                    // You can perform additional actions upon successful update if needed
                } else {
                    throw new Error('Failed to update sprint');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                // Handle the error or perform necessary actions
            });
    };

    return (
        <div className="confirmation-popup">
            <ConfirmationPopupContent
                handleConfirm={handleConfirm}
                closePopup={closePopup}
                text="Do You want to activate the sprint?"
            />
        </div>
    );
};

export default ActiateSprint;

