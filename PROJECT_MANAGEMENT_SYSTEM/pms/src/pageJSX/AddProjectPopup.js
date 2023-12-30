import React, { useState } from 'react';
import axios from 'axios';
import '../css/ProjectForm.css';
import config from '../config.json';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { getItemFromLocalStorage } from './AuthStorageUtil';
import { FaCalendarAlt, FaTimes } from 'react-icons/fa';
import {
  Button,
  Typography
} from '@material-ui/core';
import { ToastContainer, toast } from 'react-toastify';

const AddProjectPopup = ({ isOpen, closeAddProjecttogglePopup }) => {
  const [startDate, setStartDate] = useState(null);
  const [endDate, setEndDate] = useState(null);
  const [errorMessage, setErrorMessage] = useState('');

  const handleStartDateChange = (date) => {
    setStartDate(date);
    setErrorMessage('');
  };

  const handleEndDateChange = (date) => {
    setEndDate(date);
    setErrorMessage('');
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

    if (!e.target.type.value) {
      setErrorMessage('*Project should have a type*');
      return;
    }

    if (startDate || endDate) {
      if (endDate < startDate) {
        setErrorMessage('*End date should be after the start date*');
        return;
      }
    }
    const token = getItemFromLocalStorage('token');
    const ServerURL = config.APIURL;

    const username = getItemFromLocalStorage('username');
    // Perform any form data processing or validation here
    // Make an API call to your backend
    axios.post(`${ServerURL}projects/create`, {
      name: e.target.name.value,
      description: e.target.description.value,
      projectType: e.target.type.value,
      startDate: e.target.startDate.value,
      endDate: e.target.endDate.value,
      projectManagerId: username,
      creatorId: username,
      adminId: username,
    }, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`, // Add the authorization token here
      },
    })
      .then((response) => {
        // Handle the response from the backend
        if (response.status === 201) {
          // API call successful, handle success case
          console.log('API call successful');
          toast.success('Project Created Sucessfully')
          closeAddProjecttogglePopup();
        } else {
          // API call failed, handle error case
          console.log('API call failed');
        }
      })
      .catch((error) => {
        // Handle any errors during the API call
        console.log('API call error:', error);
      });

  };

  return (
    <div>
      {isOpen && (
        <div className="popup">
          <div className="popup-content">
            <div className="header-popup">
              <h2 className="header">New Project</h2>
              <FaTimes className="close-icon" onClick={closeAddProjecttogglePopup} />
            </div>
            <form onSubmit={handleSubmit}>
              <div className="input-data">
                <div className="project-input-field">
                  <input type="text" placeholder="Project Name" name="name" maxLength="25" onKeyUp={handleKeyUp} />
                  <div className="underline" />
                </div>
                <div className="project-input-field">
                  <input type="text" placeholder="Project Description" name="description" maxLength="125" onKeyUp={handleKeyUp} />
                  <div className="underline" />
                </div>
                <div className="project-input-field">
                  <input type="text" placeholder="Project Type" name="type" maxLength="25" onKeyUp={handleKeyUp} />
                  <div className="underline" />
                </div>

                <div className="date">
                  <div className="start-date">
                    <FaCalendarAlt
                      className="calendar-icon"
                      onClick={() => document.querySelector('.start-date input').click()}
                    />
                    <div className="date-container">
                      <DatePicker
                        className="datePick"
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
                      onClick={() => document.querySelector('.end-date input').click()}
                    />
                    <div className="date-container">
                      <DatePicker
                        className="datePick"
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
                    Add Project
                  </Button>
                </div>
              </div>

              <Typography>
                {errorMessage && <p className="form-error">{errorMessage}</p>}
              </Typography>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default AddProjectPopup;

