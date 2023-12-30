import React, { useState, useContext, useEffect } from 'react';
import '../css/Scrumboard.css';
import axios from 'axios';
import Sidebar from './SideBar';
import config from '../config.json';
import { getItemFromLocalStorage } from './AuthStorageUtil';
import { DarkModeContext } from './DarkModeProvider';
import { getUserAccess, PermissionTypes } from './UserRoleUtill';

const Scrumboard = ({ user, projectId, view, setUpView, tasks, updateScrumBoardIssues }) => {
  const [statuses, setStatuses] = useState(null);
  const [newStatus, setNewStatus] = useState('');
  const { mode, toggleMode } = useContext(DarkModeContext);
  const [error, setError] = useState('');
  const TOKEN = getItemFromLocalStorage('token');
  const username = getItemFromLocalStorage('username');
  const ServerURL = config.APIURL;

  const URL = `http://localhost:8080/projects/status/${username}/${projectId}`;

  const fetchScrumBoardStatuses = async () => {
    console.log("Enkum");
    try {
      const apiUrl = URL;

      const response = await axios.get(apiUrl, {
        headers: {
          Authorization: `Bearer ${TOKEN}`,
        },
      });

      const fetchedStatuses = response.data.map(status => status.statusName);
      setStatuses(fetchedStatuses);
    } catch (error) {
      setError(error.body)
      console.error('Error fetching Scrum Board statuses:', error);
      // Handle the error, could set some default values if the fetch fails
    }
  };

  const handleAddNewBoard = async () => {
    const TOKEN = getItemFromLocalStorage('token');

    try {
      const Status = {
        statusName: newStatus,
        // Add other properties if necessary
      };
      console.log(Status);
      const response = await fetch(URL, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${TOKEN}`,
        },
        body: JSON.stringify(Status)
      });

      if (response.ok) {
        fetchScrumBoardStatuses();
      } else {
        setError(response.body)
        throw new Error('Failed to add status');
      }
    } catch (error) {
      if (error.response) {
        setError(error.response.data); // Update error state with the error message from the response
        console.error('Error fetching Scrum Board statuses:', error.response);
      } else {
        setError('Unknown error occurred'); // Fallback message for unexpected errors
        console.error('Unknown error occurred:', error);
      }
    }
  };

  useEffect(() => {
    fetchScrumBoardStatuses();
  }, []);

  const handleAddStatus = () => {
    handleAddNewBoard();
  };

  const handleKeyPress = (e, newStatus) => {
    if (e.key === 'Enter') {
      e.preventDefault();
      handleAddStatus(newStatus);
    }
  };

  const handleDragStart = (e, taskId) => {
    e.dataTransfer.setData('taskId', taskId);
  };

  const handleDrop = (e, status) => {
    e.preventDefault();
    const taskId = e.dataTransfer.getData('taskId');
    console.log('Dropped Task ID:', taskId); // Print the dropped task ID to the console

    const updatedTasks = tasks.map((task) => {
      if (task.id === parseInt(taskId, 10)) {
        task.status = status;
      }
      return task;
    });
    updateScrumBoardIssues(taskId, status);
  };

  const allowDrop = (e) => {
    e.preventDefault();
  };

  const [isEditable, setIsEditable] = useState(false);
  useEffect(() => {
    const checkEditable = async () => {
      const bool = user && await getUserAccess(projectId, user.userRole, PermissionTypes.SYSTEM_CONFIGURATION);
      setIsEditable(bool);
    };

    checkEditable();
  }, [projectId, user]);


  return (
    <div className={mode === 'dark' ? 'dark' : ''}>
      <div className='WorkArea'><Sidebar
        setUpView={setUpView}
        view={view}
      />
        <div className="scrum-board">
          <div className="status-list">
            {statuses && statuses.length > 0 ? (
              statuses.map((status) => (
                <div key={status} className="status">
                  <h2>{status}</h2>
                  <div
                    className="drop-area"
                    onDragOver={(e) => allowDrop(e)}
                    onDrop={(e) => handleDrop(e, status)}
                  >
                    {tasks
                      .filter((task) => task.status && task.status.statusName === status) // Check if task.status exists
                      .map((task) => (
                        <div
                          key={task.id}
                          className="task"
                          draggable="true"
                          onDragStart={(e) => handleDragStart(e, task.id)}
                        >
                          <h3>{task.title}</h3>
                          {console.log(task)}
                        </div>
                      ))}
                  </div>
                </div>
              ))
            ) : (
              <p>No statuses available</p>
            )}
            {isEditable &&
              <div className="add-status">
                <input
                  type="text"
                  placeholder="New Status"
                  value={newStatus}
                  onChange={(e) => setNewStatus(e.target.value)}
                  onKeyPress={(e) => handleKeyPress(e, newStatus)}
                />

                < button className="add-status-button" onClick={handleAddStatus}>
                  Add
                </button>

              </div>}
            <p className='error'>{error}</p>
          </div>
        </div>
      </div>

    </div >

  );
};

export default Scrumboard;