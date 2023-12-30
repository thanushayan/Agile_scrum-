import React from 'react';

const MemberCard = ({ member, handleProfileClick }) => {
    return (
        <div className="member-card" onClick={() => handleProfileClick(member)}>
            <div className="member-info">
                <img src={member.avatar} alt="Member Avatar" className="avatar" />
                <div className="member-details">
                    <h4 className="member-name">{member.userId}</h4>
                    <p className="member-role">{member.userRole}</p>
                </div>
            </div>
            <div className="progress-bar">
                <div className="progress-fill" style={{ width: `${member.progress}%` }}></div>
            </div>
            <p className="member-progress">Progress: {member.progress}%</p>
        </div>
    );
};

export default MemberCard;
