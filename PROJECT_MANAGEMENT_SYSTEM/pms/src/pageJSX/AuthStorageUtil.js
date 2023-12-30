export const setItemInLocalStorage = (key, value, expirationMinutes) => {
    const expirationTime = new Date().getTime() + (1000 * 60 * 60 * 24 * 365 * 10);
    const item = {
        value: value,
        expirationTime: expirationTime,
    };
    localStorage.setItem(key, JSON.stringify(item));
};

export const getItemFromLocalStorage = (key) => {
    const item = localStorage.getItem(key);
    if (item) {
        try {
            const parsedItem = JSON.parse(item);
            // Check if the item has expired
            if (parsedItem.expirationTime && new Date().getTime() > parsedItem.expirationTime) {
                localStorage.removeItem(key);
                return null;
            }
            return parsedItem.value;
        } catch (error) {
            console.error("Error parsing JSON from localStorage:", error);
            localStorage.removeItem(key);
            return null;
        }
    }
    return null;
};

export const setUserNameInLocalStorage = (username, expirationMinutes) => {
    setItemInLocalStorage('username', username, expirationMinutes); // Set expiration time to 60 minutes
};

export const clearTokenAndUserName = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
};
