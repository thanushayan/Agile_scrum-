import React, { useState, useEffect } from 'react';
import '../css/UserProfile.css';
import config from '../config.json'
import { getItemFromLocalStorage, clearTokenAndUserName } from './AuthStorageUtil';
import defaultAvatar from '../media/avatar.png';
import badge from '../media/badge.png';
import medal from '../media/medal.png';
import axios from 'axios';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faEdit } from '@fortawesome/free-solid-svg-icons';
import { faSpinner } from '@fortawesome/free-solid-svg-icons';


const UserProfile = ({ openDelConfirmPopup }) => {
    const [isEditable, setIsEditable] = useState(false);
    const [userData, setUserData] = useState(null);

    const token = getItemFromLocalStorage('token');
    const ServerURL = config.APIURL;
    const username = getItemFromLocalStorage('username')
    const [avatar, setAvatar] = useState(defaultAvatar);

    //view
    const handleProfileView = () => {
        axios.get(`${ServerURL}api/userprofiles/${username}`, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        })
            .then((response) => {

                if (response.status === 200) {
                    const { firstName, lastName, email, phone, location, skills, experience, interests, ...otherData } = response.data;
                    const updatedUserData = {

                        firstName,
                        lastName,
                        email,
                        phone,
                        location,
                        skills: skills || [],
                        experience: experience || [],
                        interests: interests || [],
                        ...otherData
                    };
                    setUserData(updatedUserData);
                    console.log("skills " + userData.skills + " " + Array.isArray(userData.skills));
                }
            })
            .catch((error) => {
                if (error.response && error.response.status === 500) {
                    // Perform the necessary action to handle the expired token
                    clearTokenAndUserName();
                } else {
                    // Handle other errors during the delete request
                    console.log('request error:', error);
                }
            });
    };


    const fetchProfile = () => {
        handleProfileView();
    };

    useEffect(() => {
        const interval = setInterval(() => {
            fetchProfile();
        }, 500);

        return () => clearInterval(interval);
    }, [fetchProfile]);

    //update
    const handleUpdate = () => {
        const updatedFirstName = document.getElementById('firstName').value;
        const updatedLastName = document.getElementById('lastName').value;
        const updatedSkills = document.getElementById('skills').value;
        const updatedExperience = document.getElementById('experience').value;
        const updatedInterest = document.getElementById('interests').value;
        const updatedEmail = document.getElementById('email').value;
        const updatedPhone = document.getElementById('phone').value;
        const updatedLocation = document.getElementById('location').value;


        // Make the API call to update the user profile data
        axios.put(`${ServerURL}api/userprofiles/${username}`, {
            email: updatedEmail,
            phone: updatedPhone,
            firstName: updatedFirstName,
            lastName: updatedLastName,
            skills: updatedSkills,
            experience: updatedExperience,
            interest: updatedInterest,
            location: updatedLocation,
        }, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        })
            .then((response) => {
                // Handle the successful response
                console.log('Profile updated successfully');
                // Update the state with the updated user data
                setUserData(response.data);
                // Disable the edit mode after updating
                setIsEditable(false);
            })
            .catch((error) => {
                // Handle the error response
                console.error('Error updating profile:', error);
            });
    };


    const handleEdit = () => {
        setIsEditable(true);
    };

    const handleUpdateBTN = () => {
        handleUpdate();
        setIsEditable(false);
    };

    function convertToArray(newString) {
        if (typeof newString !== 'string') {
            return [];
        }
        return newString.split(',');
    }

    const handleFileUpload = (event) => {
        const selectedFile = event.target.files[0];
        if (selectedFile) {
            const reader = new FileReader();
            reader.onload = () => {
                setAvatar(reader.result);
            };
            reader.readAsDataURL(selectedFile);
        }
    };

    return (
        <div>
            <div className="user-profile">
                {userData ? (
                    <React.Fragment>
                        <div className="profile-header">
                            <div className="avatarDiv">
                                <img className="avatarProfile" src={avatar} alt="Avatar" />
                                <label htmlFor="profileDP" className="profileDP">
                                    <input
                                        id="profileDP"
                                        type="file"
                                        accept="image/*"
                                        onChange={handleFileUpload}
                                    />
                                    <FontAwesomeIcon icon={faEdit} className="editIcon" />
                                </label>
                            </div>


                            <div className="header-details">
                                <h2 className="username">{userData.username}</h2>

                                {isEditable ? (
                                    <div className="input-container">
                                        <div className="detail">
                                            <label htmlFor="location">Location:</label>
                                            <input
                                                type="text"
                                                id="location"
                                                name="location"
                                                defaultValue={userData.location}
                                            />
                                        </div>
                                        <div className="detail">
                                            <label htmlFor="email">Email:</label>
                                            <input
                                                type="email"
                                                id="email"
                                                name="email"
                                                defaultValue={userData.email}
                                            />
                                        </div>
                                        <div className="detail">
                                            <label htmlFor="phone">Phone:</label>
                                            <input
                                                type="tel"
                                                id="phone"
                                                name="phone"
                                                defaultValue={userData.phone}
                                            />
                                        </div>
                                    </div>
                                ) : (
                                    <div className='baseContent'>
                                        <p className="location">{userData.location}</p>
                                        <p>Email: {userData.email}</p>
                                        <p>Phone: {userData.phone}</p>
                                    </div>
                                )}
                                <div className='delete-account' onClick={openDelConfirmPopup}>
                                    Delete Account
                                </div>
                            </div>
                        </div>
                        <div className="profile-details">
                            <div className={isEditable ? 'edit-name-fields' : 'name-fields'}>
                                <div className={isEditable ? 'edit-name-field' : 'name-field'}>
                                    <label htmlFor="firstName" className='Namelable'>First Name:</label>
                                    {isEditable ? (
                                        <input
                                            type="text"
                                            id="firstName"
                                            className="name"
                                            name="firstName"
                                            defaultValue={userData.firstName}
                                        />
                                    ) : (
                                        <p className="nameF">{userData.firstName}</p>
                                    )}
                                </div>
                                <div className={isEditable ? 'edit-name-field' : 'name-field'}>
                                    <label htmlFor="lastName" className='Namelable'>Last Name:</label>
                                    {isEditable ? (
                                        <input
                                            type="text"
                                            id="lastName"
                                            className="name"
                                            name="lastName"
                                            defaultValue={userData.lastName}
                                        />
                                    ) : (
                                        <p className="nameF">{userData.lastName}</p>
                                    )}
                                </div>
                            </div>

                            <div className="detail">
                                <h3>Skills</h3>
                                {isEditable ? (
                                    <div className="input-container">
                                        <textarea
                                            defaultValue={userData.skills}
                                            className="skills-textarea"
                                            name="skills"
                                            id="skills"
                                        ></textarea>
                                    </div>
                                ) : (
                                    <div className='contentArea'>
                                        <ul className='SetList'>
                                            {Array.isArray(convertToArray(userData.skills)) && convertToArray(userData.skills).map((skill, index) => (
                                                <li key={index}>{skill}</li>
                                            ))}
                                        </ul>
                                    </div>
                                )}
                            </div>


                            <div className="detail">
                                <h3>Experience</h3>
                                {isEditable ? (
                                    <div className="input-container">
                                        <textarea
                                            defaultValue={userData.experience}
                                            name="experience"
                                            id="experience"
                                            rows="4"
                                        ></textarea>
                                    </div>
                                ) : (
                                    <div className='contentArea'>
                                        <ul className='SetList'>
                                            {Array.isArray(convertToArray(userData.experience)) && convertToArray(userData.experience).map((exp, index) => (
                                                <li key={index}>{exp}</li>
                                            ))}
                                        </ul>
                                    </div>
                                )}
                            </div>




                            <div className="detail">
                                <h3>Badges</h3>
                                <div className="badge-container">
                                    <img className="badge" src={badge} alt="Badge 1" />
                                    <img className="badge" src={medal} alt="Badge 2" />
                                </div>
                            </div>

                            <div className="detail">
                                <h3>Interests</h3>
                                {isEditable ? (
                                    <div className="input-container">
                                        <textarea
                                            defaultValue={userData.interest}
                                            name="interests"
                                            id="interests"
                                            rows="4"
                                        ></textarea>
                                    </div>
                                ) : (
                                    <div className='contentArea'>
                                        <ul className='SetList'>
                                            {Array.isArray(convertToArray(userData.interest)) &&
                                                convertToArray(userData.interest).map((int, index) => (
                                                    <li key={index}>{int}</li>
                                                ))}
                                        </ul>
                                    </div>
                                )}
                            </div>

                            <div className="buttons-container">
                                {isEditable ? (
                                    <button className="update-button" onClick={handleUpdateBTN}>
                                        Update
                                    </button>
                                ) : (
                                    <div className='edit-button' onClick={handleEdit}>
                                        <FontAwesomeIcon icon={faEdit} />
                                    </div>
                                )}
                            </div>
                        </div>

                    </React.Fragment>
                ) : (
                    <p><FontAwesomeIcon icon={faSpinner} className='fa-duotone fa-spin-pulse' style={{ '--fa-primary-color': '#1da5d3', '--fa-secondary-color': '#b1d5ec' }} /> Loading user data...</p>
                )}
            </div>
        </div >
    );
};

export default UserProfile;
