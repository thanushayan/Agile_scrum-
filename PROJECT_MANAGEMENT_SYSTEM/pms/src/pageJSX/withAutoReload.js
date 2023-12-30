import React, { useEffect } from 'react';
import { useHistory } from 'react-router-dom';

const withAutoReload = (WrappedComponent) => {
    const AutoReloadComponent = (props) => {
        const history = useHistory();

        useEffect(() => {
            const handleRouteChange = () => {
                window.location.reload();
            };

            const unlisten = history.listen(handleRouteChange);

            return () => {
                unlisten();
            };
        }, [history]);

        return <WrappedComponent {...props} />;
    };

    return AutoReloadComponent;
};

export default withAutoReload;
