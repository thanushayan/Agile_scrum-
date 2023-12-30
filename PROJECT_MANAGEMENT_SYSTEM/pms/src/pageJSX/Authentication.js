import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { setItemInLocalStorage } from './AuthStorageUtil';
import { useHistory } from 'react-router-dom';
import '../css/Auth.css';
import {
    Container,
    Typography,
    TextField,
    Button,
    Grid,
    Link,
    Card,
    CardContent,
    CardHeader,
} from '@material-ui/core';
import useReloadOnRouteChange from './useReloadOnRouteChange';
import { fetchUsername } from './fetchUserName';

const quotes = [
    "Planning is bringing the future into the present so that you can do something about it now. - Alan Lakein",
    "A goal without a plan is just a wish. - Antoine de Saint-Exupéry",
    "It does not matter how slowly you go as long as you do not stop. - Confucius",
    "Quality is never an accident. It is always the result of intelligent effort. - John Ruskin",
    "Time is the scarcest resource, and unless it is managed, nothing else can be managed. - Peter Drucker",
    "The best way to predict the future is to create it. - Peter Drucker",
    "You can't control the wind, but you can adjust your sails. - Yiddish Proverb",
    "Communication works for those who work at it. - John Powell",
    "Scope, time, budget, pick any two. - Unknown",
    "Risk comes from not knowing what you're doing. - Warren Buffett",
    "The key to successful leadership today is influence, not authority. - Ken Blanchard",
    "The project you are most resisting carries your greatest growth. - Robin Sharma",
    "The best project you'll ever work on is you. - Unknown",
    "The bitterness of poor quality remains long after the sweetness of meeting the schedule has been forgotten. - Aldo Gucci",
    "Projects are complete when they start working for you, rather than you working for them. - Scott Allen"
];

const Authentication = () => {
    const [isLoginForm, setIsLoginForm] = useState(true);
    const [shouldRenderForm, setShouldRenderForm] = useState(false);
    const [quoteIndex, setQuoteIndex] = useState(0);
    const history = useHistory();
    const [errorMessage, setErrorMessage] = useState('');

    const handleLoginSubmit = async (e) => {
        e.preventDefault();
        const formData = {
            username: e.target.name.value,
            password: e.target.password.value,
        };

        if (!formData.username || !formData.password) {
            setErrorMessage('Please fill in all fields.');
            return;
        }
        setErrorMessage('');

        formData.username = formData.username.toLowerCase();

        try {
            const response = await axios.post('http://localhost:9898/auth/login', formData);
            if (response.status === 200) {
                console.log('API call successful');
                setItemInLocalStorage('token', response.data, 180);
                fetchUsername();
                history.push('/home');
            } else {
                console.log('API call failed');
            }
        } catch (error) {
            console.log('API call error:', error);
            if (error.response && error.response.data) {
                setErrorMessage(error.response.data);
            } else {
                setErrorMessage('An error occurred during the API call.');
            }
        }
    };

    const handleRegisterSubmit = async (e) => {
        e.preventDefault();
        const formData = {
            fname: e.target.fname.value,
            lname: e.target.lname.value,
            name: e.target.name.value,
            email: e.target.email.value,
            password: e.target.password.value,
        };

        if (!formData.name || !formData.email || !formData.password || !formData.fname || !formData.lname) {
            setErrorMessage('Please fill in all fields.');
            return;
        }

        formData.name = formData.name.toLowerCase();

        const confirmPassword = e.target.passwordre.value;
        if (formData.password !== confirmPassword) {
            setErrorMessage('Passwords do not match.');
            return;
        }

        const passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%?&])[A-Za-z\d@$!%?&]{8,}$/;

        if (!passwordPattern.test(formData.password)) {
            setErrorMessage('Password must contain at least 8 characters, one uppercase letter, one lowercase letter, one digit, and one special symbol.');
            return;
        }


        setErrorMessage('');
        try {
            const response = await axios.post('http://localhost:9898/auth/register', formData);
            if (response.status === 201) {
                console.log('API call successful');
                handleLoginSubmit(e);
            } else {
                console.log('API call failed');
            }
        } catch (error) {
            console.log('API call error:', error);
            if (error.response && error.response.data) {
                setErrorMessage(error.response.data);
            } else {
                setErrorMessage('An error occurred during the API call.');
            }
        }
    };

    const handleSubmit = (e) => {
        if (isLoginForm) {
            handleLoginSubmit(e);
        } else {
            handleRegisterSubmit(e);
        }
    };

    const toggleForm = () => {
        const nameInput = document.querySelector('input[name="name"]');
        const passwordInput = document.querySelector('input[name="password"]');

        if (nameInput && passwordInput) {
            nameInput.value = '';
            passwordInput.value = '';
        }
        setErrorMessage('');
        setIsLoginForm(!isLoginForm);
    };

    useEffect(() => {
        const timer = setTimeout(() => {
            setShouldRenderForm(true);
        }, 500);

        const quoteInterval = setInterval(() => {
            setQuoteIndex((quoteIndex + 1) % quotes.length);
        }, 10000);

        return () => {
            clearTimeout(timer);
            clearInterval(quoteInterval);
        };
    }, [isLoginForm, quoteIndex]);

    useReloadOnRouteChange(history);

    return (
        <div className='authPage'>
            <div className='bg'>
                <div className='quote-background'></div>
                <Typography className="quote-text">{quotes[quoteIndex]}</Typography>
            </div>
            <div className='formArea'>
                <Container maxWidth="sm" className="cardContainer">
                    <Card className={`cardAuth ${isLoginForm ? 'login' : 'register'}`}>
                        {shouldRenderForm && (
                            <CardContent className="cardContent">
                                <div className={`flip-container ${isLoginForm ? '' : 'flip'}`}>
                                    <div className={`${isLoginForm ? 'front' : 'back'}`}>
                                        <CardHeader className="cardHeader" title={isLoginForm ? 'Login' : 'Register'} />
                                        <form onSubmit={handleSubmit}>
                                            <Grid container spacing={2}>
                                                {!isLoginForm && (
                                                    <Grid item xs={6}>
                                                        <TextField
                                                            label="First Name"
                                                            name="fname"
                                                            type="text"
                                                            variant="outlined"
                                                            fullWidth
                                                        />
                                                    </Grid>
                                                )}
                                                {!isLoginForm && (
                                                    <Grid item xs={6}>
                                                        <TextField
                                                            label="Last Name"
                                                            name="lname"
                                                            type="text"
                                                            variant="outlined"
                                                            fullWidth
                                                        />
                                                    </Grid>
                                                )}
                                                <Grid item xs={12}>
                                                    <TextField
                                                        label="User name"
                                                        name="name"
                                                        type="text"
                                                        variant="outlined"
                                                        fullWidth
                                                        className="focused-input"
                                                    />
                                                </Grid>
                                                {!isLoginForm && (
                                                    <Grid item xs={12}>
                                                        <TextField
                                                            label="Email Address"
                                                            name="email"
                                                            type="email"
                                                            variant="outlined"
                                                            fullWidth
                                                        />
                                                    </Grid>
                                                )}
                                                <Grid item xs={12}>
                                                    <TextField
                                                        label="Password"
                                                        name="password"
                                                        type="password"
                                                        variant="outlined"
                                                        fullWidth
                                                    />
                                                </Grid>
                                                {!isLoginForm && (
                                                    <Grid item xs={12}>
                                                        <TextField
                                                            label="Confirm Password"
                                                            type="password"
                                                            name="passwordre"
                                                            variant="outlined"
                                                            fullWidth
                                                        />
                                                    </Grid>
                                                )}
                                                <Grid item xs={12}>
                                                    <Button
                                                        type="submit"
                                                        variant="contained"
                                                        fullWidth
                                                        style={{ backgroundColor: 'darkcyan', color: 'white' }}
                                                    >
                                                        {isLoginForm ? 'Login' : 'Register'}
                                                    </Button>
                                                </Grid>
                                                <Grid item xs={12}>
                                                    <Typography align="center">
                                                        {isLoginForm ? "Don't have an account? " : 'Already have an account? '}
                                                        <Link href="#" onClick={toggleForm} className="link">
                                                            {isLoginForm ? 'Register' : 'Login'}
                                                        </Link>
                                                    </Typography>
                                                </Grid>
                                                <Grid item xs={12}>
                                                    <Typography align="center" className='error-message' color="error">
                                                        {errorMessage}
                                                    </Typography>
                                                </Grid>
                                            </Grid>
                                        </form>
                                    </div>
                                </div>
                            </CardContent>
                        )}
                    </Card>
                </Container>
            </div>
        </div>
    );
};

export default Authentication;