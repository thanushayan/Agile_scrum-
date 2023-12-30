import React, { useState, useContext, useEffect } from 'react';
import { useHistory, useParams } from 'react-router-dom';
import '../css/ProjectWorkArea.css';
import { getItemFromLocalStorage } from './AuthStorageUtil';
import config from '../config.json';
import { DarkModeContext } from './DarkModeProvider';
import '../css/Mode.css';
import TopBar from './TopBar';
import { clearTokenAndUserName } from './AuthStorageUtil';
import ProfileSlider from './ProfileSlider';
import BacklogArea from './BacklogArea';
import MembersPage from './MembersPage';
import getUserRole from './UserRoleUtill';
import PokerArea from './PokerArea';
import Reporting from './Reporting';
import Dashboard from './Dashboard';
import PermissionsPage from './PermissionsPage';
import Scrumboard from './Scrumboard';
import axios from 'axios';
import Ganttchart from './GanttChart';

const ProjectWorkArea = () => {
    const { mode, toggleMode } = useContext(DarkModeContext);
    const [SliderisOpen, setSliderIsOpen] = useState(false);
    const history = useHistory();
    const [view, setView] = useState('backlog');
    const [user, setUser] = useState({});
    const { projectId } = useParams();
    const ServerURL = config.APIURL;
    const TOKEN = getItemFromLocalStorage('token');
    const username = getItemFromLocalStorage('username');
    const [activeSprint, setNewActiveSprint] = useState(null);
    const [tasks, setNewTasks] = useState([]);




    useEffect(() => {
        const fetchUserRole = async () => {
            const userRole = await getUserRole({ projectId });
            setUser(userRole);
            console.log(userRole.userRole);
        };

        fetchUserRole();
    }, [projectId]);

    useEffect(() => {
        const savedView = localStorage.getItem('lastSelectedView');
        if (savedView) {
            setView(savedView);
        }
    }, []);



    function setUpView(view) {
        setView(view);
        // Save the selected view to localStorage
        localStorage.setItem('lastSelectedView', view);
    }

    const toggleSlider = () => {
        setSliderIsOpen(!SliderisOpen);
    };

    const LogOut = () => {
        clearTokenAndUserName();
        history.push('/auth');
    }

    const fetchActiveSprintIssues = async (projectId, TOKEN, setNewActiveSprint, ServerURL) => {
        try {
            const apiUrl = `${ServerURL}projects/${username}/${projectId}/activeSprint`;

            const response = await axios.get(apiUrl, {
                headers: {
                    Authorization: `Bearer ${TOKEN}`
                }
            });

            const newActiveSprint = response.data;
            setNewActiveSprint(newActiveSprint);

            if (newActiveSprint && newActiveSprint.issues) {
                setNewTasks(newActiveSprint.issues);
                console.log("kurumi")
            } else {
                setNewTasks([]); // If there are no issues or activeSprint is null, set tasks to an empty array
            }
        } catch (error) {
            console.error('Error fetching active sprint:', error);
            setNewTasks([]); // Handle the error by setting tasks to an empty array
        }
    };

    // Inside your component
    useEffect(() => {
        fetchActiveSprintIssues(projectId, TOKEN, setNewActiveSprint, ServerURL);
    }, [projectId]);// Added projectId as a dependency to fetch data when it changes

    useEffect(() => {
        // Log the updated value of activeSprint when it changes
        setNewActiveSprint(activeSprint);
        console.log('Updated Active Sprint Tasks:', tasks);
    }, [activeSprint]);


    const updateScrumBoardIssues = async (taskId, status) => {
        try {
            const api = `${ServerURL}issues/${taskId}/status/${status}`;
            console.log('Dropped Task ID:', taskId)
            console.log('Dropped Area:', status)
            const response = await axios.put(api, {}, {
                headers: {
                    Authorization: `Bearer ${TOKEN}`,
                },
            });
            console.log('Token:', TOKEN)
            if (response.status === 200) {
                fetchActiveSprintIssues(projectId, TOKEN, setNewActiveSprint, ServerURL);
            } else {
                throw new Error('Failed to add role');
            }
        } catch (error) {
            if (error.response) {
                // The request was made and the server responded with a status code
                // that falls out of the range of 2xx
                console.error('Server Error:', error.response.data);
                console.error('Status:', error.response.status);
                console.error('Headers:', error.response.headers);

            } else if (error.request) {
                // The request was made but no response was received
                console.error('Request Error:', error.request);

            } else {
                // Something happened in setting up the request that triggered an error
                console.error('Error:', error.message);

            }
            // Handle the error state or any other error-related functionality here
        }
    };

    const [pokerIssue, setPoker] = useState(null);

    useEffect(() => {
    }, [pokerIssue]);

    const fetchPokerIssuesByProjectId = async (projectId) => {
        try {
            const url = `${ServerURL}projects/pokerIssue/${projectId}`;

            const response = await axios.get(
                url,
                {
                    headers: {
                        Authorization: `Bearer ${TOKEN}`,
                        'Content-Type': 'application/json',
                    },
                }
            );

            // Handle the response data here
            setPoker(response.data);
            return response.data; // Return or handle the data as needed
        } catch (error) {
            console.error('Error fetching poker issues:', error);
            throw new Error('Failed to fetch poker issues');
        }
    };

    useEffect(() => {
        const fetchData = async () => {
            await fetchPokerIssuesByProjectId(projectId);
        };

        // Fetch initially
        fetchData();

        // Fetch every second (1000 milliseconds)
        const intervalId = setInterval(fetchData, 1000);

        // Clean up the interval on unmount or when projectId/token changes
        return () => clearInterval(intervalId);
    }, [projectId, TOKEN]);


    return (

        <div className={mode === 'dark' ? 'dark' : ''}>
            <TopBar
                mode={mode}
                toggleMode={toggleMode}
                toggleSlider={toggleSlider}
                projectId={projectId}
            />
            <div>
                {view === 'backlog' && (
                    <div className='backLogArea'>
                        <BacklogArea
                            setUpView={setUpView}
                            view={view}
                            activeSprint={activeSprint}
                            projectId={projectId}
                            user={user}
                            setPoker={setPoker}
                            pokerIssue={pokerIssue}
                        />
                    </div>
                )}

                {view === 'dashboard' && (
                    <div className='dashboard'>
                        <Dashboard
                            setUpView={setUpView}
                            view={view}
                            user={user}
                            projectId={projectId}
                        />
                    </div>
                )}

                {view === 'activeSprint' && (
                    <div className='active'>
                        <Scrumboard
                            projectId={projectId}
                            setUpView={setUpView}
                            view={view}
                            user={user}
                            tasks={tasks}
                            updateScrumBoardIssues={updateScrumBoardIssues}
                            setTasks={setNewTasks}
                        />
                    </div>
                )}



                {view === 'team' && (
                    <div className='MembersArea'>
                        <MembersPage
                            setUpView={setUpView}
                            view={view}
                            user={user}
                        />
                    </div>
                )}

                {view === 'Report' && (
                    <div>
                        <Ganttchart
                            setUpView={setUpView}
                            view={view}
                            projectId={projectId}
                        />
                    </div>
                )}

                {pokerIssue && view === 'poker' && (
                    <div className='PokerArea'>
                        <PokerArea
                            setUpView={setUpView}
                            view={view}
                            projectId={projectId}
                            username={username}
                            user={user}
                            pokerIssue={pokerIssue}
                        />
                    </div>
                )}

                {view === 'permission' && (
                    <div className='permissions'>
                        <PermissionsPage
                            projectId={projectId}
                            setUpView={setUpView}
                            view={view}
                            user={user}
                        />
                    </div>
                )}
                {
                    SliderisOpen && (
                        <ProfileSlider
                            SliderisOpen={SliderisOpen}
                            LogOut={LogOut}
                            setSliderIsOpen={setSliderIsOpen}
                        />
                    )
                }
            </div>
        </div>
    );
};

export default ProjectWorkArea;