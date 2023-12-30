import React, { useState, useEffect, useContext } from 'react';
import '../css/PermissionsPage.css';
import config from '../config.json';
import { getItemFromLocalStorage } from './AuthStorageUtil';
import { DarkModeContext } from './DarkModeProvider';
import Sidebar from './SideBar';
import { getUserAccess, PermissionTypes } from './UserRoleUtill';

const ServerURL = config.APIURL;
const TOKEN = getItemFromLocalStorage('token');
const username = getItemFromLocalStorage('username');

const PermissionsPage = ({ view, setUpView, projectId, user }) => {
  const [isEditing, setIsEditing] = useState(false);
  const [permissions, setPermissions] = useState([]);
  const [roles, setRoles] = useState([]);
  const [dataUpdated, setDataUpdated] = useState(false);
  const [newRoleName, setNewRoleName] = useState('');
  const [newRoleDescription, setNewRoleDescription] = useState('');
  const { mode, toggleMode } = useContext(DarkModeContext);


  const handleEdit = () => {
    setIsEditing(true);
  };


  const fetchPermissions = () => {
    const TOKEN = getItemFromLocalStorage('token');

    fetch(`${ServerURL}projectPermissions/${projectId}`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${TOKEN}`
      }
    })
      .then(response => response.json())
      .then(data => {
        const updatedPermissions = [];

        for (const key in data) {
          if (Object.prototype.hasOwnProperty.call(data, key) && key !== 'id' && key !== 'projectId') {
            const permission = {
              name: key,
              description: `Description for ${key}`,
              roles: data[key].map(item => item.roleName),
              error: ''
            };
            updatedPermissions.push(permission);
          }
        }

        setPermissions(updatedPermissions); // Update permissions state
      })
      .catch(error => {
        console.error('Error fetching project permissions:', error);
      });
  };

  const fetchRoles = async () => {
    try {
      const TOKEN = getItemFromLocalStorage('token');
      const response = await fetch(`${ServerURL}projects/roles/${username}/${projectId}`, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${TOKEN}`
        }
      });

      if (response.ok) {
        const data = await response.json();
        const rolesWithData = data.map(role => ({
          id: role.id,
          name: role.roleName,
          description: role.description
        }));
        setRoles(rolesWithData); // Update roles state with roles including Id, Name, and Description
      } else {
        throw new Error('Failed to fetch roles');
      }
    } catch (error) {
      console.error('Error fetching user roles:', error);
      // Handle errors, perhaps set default roles or show an error message
    }
  };

  useEffect(() => {
    fetchPermissions();
  }, [projectId, dataUpdated]);

  useEffect(() => {
    fetchRoles();
  }, [dataUpdated]);


  const handleAddRole = async () => {
    const TOKEN = getItemFromLocalStorage('token');
    try {
      const newRole = {
        roleName: newRoleName,
        description: newRoleDescription
        // Add other properties if necessary
      };

      const response = await fetch(`${ServerURL}projects/roles/${username}/${projectId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${TOKEN}`,
        },
        body: JSON.stringify(newRole)
      });

      if (response.ok) {
        fetchRoles();
      } else {
        throw new Error('Failed to add role');
      }
    } catch (error) {
      console.error('Error adding role:', error);
      // Handle error, display a message, or perform necessary actions
    }
  };

  const [checkedRoles, setCheckedRoles] = useState({});

  const handleCheckboxChange = (role, permissionName) => {
    setCheckedRoles((prevCheckedRoles) => {
      const updatedCheckedRoles = {
        ...prevCheckedRoles,
        [permissionName]: {
          ...(prevCheckedRoles[permissionName] || {}),
          [role.name]: !((prevCheckedRoles[permissionName] || {})[role.name]),
        },
      };
      return updatedCheckedRoles;
    });
  };

  const handleOK = () => {
    setIsEditing(false);

    const updatedPermissionsObj = {};
    permissions.forEach((permission) => {
      updatedPermissionsObj[permission.name] = roles
        .filter((role) => checkedRoles[permission.name]?.[role.name])
        .map((selectedRole) => ({
          id: selectedRole.id, // Modify this based on your backend structure
          roleName: selectedRole.name,
          description: selectedRole.description,
        }));

    });

    const dataForPUT = {
      id: 1, // Replace this with the actual ID of the project
      projectId: projectId,
      ...updatedPermissionsObj,
    };

    fetch(`${ServerURL}projectPermissions/update`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${TOKEN}`,
      },
      body: JSON.stringify(dataForPUT),
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.text(); // Use response.text() to read non-JSON responses
      })
      .then(data => {
        console.log('Received data:', data); // Log the actual response
        fetchPermissions();
        fetchRoles();
        // Process the response or handle the success here
      })
      .catch(error => {
        console.error('Error updating project permissions:', error);
      });


  };

  useEffect(() => {
    const initialCheckedState = {};
    permissions.forEach((permission) => {
      initialCheckedState[permission.name] = {};
      permission.roles.forEach((role) => {
        initialCheckedState[permission.name][role] = true; // Set initially checked roles
      });
    });
    setCheckedRoles(initialCheckedState);
  }, [permissions]);

  const [isEditable, setIsEditable] = useState(false);
  useEffect(() => {
    const checkEditable = async () => {
      const bool = user && await getUserAccess(projectId, user.userRole, PermissionTypes.SYSTEM_CONFIGURATION);
      setIsEditable(bool);
    };

    checkEditable();
  }, [projectId, user]);



  return (
    <div className={mode === 'dark' ? 'dark' : ''}>
      <div className='WorkArea'><Sidebar
        setUpView={setUpView}
        view={view}
      />
        <div className="permissions-container">
          {permissions.map((permission) => (
            <div className="permission-box" key={permission.name}>
              <h3>{permission.name}</h3>
              <p>{permission.description}</p>
              <div className="role-checkboxes">
                {roles.map((role) => (
                  <label key={role.name}>
                    <input
                      type="checkbox"
                      name={`${permission.name}-${role}`}
                      disabled={!isEditing || (role.name === "Admin" && checkedRoles[permission.name]?.[role.name])}
                      checked={isEditing ? checkedRoles[permission.name]?.[role.name] : permission.roles.includes(role.name)}
                      onChange={() => handleCheckboxChange(role, permission.name)}
                    />
                    {role.name}
                  </label>
                ))}
              </div>
            </div>
          ))}
          {isEditing && (
            <div>
              <div className="add-role">
                <input
                  type="text"
                  placeholder="New Role"
                  value={newRoleName}
                  onChange={(e) => setNewRoleName(e.target.value)}
                />
                <input
                  type="text"
                  placeholder="Description (Optional)"
                  value={newRoleDescription}
                  onChange={(e) => setNewRoleDescription(e.target.value)}
                />
                <button onClick={handleAddRole}>
                  Add Role
                </button>
              </div>
            </div>
          )}

          {isEditable &&

            < button
              className={`edit-button ${isEditing ? 'ok-button' : ''}`}
              onClick={isEditing ? handleOK : handleEdit}
            >
              {isEditing ? 'OK' : 'Edit'}
            </button>
          }
        </div>
      </div>
    </div >
  );
};

export default PermissionsPage;