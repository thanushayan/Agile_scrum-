import React, { useState, useEffect, useContext } from 'react';


export const DarkModeContext = React.createContext();

export const DarkModeProvider = ({ children }) => {
    const [mode, setMode] = useState(() => {
        const savedMode = localStorage.getItem('mode');
        return savedMode ? savedMode : 'light';
    });

    useEffect(() => {
        localStorage.setItem('mode', mode);
        document.documentElement.setAttribute('data-theme', mode);
    }, [mode]);

    const toggleMode = () => {
        const newMode = mode === 'light' ? 'dark' : 'light';
        setMode(newMode);
        console.log(newMode);
    };

    return (
        <DarkModeContext.Provider value={{ mode, toggleMode }}>
            {children}
        </DarkModeContext.Provider>
    );
};
