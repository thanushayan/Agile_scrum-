import React, { useState, useEffect, useContext } from 'react';
import '../css/MembersPage.css';
import axios from 'axios';
import config from '../config.json';
import { useParams } from 'react-router-dom';
import { getItemFromLocalStorage } from './AuthStorageUtil';
import { DarkModeContext } from './DarkModeProvider';
import { FaSun, FaMoon, FaPlus } from 'react-icons/fa';
import MemberCard from './MemberCard.js';
import Sidebar from './SideBar';
import Chat from './chat';
import { getUserAccess, PermissionTypes } from './UserRoleUtill';
import { ToastContainer, toast } from 'react-toastify';
import { useHistory } from 'react-router-dom';
import UserProfilePopup from './UserProfilePopUp.js';

const MembersPage = ({ view, setUpView, user }) => {
    const [isOpen, setIsOpen] = useState(false);
    const [members, setMembers] = useState([]);
    const { projectId } = useParams();
    const { mode, toggleMode } = useContext(DarkModeContext);
    const [error, setError] = useState('');
    const [isPopupVisible, setPopupVisible] = useState(false);
    const [member, setMember] = useState(null);

    const history = useHistory();

    const handleNewMeetingClick = () => {
        history.push(`/teamVC/${projectId}`)
    };

    const handleProfileClick = (member) => {
        setMember(member);
        setPopupVisible(true);
    };

    // Update fetchMembers function to fetch members and update progress based on fetchProgressData
    const fetchMembers = async () => {
        try {
            const response = await axios.get(`${ServerURL}api/project-and-user/get-users/${projectId}`, {
                headers: {
                    Authorization: TOKEN,
                },
            });
            if (Array.isArray(response.data)) {
                const membersList = response.data;
                const membersWithProgress = [];

                // Fetch progress data for each member and update membersWithProgress array
                await Promise.all(
                    membersList.map(async (member) => {
                        const progress = await fetchProgressData(member.userId);
                        membersWithProgress.push({
                            ...member,
                            progress: progress || 0, // Use progress data for each member or default to 0
                        });
                    })
                );

                setMembers(membersWithProgress);
            } else {
                console.log('Invalid response format: Expected an array of members');
            }
        } catch (error) {
            // Error handling
        }
    };

    // Modify useEffect to fetch progress data
    useEffect(() => {
        fetchProgressData(); // Fetch progress data
        fetchMembers(); // Fetch members with updated progress
    }, []);


    const OpenAddMemberPopup = () => {
        setIsOpen(true);
    };

    const closeAddMemberPopup = () => {
        setIsOpen(false);
        setError('');
    };

    const ServerURL = config.APIURL;
    const TOKEN = getItemFromLocalStorage('token');
    const username = getItemFromLocalStorage('username');

    useEffect(() => {
        setInterval(fetchMembers, 500);
        return () => {
            fetchMembers();
        };
    }, []);

    const showNotification = (notification) => {
        toast.success(notification);
    };



    const sendRequest = (e) => {
        e.preventDefault();

        const formData = new FormData(e.target);
        const userID = formData.get('id');

        const requestBody = {
            userName: userID,
            projectId: projectId,
            requesterName: username,
        };

        axios
            .post(`${ServerURL}request-pending/create`, requestBody, {
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: TOKEN,
                },
            })
            .then((response) => {
                if (response.status === 201) {
                    showNotification('Request Sent');
                    fetchMembers();
                    closeAddMemberPopup();
                } else {
                    console.log('Error adding member');
                    setError(response.data.message || 'Unknown error occurred'); // Set the error message or a default message
                    setTimeout(() => {
                        setError('');
                    }, 30000);
                }
            })
            .catch((error) => {
                console.log('API call error:', error);
                console.log('Response data:', error.response?.data);
                console.log('Status code:', error.response?.status);
                setError(error.response?.data?.message || 'Unknown error occurred');
                setTimeout(() => {
                    setError('');
                }, 30000);
            });
    }


    const fetchProgressData = async (userName) => {
        try {
            const response = await fetch(`http://localhost:8080/issues/personal/${projectId}/${userName}`, {
                headers: {
                    Authorization: `Bearer ${TOKEN}`,
                    'Content-Type': 'application/json',
                },
            });

            if (response.ok) {
                const data = await response.json();
                return data;
            } else {
                // Handle error response
            }
        } catch (error) {
            // Handle fetch error
        }
    };

    fetchProgressData();


    const handleInputChange = () => {
        setError('');
    };

    // Sort members based on progress
    const sortedMembers = [...members].sort((a, b) => b.progress - a.progress);

    const [havePermit, sethavePermit] = useState(false);
    useEffect(() => {
        const checkHavePermit = async () => {
            const bool = user && await getUserAccess(projectId, user.userRole, PermissionTypes.MANAGE_USERS);
            sethavePermit(bool);
        };

        checkHavePermit();
    }, [projectId, user]);

    return (
        <div className={mode === 'dark' ? 'dark' : ''}>
            <div className="WorkArea">
                <Sidebar
                    setUpView={setUpView}
                    view={view}
                />

                <div className="members-container">
                    <div className="members-header">
                        <h2>Team Members</h2>
                        {user && havePermit && <button className="add-member-button" onClick={OpenAddMemberPopup}>
                            <FaPlus /> Add Member
                        </button>}
                        {user && havePermit && <button className="add-member-button" onClick={handleNewMeetingClick}>
                            <FaPlus /> New Meeting
                        </button>}
                    </div>

                    <div className="members-list">
                        {sortedMembers.map((member) => (
                            <MemberCard key={member.userId} member={member} handleProfileClick={handleProfileClick} />
                        ))}

                        {isPopupVisible &&
                            <UserProfilePopup setPopupVisible={setPopupVisible} isPopupVisible={isPopupVisible} user={member} />
                        }
                    </div>
                </div>
                <Chat
                    username={username}
                    projectId={projectId}
                />
                {isOpen && (
                    <div className="add-member-popup">
                        <div className="add-member-popup-content">
                            <h3>Add Member</h3>
                            <form onSubmit={sendRequest} className="add-member-form">
                                <input type="text" name="id" placeholder="Enter ID" onChange={handleInputChange} />
                                <div className="buttons-pop">
                                    <button type="submit" className="Add-button">Add</button>
                                    <button className="cancel-button" onClick={closeAddMemberPopup}>Cancel</button>
                                </div>
                                {error && <p className="error-message">{error}</p>}
                            </form>
                        </div>
                    </div>
                )}
            </div>
            <div className='chat-area'></div>
            <div>

            </div>
        </div>

    );
};

export default MembersPage;
