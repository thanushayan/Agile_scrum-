import React, { useState, useEffect } from 'react';
import '../css/issuePop.css';
import axios from 'axios';
import { getUserAccess, PermissionTypes } from './UserRoleUtill';
import { getItemFromLocalStorage } from './AuthStorageUtil';
import config from '../config.json';
import { FaCheck, FaChevronDown, FaChevronRight, FaEdit, FaTrashAlt, FaPlay, FaPause } from 'react-icons/fa';

const IssuePopUp = ({ onClose, setPoker, Issue, user, projectId, handleDeleteIssueFromSprint, sprint }) => {
    const [issueData, setIssueData] = useState(null);

    const [issueName, setIssueName] = useState('');
    const [description, setDescription] = useState('');
    const [priority, setPriority] = useState('low');
    const [dueDate, setDueDate] = useState('');
    const [type, setType] = useState('');
    const [assignee, setAssignee] = useState('');
    const [status, setStatus] = useState('To do');
    const [comment, setComment] = useState('');
    const [startDate, setStartDate] = useState('');
    // Other state variables...
    const [progress, setProgress] = useState(0);

    const [isEditable, setIsEditable] = useState(false);
    const token = getItemFromLocalStorage('token');
    const ServerURL = config.APIURL;
    const username = getItemFromLocalStorage('username')

    useEffect(() => {
        const checkEditable = async () => {
            const bool = user && await getUserAccess(projectId, user.userRole, PermissionTypes.TASK_SPRINT);
            setIsEditable(bool);
        };

        checkEditable();
    }, [projectId, user]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        const {
            issueName,
            projectId,
            description,
            priority,
            dueDate,
            type,
            assignee,
            status,
            comment,
            startDate,
            progress: progressElement, // Renamed to avoid conflict with state variable name
        } = e.target.elements;

        const statusData = {
            statusName: status.value
        };

        const updatedIssueData = {
            title: issueName.value,
            projectId: projectId,
            description: description.value,
            priority: priority.value,
            dueDate: dueDate.value,
            assignee: assignee.value,
            status: statusData,
            comment: comment.value,
            type: type.value,
            startDate: startDate.value,
            progress: progressElement.value, // Get value from the form element
        };

        console.log(updatedIssueData);
        setIssueData(updatedIssueData);
        setProgress(progressElement.value); // Update the progress state with the form element's value
        await updateIssue(updatedIssueData);
    };



    const updatePokerIssue = async (projectId, token) => {
        try {
            const apiUrl = `${ServerURL}projects/pokerIssue/${projectId}/${Issue.id}`;
            const response = await axios.put(apiUrl, {}, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            });

            if (response.status === 200) {
                onClose();
                return { success: true, message: response.data };
            } else {
                return { success: false, message: 'Failed to update poker issue' };
            }
        } catch (error) {
            // Handle errors here
            if (error.response) {
                return { success: false, message: error.response.data };
            } else {
                return { success: false, message: 'An error occurred while updating the poker issue' };
            }
        }
    };


    const updateIssue = async (updatedIssueData) => {
        try {
            const apiUrl = `${ServerURL}issues/${Issue.id}`;

            const response = await axios.put(apiUrl, {
                title: updatedIssueData.title,
                projectId: projectId,
                description: updatedIssueData.description,
                priority: updatedIssueData.priority,
                dueDate: updatedIssueData.dueDate,
                assignee: updatedIssueData.assignee,
                status: updatedIssueData.status,
                comment: updatedIssueData.comment,
                type: updatedIssueData.type,
                startDate: updatedIssueData.startDate,
                progress: updatedIssueData.progress,
            }, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            });
            onClose();
            window.location.reload();
            return response.data; // Return the updated issue data if successful
        } catch (error) {
            // Handle errors
            if (error.response) {
                // The request was made and the server responded with a status code that falls out of the range of 2xx
                console.error('Server error:', error.response.data);
                return null; // Return null or handle the error as needed
            } else if (error.request) {
                // The request was made but no response was received
                console.error('No response from server:', error.request);
                return null; // Return null or handle the error as needed
            } else {
                // Something happened in setting up the request that triggered an error
                console.error('Request setup error:', error.message);
                return null; // Return null or handle the error as needed
            }
        }
    };

    useEffect(() => {
        // Reset the input fields when Issue prop changes (e.g., when editing a different issue)
        setIssueName(Issue?.title || '');
        setDescription(Issue?.description || '');
        setPriority(Issue?.priority || 'low');
        setDueDate(Issue?.dueDate || '');
        setAssignee(Issue?.assignee || '');
        setStatus(Issue?.status || 'To Do');
        setComment(Issue?.comment || '');
        setType(Issue?.type || '');
        setStartDate(Issue?.startDate || '');
        setProgress(Issue?.progress || 0);
    }, [Issue]);

    const URL = `${ServerURL}projects/status/${username}/${projectId}`;
    const [statusOptions, setStatuses] = useState(null);
    const fetchScrumBoardStatuses = async () => {
        console.log("Enkum");
        try {
            const apiUrl = URL;

            const response = await axios.get(apiUrl, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });

            const fetchedStatuses = response.data.map(status => status.statusName);
            setStatuses(fetchedStatuses);
        } catch (error) {
            console.error('Error fetching Scrum Board statuses:', error);
            // Handle the error, could set some default values if the fetch fails
        }
    };

    useEffect(() => {
        fetchScrumBoardStatuses();
    }, []);

    return (
        <div className="issue-popup">
            <div className="issue-popup-content">
                <button className="popup-close-button" onClick={onClose}>
                    Close
                </button>

                {isEditable &&
                    < button
                        className="delete-button"
                        onClick={() => handleDeleteIssueFromSprint(sprint, Issue.issueId)}
                    >
                        <FaTrashAlt />
                    </button>}

                <h2>{Issue ? 'Edit Issue' : 'Add Issue'}</h2>
                <form onSubmit={handleSubmit}>
                    <div className="form-group subtask">
                        <div className="form-group progress">
                            <label className="label" htmlFor="progress">
                                Progress:
                            </label>
                            <input
                                type="range"
                                id="progress"
                                min="0"
                                max="100"
                                disabled={assignee !== username}
                                value={progress}
                                onChange={(e) => setProgress(e.target.value)}
                            />

                        </div>
                        <label className="label" htmlFor="issueName">
                            Issue name:
                        </label>
                        <input
                            type="text"
                            id="issueName"
                            value={issueName}
                            onChange={(e) => setIssueName(e.target.value)}
                            readOnly={!isEditable}
                        />
                    </div>
                    <div className="form-group des">
                        <label className="label" htmlFor="description">
                            Description:
                        </label>
                        <textarea
                            id="description"
                            value={description}
                            onChange={(e) => setDescription(e.target.value)}
                            readOnly={!isEditable}
                        />
                    </div>
                    <div className="form-group priority">
                        <label className="label" htmlFor="priority">
                            Priority:
                        </label>
                        <select
                            id="priority"
                            value={priority}
                            onChange={(e) => setPriority(e.target.value)}
                            disabled={!isEditable}
                        >
                            <option value="low">Low</option>
                            <option value="medium">Medium</option>
                            <option value="high">High</option>
                        </select>
                    </div>
                    <div className="form-group startdate">
                        <label className="label" htmlFor="startDate">
                            Start Date:
                        </label>
                        <input
                            type="date"
                            id="startDate"
                            value={startDate}
                            onChange={(e) => setStartDate(e.target.value)}
                            readOnly={!isEditable}
                        />
                    </div>
                    <div className="form-group duedate">
                        <label className="label" htmlFor="dueDate">
                            Due Date:
                        </label>
                        <input
                            type="date"
                            id="dueDate"
                            value={dueDate}
                            onChange={(e) => setDueDate(e.target.value)}
                            readOnly={!isEditable}
                        />
                    </div>
                    <div className="form-group type">
                        <label className="label" htmlFor="type">
                            Type:
                        </label>
                        <input
                            type="text"
                            id="type"
                            value={type}
                            onChange={(e) => setType(e.target.value)}
                            readOnly={!isEditable}
                        />
                    </div>
                    <div className="form-group assignee">
                        <label className="label" htmlFor="assignee">
                            Assignee:
                        </label>
                        <input
                            type="text"
                            id="assignee"
                            value={assignee}
                            onChange={(e) => setAssignee(e.target.value)}
                            readOnly={!isEditable}
                        />
                    </div>
                    <div className="form-group status">
                        <label className="label" htmlFor="status">
                            Status:
                        </label>
                        <select
                            id="status"
                            value={status.statusName}
                            onChange={(e) => setStatus(e.target.value)}
                            readOnly={!isEditable}
                        >
                            {statusOptions && statusOptions.map((status) => (
                                <option key={status} value={status}>
                                    {status}
                                </option>
                            ))}
                        </select>
                    </div>
                    <div className="form-group comment">
                        <label className="label" htmlFor="comment">
                            Comment:
                        </label>
                        <textarea
                            id="comment"
                            value={comment}
                            onChange={(e) => setComment(e.target.value)}
                            readOnly={!isEditable}
                        />
                    </div>


                    <div className="form-group-btn">
                        {assignee === username || isEditable ? (
                            <button className="add-button" type="submit">
                                {Issue ? 'Update' : 'Add'}
                            </button>
                        ) : null}
                        {isEditable ? (
                            <button className="open-for-poker-button" onClick={() => updatePokerIssue(projectId, token)}>
                                {Issue ? 'openPoker' : 'openPoker'}
                            </button>
                        ) : null}
                    </div>
                </form>
            </div>
        </div>
    );
};

export default IssuePopUp;
