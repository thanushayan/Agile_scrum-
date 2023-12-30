import React, { useState, useEffect } from "react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { FaCalendarAlt, FaTimes } from "react-icons/fa";
import '../css/ProjectForm.css';
import axios from 'axios';
import config from '../config.json';
import { getItemFromLocalStorage } from './AuthStorageUtil';
import {
    Button,
    Typography
} from '@material-ui/core';

const EditProjectPopUp = ({ project, closeEdittogglePopup, showEditPopup }) => {
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [formValues, setFormValues] = useState({
        name: "",
        description: "",
        projectType: ""
    });
    const [errorMessage, setErrorMessage] = useState('');

    const handleStartDateChange = (date) => {
        setStartDate(date);
        setErrorMessage('');
    };

    const handleEndDateChange = (date) => {
        setEndDate(date);
        setErrorMessage('');
    };

    useEffect(() => {
        if (project) {
            const { name, description, projectType, startDate, endDate } = project;
            setFormValues({
                name,
                description,
                projectType
            });
            const reactStartDate = project.startDate ? new Date(project.startDate) : null;
            const reactEndDate = project.endDate ? new Date(project.endDate) : null;

            setStartDate(new Date(reactStartDate));
            setEndDate(new Date(reactEndDate));
        }
    }, [project]);

    const handleSubmit = (e) => {
        e.preventDefault();

        if (!e.target.name.value) {
            setErrorMessage('*Project should have a name*');
            return;
        }

        if (!e.target.description.value) {
            setErrorMessage('*Project should have a description*');
            return;
        }

        if (!e.target.projectType.value) {
            setErrorMessage('*Project should have a type*');
            return;
        }

        if (startDate || endDate) {
            if (endDate < startDate) {
                setErrorMessage('*End date should be after the start date*');
                return;
            }
        }

        const ServerURL = config.APIURL;
        const userID = getItemFromLocalStorage('username');
        const token = getItemFromLocalStorage('token');

        axios.put(`${ServerURL}projects/update/${userID}/${project.projectId}`, {
            name: e.target.name.value,
            description: e.target.description.value,
            projectType: e.target.projectType.value,
            startDate: e.target.startDate.value,
            endDate: e.target.endDate.value,
            projectManagerId: userID,
            creatorId: "67890",
        }, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
        })
            .then((response) => {
                if (response.status === 202) {
                    console.log('API call successful');
                    closeEdittogglePopup();
                } else {
                    console.log(response.status);
                }
            })
            .catch((error) => {
                console.log('API call error:', error);
            });
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormValues((prevValues) => ({
            ...prevValues,
            [name]: value
        }));
    };

    const handleKeyUp = (e) => {
        const element = e.target.value;
        const maxLength = e.target.maxLength;

        if (element.length === maxLength) {
            setErrorMessage(`*This field cannot exceed ${maxLength} characters*`);
        } else {
            setErrorMessage('');
        }
    };

    return (
        <div>
            {showEditPopup && (
                <div className="popup">
                    <div className="popup-content">
                        <div className='header-popup'>
                            <h2 className='header'>Edit Project</h2>
                            <FaTimes className="close-icon" onClick={closeEdittogglePopup} />
                        </div>
                        <form onSubmit={handleSubmit}>
                            <div className="input-data">
                                <div className="project-input-field">
                                    <input
                                        type="text"
                                        placeholder="Project Name"
                                        name="name"
                                        value={formValues.name}
                                        onChange={handleChange}
                                        onKeyUp={handleKeyUp}
                                        maxLength="25"
                                    />
                                    <div className="underline"></div>
                                </div>
                                <div className="project-input-field">
                                    <input
                                        type="text"
                                        placeholder="Project Description"
                                        name="description"
                                        value={formValues.description}
                                        onChange={handleChange}
                                        onKeyUp={handleKeyUp}
                                        maxLength="125"
                                    />
                                    <div className="underline"></div>
                                </div>
                                <div className="project-input-field">
                                    <input
                                        type="text"
                                        placeholder="Project Type"
                                        name="projectType"
                                        value={formValues.projectType}
                                        onChange={handleChange}
                                        onKeyUp={handleKeyUp}
                                        maxLength="25"
                                    />
                                    <div className="underline"></div>
                                </div>
                                <div className="date">
                                    <div className="start-date">
                                        <FaCalendarAlt
                                            className="calendar-icon"
                                            onClick={() => document.querySelector(".start-date input").click()}
                                        />
                                        <div className="date-container">
                                            <DatePicker
                                                className='datePick'
                                                selected={startDate}
                                                onChange={handleStartDateChange}
                                                placeholderText="Start Date"
                                                name="startDate"
                                                dateFormat="yyyy-MM-dd"
                                            />
                                        </div>
                                    </div>
                                    <div className="end-date">
                                        <FaCalendarAlt
                                            className="calendar-icon"
                                            onClick={() => document.querySelector(".end-date input").click()}
                                        />
                                        <div className="date-container">
                                            <DatePicker
                                                className='datePick'
                                                selected={endDate}
                                                onChange={handleEndDateChange}
                                                placeholderText="End Date"
                                                name="endDate"
                                                dateFormat="yyyy-MM-dd"
                                            />
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <br />
                            <div className="form-row submit-btn">
                                <div className="input-data">
                                    <div className="inner" />
                                    <Button type="submit" variant="contained" fullWidth>
                                        Save changes
                                    </Button>
                                </div>
                                <Typography>
                                    {errorMessage && <p className="form-error">{errorMessage}</p>}
                                </Typography>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default EditProjectPopUp;
