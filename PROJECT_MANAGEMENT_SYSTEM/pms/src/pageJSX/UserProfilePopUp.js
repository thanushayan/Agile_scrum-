import React, { useState } from 'react';
import '../css/UserProfilePopup.css';

const UserProfilePopup = ({ setPopupVisible, isPopupVisible, user }) => {
    const [isEditing, setEditing] = useState(false);
    const [userRole, setUserRole] = useState('');
    const [editedUserRole, setEditedUserRole] = useState('');
    const [userRolesArray, setUserRolesArray] = useState(['Project Manager', 'Developer', 'Admin', 'Designer']);
    const [progressPercentage, setProgressPercentage] = useState(100); // Replace with your actual data

    const handleEditClick = () => {
        setEditing(true);
        setEditedUserRole(userRole);
    };

    const handleOkClick = () => {
        setEditing(false);
        setUserRole(editedUserRole);
        setEditedUserRole('');
    };

    const handleCancelClick = () => {
        setEditing(false);
        setEditedUserRole('');
    };

    const handleUserRoleChange = (event) => {
        setEditedUserRole(event.target.value);
    };

    const handlePopupClose = () => {
        setPopupVisible(false);
    };

    return (
        <div className="user-profile">


            {isPopupVisible && (
                <div className={`user-profile-popup ${isEditing ? 'editing' : ''}`}>
                    <div className="popup-content">
                        <div className="close-button" onClick={handlePopupClose}>
                            &times;
                        </div>
                        <div className="profile-pic">...</div>
                        <div className="name">Name : {user.userId}</div>
                        <div className='progress'>Progress : </div>
                        <div className="progress-bar">

                            <div
                                className="progress-inner"
                                style={{ width: `${progressPercentage}%` }}
                            >{progressPercentage}%</div>
                        </div>


                        <form className="edit-form">
                            <label htmlFor="userRole">User Role:</label>
                            <select
                                id="userRole"
                                value={user.userRole}
                                readOnly={!isEditing}
                                onChange={handleUserRoleChange}
                                className="dark-cyan-dropdown"
                            >
                                {userRolesArray.map((role) => (
                                    <option key={role} value={role}>
                                        {role}
                                    </option>
                                ))}
                            </select>

                            <div className="button-group">
                                <button
                                    className="dark-cyan-button"
                                    type="button"
                                    onClick={handleOkClick}
                                >
                                    OK
                                </button>
                                <button
                                    className="dark-cyan-button"
                                    type="button"
                                    onClick={handleCancelClick}
                                >
                                    Cancel
                                </button>
                            </div>
                        </form>
                        <div className="user-role">
                            <button className="dark-cyan-button" onClick={handleEditClick}>
                                Edit
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default UserProfilePopup;