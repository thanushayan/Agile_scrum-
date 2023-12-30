import React, { useState, useEffect } from 'react';
import axios from 'axios';
import GitHubLogin from 'react-github-login';

const GitHubOAuth = () => {
    const [accessToken, setAccessToken] = useState('');
    const [repoName, setRepoName] = useState('');
    const [repos, setRepos] = useState([]);

    const fetchRepos = async (token) => {
        try {
            const response = await axios.get('https://api.github.com/user/repos', {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            });
            setRepos(response.data || []);
        } catch (error) {
            console.error('Error fetching repositories:', error);
        }
    };

    useEffect(() => {
        if (accessToken) {
            fetchRepos(accessToken);
        }
    }, [accessToken]);

    const createRepository = async () => {
        try {
            const response = await axios.post(
                'https://api.github.com/user/repos',
                { name: repoName },
                {
                    headers: {
                        Authorization: `Bearer ${accessToken}`,
                        'Content-Type': 'application/json',
                    },
                }
            );

            if (response.status === 201) {
                console.log('Repository created successfully!');
                // Refresh repository list after creating a new repository
                fetchRepos();
                setRepoName('');
                setAccessToken('');
            } else {
                console.error('Failed to create repository');
            }
        } catch (error) {
            console.error('Error creating repository:', error);
        }
    };

    const handleGitHubLoginSuccess = (response) => {
        const token = "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"; // or response.access_token depending on the library used
        setAccessToken(token);
    };

    const handleGitHubLoginFailure = (response) => {
        console.error('GitHub login failed:', response);
    };

    const handleRepoNameChange = (e) => {
        setRepoName(e.target.value);
    };

    return (
        <div>
            <input
                type="text"
                placeholder="Repository Name"
                value={repoName}
                onChange={handleRepoNameChange}
            />
            <button onClick={createRepository}>Create Repository</button>

            <ul>
                {repos.map((repo) => (
                    <li key={repo.id}>{repo.name}</li>
                ))}
            </ul>

            <GitHubLogin
                clientId="3a0c69c6b61c1618b05a"
                redirectUri="http://localhost:3000/"
                onSuccess={handleGitHubLoginSuccess}
                onFailure={handleGitHubLoginFailure}
                buttonText="Login with GitHub"
            />
        </div>
    );
};

export default GitHubOAuth;
