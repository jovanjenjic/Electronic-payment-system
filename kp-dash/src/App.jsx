import React from 'react';
import 'antd/dist/antd.css';

import { Route, BrowserRouter as Router, Switch } from 'react-router-dom';

import Login from './pages/Login';
import Register from './pages/Register';
import Home from './pages/home';
import PaymentSuccess from './pages/PaymentSuccess';
import PaymentCancel from './pages/PaymentCancel';
import SubscriptionSuccess from './pages/SubscriptionSuccess';
import SubscriptionCancel from './pages/SubscriptionCancel';
import PaymentFailed from './pages/PaymentFailed';
import BankPayment from './pages/BankPayment';

import { AppContext } from './context/app';

const App = () => {
  /** `collapsed` side of the menu */
  const [collapsed, updateCollapsed] = React.useState(false);

  return (
    <AppContext.Provider value={[collapsed, updateCollapsed]}>
      <Router>
        <Switch>
          <Route path="/login">
            <Login />
          </Route>
          <Route path="/register">
            <Register />
          </Route>
          <Route path="/payments/:paymentId/:paymentType/success">
            <PaymentSuccess />
          </Route>
          <Route path="/payments/:paymentId/:paymentType/cancel">
            <PaymentCancel />
          </Route>
          <Route path="/bank/success/:paymentId">
            <PaymentSuccess />
          </Route>
          <Route path="/bank/failed/:paymentId">
            <PaymentCancel />
          </Route>
          <Route path="/bank/error/:paymentId">
            <PaymentFailed />
          </Route>
          <Route path="/payments/paypal/subscriptions/:subscriptionId/success">
            <SubscriptionSuccess />
          </Route>
          <Route path="/payments/paypal/subscriptions/:subscriptionId/cancel">
            <SubscriptionCancel />
          </Route>
          <Route path="/bank/payment">
            <BankPayment />
          </Route>
          <Route path="/">
            <Home />
          </Route>
        </Switch>
      </Router>
    </AppContext.Provider>
  );
};

export default App;
