import React, { Component } from 'react';
import { ScheduleComponent, Inject, Day, Week, WorkWeek, Month, Agenda } from '@syncfusion/ej2-react-schedule';

class CalendarEvent extends Component {
    constructor(props) {
        super(props);

        this.state = {
            events: [
                {
                    Id: 1,
                    Subject: 'Project Kickoff Meeting',
                    StartTime: new Date(2023, 10, 20, 10, 0),
                    EndTime: new Date(2023, 10, 20, 11, 0),
                },
                {
                    Id: 2,
                    Subject: 'Client Presentation',
                    StartTime: new Date(2023, 10, 21, 14, 0),
                    EndTime: new Date(2023, 10, 21, 16, 0),
                },
                {
                    Id: 3,
                    Subject: 'Team Building Activity',
                    StartTime: new Date(2023, 10, 23, 9, 0),
                    EndTime: new Date(2023, 10, 23, 12, 0),
                },

            ],
        };
    }

    render() {
        return (
            <ScheduleComponent
                currentView='Month'
                eventSettings={{ dataSource: this.state.events }}
                selectedDate={new Date(2023, 10, 20)}
            >
                <Inject services={[Day, Week, WorkWeek, Month, Agenda]} />
            </ScheduleComponent>
        );
    }
}

export default CalendarEvent;