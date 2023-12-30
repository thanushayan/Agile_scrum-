import React, { useState, useEffect, useRef } from 'react';
import { FaCheck, FaChevronDown, FaChevronRight, FaEdit, FaTrashAlt, FaPlay, FaPause } from 'react-icons/fa';
import config from '../config.json';
import { getItemFromLocalStorage } from './AuthStorageUtil';
import axios from 'axios';
import DeleteSprint from './DeleteSprint';
import Sidebar from './SideBar';
import IssuePopup from './IssuePopUp';
import ActiateSprint from './ActivateSprint';
import { getUserAccess, PermissionTypes } from './UserRoleUtill';


const BacklogArea = ({ view, setUpView, projectId, user, activeSprint, setPoker, pokerIssue }) => {
    const [backlogs, setBacklogsIssues] = useState([]);
    const [sprints, setSprintIssues] = useState([]);
    const [newBacklog, setNewBacklog] = useState('');
    const [showAddissueInput, setShowAddissueInput] = useState(false);
    const [showAddSubissueInput, setShowAddSubissueInput] = useState(null);
    const [showExpandSprint, setShowSprintExpand] = useState(null);
    const [issueNameErrorSprint, setIssueNameErrorSprint] = useState('');
    const [issueNameErrorBacklog, setIssueNameErrorBacklog] = useState('');
    const subissueInputRef = useRef(null);
    const sprintTableRef = useRef(null);
    const [Sprintexpand, setSprintExpand] = useState(false);
    const [selectedSprintId, setSelectedSprintId] = useState(null);
    const [isOpenDelConfirm, setIsOpenDelConfirm] = useState(false);
    const [isOpenActivateConfirm, setIsOpenActivateConfirm] = useState(false);
    const [sprintReadyForActive, setSprintReadyForActive] = useState(null);




    useEffect(() => {
        fetchSprints();
        fetchBacklogs();
    }, []);

    const ServerURL = config.APIURL;
    const TOKEN = getItemFromLocalStorage('token');
    const username = getItemFromLocalStorage('username');

    const fetchSprints = async () => {
        try {
            const response = await axios.get(`${ServerURL}sprints/get/${username}/${projectId}`, {
                headers: {
                    Authorization: TOKEN,
                },
            });

            // Check if the response data is an array
            if (Array.isArray(response.data)) {
                setSprintIssues(response.data);
            } else {
                console.error('Invalid response format: Expected an array of sprints');
                setSprintIssues([]);
            }
        } catch (error) {
            console.error('Error fetching sprints:', error.message);
            setSprintIssues([]);
        }
    };

    const fetchBacklogs = async () => {
        try {
            const response = await axios.get(`${ServerURL}backlog/get/${username}/${projectId}`, {
                headers: {
                    Authorization: TOKEN,
                },
            });

            // Check if the response data contains the 'backlogs' property and it is an object
            if (response.data && typeof response.data === 'object' && Array.isArray(response.data.issues)) {
                setBacklogsIssues(response.data.issues);
            } else {
                console.error('Invalid response format: Expected an object with "issues" property');
                setBacklogsIssues([]);
            }
        } catch (error) {
            console.error('Error fetching backlogs:', error.message);
            setBacklogsIssues([]);
        }
    };

    const handleBacklogChange = (e) => {
        setNewBacklog(e.target.value);
    };

    const handleStartSprint = (sprintId) => {
        setSelectedSprintId(sprintId);
    };

    const handleAddSprint = async () => {
        const sprintNumber = sprints.length + 1;
        try {
            const sprint = {
                name: 'Sprint :' + `${sprintNumber}`,
                description: null,
                creatorId: `${username}`,
                startDate: null,
                endDate: null,
            };

            const response = await axios.post(
                `${ServerURL}sprints/create-sprint/${username}/add/${projectId}`,
                sprint,
                {
                    headers: {
                        'Content-Type': 'application/json',
                        Authorization: TOKEN,
                    },
                }
            );
            fetchSprints(); // Fetch the updated list of sprints
        } catch (error) {
            console.error('Error adding sprint:', error.message);
        }
    };


    const handleAddissueToSprint = async (issueName, sprintId) => {
        if (issueName === "") {
            setIssueNameErrorSprint('Issue name cannot be empty');
            setTimeout(() => {
                setIssueNameErrorSprint(''); // Clear the error message after a certain time (e.g., 3000 milliseconds)
            }, 3000);
            return;
        }
        setIssueNameErrorSprint('');
        setShowAddissueInput(false); // Reset the state after adding the issue
        const newissue = {
            title: issueName,
            projectId: projectId,
            description: null,
            type: "task",
            createdBy: username,
            assignedTo: null,
        };

        AddIssueToSprint(newissue, sprintId)
    };

    const AddIssueToSprint = async (issue, sprintId) => {
        try {
            const response = await axios.post(
                `${ServerURL}issues/create-issue/sprint/${sprintId}`,
                issue,
                {
                    headers: {
                        'Content-Type': 'application/json',
                        Authorization: TOKEN,
                    },
                }
            );

            fetchSprints();
            setShowSprintExpand(sprintId);

            setSprintExpand(true);
        } catch (error) {
            console.error('Error adding issue:', error.message);
        }
    }

    const handleAddIssueToBacklog = async (issueName) => {
        if (issueName === "") {
            setIssueNameErrorBacklog('Issue name cannot be empty');
            setTimeout(() => {
                setIssueNameErrorBacklog(''); // Clear the error message after a certain time (e.g., 3000 milliseconds)
            }, 3000);
            return;
        }
        setIssueNameErrorBacklog('');
        const newissue = {
            title: issueName,
            projectId: projectId,
            description: null,
            type: "task",
            createdBy: username,
            assignedTo: null,
        };

        AddIssueToBacklog(newissue);
    };

    const AddIssueToBacklog = async (issue) => {
        try {
            const response = await axios.post(
                `${ServerURL}issues/create-issue/backlog/${projectId}`,
                issue,
                {
                    headers: {
                        'Content-Type': 'application/json',
                        Authorization: TOKEN,
                    },
                }
            );

            fetchBacklogs();
        } catch (error) {
            console.error('Error adding issue:', error.message);
        }
    }


    const handleDeleteIssueFromBacklog = async (issueId) => {
        try {
            await axios.delete(`${ServerURL}issues/delete-issue/backlog/${projectId}/${issueId}`, {
                headers: {
                    Authorization: TOKEN,
                },
            });
            fetchBacklogs();
        } catch (error) {
            console.error('Error deleting issue from backlog:', error.message);
        }
    };

    const handleDeleteIssueFromSprint = async (sprintId, issueId) => {
        try {
            await axios.delete(`${ServerURL}issues/delete-issue/sprint/${sprintId}/${issueId}`, {
                headers: {
                    Authorization: TOKEN,
                },
            });
            fetchSprints();
        } catch (error) {
            console.error('Error deleting issue from sprint:', error.message);
        }
    };

    const handleBlurSubissueInput = () => {
        setShowAddSubissueInput(null);
    };

    const handleSprintExpand = (Index) => {
        setShowSprintExpand(Index);
        setSprintExpand(!Sprintexpand);
    };

    const handleDeleteSprint = (sprintId) => {
        // Handle delete issue functionality
        console.log(`Delete issue in Sprint ${sprintId}`);
    };

    const openDelConfirmPopup = (SprintId) => {
        setSelectedSprintId(SprintId);
        setIsOpenDelConfirm(true);
    };

    const closeDelConfirmPopup = () => {
        setSelectedSprintId(null);
        setIsOpenDelConfirm(false);
    };

    const openAcitivateConfirmPopup = (Sprint) => {
        setSprintReadyForActive(Sprint);
        setIsOpenActivateConfirm(true);
    };

    const closeAcitivateConfirmPopup = () => {
        setSprintReadyForActive(null);
        setIsOpenActivateConfirm(false);
    };



    const handleEditSprint = (sprintId) => {
        // Handle edit sprint functionality
        console.log(`Edit Sprint ${sprintId}`);
    };

    const [showIssuePopup, setShowIssuePopup] = useState(false);
    const [selectedIssue, setSelectedIssue] = useState(null);

    const handleIssueDoubleClick = (issue) => {
        setShowIssuePopup(true);
        setSelectedIssue(issue)
    };

    const closeIssuePopup = () => {
        setShowIssuePopup(false);
    };

    const handleDragStart = (e, issue, source, id) => {
        e.dataTransfer.setData("text/plain", JSON.stringify({ issue, source, id }));
    };

    const handleDrop = (e, target) => {
        e.preventDefault();
        const data = JSON.parse(e.dataTransfer.getData("text/plain"));
        const { issue, source, id } = data;

        // Check if the drop target is the same as the source
        if (source === target) {
            return;
        }

        // Handle the issue movement between backlog and sprint
        if (source === 'backlog') {
            AddIssueToSprint(issue, target);
            handleDeleteIssueFromBacklog(issue.issueId); // Remove the issue from the backlog
        } else if (source === 'sprint') {
            AddIssueToBacklog(issue);
            handleDeleteIssueFromSprint(id, issue.issueId); // Remove the issue from the sprint
        }
    };

    const [isEditable, setIsEditable] = useState(false);
    useEffect(() => {
        const checkEditable = async () => {
            const bool = user && await getUserAccess(projectId, user.userRole, PermissionTypes.TASK_SPRINT);
            setIsEditable(bool);
        };

        checkEditable();
    }, [projectId, user]);


    const handleDragOver = (e) => {
        e.preventDefault();
    };

    return (
        <div className="WorkArea">
            <Sidebar
                setUpView={setUpView}
                view={view}
            />
            <div className='backlogSprintContainer'>
                <div className="backlog-panel">
                    <h2>Backlogs</h2>
                    <div className="backlog-table" onDrop={(e) => handleDrop(e, 'backlog')} onDragOver={handleDragOver}>
                        {backlogs.map((issue, index) => (
                            <div
                                className="backlog-row draggable"
                                key={index}
                                draggable="true" // Enable drag for backlog issues
                                onDragStart={(e) => handleDragStart(e, issue, 'backlog', '1')}
                            >
                                {issue.title}
                                <button
                                    className="delete-button"
                                    onClick={() => handleDeleteIssueFromBacklog(issue.issueId)}
                                >
                                    <FaTrashAlt />
                                </button>
                            </div>
                        ))}
                    </div>
                    {isEditable &&
                        <div className="add-backlog-row">
                            <input
                                type="text"
                                className="add-backlog-input"
                                placeholder="Add New issue"
                                value={newBacklog}
                                onChange={handleBacklogChange}
                                onKeyDown={(e) => {
                                    if (e.key === 'Enter') {
                                        handleAddIssueToBacklog(e.target.value);
                                        e.target.value = '';
                                        setNewBacklog('');
                                    }
                                }}
                            />

                            <button className="add-backlog-button" onClick={() => {
                                handleAddIssueToBacklog(
                                    document.querySelector('.add-backlog-input').value
                                );
                                document.querySelector('.add-backlog-input').value = '';
                                setNewBacklog('');
                            }}>
                                +
                            </button>

                        </div>
                    }
                    {issueNameErrorBacklog && <p className="error-message">{issueNameErrorBacklog}</p>}
                </div>

                <div className='sprint-workspace'>
                    <h2>Sprints</h2>
                    <div className="sprint-tables">
                        {sprints.map((sprint, sprintIndex) => (
                            <div className="sprint-table" key={sprintIndex} onDrop={(e) => handleDrop(e, sprint.sprintId)} onDragOver={handleDragOver}>
                                <div
                                    className={`sprint ${sprint.sprintId === selectedSprintId ? 'active' : 'deactive'}`}
                                    onClick={() => handleStartSprint(sprint.sprintId)}
                                    ref={sprintTableRef}
                                >
                                    <div className="sprint-header" onClick={(e) => handleSprintExpand(sprint.sprintId)}>
                                        <div
                                            className={
                                                showExpandSprint === sprint.sprintId && Sprintexpand ? 'arrow-clicked' : 'arrow'
                                            }
                                        >
                                            {showExpandSprint === sprint.sprintId && Sprintexpand ? <FaChevronDown /> : <FaChevronRight />}
                                        </div>
                                        {sprint.name}
                                        {isEditable &&
                                            <div className="sprint-icons">
                                                <FaEdit className="edit-button" onClick={() => handleEditSprint(sprint.sprintId, null)} />
                                                {/* <FaTrashAlt className="delete-button" onClick={() => openDelConfirmPopup(sprint.sprintId)} /> */}
                                                {activeSprint && activeSprint.name === sprint.name ? <FaPause className="start-button"
                                                    onClick={() => openAcitivateConfirmPopup(null)} />
                                                    : <FaPlay className="start-button" onClick={() => openAcitivateConfirmPopup(sprint)} />}
                                            </div>}
                                    </div>
                                    {showExpandSprint === sprint.sprintId && Sprintexpand && sprint.issues.map((issue, issueIndex) => (
                                        <ul className="issue-list" key={issueIndex}>
                                            <li
                                                className="issue"
                                                draggable="true" // Enable drag for sprint issues
                                                onDragStart={(e) => handleDragStart(e, issue, 'sprint', sprint.sprintId)}
                                                onClick={() => handleIssueDoubleClick(issue)}
                                            >
                                                <div className="issue-header">
                                                    {issue.title}
                                                    {/* {isEditable &&
                                                        < button
                                                            className="delete-button"
                                                            onClick={() => handleDeleteIssueFromSprint(sprint.sprintId, issue.issueId)}
                                                        >
                                                            <FaTrashAlt />
                                                        </button>} */}
                                                </div>
                                            </li>
                                        </ul>
                                    ))}
                                    {selectedSprintId === sprint.sprintId && (
                                        <div className="add-issue-row">
                                            {isEditable && (
                                                <>
                                                    {!showAddissueInput ? (
                                                        <button className="add-issue-button" onClick={() => setShowAddissueInput(true)}>
                                                            Add issue
                                                        </button>
                                                    ) : (
                                                        <div>
                                                            <input
                                                                type="text"
                                                                className="add-issue-input"
                                                                placeholder="Add New Issue"
                                                                onKeyDown={(e) => {
                                                                    if (e.key === 'Enter') {
                                                                        handleAddissueToSprint(e.target.value, sprint.sprintId);
                                                                        e.target.value = '';
                                                                        setShowAddissueInput(false);
                                                                    }
                                                                }}
                                                            />
                                                            <button
                                                                className="add-issue-button"
                                                                onClick={() => {
                                                                    handleAddissueToSprint(
                                                                        document.querySelector('.add-issue-input').value,
                                                                        sprint.sprintId
                                                                    );
                                                                    document.querySelector('.add-issue-input').value = '';
                                                                    setShowAddissueInput(false);
                                                                }}
                                                            >
                                                                <FaCheck />
                                                            </button>
                                                        </div>
                                                    )}</>
                                            )}
                                            {issueNameErrorSprint && <p className="error-message">{issueNameErrorSprint}</p>}
                                        </div>
                                    )}
                                </div>
                            </div>
                        ))}
                        {isEditable &&
                            <div className="add-sprint-button" onClick={handleAddSprint}>
                                +
                            </div>}
                    </div>
                    {showIssuePopup && <IssuePopup onClose={closeIssuePopup} setPoker={setPoker} Issue={selectedIssue} user={user} projectId={projectId} handleDeleteIssueFromSprint={handleDeleteIssueFromSprint} sprint={selectedSprintId} />}
                    {isOpenDelConfirm && (
                        <DeleteSprint
                            sprintId={selectedSprintId}
                            closePopup={closeDelConfirmPopup}
                        />
                    )}
                    {isOpenActivateConfirm && (
                        <ActiateSprint
                            projectId={projectId}
                            closePopup={closeAcitivateConfirmPopup}
                            sprint={sprintReadyForActive}
                            setShowSprintExpand={setShowSprintExpand}
                        />
                    )}
                    {pokerIssue &&
                        <li className={view === 'poker' ? 'selected' : 'list'} onClick={() => setUpView('poker')}>Poker voting</li>
                    }
                </div>
            </div>
        </div >
    );
};

export default BacklogArea;
