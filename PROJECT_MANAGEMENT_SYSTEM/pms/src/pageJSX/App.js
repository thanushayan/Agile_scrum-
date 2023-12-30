import { React, useEffect } from 'react';
import { BrowserRouter as Router, Switch, Route, Redirect } from 'react-router-dom';
import HomeWorkSpace from './HomeWorkSpace';
import AddProjectPopup from './AddProjectPopup';
import ProjectWorkArea from './ProjectWorkArea';
import withAutoReload from './withAutoReload';
import Authentication from './Authentication';
import VideoCallMain from '../VideoCallMain';
import CreateRepositoryForm from './Repo/CreateRepo';

const App = () => {
  const token = localStorage.getItem('token');
  const username = localStorage.getItem('username');

  useEffect(() => {
    // Check if the token is null and the current route is not '/auth'
    if (!token && !username && window.location.pathname !== '/auth') {
      // Redirect to the authentication page
      window.location.href = '/auth'; // Replace with your authentication page URL
    }
    if (token && username && window.location.pathname === '/auth') {
      // Redirect to the authentication page
      window.location.href = '/home'; // Replace with your authentication page URL
    }
  }, []);


  return (
    // <div>
    //   <h1>Create GitHub Repository</h1>
    //   <CreateRepositoryForm />
    // </div>
    <Router>
      <Switch>
        <Route exact path="/">
          {token ? <Redirect to="/home" /> : <Redirect to="/auth" />}
        </Route>
        <Route exact path="/home" component={withAutoReload(HomeWorkSpace)} />
        <Route exact path="/project/:projectId" component={withAutoReload(ProjectWorkArea)} />
        <Route exact path="/auth" component={Authentication} />
        <Route exact path="/TeamVc/:projectId" component={VideoCallMain} />
      </Switch>
      <AddProjectPopup />
    </Router>
  );
};

export default App;
