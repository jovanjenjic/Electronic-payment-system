import React from 'react';
import { Layout } from 'antd';
import {
  MenuUnfoldOutlined,
  MenuFoldOutlined,
  LogoutOutlined,
} from '@ant-design/icons';
import { Switch, Route, useHistory } from 'react-router-dom';
import styled from 'styled-components';

import Sidebar from '../components/Sidebar';
import Shop from '../components/Shop';
import PaymentMethods from './PaymentTypes';
import ShoppingCart from '../components/ShoppingCart';
import Subscriptions from './Subscriptions';

import { withAuth } from '../hoc/withAuth';
import useFetchPaymentTypes from '../hooks/useFetchPaymentTypes';

import { AppContext } from '../context/app';
import { ShopContext } from '../context/shop';

import './home.css';
import { get } from '../services/api';
import { CURRENT_URL } from '../constants/url';
import { responseOk } from '../utils/responseOk';
import { useAsync } from 'react-async';

const { Header } = Layout;

const RightNav = styled.div`
  display: flex;
  align-items: center;
  flex-direction: row;
  margin-right: 20px;
`;

const Logout = styled.div`
  margin-left: 10px;
  &:hover {
    cursor: pointer;
  }
`;

const fetchCurrentUser = async ({ authToken }) => {
  try {
    const response = await get(
      CURRENT_URL,
      authToken
    );
    if (responseOk(response)) return await response.json();
  } catch (error) {
    console.error(error);
  }
  return {};
};

const Home = () => {
  const authToken = localStorage.getItem('access_token');

  const [collapsed, updateCollapsed] = React.useContext(AppContext);

  const toggle = () => updateCollapsed((state) => !state);

  /** `use` state hook for updating items in the shop */
  const [items, updateItems] = React.useState([]);

  const { data: paymentTypes = [], reload: reloadPaymentTypes } = useFetchPaymentTypes();

  const history = useHistory();

  /** `logout` handler */
  const logout = () => {
    localStorage.removeItem('access_token');
    updateCollapsed(() => []);
    history.push('/login');
  };

  /** `use` async hook for fetching user */
  const { data: user = {}, reload: reloadUser } = useAsync({
    promiseFn: fetchCurrentUser,
    authToken,
    watch: authToken
  });

  /** `indicator is user seller` */
  const isUserSeller = (user.roles || []).some(v => v.name === 'ROLE_SELLER');

  return (
    <ShopContext.Provider value={{ items, updateItems, paymentTypes, reloadPaymentTypes, user, reloadUser }}>
      <Layout className="main-layout" style={{ height: window.innerHeight }}>
        <Sidebar />
        <Layout className="site-layout">
          <Header className="site-layout-background" style={{ padding: 0 }}>
            {React.createElement(
              collapsed ? MenuUnfoldOutlined : MenuFoldOutlined,
              {
                className: 'trigger',
                onClick: toggle,
              }
            )}
            <RightNav>
              <ShoppingCart />
              <Logout onClick={logout}>
                <LogoutOutlined />
              </Logout>
            </RightNav>
          </Header>
          <Switch>
            {isUserSeller && (
              <Route path="/payment-methods">
                <PaymentMethods />
              </Route>
            )}
            <Route path="/memberships">
              <Subscriptions />
            </Route>
            <Route path="/">
              <Shop />
            </Route>
          </Switch>
        </Layout>
      </Layout>
    </ShopContext.Provider>
  );
};

export default withAuth(Home);
