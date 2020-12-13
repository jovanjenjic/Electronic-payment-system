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
import ShoppingCart from '../components/ShoppingCart';

import { withAuth } from '../hoc/withAuth';
import useFetchPaymentTypes from '../hooks/useFetchPaymentTypes';

import { AppContext } from '../context/app';
import { ShopContext } from '../context/shop';

import './home.css';

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

const Home = () => {

  const [collapsed, updateCollapsed] = React.useContext(AppContext);

  const toggle = () => updateCollapsed(state => !state);

  /** `use` state hook for updating items in the shop */
  const [items, updateItems] = React.useState([]);

  const { data: paymentTypes = [] } = useFetchPaymentTypes();

  /** `logout` handler */
  const logout = () => {
    localStorage.removeItem('access_token');
    updateCollapsed(() => []);
    useHistory().push('/login');
  };

  return (
    <ShopContext.Provider value={{ items, updateItems, paymentTypes }}>
      <Layout className="main-layout" style={{ height: window.innerHeight }}>
        <Sidebar />
        <Layout className="site-layout">
          <Header className="site-layout-background" style={{ padding: 0 }}>
            {React.createElement(collapsed ? MenuUnfoldOutlined : MenuFoldOutlined, {
              className: 'trigger',
              onClick: toggle,
            })}
            <RightNav>
              <ShoppingCart />
              <Logout onClick={logout}><LogoutOutlined /></Logout>
            </RightNav>
          </Header>
          <Switch>
            <Route path="/payment-methods">
              <div>AAA</div>
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