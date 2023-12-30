import React, { useState, useEffect, useContext } from 'react';
import axios from 'axios';
import { useHistory } from 'react-router-dom';
import useReloadOnRouteChange from './useReloadOnRouteChange';
import ConfirmationPopup from './DeleteProject';
import ProfileSlider from './ProfileSlider';
import EditProjectPopUp from './EditProjectPopUp';
import AddProjectPopup from './AddProjectPopup';
import projectimage from '../media/projectimage.png';
import { DarkModeContext } from './DarkModeProvider';
import { getItemFromLocalStorage, clearTokenAndUserName } from './AuthStorageUtil';
import config from '../config.json';
import '../css/Mode.css';
import '../css/ProjectWorkSpace.css';
import TopBar from './TopBar';
import { fetchUsername } from './fetchUserName';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faFolderPlus, faSpinner, faTrashAlt, faFileEdit, faEye } from '@fortawesome/free-solid-svg-icons';
import getUserRole from './UserRoleUtill';
import { getUserAccess, PermissionTypes } from './UserRoleUtill';



const HomeWorkSpace = () => {

  const token = getItemFromLocalStorage('token');
  const ServerURL = config.APIURL;
  const username = getItemFromLocalStorage('username')

  const [projects, setProjects] = useState([]);
  const [isOpenDelConfirm, setIsOpenDelConfirm] = useState(false);
  const [showEditPopup, setshowEditPopup] = useState(false);
  const history = useHistory();
  const [selectedProjectId, setSelectedProjectId] = useState(null);
  const [selectedProject, setSelectedProject] = useState(null);
  const [isOpen, setIsOpen] = useState(false);
  const [isSidebarVisible, setIsSidebarVisible] = useState(true);
  const { mode, toggleMode } = useContext(DarkModeContext);
  const [SliderisOpen, setSliderIsOpen] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [RequestList, setRequestPendingList] = useState([]);

  const [viewType, setViewType] = useState(() => {
    const savedViewType = localStorage.getItem('viewType');
    return savedViewType ? savedViewType : 'card';
  });
  const [selectedColumns, setSelectedColumns] = useState(() => {
    const storedColumns = localStorage.getItem('selectedColumns');
    return storedColumns ? JSON.parse(storedColumns) : {
      name: true,
      type: true,
      description: true,
      startDate: true,
      endDate: true,
    };
  });

  const fetchRequestPendingByUsername = async () => {
    try {
      const response = await axios.get(`${ServerURL}request-pending/by-username/${username}`, {
        headers: {
          Authorization: token,
        },
      });
      if (Array.isArray(response.data)) {
        setRequestPendingList(response.data); // Make sure this is being called correctly

      } else {
        console.log('Invalid response format: Expected an array of RequestPending_Entity');
      }
    } catch (error) {
      handleRequestPendingFetchError(error);
    }
  };


  const handleRequestPendingFetchError = (error) => {
    if (error.response) {
      console.log('Error fetching request pending by username:', error.response.data);
      console.log('Status code:', error.response.status);
    } else if (error.request) {
      console.log('Error fetching request pending by username: No response received');
    } else {
      console.log('Error fetching request pending by username:', error.message);
    }
  };

  useEffect(() => {
    fetchRequestPendingByUsername();
  }, [RequestList]);

  const addMember = (request) => {
    const requestBody = {
      projectId: request.projectId,
      userId: username,
      userRole: 'Member',
    };

    axios
      .post(`${ServerURL}api/project-and-user/${request.requesterName}/add`, requestBody, {
        headers: {
          'Content-Type': 'application/json',
          Authorization: token,
        },
      })
      .then((response) => {
        if (response.status === 201) {
          // Member added successfully
          console.log('Member added successfully');
          deleteRequestPending(request.projectId, request.userName)
        } else {
          // Error adding member
          console.log('Error adding member');
          setError(response.data); // Set the custom error message received from the backend
          // Clear the error message after 30 seconds
          setTimeout(() => {
            setError('');
          }, 30000);
        }
      })
      .catch((error) => {
        console.log('API call error:', error);
        setError(error.response.data);
        // Clear the error message after 30 seconds
        setTimeout(() => {
          setError('');
        }, 30000);
      });
  }

  const deleteRequestPending = async (projectId, userId) => {
    try {
      const token = getItemFromLocalStorage('token'); // Fetch the token
      const response = await axios.delete(
        `${ServerURL}request-pending/delete/${projectId}/${userId}`,
        {
          headers: {
            Authorization: token, // Include the token in the request headers
          },
        }
      );
      if (response.status === 200) {
        console.log('RequestPending deleted successfully');
        // Perform any other actions after successful deletion
      }
      // Handle other status codes if needed
    } catch (error) {
      console.error('Failed to delete RequestPending:', error);
      // Handle the error scenario
    }
  };



  const handleProjectView = async () => {
    try {
      const response = await axios.get(`${ServerURL}projects/users/${username}/view-all`, {
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
      });
      if (response.status === 200) {
        const data = response.data;
        if (data.length === 0) {
          // Handle the case when data is empty
          console.log('No projects found');
          // Perform any necessary action or show a message to the user
        } else {
          // Fetch user roles for all projects
          const userRolePromises = data.map((project) =>
            fetchUserRole(project.projectId)
          );
          const user = await Promise.all(userRolePromises);

          // Update project data with user roles
          const projectsWithUserRole = data.map((project, index) => ({
            ...project,
            user: user[index],
          }));

          setProjects(projectsWithUserRole);
        }
      }
      setIsLoading(false);
    } catch (error) {
      if (error.response && error.response.status === 500) {
        clearTokenAndUserName();
      } else if (error.response && error.response.status === 404) {
        // Handle 404 Not Found error
      } else if (error.response && error.response.status === 503) {
        // Handle 503 Service Unavailable error
      } else {
        // Handle other errors during the request
        console.log('Request error:', error);
      }
      setIsLoading(false);
    }
  };

  const fetchUserRole = async (projectId) => {
    const user = await getUserRole({ projectId });
    return user;
  };

  //save table views in browser
  localStorage.setItem('selectedColumns', JSON.stringify(selectedColumns));

  //save view type in browser
  useEffect(() => {
    localStorage.setItem('viewType', viewType);
  }, [viewType]);

  useEffect(() => {
    if (getItemFromLocalStorage('username') === null) {
      fetchUsername();
    }
  }, [fetchUsername]);

  const [error, setError] = useState(false);
  useEffect(() => {
    let timeout;
    const hideSidebar = () => {
      setIsSidebarVisible(false);
    };

    const showSidebar = () => {
      setIsSidebarVisible(true);
      clearTimeout(timeout);
      timeout = setTimeout(hideSidebar, 3000); // Auto-hide after 3 seconds of inactivity
    };

    const handleMouseMove = () => {
      showSidebar();
    };

    const handleMouseLeave = () => {
      clearTimeout(timeout);
      hideSidebar();
    };

    const handleTouchStart = () => {
      showSidebar();
    };
    if (typeof document !== 'undefined') {
      document.addEventListener('mousemove', handleMouseMove);
      document.addEventListener('mouseleave', handleMouseLeave);
      document.addEventListener('touchstart', handleTouchStart);
    }
    return () => {
      if (typeof document !== 'undefined') {
        document.removeEventListener('mousemove', handleMouseMove);
        document.removeEventListener('mouseleave', handleMouseLeave);
        document.removeEventListener('touchstart', handleTouchStart);
      }
      clearTimeout(timeout);
    };
  }, []);

  const fetchProjects = () => {
    handleProjectView();
  };

  useEffect(() => {
    const intervalId = setInterval(fetchProjects, 500);
    return () => {
      clearInterval(intervalId);
    };
  }, []);


  useEffect(() => {
    const tablebody = document.querySelector('.table-body');
    const tableHeader = document.querySelector('.table-header');

    const handleScroll = () => {
      const { top } = tablebody.getBoundingClientRect();
      tableHeader.style.position = top <= 0 ? 'fixed' : 'static';
    };

    if (tablebody) {
      tablebody.addEventListener('scroll', handleScroll);
    }

    return () => {
      if (tablebody) {
        tablebody.removeEventListener('scroll', handleScroll);
      }
    };
  }, []);



  useReloadOnRouteChange(history);

  //fetch project every 0.5s
  useEffect(() => {
    const interval = setInterval(() => {
      fetchProjects();
    }, 500);

    return () => clearInterval(interval);
  }, [fetchProjects])

  //error hiding automatic
  useEffect(() => {
    let timer;
    if (error) {
      timer = setTimeout(() => {
        setError(false);
      }, 10000);
    }

    return () => {
      clearTimeout(timer);
    };
  }, [error]);



  // const handleProjectView = () => {
  //   console.log(token + " " + username);
  //   axios.get(`${ServerURL}projects/users/${username}/view-all`, {
  //     headers: {
  //       'Content-Type': 'application/json',
  //       'Authorization': `Bearer ${token}`
  //     }
  //   })
  //     .then((response) => {
  //       if (response.status === 200) {
  //         const data = response.data;
  //         if (data.length === 0) {
  //           // Handle the case when data is empty
  //           console.log('No projects found');
  //           // Perform any necessary action or show a message to the user
  //         } else {
  //           setProjects(data);
  //         }
  //       }
  //       setIsLoading(false);
  //     })
  //     .catch((error) => {
  //       if (error.response && error.response.status === 500) {
  //         clearTokenAndUserName();
  //       } else if (error.response && error.response.status === 404) {
  //         // Handle 404 Not Found error
  //       } else if (error.response && error.response.status === 503) {
  //         // Handle 503 Service Unavailable error
  //       } else {
  //         // Handle other errors during the request
  //         console.log('Request error:', error);
  //       }
  //       setIsLoading(false);
  //     });
  // };


  //table customize
  const handleColumnToggle = (columnName) => {
    if (columnName === 'name' && !selectedColumns.description) {
      setError(true);
      return;
    }

    if (columnName === 'description' && !selectedColumns.name) {
      setError(true);
      return;
    }
    setError(false);
    const updatedColumns = {
      ...selectedColumns,
      [columnName]: !selectedColumns[columnName],
    };

    setSelectedColumns(updatedColumns);
    localStorage.setItem('selectedColumns', JSON.stringify(updatedColumns));
  };

  //open pop ups
  const openDelConfirmPopup = (projectId) => {
    setSelectedProjectId(projectId);
    setIsOpenDelConfirm(true);
  };

  const closeDelConfirmPopup = () => {
    setSelectedProjectId(null);
    setIsOpenDelConfirm(false);
  };

  const OpenEdittogglePopup = (project) => {
    setSelectedProject(project);
    setshowEditPopup(true);
    setSliderIsOpen(false);
  };

  const closeEdittogglePopup = (project) => {
    setSelectedProject(project);
    setshowEditPopup(false);
  };

  const OpenAddProjecttogglePopup = () => {
    setIsOpen(true);
    setSliderIsOpen(false);
  };

  const closeAddProjecttogglePopup = () => {
    setIsOpen(false);
  };

  const toggleSlider = () => {
    setSliderIsOpen(!SliderisOpen);
  };

  //redirect
  const openProject = (projectId) => {
    history.push(`/project/${projectId}`)
    console.log("hi" + projectId);
  };

  const LogOut = () => {
    clearTokenAndUserName();
    history.push('/auth');
  }



  const checkHavePermit = async (project) => {
    try {
      const permission = await getUserAccess(
        project.projectId,
        project.user.userRole,
        PermissionTypes.PROJECT_MANAGEMENT
      );
      console.log("I am")

      return permission;
    } catch (error) {
      console.error('Error checking permission:', error);
      return false; // or handle error cases appropriately
    }
  };



  return (
    <div className={mode === 'dark' ? 'dark' : ''}>
      <div className='workspace'>
        <TopBar
          mode={mode}
          toggleMode={toggleMode}
          viewType={viewType}
          setViewType={setViewType}
          toggleSlider={toggleSlider}
        />
        {isSidebarVisible && (
          <div className={`side-bar ${isSidebarVisible ? 'sidebar-visible' : ''}`}>
            <button onClick={OpenAddProjecttogglePopup} className="ButtonWithIcon" id='AddProject'>
              <FontAwesomeIcon icon={faFolderPlus} className="Icon" />
            </button>
          </div>
        )}
        {isLoading ? (
          <div className='loading-indicator'>
            <p><FontAwesomeIcon icon={faSpinner} className='fa-duotone fa-spin-pulse' style={{ '--fa-primary-color': '#1da5d3', '--fa-secondary-color': '#b1d5ec' }} />  Loading projects...</p>
          </div>
        ) :
          viewType === 'card' ? (
            <div className="grid-container">
              {projects.map((project) => (

                <div key={project.projectId} className="Project-container" >
                  <div className="Project-card" key={project.id} >
                    <div className="image-container" onClick={() => openProject(project.projectId)}>
                      <img src={projectimage} alt={project.name} className="Project-image" />
                    </div>
                    <div className="Project-details">
                      <div className="Project-header" onClick={() => openProject(project.projectId)}>
                        <p className="Project-name">{project.name}</p>
                        <p className="Project-type">{project.projectType}</p>
                      </div>
                      <div className="Project-content" >
                        {project.startDate && (<div className="Project-dates">
                          <p className="Date-label" id='StartDate'>
                            Start Date: <span className="Start-date">{project.startDate}</span>
                          </p>
                          <p className="Date-label" id='EndDate'>
                            End Date: <span className="End-date">{project.endDate}</span>
                          </p>
                        </div>)}

                        {() => checkHavePermit(project) && (
                          <div className='buttons'>
                            <div className='actionsBtn'>
                              <div className='deleteBtn'
                                onClick={() => openDelConfirmPopup(project.projectId)}
                              >
                                <FontAwesomeIcon icon={faTrashAlt} id='deleteBtn' />
                              </div>
                              <div className='editBtn'
                                onClick={() => OpenEdittogglePopup(project)}
                              >
                                <FontAwesomeIcon icon={faFileEdit} id='editBtn' />
                              </div>
                            </div>
                          </div>)}
                      </div>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          ) : viewType === 'table' ? (
            <div className='table-view'>
              {error && <p className='error-message'>Please select at least one option between Project Name and Description.</p>}

              <div className='container'>
                <div className='table-container'>
                  <table>
                    <thead className="table-header">
                      <tr>
                        {selectedColumns.name && (
                          <th>
                            <input
                              type='checkbox'
                              checked={selectedColumns.name}
                              onChange={() => handleColumnToggle('name')}
                            />
                            Name
                          </th>
                        )}
                        {selectedColumns.type && (
                          <th>
                            <input
                              type='checkbox'
                              checked={selectedColumns.type}
                              onChange={() => handleColumnToggle('type')}
                            />
                            Type
                          </th>
                        )}
                        {selectedColumns.description && (
                          <th>
                            <input
                              type='checkbox'
                              checked={selectedColumns.description}
                              onChange={() => handleColumnToggle('description')}
                            />
                            Description
                          </th>
                        )}
                        {selectedColumns.startDate && (
                          <th>
                            <input
                              type='checkbox'
                              checked={selectedColumns.startDate}
                              onChange={() => handleColumnToggle('startDate')}
                            />
                            Start Date
                          </th>
                        )}
                        {selectedColumns.endDate && (
                          <th>
                            <input
                              type='checkbox'
                              checked={selectedColumns.endDate}
                              onChange={() => handleColumnToggle('endDate')}
                            />
                            End Date
                          </th>
                        )}
                        <th>Actions</th>
                      </tr>
                    </thead>
                    <tbody className='table-body'>
                      {projects.map((project) => (
                        <tr key={project.id} >
                          {selectedColumns.name && <td>{project.name}</td>}
                          {selectedColumns.type && <td>{project.projectType}</td>}
                          {selectedColumns.description && <td>{project.description}</td>}
                          {selectedColumns.startDate && (
                            <td id='StartDate'>{project.startDate}</td>
                          )}
                          {selectedColumns.endDate && <td id='EndDate'>{project.endDate}</td>}
                          <td>
                            <div className='actionsBtn'>
                              <div className='deleteBtn'
                                onClick={() => openDelConfirmPopup(project.projectId)}
                              >
                                <FontAwesomeIcon icon={faTrashAlt} id='deleteBtn' />
                              </div>
                              <div className='editBtn'
                                onClick={() => OpenEdittogglePopup(project)}
                              >
                                <FontAwesomeIcon icon={faFileEdit} id='editBtn' />
                              </div>
                              <div className='viewBtn'
                                onClick={() => openProject(project.projectId)}
                              >
                                <FontAwesomeIcon icon={faEye} id='viewBtn' />
                              </div>
                            </div>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
                <div className='checkBoxes'>
                  <label className='checkbox-label'>
                    <input
                      type='checkbox'
                      className='checkbox-input'
                      checked={selectedColumns.name}
                      onChange={() => handleColumnToggle('name')}
                    />
                    Project Name
                  </label>

                  <label className='checkbox-label'>
                    <input
                      type='checkbox'
                      className='checkbox-input'
                      checked={selectedColumns.type}
                      onChange={() => handleColumnToggle('type')}
                    />
                    Project Type
                  </label>

                  <label className='checkbox-label'>
                    <input
                      type='checkbox'
                      className='checkbox-input'
                      checked={selectedColumns.description}
                      onChange={() => handleColumnToggle('description')}
                    />
                    Description
                  </label>

                  <label className='checkbox-label'>
                    <input
                      type='checkbox'
                      className='checkbox-input'
                      checked={selectedColumns.startDate}
                      onChange={() => handleColumnToggle('startDate')}
                    />
                    Start Date
                  </label>

                  <label className='checkbox-label'>
                    <input
                      type='checkbox'
                      className='checkbox-input'
                      checked={selectedColumns.endDate}
                      onChange={() => handleColumnToggle('endDate')}
                    />
                    End Date
                  </label>

                </div>

              </div>
            </div>
          ) :
            (<div></div>)}

        {RequestList.length > 0 && (
          <div className="requests-container">
            <h2>Project Join Requests</h2>
            {RequestList.map((request, index) => (
              <div className="request-item" key={index}>
                <p>
                  {`You have a request to join a project by ${request.requesterName}`}
                </p>
                <div className="request-actions">
                  <button className="accept" onClick={() => addMember(request)}>Accept</button>
                  <button className="decline" onClick={() => deleteRequestPending(request.projectId, request.userName)}>Decline</button>
                </div>

              </div>
            ))}
          </div>
        )}


        {
          isOpenDelConfirm && (
            <ConfirmationPopup
              projectId={selectedProjectId}
              closePopup={closeDelConfirmPopup}
            />
          )
        }
        {
          showEditPopup && (
            <EditProjectPopUp
              projectId={selectedProjectId}
              project={selectedProject}
              closeEdittogglePopup={closeEdittogglePopup}
              showEditPopup={showEditPopup}
            />
          )
        }
        {
          isOpen && (
            <AddProjectPopup
              closeAddProjecttogglePopup={closeAddProjecttogglePopup}
              isOpen={isOpen}
            />
          )
        }
        {
          SliderisOpen && (
            <ProfileSlider
              SliderisOpen={SliderisOpen}
              LogOut={LogOut}
              setSliderIsOpen={setSliderIsOpen}
            />
          )
        }


      </div >
    </div >
  );
};

export default HomeWorkSpace;

