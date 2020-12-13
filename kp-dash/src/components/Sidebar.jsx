import React from 'react';
import styled from 'styled-components';
import { Layout, Menu } from 'antd';
import {
  DollarOutlined,
  ShopOutlined,
} from '@ant-design/icons';
import { Link } from 'react-router-dom';

import { AppContext } from '../context/app';


const { Sider } = Layout;

const Logo = styled.div`
  height: 32px;
  background: rgba(255, 255, 255, 0.3);
  margin: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 10px;
`;

const StyledHeader = styled.div`
  font-size: 20px;
  font-family: 'Arimo';
  font-weight: 700;
  color: white;
`;

const Sidebar = () => {
  /** `context` for collapsed sider */
  const [collapsed] = React.useContext(AppContext);

  return (
    <Sider trigger={null} collapsible collapsed={collapsed}>
      <Logo><StyledHeader>KP</StyledHeader></Logo>
      <Menu theme="dark" mode="inline" defaultSelectedKeys={['1']}>
        <Menu.Item key="1" icon={<ShopOutlined />}>
          <Link to="/" >Shop</Link>
      </Menu.Item>
        <Menu.Item key="2" icon={<DollarOutlined />}>
          <Link to="/payment-methods" >Payment methods</Link>
      </Menu.Item>
      </Menu>
    </Sider>
  );
};

export default Sidebar;
