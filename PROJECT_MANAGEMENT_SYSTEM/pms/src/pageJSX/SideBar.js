import { React, useState } from "react";
import '../css/SideBar.css';

const Sidebar = ({ setUpView, view }) => {
    return (
        <div>
            <div className="sidebar">
                <ul className="Menu">
                    <li className={view === 'dashboard' ? 'selected' : 'list'} onClick={() => setUpView('dashboard')}>Dashboard</li>
                    <li className={view === 'backlog' ? 'selected' : 'list'} onClick={() => setUpView('backlog')}>Backlogs</li>
                    <li className={view === 'activeSprint' ? 'selected' : 'list'} onClick={() => setUpView('activeSprint')}>Active Sprint</li>
                    <li className={view === 'reporting' ? 'selected' : 'list'} onClick={() => setUpView('Report')}>Reporting</li>
                    <li className={view === 'team' ? 'selected' : 'list'} onClick={() => setUpView('team')}>Team Members</li>
                    <li className={view === 'Permissions' ? 'selected' : 'list'} onClick={() => setUpView('permission')}>Permission Config</li>
                </ul>
            </div>
        </div>

    )
}

export default Sidebar;