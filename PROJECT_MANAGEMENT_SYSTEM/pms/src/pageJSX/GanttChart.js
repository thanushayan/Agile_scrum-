import { React, useContext, useState, useEffect } from 'react';
import { Gantt, Task, ViewMode } from 'gantt-task-react';
import 'gantt-task-react/dist/index.css';
import { DarkModeContext } from './DarkModeProvider';
import Sidebar from './SideBar';
import axios from 'axios';
import { getUserAccess, PermissionTypes } from './UserRoleUtill';
import { getItemFromLocalStorage } from './AuthStorageUtil';
import config from '../config.json';
import '../css/gantt.css';

const Ganttchart = ({ view, setUpView, projectId }) => {
    const { mode, toggleMode } = useContext(DarkModeContext);
    const [tasksForGantt, setTasks] = useState([
        {
            start: new Date(2023, 11, 13), // Note: Months are zero-indexed, so December is 11
            end: new Date(2023, 11, 13),
            name: "None",
            id: 1,
            progress: 0,
            // Add other necessary properties based on your data structure
        }
    ]);


    const TOKEN = getItemFromLocalStorage('token');
    const ServerURL = config.APIURL;
    const userId = getItemFromLocalStorage('username');
    const username = getItemFromLocalStorage('username');



    useEffect(() => {
        const fetchScrumBoardStatuses = async () => {
            const URL = `${ServerURL}projects/${username}/${projectId}/activeSprint`;
            try {
                const response = await axios.get(URL, {
                    headers: {
                        Authorization: `Bearer ${TOKEN}`,
                    },
                });
                if (response && response.data) {
                    const data = response.data;

                    console.log("you too", data)
                    // Check if data.issues is an array
                    if (Array.isArray(data.issues)) {

                        const formattedTasks = data.issues.map((issue, index) => ({
                            start: issue.startDate ? new Date(issue.startDate) : new Date(2023, 12, 13),
                            end: issue.dueDate ? new Date(issue.dueDate) : new Date(2023, 12, 13),
                            name: issue.title,
                            id: `Task ${index}`,
                            progress: issue.progress,
                            // Add other necessary properties based on your data structure
                        }));
                        setTasks(formattedTasks); // Update tasks state here


                    } else {
                        console.error('Issues data is not an array');
                    }
                }
            } catch (error) {
                console.error('dffg:', error);
            }
        };

        fetchScrumBoardStatuses();
    }, [TOKEN, ServerURL, projectId, username]);

    useEffect(() => {
        console.log("so data : ", tasksForGantt);
    }, [tasksForGantt]);


    // Define your tasks array
    let tasks = tasksForGantt;

    // Define your event handling functions
    const onTaskChange = (task: Task) => {
        // Handle task change
    };

    const onTaskDelete = (taskId: string) => {
        // Handle task deletion
    };

    const onProgressChange = (task: Task) => {
        // Handle progress change
    };

    const onDblClick = (task: Task) => {
        // Handle double click
    };

    const onClick = (task: Task) => {
        // Handle click
    };

    // Define your view mode (you may want to replace this with your specific view mode)
    const viewMode = ViewMode.Day;

    return (
        <div className={mode === 'dark' ? 'dark' : ''}>
            <div className='WorkArea'><Sidebar
                setUpView={setUpView}
                view={view}
            />
                <div className="gantt-container">
                    <Gantt
                        tasks={tasks}
                        viewMode={viewMode}
                        onDateChange={onTaskChange}
                        onTaskDelete={onTaskDelete}
                        onProgressChange={onProgressChange}
                        onDoubleClick={onDblClick}
                        onClick={onClick}
                    />
                </div>
            </div>
        </div>
    );
};

export default Ganttchart;
