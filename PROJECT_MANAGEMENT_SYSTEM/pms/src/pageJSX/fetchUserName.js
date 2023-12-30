import axios from 'axios';
import { getItemFromLocalStorage, setUserNameInLocalStorage } from './AuthStorageUtil';
export const fetchUsername = async () => {
    const jwtToken = getItemFromLocalStorage('token');
    if (jwtToken === null) {
        return;
    }
    try {
        const response = await axios.get(`http://localhost:9898/auth/validate/${jwtToken}`);
        const username = response.data;
        console.log(username);
        if (username !== null) {
            setUserNameInLocalStorage(username, 180);
            console.log(username);
        } else {
            // Retry after a delay (e.g., 1 second)
            setTimeout(fetchUsername, 1000);
        }
    } catch (error) {
        console.log(error);
        setTimeout(fetchUsername, 1000);
    }
};