import axios from 'axios';
import config from '../config.json';
import { getItemFromLocalStorage } from './AuthStorageUtil';

export const PermissionTypes = {
    PROJECT_MANAGEMENT: "projectManagementPermissions",
    TASK_SPRINT: "taskSprintPermissions",
    MANAGE_USERS: "manageUsersPermissions",
    ADD_COMMENTS: "addCommentsPermissions",
    UPLOAD_DOCUMENTS: "uploadDocumentsPermissions",
    VIEW_REPORTS: "viewReportsPermissions",
    SYSTEM_CONFIGURATION: "systemConfigurationPermissions",
};

const getUserRole = async ({ projectId }) => {
    const ServerURL = config.APIURL;
    const TOKEN = getItemFromLocalStorage('token');
    const username = getItemFromLocalStorage('username');
    try {
        const response = await axios.get(`${ServerURL}api/project-and-user/${username}/${projectId}`, {
            headers: {
                Authorization: TOKEN,
            },
        });
        if (response.status === 200) {
            return response.data;
        }
        return null;
    } catch (error) {
        if (error.response) {
            console.error('Error getting user:', error.response.data);
            console.error('Status code:', error.response.status);
        } else if (error.request) {
            console.error('Error getting user: No response received');
        } else {
            console.error('Error getting user:', error.message);
        }
        return null; // Return a default value in case of an error
    }
};

export const getUserAccess = (projectId, givenRole, permissionType) => {
    return new Promise((resolve, reject) => {
        getUserPermissions({ projectId })
            .then((rolesArray) => {
                if (!rolesArray) {
                    console.error('Error fetching permissions');
                    resolve(false);
                    return;
                }

                switch (permissionType) {
                    case PermissionTypes.PROJECT_MANAGEMENT:
                        resolve(rolesArray.projectManagementPermissions.some(element => element.roleName === givenRole));
                        break;
                    case PermissionTypes.TASK_SPRINT:
                        resolve(rolesArray.taskSprintPermissions.some(element => element.roleName === givenRole));
                        break;
                    case PermissionTypes.MANAGE_USERS:
                        resolve(rolesArray.manageUsersPermissions.some(element => element.roleName === givenRole));
                        break;
                    case PermissionTypes.ADD_COMMENTS:
                        resolve(rolesArray.addCommentsPermissions.some(element => element.roleName === givenRole));
                        break;
                    case PermissionTypes.UPLOAD_DOCUMENTS:
                        resolve(rolesArray.uploadDocumentsPermissions.some(element => element.roleName === givenRole));
                        break;
                    case PermissionTypes.VIEW_REPORTS:
                        resolve(rolesArray.viewReportsPermissions.some(element => element.roleName === givenRole));
                        break;
                    case PermissionTypes.SYSTEM_CONFIGURATION:
                        resolve(rolesArray.systemConfigurationPermissions.some(element => element.roleName === givenRole));
                        break;
                    default:
                        resolve(false); // Or handle the default case based on your requirements
                }
            })
            .catch((error) => {
                console.error('Error in getUserAccess:', error);
                resolve(false);
            });
    });
};



const getUserPermissions = async ({ projectId }) => {
    const ServerURL = config.APIURL;
    const TOKEN = getItemFromLocalStorage('token');
    const username = getItemFromLocalStorage('username');
    try {
        const response = await axios.get(`${ServerURL}projectPermissions/${projectId}`, {
            headers: {
                Authorization: TOKEN,
            },
        });
        if (response.status === 200) {
            return response.data;
        }
        return null;
    } catch (error) {
        if (error.response) {
            console.error('Error getting Permissions:', error.response.data);
            console.error('Status code:', error.response.status);
        } else if (error.request) {
            console.error('Error getting Permissions: No response received');
        } else {
            console.error('Error getting Permissions:', error.message);
        }
        return null; // Return a default value in case of an error
    }
};

export default getUserRole;
