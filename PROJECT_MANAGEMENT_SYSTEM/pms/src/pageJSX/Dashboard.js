import React, { useState, useContext, useEffect } from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, PieChart, Pie, Cell, ScatterChart, Scatter, LineChart, Line, ComposedChart } from 'recharts';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { Card, List, Button } from 'semantic-ui-react';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import { useHistory } from 'react-router-dom';
import { DarkModeContext } from './DarkModeProvider';
import '../css/Dashboard.css';
import Sidebar from './SideBar';
import { getItemFromLocalStorage } from './AuthStorageUtil';
import config from '../config.json';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCheckCircle } from '@fortawesome/free-solid-svg-icons';
import Modal from 'react-modal';
import { PermissionTypes, getUserAccess } from './UserRoleUtill';

const Dashboard = ({ view, setUpView, user, projectId }) => {

    const [GraphData, setChartData] = useState([])
    const [DeadlineWork, setDeadLineWork] = useState([])

    const ServerURL = config.APIURL;
    const TOKEN = getItemFromLocalStorage('token');
    const username = getItemFromLocalStorage('username');

    useEffect(() => {
        const fetchChartData = async () => {
            try {
                const response = await fetch(`http://localhost:8080/issues/${projectId}`, {
                    headers: {
                        Authorization: `Bearer ${TOKEN}`,
                        'Content-Type': 'application/json',
                    },
                });

                if (response.ok) {
                    const data = await response.json();
                    setChartData(data);
                } else {
                    // Handle error response
                }
            } catch (error) {
                // Handle fetch error
            }
        };

        fetchChartData();
    }, [TOKEN]);

    useEffect(() => {
        console.log("so data : ", user);
    }, [GraphData]);

    useEffect(() => {
        const fetchDeadLineWork = async () => {
            try {
                const response = await fetch(`http://localhost:8080/issues/${projectId}/${username}`, {
                    headers: {
                        Authorization: `Bearer ${TOKEN}`,
                        'Content-Type': 'application/json',
                    },
                });

                if (response.ok) {
                    const data = await response.json();
                    setDeadLineWork(data);
                } else {
                    // Handle error response
                }
            } catch (error) {
                // Handle fetch error
            }
        };

        fetchDeadLineWork();
    }, [TOKEN]);
    const [isEditable, setIsEditable] = useState(false);

    useEffect(() => {
        const checkEditable = async () => {
            const bool = user && await getUserAccess(projectId, user.userRole, PermissionTypes.VIEW_REPORTS);
            setIsEditable(bool);
        };

        checkEditable();
    }, [projectId, user]);

    let data;
    let pieChartData;

    if (isEditable) {
        data = GraphData.map((item) => ({
            name: item.title,
            completed: item.progress,
        }));


        pieChartData = GraphData.map((item, index) => ({
            name: item.title,
            value: item.progress,
            fill: index === 0 ? '#82ca9d' : '#8884d8',
        }));
    }
    else {
        data = DeadlineWork.map((item) => ({
            name: item.title,
            completed: item.progress,
        }));


        pieChartData = DeadlineWork.map((item, index) => ({
            name: item.title,
            value: item.progress,
            fill: index === 0 ? '#82ca9d' : '#8884d8',
        }));
    }
    // Sample data for charts



    const history = useHistory();
    const { mode, toggleMode } = useContext(DarkModeContext);
    const [selectedDate, setSelectedDate] = useState(new Date());
    const [hoveredDate, setHoveredDate] = useState(null);
    const [tooltipContent, setTooltipContent] = useState('');
    const [modalOpen, setModalIsOpen] = useState(false);
    const [modalContent, setModalContent] = useState('');



    const deadlineWorks = DeadlineWork.map((item) => ({
        date: new Date(item.dueDate),
        tasks: [
            { id: item.id, name: item.title }
        ],
    }));

    useEffect(() => {
        console.log("so data : ", deadlineWorks);
    }, [deadlineWorks]);


    const handleTileMouseEnter = (date) => {
        setHoveredDate(date);

        const foundWork = deadlineWorks.find((work) => {
            return (
                work.date.getDate() === date.getDate() &&
                work.date.getMonth() === date.getMonth() &&
                work.date.getFullYear() === date.getFullYear()
            );
        });

        if (foundWork) {
            const tasksDetails = foundWork.tasks.map((task) => {
                return `${task.name}: ${task.details}`;
            });

            const details = tasksDetails.join('\n');
            setTooltipContent(details);
        }
    };

    const handleTileMouseLeave = () => {
        setHoveredDate(null);
        setTooltipContent('');
    };

    const handleTileClick = (date) => {
        const foundWork = deadlineWorks.find((work) => {
            return (
                work.date.getDate() === date.getDate() &&
                work.date.getMonth() === date.getMonth() &&
                work.date.getFullYear() === date.getFullYear()
            );
        });

        if (foundWork) {
            const tasksDetails = foundWork.tasks.join('\n');
            setModalContent(tasksDetails);
            setModalIsOpen(true);
        }
    };

    const closeModal = () => {
        setModalIsOpen(false);
        setModalContent('');
    };

    const getTileClassName = (date) => {
        if (hoveredDate && date.getDate() === hoveredDate.getDate() && date.getMonth() === hoveredDate.getMonth()) {
            return 'calendar-tile-hover';
        }
        return null;
    };

    return (
        <div className={mode === 'dark' ? 'dark' : ''}>
            <div className="WorkArea">
                <Sidebar
                    setUpView={setUpView}
                    view={view}
                />
                <div className="content">
                    <h1>Dashboard</h1>
                    <div className="overview">
                        <div className="chart">
                            <Card>
                                <Card.Content>
                                    <Card.Header>Task Completion</Card.Header>
                                </Card.Content>
                                <Card.Content>
                                    <BarChart width={400} height={300} data={data}>
                                        <CartesianGrid strokeDasharray="3 3" />
                                        <XAxis dataKey="name" />
                                        <YAxis />
                                        <Tooltip />
                                        <Legend />
                                        <Bar dataKey="completed" fill="#19a7ad" />
                                    </BarChart>
                                </Card.Content>
                            </Card>
                        </div>

                        <div className="Assigned-To-Me">
                            <h3>Assigned To Me</h3>
                            {deadlineWorks.map((item, index) => (
                                <div key={index}>
                                    <p>Date: {item.date.toLocaleDateString()}</p>
                                    <ul>
                                        {item.tasks.map(task => (
                                            <li key={task.id}>
                                                {task.name}
                                                {/* Add additional task details if needed */}
                                            </li>
                                        ))}
                                    </ul>
                                </div>
                            ))}
                        </div>

                        <div className="chart">
                            <Card>
                                <Card.Content>
                                    <Card.Header>Work Analysis</Card.Header>
                                </Card.Content>
                                <Card.Content>
                                    <PieChart width={400} height={300}>
                                        <Pie
                                            dataKey="value"
                                            isAnimationActive={false}
                                            data={pieChartData}
                                            cx="50%"
                                            cy="50%"
                                            outerRadius={80}
                                            fill="#19a7ad"
                                            label
                                        >
                                            {pieChartData.map((entry, index) => (
                                                <Cell key={`cell-${index}`} fill={index === 0 ? '#82ca9d' : '#8884d8'} />
                                            ))}
                                        </Pie>
                                        <Tooltip />
                                    </PieChart>
                                </Card.Content>
                            </Card>
                        </div>


                    </div>

                    <div className="tasks">
                        <Card>

                            <div className="deadline-works">
                                <h3>Deadline Works</h3>
                                <Calendar
                                    value={selectedDate}
                                    onChange={setSelectedDate}
                                    onClickDay={handleTileClick}
                                    onMouseEnter={handleTileMouseEnter}
                                    onMouseLeave={handleTileMouseLeave}
                                    tileClassName={getTileClassName}
                                    tileContent={({ date }) => {
                                        const foundWork = deadlineWorks.find((work) => {
                                            return (
                                                work.date.getDate() === date.getDate() &&
                                                work.date.getMonth() === date.getMonth() &&
                                                work.date.getFullYear() === date.getFullYear()
                                            );
                                        });

                                        if (foundWork) {
                                            const tasksDetails = foundWork.tasks.map((task) => {
                                                return `${task.name}: ${task.details}`;
                                            });

                                            const tooltipContent = tasksDetails.join('<br />');

                                            return (
                                                <div className="tooltip-wrapper">
                                                    <FontAwesomeIcon icon={faCheckCircle} className="has-deadline" />
                                                    <div className="tooltipEke" dangerouslySetInnerHTML={{ __html: tooltipContent }} />
                                                </div>
                                            );
                                        }

                                        return null;
                                    }}
                                />
                            </div>
                        </Card>


                    </div>

                </div>

                <Modal open={modalOpen} onClose={closeModal} size="small">
                    <Modal.Content>
                        <Modal.Description>
                            <pre>{modalContent}</pre>
                        </Modal.Description>
                    </Modal.Content>
                    <Modal.Actions>
                        <Button onClick={closeModal}>Close</Button>
                    </Modal.Actions>
                </Modal>
            </div>
        </div >
    );
};

export default Dashboard;