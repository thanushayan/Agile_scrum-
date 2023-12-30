import React from 'react';
import axios from 'axios';
import '../css/Mode.css';
import config from '../config.json';
import { getItemFromLocalStorage } from './AuthStorageUtil';
import ConfirmationPopupContent from './ConfimationPopup';

const DeleteSprint = ({ sprintId, closePopup }) => {
    const handleConfirm = () => {
        // Perform the action to confirm
        handleDeleteProject(sprintId);
        console.log(sprintId)
    };
    const token = getItemFromLocalStorage('token');
    const username = getItemFromLocalStorage('username');
    const ServerURL = config.APIURL;

    const handleDeleteProject = (sprintId) => {
        axios.delete(`${ServerURL}sprints/delete/${username}/${sprintId}`, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        })
            .then((response) => {
                if (response.status === 204) {
                    // Delete request successful, handle success case
                    console.log('Delete request successful');
                    window.location.reload();
                    closePopup();
                } else {
                    // Delete request failed, handle error case
                    console.log('Delete request failed');
                }
            })
            .catch((error) => {
                // Handle any errors during the delete request
                console.log('Delete request error:', error);
            });
    };

    return (
        <div className="confirmation-popup">
            <ConfirmationPopupContent
                handleConfirm={handleConfirm}
                closePopup={closePopup}
                text="Do You want to delete the sprint?"
            />
        </div>
    );
};

export default DeleteSprint;
