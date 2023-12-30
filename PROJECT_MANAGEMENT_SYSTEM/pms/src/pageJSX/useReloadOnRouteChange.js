import { useEffect } from 'react';

const useReloadOnRouteChange = (history) => {
    useEffect(() => {
        const handleRouteChange = (location, action) => {
            if (action === 'PUSH') {
                window.location.reload();
            }
        };

        const unlisten = history.listen(handleRouteChange);

        return () => {
            unlisten();
        };
    }, [history]);
};

export default useReloadOnRouteChange;
