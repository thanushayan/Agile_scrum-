import React, { useState, useEffect, useContext } from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend } from 'recharts';
import Sidebar from './SideBar';
import '../css/PokerArea.css';
import Axios from 'axios';
import { DarkModeContext } from './DarkModeProvider';
import { getUserAccess, PermissionTypes } from './UserRoleUtill';

const PokerArea = ({ pokerIssue, view, setUpView, projectId, username, user }) => {
    const [votes, setVotes] = useState([]);
    const [voteEntity, setVoteEntity] = useState([]);
    const [selectedVote, setSelectedVote] = useState(null);
    const { mode, toggleMode } = useContext(DarkModeContext);


    const fetchData = (endpoint, setDataFunction) => {
        Axios.get(endpoint)
            .then((response) => {
                // Assuming the response data is an array
                setDataFunction(response.data);
            })
            .catch((error) => {
                console.error('Error fetching data:', error);
            });
    };

    useEffect(() => {
        // Initial data fetch
        fetchData(`http://localhost:8095/poker/listByProjectAndTask?projectId=${projectId}&taskId=${pokerIssue.id}`, setVotes);
        fetchData(`http://localhost:8095/poker/listEntitiesByProjectAndTask?projectId=${projectId}&taskId=${pokerIssue.id}`, setVoteEntity);

        // Set up interval for periodic data fetch
        const intervalId = setInterval(() => {
            fetchData(`http://localhost:8095/poker/listByProjectAndTask?projectId=${projectId}&taskId=${pokerIssue.id}`, setVotes);
            fetchData(`http://localhost:8095/poker/listEntitiesByProjectAndTask?projectId=${projectId}&taskId=${pokerIssue.id}`, setVoteEntity);
        }, 500); // 10 seconds interval

        // Clean up the interval when the component unmounts
        return () => clearInterval(intervalId);
    }, [projectId]);


    const handleVote = (value) => {
        // Check if the user has already voted for this project and task
        if (votes.some((vote) => vote.taskId === pokerIssue.id && vote.pokerValue === value)) {
            console.error('User has already voted for this project and task.');
            return;
        }

        // Create a vote object with the selected value
        const voteObject = {
            userId: username,
            projectId: projectId,
            taskId: pokerIssue.id,
            pokerValue: value,
        };

        // Send a POST request to your API endpoint
        Axios.post('http://localhost:8095/poker/create', voteObject) // Replace with your API endpoint
            .then((response) => {
                // Handle the response as needed
                console.log('Vote successfully recorded:', response.data);
            })
            .catch((error) => {
                console.error('Error recording vote:', error);
            });

        // Update the selected vote in the component state
        setSelectedVote(value);

        // Add the vote to the votes state
        //setVotes((prevVotes) => [...prevVotes, voteObject.pokerValue]);
    };

    const calculateAverage = () => {
        if (votes.length === 0) return 0;
        const sum = votes.reduce((acc, curr) => acc + curr);
        return sum / votes.length;
    };

    const countVotes = () => {
        const voteCounts = {};
        for (let vote of votes) {
            if (voteCounts[vote]) {
                voteCounts[vote]++;
            } else {
                voteCounts[vote] = 1;
            }
        }
        return voteCounts;
    };

    const voteCounts = countVotes();

    const data = Object.keys(voteCounts).map((key) => ({
        vote: key,
        count: voteCounts[key],
    }));

    const sortedVotes = voteEntity.slice().sort((a, b) => a.pokerValue - b.pokerValue);

    const [havePermit, sethavePermit] = useState(false);
    useEffect(() => {
        const checkHavePermit = async () => {
            const bool = user && await getUserAccess(projectId, user.userRole, PermissionTypes.VIEW_REPORTS);
            sethavePermit(bool);
        };

        checkHavePermit();
    }, [projectId, user]);

    return (
        <div className={mode === 'dark' ? 'dark' : ''}>
            <div className='WorkArea'><Sidebar
                setUpView={setUpView}
                view={view}
            />
                <div className="voting-system-container">
                    <h1></h1>
                    <div className="cards-container">
                        <Card value={0} onVote={handleVote} selected={selectedVote === 0} />
                        <Card value={1} onVote={handleVote} selected={selectedVote === 1} />
                        <Card value={2} onVote={handleVote} selected={selectedVote === 2} />
                        <Card value={3} onVote={handleVote} selected={selectedVote === 3} />
                        <Card value={5} onVote={handleVote} selected={selectedVote === 5} />
                        <Card value={8} onVote={handleVote} selected={selectedVote === 8} />
                        <Card value={13} onVote={handleVote} selected={selectedVote === 13} />
                        <Card value={20} onVote={handleVote} selected={selectedVote === 20} />
                        <Card value={40} onVote={handleVote} selected={selectedVote === 40} />
                    </div>
                    <div className="votes-summary of">
                        <h2>Votes Summary of {`${pokerIssue.title}`}</h2>
                        {user && havePermit && <table className="votes-table">
                            <thead>
                                <tr>
                                    <th>User ID</th>
                                    <th>Poker Value</th>
                                </tr>
                            </thead>
                            <tbody>
                                {sortedVotes.map((vote, index) => (
                                    <tr key={index}>
                                        <td>{vote.userId}</td>
                                        <td>{vote.pokerValue}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>}


                        <div className="average">
                            <h3>Average: {calculateAverage()}</h3>
                        </div>
                        <div className="chart">
                            <h3>Voting Distribution</h3>
                            <BarChart width={600} height={300} data={data}>
                                <CartesianGrid strokeDasharray="3 3" />
                                <XAxis dataKey="vote" />
                                <YAxis />
                                <Tooltip />
                                <Legend />
                                <Bar dataKey="count" fill="#19a7ad" />
                            </BarChart>
                        </div>
                    </div>
                </div>
            </div >
        </div>
    );
};

const Card = ({ value, onVote, selected }) => {
    const handleVote = () => {
        onVote(value);
    };

    return (
        <div
            className={`poker-card ${selected ? 'selected' : ''}`}
            onClick={handleVote}
        >
            <div className="value">{value}</div>
        </div>
    );
};

export default PokerArea;