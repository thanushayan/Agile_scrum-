import React, { useState } from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, PieChart, Pie, Cell, ScatterChart, Scatter, LineChart, Line, ComposedChart } from 'recharts';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { Card, List, Modal, Button } from 'semantic-ui-react';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import { useHistory } from 'react-router-dom';
import '../css/Reporting.css';
import Sidebar from './SideBar';

const Reporting = ({ view, setUpView, user }) => {
    // Sample data for charts
    const data = [
        { name: 'Task 1', completed: 80 },
        { name: 'Task 2', completed: 60 },
        { name: 'Task 3', completed: 40 },
        { name: 'Task 4', completed: 20 },
        { name: 'Task 5', completed: 10 },
    ];

    const pieChartData = [
        { name: 'Completed', value: 60 },
        { name: 'Pending', value: 40 },
    ];

    const scatterChartData = [
        { x: 1, y: 5 },
        { x: 2, y: 3 },
        { x: 3, y: 8 },
        { x: 4, y: 6 },
        { x: 5, y: 4 },
    ];

    const lineChartData = [
        { name: 'Page A', uv: 4000, pv: 2400, amt: 2400 },
        { name: 'Page B', uv: 3000, pv: 1398, amt: 2210 },
        { name: 'Page C', uv: 2000, pv: 9800, amt: 2290 },
        { name: 'Page D', uv: 2780, pv: 3908, amt: 2000 },
        { name: 'Page E', uv: 1890, pv: 4800, amt: 2181 },
        { name: 'Page F', uv: 2390, pv: 3800, amt: 2500 },
        { name: 'Page G', uv: 3490, pv: 4300, amt: 2100 },
    ];

    const comboChartData = [
        { name: 'Page A', uv: 4000, pv: 2400, amt: 2400 },
        { name: 'Page B', uv: 3000, pv: 1398, amt: 2210 },
        { name: 'Page C', uv: 2000, pv: 9800, amt: 2290 },
        { name: 'Page D', uv: 2780, pv: 3908, amt: 2000 },
        { name: 'Page E', uv: 1890, pv: 4800, amt: 2181 },
        { name: 'Page F', uv: 2390, pv: 3800, amt: 2500 },
        { name: 'Page G', uv: 3490, pv: 4300, amt: 2100 },
    ];

    // Function to show a notification
    const showNotification = () => {
        toast.success('New notification received!');
    };

    const history = useHistory();

    const [selectedDate, setSelectedDate] = useState(new Date());
    const [hoveredDate, setHoveredDate] = useState(null);
    const [tooltipContent, setTooltipContent] = useState('');
    const [modalOpen, setModalOpen] = useState(false);
    const [modalContent, setModalContent] = useState('');

    const deadlineWorks = [
        {
            date: new Date(2023, 6, 20),
            tasks: [
                { id: 1, name: 'Task 1', details: 'Lorem ipsum dolor sit amet', link: '/tasks/1' },
                { id: 2, name: 'Task 2', details: 'Consectetur adipiscing elit', link: '/tasks/2' },
            ],
        },
        // Add more deadline works here
    ];

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
            const tasksDetails = foundWork.tasks.map((task) => {
                return `${task.name}: ${task.details}`;
            });

            const details = tasksDetails.join('\n');
            setModalContent(details);
            setModalOpen(true);
        }
    };

    const getTileClassName = (date) => {
        if (hoveredDate && date.getDate() === hoveredDate.getDate() && date.getMonth() === hoveredDate.getMonth()) {
            return 'calendar-tile-hover';
        }
        return null;
    };

    const closeModal = () => {
        setModalOpen(false);
        setModalContent('');
    };

    return (
        <div className="dashboard-container">
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
                    <div className="assign-to-me">
                        <h3>Assign to Me</h3>
                        <List divided relaxed>
                            <List.Item>
                                <List.Content>
                                    <List.Header as="a">Task 1</List.Header>
                                    <List.Description>
                                        <a href="/tasks/1">View details</a>
                                    </List.Description>
                                </List.Content>
                            </List.Item>
                            <List.Item>
                                <List.Content>
                                    <List.Header as="a">Task 2</List.Header>
                                    <List.Description>
                                        <a href="/tasks/2">View details</a>
                                    </List.Description>
                                </List.Content>
                            </List.Item>
                        </List>
                    </div>

                    <div className="notifications">
                        <h3>Notifications</h3>
                        <button onClick={showNotification}>Show Notification</button>
                        <ToastContainer />
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

                    <div className="chart">
                        <Card>
                            <Card.Content>
                                <Card.Header>Line Chart</Card.Header>
                            </Card.Content>
                            <Card.Content>
                                <LineChart width={400} height={300} data={lineChartData}>
                                    <CartesianGrid strokeDasharray="3 3" />
                                    <XAxis dataKey="name" />
                                    <YAxis />
                                    <Tooltip contentStyle={{ backgroundColor: '#19a7ad', color: '#fff' }} />
                                    <Legend />
                                    <Line type="monotone" dataKey="uv" stroke="#19a7ad" />
                                    <Line type="monotone" dataKey="pv" stroke="#8884d8" />
                                </LineChart>
                            </Card.Content>
                        </Card>
                    </div>

                    <div className="chart">
                        <Card>
                            <Card.Content>
                                <Card.Header>Scatter Chart</Card.Header>
                            </Card.Content>
                            <Card.Content>
                                <ScatterChart width={400} height={300}>
                                    <CartesianGrid strokeDasharray="3 3" />
                                    <XAxis dataKey="x" />
                                    <YAxis dataKey="y" />
                                    <Tooltip cursor={{ strokeDasharray: '3 3' }} />
                                    <Legend />
                                    <Scatter data={scatterChartData} fill="#19a7ad" />
                                </ScatterChart>
                            </Card.Content>
                        </Card>
                    </div>


                    <div className="chart">
                        <Card>
                            <Card.Content>
                                <Card.Header>Combo Chart</Card.Header>
                            </Card.Content>
                            <Card.Content>
                                <ComposedChart width={400} height={300} data={comboChartData}>
                                    <CartesianGrid strokeDasharray="3 3" />
                                    <XAxis dataKey="name" />
                                    <YAxis />
                                    <Tooltip contentStyle={{ backgroundColor: '#19a7ad', color: '#fff' }} />
                                    <Legend />
                                    <Line type="monotone" dataKey="uv" stroke="#19a7ad" />
                                    <Bar dataKey="pv" barSize={20} fill="#8884d8" />
                                    <Scatter dataKey="amt" fill="#82ca9d" />
                                </ComposedChart>
                            </Card.Content>
                        </Card>
                    </div>
                </div>

                <div className="tasks">
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
                                    return (
                                        <div className="tooltip">
                                            {foundWork.tasks.map((task) => (
                                                <div key={task.id}>{task.name}</div>
                                            ))}
                                        </div>
                                    );
                                }
                            }}
                        />
                    </div>

                    <div className="links">
                        <h3>Frequently Accessed Links</h3>
                        <ul>
                            <li>Link 1</li>
                            <li>Link 2</li>
                            <li>Link 3</li>
                        </ul>
                    </div>
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
    );
};

export default Reporting;