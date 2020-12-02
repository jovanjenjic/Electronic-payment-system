import React from 'react';
import 'antd/dist/antd.css';

import { Route, BrowserRouter as Router, Switch } from 'react-router-dom';

import Login from './containers/Login';
import Register from './containers/Register';
import Home from './containers/home';

const App = () => {

  return (
    <Router>
      <Switch>
        <Route path="/home">
          <Home />
        </Route>
        <Route path="/login">
          <Login />
        </Route>
        <Route path="/register">
          <Register />
        </Route>
      </Switch>
    </Router>
  );
};

export default App;

