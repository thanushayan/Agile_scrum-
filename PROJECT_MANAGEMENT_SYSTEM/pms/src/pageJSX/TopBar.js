import { React, useState } from 'react';
import { FaSun, FaMoon } from 'react-icons/fa';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUserCircle } from '@fortawesome/free-solid-svg-icons';

const TopBar = ({ mode, toggleMode, viewType, setViewType, toggleSlider, projectId }) => {
    const [dp, setDp] = useState(null);
    return (
        <div className='top-bar'>
            {/* <div className='home-button' onClick={goToHomePage}>
                <FontAwesomeIcon icon={faHome} />
            </div> */}
            {!projectId && <div className='togglecase' onClick={toggleMode}>
                <div onClick={toggleMode} className='toggle'>
                    {mode === 'dark' ? <FaMoon /> : <FaSun />}
                </div>
            </div>}
            {!projectId && <select className='DropDown' value={viewType} onChange={(e) => setViewType(e.target.value)}>
                <option value='card'>Card View</option>
                <option value='table'>Table View</option>
                <option value='overview'>Overview</option>
            </select>}
            <div className='toggle-button' onClick={toggleSlider}>
                {!dp ? (
                    <FontAwesomeIcon icon={faUserCircle} />
                ) : (
                    <img src='profile' alt='Profile' />
                )}
            </div>
        </div>
    );
};

export default TopBar;